package admin.demo.Controller;

import admin.demo.Entity.Bill;
import admin.demo.Entity.Record;
import admin.demo.Repository.BillRepository;
import admin.demo.Repository.RecordRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import admin.demo.Dto.ReceptionBill;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reception")
public class ReceptionController {
    @Autowired
    BillRepository billrepository;
    @Autowired
    RecordRepository recordrepository;


    @ApiOperation(value = "返回账单页面")
    @RequestMapping(value = "/bill",method = RequestMethod.GET)
    public String getBill(){
        return "bill";
    }

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

    @ApiOperation(value = "返回详单页面")
    @RequestMapping(value = "/invoice",method = RequestMethod.GET)
    public String getInvoice(){
        return "invoice";
    }

    @ApiOperation(value = "根据用户ID查询详单",notes = "返回用户住房时间内的详单")
    @RequestMapping(value ="/invoice",method =RequestMethod.POST)
    @ResponseBody
    public String postInvoice(Model model,@RequestParam Integer user_id){

       //record sort=recordrepository.getByUserId(user_id);
       List<Record> records=new ArrayList<Record>(recordrepository.getByUserId(user_id));
       model.addAttribute("record",records);
       return "invoice";

    }
}
