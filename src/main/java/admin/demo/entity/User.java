package admin.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class User {
    @Id
    private Integer userId;
    private Integer roomId;
    private Double checkin;
    private Double checkout;
}
