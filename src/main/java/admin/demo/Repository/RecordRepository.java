package admin.demo.Repository;

import admin.demo.Entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record,Integer> {
    @Query(value = "select*from  record where user_id=?1",nativeQuery=true)
    public Record getByUserId(Integer user_id);


    List<Record> findByRoomIdAndEndTimeGreaterThanAndRequestStartTimeIsLessThanOrderByRequestStartTime(Integer RoomId, Long EndTime, Long RequestStartTime);

    @Query("select max(r.roomId) from Record r")
    Integer querybigroomid();

    //中央空调部分的调用
    Record findByRoomIdAndIsComplete(Integer roomId, Integer isComplete);
    List<Record> findByRoomId(Integer id);
    void deleteByRoomIdAndIsComplete(Integer roomId, Integer isComplete);
}
