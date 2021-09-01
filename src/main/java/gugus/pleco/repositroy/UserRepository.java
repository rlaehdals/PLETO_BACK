package gugus.pleco.repositroy;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import gugus.pleco.domain.User;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select u from User u where u.username= :username order by u.username")
    Optional<User> findByUsername(String username);
}
