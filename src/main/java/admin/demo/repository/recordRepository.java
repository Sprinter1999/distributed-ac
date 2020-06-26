package admin.demo.repository;

import admin.demo.entity.record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface recordRepository extends JpaRepository<record,Integer> {
    @Query(value = "select*from  record where user_id=?1",nativeQuery=true)
    public record getByUserId(Integer user_id);


    List<record> findByRoomIdAndEndTimeGreaterThanAndRequestStartTimeIsLessThanOrderByRequestStartTime(Integer RoomId,Long EndTime,Long RequestStartTime);

    @Query("select max(r.room_id) from record r")
    Integer querybigroomid();
}
