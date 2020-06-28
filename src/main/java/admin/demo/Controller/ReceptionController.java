package admin.demo.Controller;

import admin.demo.Entity.Bill;
import admin.demo.Entity.Record;
import admin.demo.Entity.User;
import admin.demo.Repository.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import admin.demo.Dto.ReceptionBill;
import org.thymeleaf.context.Context;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/reception")
public class ReceptionController {
    @Autowired
    BillRepository billrepository;
    @Autowired
    RecordRepository recordrepository;
    @Autowired
    UserRepository userRepository;

//    @ApiOperation(value = "返回首页")
//    @RequestMapping(value="/",method=RequestMethod.GET)
//    public String getIndex() {return "index";}


//    @ApiOperation(value = "返回账单页面")
//    @RequestMapping(value = "/bill",method = RequestMethod.GET)
//    public String getBill(){
//        return "billPage";
//    }

    @ApiOperation(value = "根据用户ID查询账单",notes = "返回用户所住房间这段时间的账单")
    @RequestMapping(value = "/bill",method = RequestMethod.POST)
    @ResponseBody
    public String postBill( @RequestParam Integer user_id){
         Bill bill = new Bill();
         User user = userRepository.findUserByUserId(user_id);
         bill.setCheckin(user.checkin);
         bill.setCheckout(user.checkout);
         bill.setRoomId(user.roomId);
         bill.setUserId(user_id);
         bill.setTotalFee(recordrepository.gettTotalfeeByUserId(user_id));
         billrepository.save(bill);
        Date datein = new Date(bill.checkin);
        Date dateout = new Date(bill.checkout);
        String data = "用户id："+bill.userId.toString()+" 房间号:"+bill.roomId+" 入住时间:"+datein+" 退房时间:"+dateout+" 总费用:"+bill.totalFee;
        return data;
    }

//    @ApiOperation(value = "返回详单页面")
//    @RequestMapping(value = "/record",method = RequestMethod.GET)
//    public String getRecord(){
//        return "record";
//    }

    @ApiOperation(value = "根据用户ID查询详单",notes = "返回用户住房时间内的详单")
    @RequestMapping(value ="/record",method =RequestMethod.POST)
    @ResponseBody
    public String postRecord(Model model,@RequestParam Integer user_id){
       //record sort=recordrepository.getByUserId(user_id);
       List<Record> records=new ArrayList<Record>(recordrepository.getByUserId(user_id));
       model.addAttribute("records",records);
       return "record";

    }
}
