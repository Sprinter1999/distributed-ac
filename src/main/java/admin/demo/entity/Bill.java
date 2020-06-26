package admin.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Bill {
    @Id
    public String reansactionId;
    public Integer roomId;
    public Integer userId;
    public Double checkin;
    public Double checkout;
    public Double totalFee;
}
