package admin.demo.Repository;

import admin.demo.Entity.Conditioner;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Mapper
@Repository
@Transactional
public interface AdminRepository extends JpaRepository<Conditioner,Integer> {
    @Modifying
    @Query("update Conditioner as c set c.isAtWork=?1 where c.roomId=?2")
    public void updateByRoomId(Integer is_at_work,Integer room_id);

}
