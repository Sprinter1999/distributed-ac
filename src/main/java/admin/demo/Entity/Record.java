package admin.demo.Entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Record {
    @Id
    public Integer recordId;
    public Integer userId;
    public Integer roomId;
    public Integer windSpeed;
    public Double startTemp;
    public Double setTemp;
    public Long requestStartTime;
    public Long actualStartTime;
    public Long endTime;
    public Double electricity;
    public Integer isComplete;
}
