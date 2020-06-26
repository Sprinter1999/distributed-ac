package admin.demo.comment;

import lombok.Data;

@Data
public class ManagerReport {
    int roomId;
    int useCount;//使用次数
    Double mostUseTem;//最常用温度
    int mostUseWind;//最常用风速
    int goalCount;//达到目标次数
    int scheduleCount;//被调度次数
    int recordNum;//详单数
    Double costTotal;//总费用

    public ManagerReport(){
        useCount=0;//使用次数
        mostUseTem=0.0;//最常用温度
        mostUseWind=0;//最常用风速
        goalCount=0;//达到目标次数
        scheduleCount=0;//被调度次数
        recordNum=0;//详单数
        costTotal=0.0;//总费用
    }

}
