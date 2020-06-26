package admin.demo.Entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Conditioner {
    @Id
    public Integer roomId;
    public Long userId;
    public Integer initTemp;
    public  Double setTemp;
    public  Double curTemp;
    public  Integer isAtWork;
    public  Double serveStartTime;
    public  Double waitStartTime;
    public  Integer windSpeed;
}