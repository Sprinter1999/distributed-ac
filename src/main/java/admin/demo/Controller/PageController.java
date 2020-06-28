package admin.demo.Controller;

import admin.demo.Comment.Result;
import admin.demo.Dto.UserLogin;
import admin.demo.Entity.Conditioner;
import admin.demo.Entity.User;
import admin.demo.Repository.ConditionerRepository;
import admin.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/")
public class PageController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ConditionerRepository conditionerRepository;

    @GetMapping("/login")
    String login(Model model)
    {
        model.addAttribute("userLogin",new UserLogin());
        return "login";
    }

    @GetMapping("/checkOut")
    String checkOut()
    {
        return "checkOut";
    }

    @GetMapping("/record")
    String record()
    {
        return "record";
    }

    @GetMapping("/billPage")
    String billPage()
    {
        return "billPage";
    }

    @PostMapping("/userLogin")
    String userLogin(@ModelAttribute UserLogin userLogin)
    {
        if(userLogin.userId==999)
        {
            return "admin";
        }
        else if(userLogin.userId==9999)
        {
            return "manager";
        }
        else{
            //因为只有五个用户，默认用户id和roomid一致；
            return "user";
        }
    }

    @PostMapping("/userReg")
    String userReg(@ModelAttribute UserLogin userLogin){
        Date date = new Date();
        Long datetime = date.getTime();
        Conditioner conditioner = new Conditioner();
        conditioner.userId = (int)userLogin.userId;
        conditioner.initTemp = userLogin.inittemp;
        conditioner.roomId=(int)userLogin.userId;
        conditionerRepository.save(conditioner);
        conditioner = conditionerRepository.findByUserId((int)userLogin.userId);
        //System.out.println(conditioner);
        User user = new User();
        user.userId = (int)userLogin.userId;
        user.password = userLogin.password;
        user.roomId = conditioner.roomId;
        user.checkin = datetime;
        userRepository.save(user);
        return "user";
    }
//    //用户登录
//    @ApiOperation(value = "用户登录")
//    @PostMapping(value = "login", produces = "application/json")
//    public Result<Object> login(Integer userId, String password, Model model){
//        User user = userRepository.findUserByUserId(userId);
//        if(user.userId==999 && user.password.equals(password)) return Result.error("admin");
//        else if (user.userId == 9999 && user.password.equals(password)) return Result.error("manager");
//
//        if (user == null || !user.password.equals(password)) return Result.error("用户名或密码错误");
//        if (user.checkout != null) return Result.error("用户已退房");
//        Conditioner conditioner = conditionerRepository.findByUserId(userId);
//        model.addAttribute("roominfo",conditioner);
//        return Result.ok(conditioner);
//    }


}
