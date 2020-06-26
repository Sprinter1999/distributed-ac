package admin.demo.repository;

import admin.demo.entity.conditioner;
import org.hibernate.annotations.SQLUpdate;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper
@Repository
@Transactional
public interface adminRepository extends JpaRepository<conditioner,Integer> {
    @Modifying
    @Query("update conditioner as c set c.is_at_work=?1 where c.room_id=?2")
    public void updateByRoomId(Integer is_at_work,Integer room_id);

}
