package admin.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class record {
    @Id
    private Integer record_id;
    private Integer user_id;
    private Integer room_id;
    public Integer wind_speed;
    private Double start_temp;
    public Double set_temp;
    private Long request_start_time;
    public Long actual_start_time;
    public Long end_time;
    public Double electricity;
    private Integer is_complete;
}
