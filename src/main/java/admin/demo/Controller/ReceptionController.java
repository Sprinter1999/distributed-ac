package admin.demo.Controller;

import admin.demo.Entity.Bill;
import admin.demo.Entity.Record;
import admin.demo.Repository.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import admin.demo.Dto.ReceptionBill;
import org.thymeleaf.context.Context;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reception")
public class ReceptionController {
    @Autowired
    BillRepository billrepository;
    @Autowired
    RecordRepository recordrepository;

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
    public ReceptionBill postBill( @RequestParam Integer user_id){
        ReceptionBill userbill=new ReceptionBill();
         Bill bill=billrepository.getBillById(user_id);
         userbill.setUserId(bill.userId);
         userbill.setRoomId(bill.roomId);
         userbill.setCostTotal(bill.totalFee);
         userbill.setCheckin(bill.checkin);
         userbill.setCheckout(bill.checkout);
        //model.addAttribute("userbill",userbill);
        return userbill;
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
