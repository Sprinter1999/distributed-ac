package admin.demo.Repository;

import admin.demo.Dto.ReceptionBill;
import admin.demo.Entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface BillRepository extends JpaRepository<Bill,String> {
    @Query(value = "select * from bill where user_id=?1",nativeQuery=true)
    public Bill getBillById(Integer user_id);
}
