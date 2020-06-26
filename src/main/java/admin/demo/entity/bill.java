package admin.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class bill {
    @Id
    private String reansaction_id;
    private Integer room_id;
    private Integer user_id;
    private Double checkin;
    private Double checkout;
    private Double total_fee;
}
