package admin.demo.Repository;

import admin.demo.Entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface BillRepository extends JpaRepository<Bill,String> {
    @Query(value = "select totalFee from Bill where userId=?1")
    public Double getTotalfeeById(Integer user_id);
}
