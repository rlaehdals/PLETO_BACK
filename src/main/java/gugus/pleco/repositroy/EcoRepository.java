package gugus.pleco.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import gugus.pleco.domain.Eco;

public interface EcoRepository extends JpaRepository<Eco, Long> {
}
