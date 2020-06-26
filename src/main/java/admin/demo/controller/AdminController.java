package admin.demo.controller;

import admin.demo.comment.Result;
import admin.demo.entity.Conditioner;
import admin.demo.repository.AdminRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminRepository adminrepository;

    @ApiOperation(value = "控制空调开关",notes = "拨转is_at_work状态")
    @RequestMapping(value = "/{roomID}",method = RequestMethod.PUT)
    @ResponseBody
    public Result<Object> controlRoom(@RequestParam Integer room_id, @RequestParam Integer is_at_work){

        adminrepository.updateByRoomId(is_at_work, room_id);
        return Result.ok("success");
    }

    @ApiOperation(value = "查询所有用户",notes = "查询所有房间信息")
    @GetMapping(value = "/monitor")
    @ResponseBody
    public List<Conditioner> getMonitor(){

        return  adminrepository.findAll();
}
}
