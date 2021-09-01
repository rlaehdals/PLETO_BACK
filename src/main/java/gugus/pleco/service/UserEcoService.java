package gugus.pleco.service;

import gugus.pleco.domain.User;
import gugus.pleco.domain.UserEco;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserEcoService {

    void confirmCoolTime(String username, String ecoName, LocalDateTime now, String pleeName);


    List<UserEco> UserEcoTime(String email, LocalDateTime now);

}
