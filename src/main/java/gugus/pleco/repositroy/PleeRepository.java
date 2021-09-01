package gugus.pleco.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import gugus.pleco.domain.Plee;

public interface PleeRepository extends JpaRepository<Plee, Long> {
}
