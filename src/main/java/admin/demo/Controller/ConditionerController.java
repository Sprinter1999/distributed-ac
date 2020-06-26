package admin.demo.Controller;

import admin.demo.Comment.Result;
import admin.demo.Entity.Conditioner;
import admin.demo.Entity.Record;
import admin.demo.Entity.User;
import admin.demo.Repository.ConditionerRepository;
import admin.demo.Repository.UserRepository;
import admin.demo.Service.impl.ConditionerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.Configuration;
import java.util.Date;

@Controller
@RequestMapping("/conditioner")
public class ConditionerController {
    @Autowired
    ConditionerRepository conditionerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ConditionerService conditionerService;

    @ApiOperation(value = "风速、温度")
    @PostMapping(value = "")
    public Result<Object> getRoomInfo(@RequestParam Integer roomId){
        Conditioner conditioner = conditionerRepository.findByRoomId(roomId);
        return Result.ok(conditioner);
    }

    @ApiOperation(value = "设置温度、风速")
    @GetMapping(value = "/request")
    public Result<Object> setTemp(@RequestParam Integer roomId, @RequestParam Double temp, @RequestParam Integer windSpeed){
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
        return Result.ok(conditioner);
    }



}
