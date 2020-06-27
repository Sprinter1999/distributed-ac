package admin.demo.Repository;

import admin.demo.Entity.Conditioner;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ConditionerRepository extends JpaRepository<Conditioner,Integer> {
    //中央空调部分的调用
    //ArrayList<Conditioner> findByIsAtWorkEqualsAndWindSpeedLessThanAndOrderByWindSpeedAndOrderByServeStartTime(Integer isAtWork, Integer windSpeed);//查找处于服务中低于给定风速的空调，按风速由低到高排列，风速相等按服务时间由长到短排列
    //ArrayList<Conditioner> findByIsAtWorkEqualsOrderByWindSpeedDescAndOrderByWaitStartTime(Integer isAtWork);
    ArrayList<Conditioner> findByIsAtWorkEqualsOrderByServeStartTimeDesc(Integer isAtWork);
    ArrayList<Conditioner> findByIsAtWorkOrderByWaitStartTimeAsc(Integer isAtWork);
    ArrayList<Conditioner> findByIsAtWorkOrderByServeStartTimeAsc(Integer isAtWork);
    ArrayList<Conditioner> findByIsAtWork(Integer isAtWork);
    ArrayList<Conditioner> findAll();
    Conditioner findByRoomId(Integer roomId);
    @Query(value = "select c from Conditioner c where c.userId = ?1")
    Conditioner findByUserId(Integer userId);
    @Query(value = "select c from Conditioner c where c.isAtWork = ?1 and c.windSpeed < ?2")//找到正在服务队列中最应该被移出的
    ArrayList<Conditioner> findByIsAtWorkAndWindSpeedLessThanAndOrder(@Param("isAtWork")Integer isAtWork, @Param("windSpeed")Integer windSpeed, Sort sort);
    @Query(value = "select c from Conditioner c where c.isAtWork = ?1")//找到正在等待队列中最应该被服务的
    ArrayList<Conditioner> findByIsAtWorkAndOrder(Integer isAtWork, Sort sort);
}
