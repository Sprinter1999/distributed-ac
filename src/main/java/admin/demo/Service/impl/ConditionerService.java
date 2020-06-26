package admin.demo.Service.impl;

import admin.demo.Entity.Conditioner;
import admin.demo.Entity.Record;
import admin.demo.Repository.ConditionerRepository;
import admin.demo.Repository.RecordRepository;
import admin.demo.Service.IConditionerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;

@Service
public class ConditionerService implements IConditionerService {
    @Autowired
    ConditionerRepository cR;
    @Autowired
    RecordRepository rR;

    int MAX_NUM = 5;

    Logger logger = LoggerFactory.getLogger(ConditionerService.class.getName());

    //在某个房间发出送风请求时调用
    //TODO:需要保证每次发出送风请求时Record中没有此房间正在队列中的请求
    @Override
    @Transactional
    public void ServiceRequest(Record record){
        Date date = new Date();
        Long datetime = date.getTime();

        Integer roomId = record.roomId;
        Conditioner requestConditioner = cR.findByRoomId(roomId);//roomId与Conditioner一一对应

        //检查发出请求的房间是否有未完成的请求
        Record currentRecord = rR.findByRoomIdAndIsComplete(roomId, 0);
        if (currentRecord != null)
            turnOffOrStandBy(requestConditioner, 0);

        requestConditioner.setRecord(record);
        //调度开始
        ArrayList<Conditioner> currentConditionerList = cR.findByIsAtWork(2);//正在服务中的空调列表
        int currentServeNum = currentConditionerList.size();
        if (currentServeNum < MAX_NUM){//服务队列有空闲
            //直接放入服务队列
            requestConditioner.setIsAtWork(2);
            requestConditioner.setServeStartTime(datetime);
            cR.save(requestConditioner);
            record.setActualStartTime(datetime);
            rR.save(record);
        }else{
            Integer requestWindSpeed = record.windSpeed;
            Sort sort = Sort.by(Direction.ASC, "isAtWork")
                    .and(Sort.by(Direction.ASC, "windSpeed"));
            ArrayList<Conditioner> lowerWindSpeedList = cR.findByIsAtWorkAndWindSpeedLessThanAndOrder(2, requestWindSpeed, sort);
            if(lowerWindSpeedList.size() != 0){//存在更小的风速的空调
                Conditioner replaceConditioner = lowerWindSpeedList.get(0);
                turnToWait(replaceConditioner);
                //直接放入服务队列
                requestConditioner.setIsAtWork(2);
                requestConditioner.setServeStartTime(datetime);
                cR.save(requestConditioner);
                record.setActualStartTime(datetime);
                rR.save(record);
            } else {
                //直接放入等待队列
                requestConditioner.setWaitStartTime(datetime);//设置等待开始时间
                requestConditioner.setIsAtWork(1);
                cR.save(requestConditioner);
                record.setRequestStartTime(datetime);
                rR.save(record);
            }
        }

    }

    //某个房间进入待机状态时调用
    //TODO:新增待机状态，直接进入待机状态
    @Override
    @Transactional
    public void StandByRequest(Integer roomId){
        //本房间的操作
        Conditioner currentConditioner = cR.findByRoomId(roomId);
        turnOffOrStandBy(currentConditioner, 4);

        //其它房间的操作
        ArrayList<Conditioner> waitConditionerList = cR.findByIsAtWorkOrderByWaitStartTimeAsc(1);
        if (!waitConditionerList.isEmpty()){
            Conditioner waitConditioner = waitConditionerList.get(0);
            turnToServe(waitConditioner);
        }
    }

    //每隔1分钟调用
    @Scheduled(fixedRate = 60000)
    @Async
    @Override
    @Transactional
    public void Schedule(){
        Date date = new Date();
        long datetime = date.getTime();
        //空调状态变换
        ArrayList<Conditioner> allConditionerList = cR.findAll();
        if (allConditionerList.isEmpty()){//没有房间
            Thread.currentThread().setName("房间当前时间下的状态");
            logger.info("{}.状态如下{}",Thread.currentThread().getName(), null);
            return;
        }
        for (Conditioner conditioner : allConditionerList){
            Record record = rR.findByRoomIdAndIsComplete(conditioner.roomId, 0);//TODO:查找时加入了是否完成，改正了之前可能查找到多个Record的bug
            if(conditioner.isAtWork == 2){
                //房间温度变换
                if(conditioner.windSpeed == 1){
                    if(conditioner.setTemp > conditioner.curTemp){
                        conditioner.curTemp += 0.5;
                    }else{
                        conditioner.curTemp -= 0.5;
                    }
                }else if(conditioner.windSpeed == 0){
                    long serveTime = datetime/(6000)-conditioner.serveStartTime/(6000);
                    if(conditioner.setTemp > conditioner.curTemp){
                        conditioner.curTemp += 0.4;
                    }else{
                        conditioner.curTemp -= 0.4;
                    }
                }else{
                    long serveTime = datetime/(6000)-conditioner.serveStartTime/(6000);
                    if(conditioner.setTemp > conditioner.curTemp){
                        conditioner.curTemp += 0.6;
                    }else{
                        conditioner.curTemp -= 0.6;
                    }
                }
                cR.save(conditioner);
                //耗电量变换
                if(conditioner.windSpeed == 1){
                    record.electricity += 0.5;
                }else if(conditioner.windSpeed == 0){
                    record.electricity += 0.333;
                }else{
                    record.electricity += 1;
                }
                rR.save(record);
            }else {//当空调不工作时，温度自动向init变化
                double diff = conditioner.curTemp - conditioner.initTemp;
                if (diff >= 0.5){
                    conditioner.curTemp -= 0.5;
                }else if(diff <= -0.5) {
                    conditioner.curTemp += 0.5;
                }
            }
        }

        //等待队列的处理
        Sort sort = Sort.by(Direction.DESC, "windSpeed")
                .and(Sort.by(Direction.ASC, "waitStartTime"));
        ArrayList<Conditioner> waitConditionerList = cR.findByIsAtWorkAndOrder(1, sort);
        ArrayList<Conditioner> serveConditionerList = cR.findByIsAtWorkOrderByServeStartTimeAsc(2);
        if (!waitConditionerList.isEmpty()){
            if(serveConditionerList.size() < MAX_NUM){
                Conditioner waitConditioner = waitConditionerList.get(0);
                turnToServe(waitConditioner);
            }else {
                for(Conditioner conditioner : waitConditionerList){
                    long waitTime = datetime/(6000)-conditioner.serveStartTime/(6000);
                    if(waitTime >= 2 && waitTime%2 == 0){
                        Conditioner serveConditioner = serveConditionerList.get(0);
                        turnToWait(serveConditioner);
                        turnToServe(conditioner);
                        break;
                    }
                }
            }
        }

        //输出日志
        Thread.currentThread().setName("房间当前时间下的状态");
        logger.info("{}.状态如下{}",Thread.currentThread().getName(), cR.findAll());
    }

    //管理员调用
    //将处于等待队列或服务队列的空调关机
    @Override
    @Transactional
    public void StopRequest(Integer roomId){
        Conditioner conditioner = cR.findByRoomId(roomId);
        turnOffOrStandBy(conditioner, 0);
        //检查服务队列是否有空闲
        ArrayList<Conditioner> serveConditionerList = cR.findByIsAtWorkOrderByServeStartTimeAsc(2);
        Sort sort = Sort.by(Direction.DESC, "windSpeed")
                .and(Sort.by(Direction.ASC, "waitStartTime"));
        ArrayList<Conditioner> waitConditionerList = cR.findByIsAtWorkAndOrder(1, sort);
        if (!waitConditionerList.isEmpty()){
            if(serveConditionerList.size() < MAX_NUM){
                turnToServe(waitConditionerList.get(0));
            }
        }
    }


    //将服务队列中的空调转入等待队列
    void turnToWait(Conditioner conditioner){
        Date date = new Date();
        Long datetime = date.getTime();

        //更新空调的状态和请求
        conditioner.setIsAtWork(1);
        conditioner.setWaitStartTime(datetime);
        cR.save(conditioner);
        Record currentRecord = rR.findByRoomIdAndIsComplete(conditioner.roomId, 0);
        if (currentRecord != null){
            currentRecord.setEndTime(datetime);
            currentRecord.setIsComplete(1);
            rR.save(currentRecord);
            //产生新的请求
            Record newRecord = new Record();
            newRecord.setRecord(currentRecord);
            newRecord.setStartTemp(conditioner.curTemp);
            newRecord.setRequestStartTime(datetime);
            rR.save(newRecord);
        }

        /*record.setRecordId(null);
        record.startTemp = conditioner.curTemp;
        record.setRequestStartTime(datetime);
        record.setElectricity((double) 0);
        record.setIsComplete(0);
        rR.save(record);*/
    }

    //将等待队列中的空调转入服务队列
    void turnToServe(Conditioner conditioner){
        Date date = new Date();
        Long datetime = date.getTime();
        conditioner.setIsAtWork(2);
        conditioner.setServeStartTime(datetime);
        cR.save(conditioner);
        Record record = rR.findByRoomIdAndIsComplete(conditioner.roomId, 0);
        record.setStartTemp(conditioner.curTemp);
        record.setActualStartTime(datetime);
        rR.save(record);
    }

    //关掉某房间的空调时调用
    //TODO:等待队列中的record记录结束不能存入数据库
    void turnOffOrStandBy(Conditioner conditioner, Integer isAtWork){
        Date date = new Date();
        Long datetime = date.getTime();
        Record record = rR.findByRoomIdAndIsComplete(conditioner.roomId, 0);
        if(record != null) {
            if (conditioner.isAtWork == 1){//此请求处于等待队列
                rR.deleteByRoomIdAndIsComplete(conditioner.roomId, 0);
            }else {//此请求处于服务队列
                record.setEndTime(datetime);
                record.setIsComplete(1);
                rR.save(record);
            }
        }
        conditioner.setIsAtWork(isAtWork);
        cR.save(conditioner);
    }

}
