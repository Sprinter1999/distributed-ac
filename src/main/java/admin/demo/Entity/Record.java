package admin.demo.Entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer recordId;
    public Integer userId;//用户填入
    public Integer roomId;//用户填入
    public Integer windSpeed;//用户填入
    public Double startTemp;//用户填入,需要由服务队列转为等待队列时更新
    public Double setTemp;//用户填入
    public Long requestStartTime;//用户填入,需要在进入等待队列时更新
    public Long actualStartTime;//需要在进入服务队列时更新
    public Long endTime;//结束时填入
    public Double electricity = (double)0;//用户填入实时更新
    public Integer isComplete=0;

    public void setRecord(Record record){
        this.userId = record.userId;
        this.roomId = record.roomId;
        this.windSpeed = record.windSpeed;
        this.setTemp = record.setTemp;
        this.electricity = (double)0;
        this.isComplete = 0;
    }
}
