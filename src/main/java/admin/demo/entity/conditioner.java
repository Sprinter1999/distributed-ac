package admin.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class conditioner {
    @Id
    private Integer room_id;
    private Long user_id;
    private Integer init_temp;
    private  Double set_temp;
    private  Double cur_temp;
    private  Integer is_at_work;
    private  Double serve_start_time;
    private  Double wait_start_time;
    private  Integer wind_speed;
}