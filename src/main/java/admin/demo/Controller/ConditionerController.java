package admin.demo.Controller;

import admin.demo.Comment.Result;
import admin.demo.Entity.Record;
import admin.demo.Repository.ConditionerRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class ConditionerController {
    @Autowired
    ConditionerRepository conditionerRepository;

    @PostMapping
    @ApiOperation(value = "显示模式、风速、温度")
    public Record postWinSpeedandCurrentTemp{

        return conditionerRepository.getWindSpeedandCurrentTemp();
    }

    @PutMapping
    @ApiOperation(value = "设置温度")
    public Result<Object> setTemp(@RequestParam Integer room_id, @RequestParam Double set_temp){

        conditionerRepository.updateTempByRoomId(set_temp,room_id);
        return Result.ok("success");
    }

    @PutMapping
    @ApiOperation(value = "设置风速")
    public Result<Object> setWindSpeed(@RequestParam Integer room_id, @RequestParam Integer wind_speed){

        conditionerRepository.updateWindSpeedByRoomId(wind_speed,room_id);
        return Result.ok("success");
    }

}
