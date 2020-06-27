package admin.demo;

import admin.demo.Entity.Conditioner;
import admin.demo.Entity.Record;
import admin.demo.Repository.ConditionerRepository;
import admin.demo.Service.impl.ConditionerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public  class DemoApplicationTests {
    @Autowired
    ConditionerRepository conditionerRepository;
    @Autowired
    ConditionerService conditionerService;


    @Test
    public void Test() throws InterruptedException {
        //0min
        turnOn(1);
        Thread.currentThread().sleep(60000);
        //1min
        setT(1,20.0);turnOn(2);turnOn(5);
        Thread.currentThread().sleep(60000);
        //2min
        turnOn(3);
        Thread.currentThread().sleep(60000);
        //3min
        setT(2,19.0);turnOn(4);
        Thread.currentThread().sleep(60000);
        //4min
        setT(5,24.0);
        Thread.currentThread().sleep(60000);
        //5min
        setW(1,2);
        Thread.currentThread().sleep(60000);
        //6min
        turnOff(2);
        Thread.currentThread().sleep(60000);
        //7min
        turnOn(2);setW(5,2);
        Thread.currentThread().sleep(60000);
        //8min
        Thread.currentThread().sleep(60000);
        //9min
        setT(1,22.0);setTW(4,19.0,2);
        Thread.currentThread().sleep(60000);
        //10min
        Thread.currentThread().sleep(60000);
        //11min
        setT(2,23.0);
        Thread.currentThread().sleep(60000);
        //12min
        setW(5,0);
        Thread.currentThread().sleep(60000);
        //13min
        Thread.currentThread().sleep(60000);
        //14min
        turnOff(1);setTW(3,22.0,0);
        Thread.currentThread().sleep(60000);
        //15min
        setTW(5,20.0,2);
        Thread.currentThread().sleep(60000);
        //16min
        turnOff(2);
        Thread.currentThread().sleep(60000);
        //17min
        setW(3,2);
        Thread.currentThread().sleep(60000);
        //18min
        turnOn(1);setTW(4,20.0,1);
        Thread.currentThread().sleep(60000);
        //19min
        turnOn(2);
        Thread.currentThread().sleep(60000);
        //20min
        setT(5,25.0);
        Thread.currentThread().sleep(60000);
        //21min
        Thread.currentThread().sleep(60000);
        //22min
        turnOff(3);
        Thread.currentThread().sleep(60000);
        //23min
        turnOff(5);
        Thread.currentThread().sleep(60000);
        //24min
        turnOff(1);
        Thread.currentThread().sleep(60000);
        //25min
        turnOff(2);turnOff(4);
    }

    //开机
    Conditioner turnOn(Integer roomId){
        Conditioner conditioner = conditionerRepository.findByRoomId(roomId);
        if (conditioner.isAtWork != 0)//空调已开机
            return conditioner;
        return request(roomId, (double)25, 1);
    }
    //关机
    Conditioner turnOff(Integer roomId){
        Conditioner conditioner = conditionerRepository.findByRoomId(roomId);
        if (conditioner.isAtWork == 0)//空调已关机
            return conditioner;
        return conditionerService.StopRequest(roomId);
    }
    //设置温度
    Conditioner setT(Integer roomId, Double temp){
        Conditioner conditioner = conditionerRepository.findByRoomId(roomId);
        if (conditioner.isAtWork == 0)//空调已关机
            return conditioner;
        return request(roomId, temp, conditioner.windSpeed);
    }
    //设置风速
    Conditioner setW(Integer roomId, Integer windSpeed){
        Conditioner conditioner = conditionerRepository.findByRoomId(roomId);
        if (conditioner.isAtWork == 0)//空调已关机
            return conditioner;
        return request(roomId, conditioner.setTemp, windSpeed);
    }
    //设置温度和风速
    Conditioner setTW(Integer roomId, Double temp, Integer windSpeed){
        Conditioner conditioner = conditionerRepository.findByRoomId(roomId);
        if (conditioner.isAtWork == 0)//空调已关机
            return conditioner;
        return request(roomId, temp, windSpeed);
    }
    //可以忽略
    private Conditioner request(Integer roomId, Double temp, Integer windSpeed){
        Date date = new Date();
        Long datetime = date.getTime();
        Conditioner conditioner = conditionerRepository.findByRoomId(roomId);
        Record record = new Record();
        record.userId = conditioner.userId;
        record.roomId = roomId;
        record.windSpeed = windSpeed;
        record.startTemp = conditioner.curTemp;
        record.setTemp = temp;
        record.requestStartTime = datetime;
        conditionerService.ServiceRequest(record);
        return conditionerRepository.findByRoomId(roomId);
    }
}


