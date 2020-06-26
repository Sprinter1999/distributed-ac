package admin.demo.Repository;

import admin.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User,Integer> {
    User findUserByUserId(Integer userId);

    User findByRoomId(Integer roomId);
}
