package admin.demo.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import admin.demo.comment.ManagerReport;
import admin.demo.entity.record;
import admin.demo.repository.recordRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/manager")
public class ManagerController {

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
     //转换日期格式
     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
     //注册自定义的编辑器
     binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Autowired
    recordRepository recordrepository;

    @GetMapping
    @ApiOperation(value = "获取页面")
    public String manager__get(){
        return "a";
    }

    @PostMapping
    //@ResponseBody
    @ApiOperation(value = "返回日期")
    public String manager__post(Model model,@RequestParam("startTime") Date start, @RequestParam("endTime") Date end){
        Long starttime = start.getTime();
        Long endtime = end.getTime();
        List<ManagerReport> reports = new ArrayList<ManagerReport>();//用于汇总格式化报告
        Long[] tempcount = new Long[31];//记录每个目标温度使用时间；
        Long[] windcount = new Long[3];//记录每个风速使用时间
        int goaltimes = 0;//记录达到目标次数
        int scheduletimes = 0;//记录被调度次数
        int usetime = 0;//记录被使用次数
        Double electotal = 0.0;//记录总电量

        Integer i = 0;
        int upline = recordrepository.querybigroomid();
        for(i=1;i<=upline;i++){

            //查询有用日期内roomid为i的所有记录
            List<record> roomrecords = recordrepository.findByRoomIdAndEndTimeGreaterThanAndRequestStartTimeIsLessThanOrderByRequestStartTime(i,starttime,endtime);
            if(roomrecords.isEmpty()){//若无记录则跳过该房间
                continue;
            }
            ManagerReport temp = new ManagerReport();//临时存放单个房间记录
            //初始化统计用变量
            for(int j=0;j<31;j++)
                tempcount[j]= Long.valueOf(0);
            windcount[0]= Long.valueOf(0);
            windcount[1]= Long.valueOf(0);
            windcount[2]= Long.valueOf(0);
            goaltimes = 0;//记录达到目标次数
            scheduletimes = 0;//记录被调度次数
            usetime = 0;//记录被使用次数
            electotal = 0.0;//记录总电量
            //初始化结束

            temp.setRoomId(i);//房间号
            temp.setRecordNum(roomrecords.size());//详单数
            int tempuse;
            //统计各风速、温度使用时间
            for(record record : roomrecords){
                tempuse = record.set_temp.intValue();
                if(record.actual_start_time<starttime&&record.end_time<endtime){
                    tempcount[tempuse] = tempcount[tempuse] + record.end_time - starttime;
                    windcount[record.wind_speed] = windcount[record.wind_speed] + record.end_time - starttime;
                    goaltimes++;
                }
                else if(record.end_time>endtime&&record.actual_start_time>starttime){
                    tempcount[tempuse] = tempcount[tempuse] + endtime - record.actual_start_time;
                    windcount[record.wind_speed] = windcount[record.wind_speed] + endtime - record.actual_start_time;
                    scheduletimes++;
                }
                else {
                    tempcount[tempuse] = tempcount[tempuse] + record.end_time - record.actual_start_time;
                    windcount[record.wind_speed] = windcount[record.wind_speed] + record.end_time - record.actual_start_time;
                    goaltimes++;
                    usetime++;
                    scheduletimes++;
                }
                electotal=record.electricity+electotal;
            }


            Long maxtime = tempcount[18];
            int max=18;
            for(int j=19;j<31;j++){
                if(tempcount[j]>maxtime) {
                    maxtime = tempcount[j];
                    max=j;
                }
            }
            temp.setMostUseTem(Double.valueOf(max));//最常用温度
            maxtime = windcount[0];
            max=0;
            for(int j=1;j<3;j++){
                if(windcount[j]>maxtime) {
                    maxtime = tempcount[j];
                    max=j;
                }
            }
            temp.setMostUseWind(max);//最常用风速
            temp.setGoalCount(goaltimes);//达到目标次数
            temp.setUseCount(usetime);//被使用次数
            temp.setScheduleCount(scheduletimes);//被调度次数
            temp.setCostTotal(electotal);//总费用
            reports.add(temp);
        }
        model.addAttribute("reports",reports);
        return "a";//用的时候改成展示页面的html名
    }

}
