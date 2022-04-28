package gugus.pleco.domain.eco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import gugus.pleco.domain.eco.domain.Eco;

public interface EcoRepository extends JpaRepository<Eco, Long> {
}
