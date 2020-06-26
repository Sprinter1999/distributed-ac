package admin.demo.controller;

import admin.demo.entity.record;
import admin.demo.repository.billRepository;
import admin.demo.repository.recordRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/reception")
public class receptionController {
    @Autowired
    billRepository billrepository;
    @Autowired
    recordRepository recordrepository;

    @ApiOperation(value = "返回账单页面")
    @RequestMapping(value = "/bill",method = RequestMethod.GET)
    @ResponseBody
    public String getBill(){
        return "bill.html";
    }

    @ApiOperation(value = "根据用户ID查询费用",notes = "返回用户所住房间这段时间应该付多少钱")
    @RequestMapping(value = "/bill",method = RequestMethod.POST)
    @ResponseBody
    public Double postBill(@RequestParam Integer user_id){

        return billrepository.getTotalfeeById(user_id);
    }

    @ApiOperation(value = "返回详单页面")
    @RequestMapping(value = "/invoice",method = RequestMethod.GET)
    @ResponseBody
    public String getInvoice(){
        return "invoice.html";
    }

    @ApiOperation(value = "根据用户ID查询详单",notes = "返回用户住房时间内的详单")
    @RequestMapping(value ="/invoice",method =RequestMethod.POST)
    @ResponseBody
    public record postInvoice(@RequestParam Integer user_id){

       //record sort=recordrepository.getByUserId(user_id);
       return recordrepository.getByUserId(user_id);

    }
}
