package admin.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class user {
    @Id
    private Integer user_id;
    private Integer room_id;
    private Double checkin;
    private Double checkout;
}
