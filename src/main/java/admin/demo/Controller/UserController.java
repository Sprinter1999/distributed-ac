package admin.demo.Controller;

import admin.demo.Comment.Result;
import admin.demo.Entity.Conditioner;
import admin.demo.Entity.User;
import admin.demo.Repository.ConditionerRepository;
import admin.demo.Repository.UserRepository;
import admin.demo.Service.impl.ConditionerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;


@RestController
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ConditionerRepository conditionerRepository;
    @Autowired
    ConditionerService conditionerService;


    //用户注册
    @ApiOperation(value = "用户注册",notes = "存储新用户并跳转")
    @PostMapping(value = "/checkin", produces = "application/json")
    public Result<Object> login(Integer userId, String password, Double initTemp, Model model){
        Date date = new Date();
        Long datetime = date.getTime();
        if (userRepository.findUserByUserId(userId) != null) return Result.error("用户已注册");
        Conditioner conditioner = new Conditioner();
        conditioner.userId = userId;
        conditioner.initTemp = initTemp;
        conditioner.roomId=userId;
        conditionerRepository.save(conditioner);
        conditioner = conditionerRepository.findByUserId(userId);
        //System.out.println(conditioner);
        User user = new User();
        user.userId = userId;
        user.password = password;
        user.roomId = conditioner.roomId;
        user.checkin = datetime;
        userRepository.save(user);
        return Result.ok(user);
    }

    @ApiOperation(value = "用户退房")
    @PostMapping(value = "/checkout", produces = "application/json")
    public Result<Object> checkout(Integer userId, String password){
        User user = userRepository.findUserByUserId(userId);
        if (user == null || !user.password.equals(password)) return Result.error("用户名或密码错误");
        if (user.checkout != null) return Result.error("用户已退房");
        conditionerService.CheckOut(userId);
        return Result.ok("退房成功");
    }

}
