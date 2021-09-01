package gugus.pleco.repositroy;

import gugus.pleco.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import gugus.pleco.domain.UserEco;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserEcoRepository extends JpaRepository<UserEco, Long> {

    @Query("select uc from UserEco uc join fetch uc.user where uc.user.username  = :username order by uc.id")
    List<UserEco> findByUsername(String username);

    @Query("select uc from UserEco  uc join fetch uc.user join fetch uc.eco where uc.user.username= :useranme and uc.eco.ecoName= :ecoName")
    Optional<UserEco> findByUserAndEcoName(@Param("useranme")String username, @Param("ecoName") String ecoName);



}
