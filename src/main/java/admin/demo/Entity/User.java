package admin.demo.Entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class User {
    @Id
    public Integer userId;
    public String password;
    public Integer roomId;
    public Long checkin;
    public Long checkout;
}
