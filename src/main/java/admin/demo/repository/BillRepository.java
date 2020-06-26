package admin.demo.repository;

import admin.demo.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface BillRepository extends JpaRepository<Bill,String> {
    @Query(value = "select total_fee from Bill where user_id=?1")
    public Double getTotalfeeById(Integer user_id);
}
