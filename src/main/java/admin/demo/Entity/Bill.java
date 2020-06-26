package admin.demo.Entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer transactionId;
    public Integer userId;
    public Integer roomId;
    public Long checkin;
    public Long checkout;
    public Double totalFee;
}
