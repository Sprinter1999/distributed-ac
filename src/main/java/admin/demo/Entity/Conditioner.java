package admin.demo.Entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Conditioner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer roomId;
    public Integer userId;
    public Double initTemp = (double)25;
    public Double setTemp = (double)25;
    public Double curTemp = (double)25;
    public Integer isAtWork = 0;//0不工作、1等待、2服务、3待机
    public Long serveStartTime = (long)0;
    public Long waitStartTime = (long)0;
    public Integer windSpeed = 0;//0低风、1中风、2高风

    public void setRecord(Record record){
        this.setTemp = record.setTemp;
        this.curTemp = record.startTemp;
        this.windSpeed = record.windSpeed;
    }
}