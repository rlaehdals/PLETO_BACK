package gugus.pleco.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import gugus.pleco.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);
}
