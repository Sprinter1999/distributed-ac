package admin.demo.repository;

import admin.demo.entity.bill;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface billRepository extends JpaRepository<bill,String> {
    @Query(value = "select total_fee from bill where user_id=?1")
    public Double getTotalfeeById(Integer user_id);
}
