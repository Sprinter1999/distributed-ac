package admin.demo.Controller;

import admin.demo.Comment.Result;
import admin.demo.Entity.Conditioner;
import admin.demo.Entity.Record;
import admin.demo.Repository.AdminRepository;
import admin.demo.Repository.ConditionerRepository;
import admin.demo.Service.impl.ConditionerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminRepository adminrepository;
    @Autowired
    ConditionerRepository conditionerRepository;
    @Autowired
    ConditionerService conditionerService;

    @ApiOperation(value = "控制空调开关",notes = "拨转is_at_work状态")
    @GetMapping(value = "/{roomId}",produces = "application/json")
    public Result<Object> controlRoom(@PathVariable Integer roomId, @RequestParam Integer isAtWork){
        if (isAtWork == 0){
            conditionerService.StopRequest(roomId);
            return Result.ok("成功关机");
        }else {
            Date date = new Date();
            Long datetime = date.getTime();
            Conditioner conditioner = conditionerRepository.findByRoomId(roomId);
            if (conditioner.isAtWork != 0)
                return Result.error("已经开机");
            Record record = new Record();
            record.userId = conditioner.userId;
            record.roomId = roomId;
            record.windSpeed = 1;
            record.startTemp = conditioner.curTemp;
            record.setTemp = (double)25;
            record.requestStartTime = datetime;
            conditionerService.ServiceRequest(record);
            return Result.ok(conditioner);
        }
    }

    @ApiOperation(value = "查询所有用户",notes = "查询所有房间信息")
    @GetMapping(value = "/monitor")
    @ResponseBody
    public List<Conditioner> getMonitor(){
        return  adminrepository.findAll();
}
}
