package admin.demo;

import admin.demo.Comment.Result;
import admin.demo.Controller.AdminController;
import admin.demo.Entity.Conditioner;
import admin.demo.Entity.Record;
import admin.demo.Repository.ConditionerRepository;
import admin.demo.Service.impl.ConditionerService;
import io.swagger.annotations.ApiOperation;
import org.hibernate.annotations.Synchronize;
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
    public void Test() {


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
        return request(roomId, conditioner.setTemp, windSpeed);
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


