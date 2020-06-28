package admin.demo.Service.impl;

import admin.demo.Entity.Bill;
import admin.demo.Entity.Conditioner;
import admin.demo.Entity.Record;
import admin.demo.Entity.User;
import admin.demo.Repository.BillRepository;
import admin.demo.Repository.ConditionerRepository;
import admin.demo.Repository.RecordRepository;
import admin.demo.Repository.UserRepository;
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
import java.util.List;

@Service
public class ConditionerService implements IConditionerService {
    @Autowired
    ConditionerRepository cR;
    @Autowired
    RecordRepository rR;
    @Autowired
    UserRepository uR;
    @Autowired
    BillRepository bR;

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
        if (currentRecord != null || requestConditioner.isAtWork == 3)
            turnOffOrStandBy(requestConditioner, 0);

        requestConditioner.setRecord(record);
        //调度开始
        ArrayList<Conditioner> currentConditionerList = cR.findByIsAtWork(2);//正在服务中的空调列表
        int currentServeNum = currentConditionerList.size();
        if (currentServeNum < MAX_NUM){//服务队列有空闲
            //直接放入服务队列
            requestConditioner.isAtWork = 2;
            requestConditioner.serveStartTime = datetime;
            cR.save(requestConditioner);
            record.actualStartTime = datetime;
            rR.save(record);
        }else{//服务队列已满
            Integer requestWindSpeed = record.windSpeed;
            Sort sort = Sort.by(Direction.ASC, "windSpeed")
                    .and(Sort.by(Direction.ASC, "serveStartTime"));
            ArrayList<Conditioner> lowerWindSpeedList = cR.findByIsAtWorkAndWindSpeedLessThanAndOrder(2, requestWindSpeed, sort);
            if(lowerWindSpeedList.size() != 0){//存在更小的风速的空调
                Conditioner replaceConditioner = lowerWindSpeedList.get(0);
                turnToWait(replaceConditioner);
                //直接放入服务队列
                requestConditioner.setTemp = record.setTemp;
                requestConditioner.isAtWork = 2;
                requestConditioner.serveStartTime = datetime;
                requestConditioner.windSpeed = record.windSpeed;
                cR.save(requestConditioner);
                record.actualStartTime = datetime;
                rR.save(record);
            }else {
                //直接放入等待队列
                requestConditioner.setTemp = record.setTemp;
                requestConditioner.waitStartTime = datetime;//设置等待开始时间
                requestConditioner.isAtWork = 1;
                requestConditioner.windSpeed = record.windSpeed;
                cR.save(requestConditioner);
                record.requestStartTime = datetime;
                rR.save(record);
            }
        }

    }


    //每隔1分钟调用
    @Scheduled(fixedRate = 60000)
    @Async
    @Transactional
    void Schedule(){
        logger.info("房间当前时间下的状态.状态如下{}", cR.findAll());
        Date date = new Date();
        long datetime = date.getTime();
        //空调状态变换
        ArrayList<Conditioner> allConditionerList = cR.findAll();
        if (allConditionerList.isEmpty()){//没有房间
            //Thread.currentThread().setName("房间当前时间下的状态");
            //logger.info("房间当前时间下的状态.状态如下{}",Thread.currentThread().getName(), null);
            System.out.println("!!!!!!!!!!!!!当前没有房间入住");
            return;
        }
        for (Conditioner conditioner : allConditionerList){
            Record record = rR.findByRoomIdAndIsComplete(conditioner.roomId, 0);//TODO:查找时加入了是否完成，改正了之前可能查找到多个Record的bug
            //TODO:改正后依然出现了bug
            if(conditioner.isAtWork == 2){//房间处于服务队列
                //房间温度变换
                double diff = conditioner.setTemp - conditioner.curTemp;
                if(conditioner.windSpeed == 1){//中风
                    if(diff > 0){
                        conditioner.curTemp += 0.5;
                        diff -= 0.5;
                    }else if (diff < 0){
                        conditioner.curTemp -= 0.5;
                        diff += 0.5;
                    }
                    if (Math.abs(diff) < 0.5)
                        standByRequest(conditioner.roomId);
                }else if(conditioner.windSpeed == 0){//低风
                    if(diff > 0){
                        conditioner.curTemp += 0.4;
                        diff -= 0.4;
                    }else if (diff < 0){
                        conditioner.curTemp -= 0.4;
                        diff += 0.4;
                    }
                    if (Math.abs(diff) < 0.4)
                        standByRequest(conditioner.roomId);
                }else{//高风
                    if(diff > 0){
                        conditioner.curTemp += 0.6;
                        diff -= 0.6;
                    }else if (diff < 0){
                        conditioner.curTemp -= 0.6;
                        diff += 0.6;
                    }
                    if (Math.abs(diff) < 0.6)
                        standByRequest(conditioner.roomId);
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
                cR.save(conditioner);
                if (conditioner.isAtWork == 3 && Math.abs(conditioner.curTemp - conditioner.setTemp) > 1){//空调处于待机状态，重新发送启动请求
                    Record record1 = new Record();
                    record1.userId = conditioner.userId;
                    record1.roomId = conditioner.roomId;
                    record1.windSpeed = conditioner.windSpeed;
                    record1.startTemp = conditioner.curTemp;
                    record1.setTemp = conditioner.setTemp;
                    record1.requestStartTime = datetime;
                    ServiceRequest(record1);
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
                    if(waitTime >= 2 && waitTime%2 == 0){//找到一个等待时间超过2分钟且等待时间是2的倍数的
                        Conditioner serveConditioner = serveConditionerList.get(0);
                        turnToWait(serveConditioner);
                        turnToServe(conditioner);
                        break;
                    }
                }
            }
        }

        //输出日志
        //Thread.currentThread().setName("房间当前时间下的状态");
    }

    //管理员调用
    //将处于等待队列或服务队列的空调关机
    @Override
    @Transactional
    public Conditioner StopRequest(Integer roomId){
        Conditioner conditioner = cR.findByRoomId(roomId);
        conditioner = turnOffOrStandBy(conditioner, 0);
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
        return conditioner;
    }

    //用户退房时调用
    //TODO:关掉房间的空调，存储总账单，设置用户退出时间
    @Override
    public void CheckOut(Integer userId){
        Date date = new Date();
        Long datetime = date.getTime();

        Conditioner conditioner = cR.findByUserId(userId);
        User user = uR.findUserByUserId(userId);
        List<Record> recordList = rR.findByRoomId(user.roomId);
        turnOffOrStandBy(conditioner, 0);
        Bill bill = new Bill();
        bill.userId = userId;
        bill.roomId = conditioner.roomId;
        bill.checkin = user.checkin;
        bill.checkout = datetime;
        for (Record record : recordList)
            bill.totalFee += record.electricity;
        bR.save(bill);
        user.checkout = datetime;
        uR.save(user);
    }


    //某个房间进入待机状态时调用
    //TODO:新增待机状态，直接进入待机状态
    @Transactional
    void standByRequest(Integer roomId){
        //本房间的操作
        Conditioner currentConditioner = cR.findByRoomId(roomId);
        turnOffOrStandBy(currentConditioner, 3);

        //其它房间的操作
        ArrayList<Conditioner> waitConditionerList = cR.findByIsAtWorkOrderByWaitStartTimeAsc(1);//等待队列有空调
        if (!waitConditionerList.isEmpty()){
            if (cR.findByIsAtWork(2).size() < MAX_NUM) {
                Conditioner waitConditioner = waitConditionerList.get(0);
                turnToServe(waitConditioner);
            }
        }
    }

    //将服务队列中的空调转入等待队列
    @Transactional
    void turnToWait(Conditioner conditioner){
        Date date = new Date();
        Long datetime = date.getTime();

        //更新空调的状态和请求
        conditioner.isAtWork = 1;
        conditioner.waitStartTime = datetime;
        cR.save(conditioner);
        Record currentRecord = rR.findByRoomIdAndIsComplete(conditioner.roomId, 0);
        if (currentRecord != null){
            currentRecord.setEndTime(datetime);
            currentRecord.isComplete = 1;
            rR.save(currentRecord);
            //产生新的请求
            Record newRecord = new Record();
            newRecord.setRecord(currentRecord);
            newRecord.startTemp = conditioner.curTemp;
            newRecord.requestStartTime = datetime;
            rR.save(newRecord);
        }else{
            System.out.println("!!!!!!!!当前空调未处于服务队列");
        }

    }

    //将等待队列中的空调转入服务队列
    void turnToServe(Conditioner conditioner){
        Date date = new Date();
        Long datetime = date.getTime();
        //找到当前的请求
        Record record = rR.findByRoomIdAndIsComplete(conditioner.roomId, 0);
        record.setStartTemp(conditioner.curTemp);
        record.setActualStartTime(datetime);
        rR.save(record);

        conditioner.setTemp = record.setTemp;
        conditioner.isAtWork = 2;
        conditioner.serveStartTime = datetime;
        conditioner.windSpeed = record.windSpeed;
        cR.save(conditioner);
    }

    //关闭空调或转入待机时调用
    //TODO:等待队列中的record记录结束不能存入数据库
    Conditioner turnOffOrStandBy(Conditioner conditioner, Integer isAtWork){
        Date date = new Date();
        Long datetime = date.getTime();
        Record record = rR.findByRoomIdAndIsComplete(conditioner.roomId, 0);
        if(record != null) {
            if (conditioner.isAtWork == 1){//此请求处于等待队列，直接删除这个请求
                rR.deleteByRoomIdAndIsComplete(conditioner.roomId, 0);
            }else if (conditioner.isAtWork == 2){//此请求处于服务队列
                record.setEndTime(datetime);
                record.setIsComplete(1);
                rR.save(record);
            }
        }
        conditioner.setIsAtWork(isAtWork);
        if(isAtWork == 0)
            conditioner.curTemp = conditioner.initTemp;
        cR.save(conditioner);
        return conditioner;
    }

}
