package gugus.pleco.repositroy;

import gugus.pleco.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import gugus.pleco.domain.Plee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PleeRepository extends JpaRepository<Plee, Long> {


    @Query("select p from Plee p join fetch p.user where p.user= :user")
    List<Plee> findByUser(@Param("user") User user);

    Optional<Plee> findByPleeName(String pleeName);


    @Query("select p from Plee p join fetch p.user where p.user= :user and p.pleeName= :pleeName")
    Optional<Plee> findByPleeNameAndUser(@Param("user") User user, @Param("pleeName") String pleeName);
}
