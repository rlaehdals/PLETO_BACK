package gugus.pleco.domain.plee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import gugus.pleco.domain.plee.domain.Plee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PleeRepository extends JpaRepository<Plee, Long> {


    @Query("select p from Plee p join fetch p.user where p.user.username= :username")
    List<Plee> findByUser(@Param("username") String username);

    Optional<Plee> findByPleeName(String pleeName);


    @Query("select p from Plee p join fetch p.user where p.user.username= :username")
    List<Plee> findPleeByUsername(@Param("username")String username);

    @Query("select p from Plee p join fetch p.user where p.user.username= :username and p.pleeName= :pleeName")
    Optional<Plee> findByPleeNameAndUser(@Param("username") String username, @Param("pleeName") String pleeName);
}
