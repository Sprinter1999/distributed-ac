package admin.demo.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionerRepository {
    @Query("select wind_speed,cur_temp from conditioner")
    public int getWindSpeedandCurrentTemp();

    @Query("update conditioner as c set c.set_temp=?1 where c.room_id=?2")
    public void updateTempByRoomId(Double set_temp,Integer room_id);

    @Query("update conditioner as c set c.wind_speed=?1 where c.room_id=?2")
    public void updateWindSpeedByRoomId(Integer wind_speed,Integer room_id);
}
