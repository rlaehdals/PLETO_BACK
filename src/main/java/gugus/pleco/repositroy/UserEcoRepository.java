package gugus.pleco.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import gugus.pleco.domain.UserEco;

public interface UserEcoRepository extends JpaRepository<UserEco, Long> {
}
