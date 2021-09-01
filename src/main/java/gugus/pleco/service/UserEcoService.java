package gugus.pleco.service;

import gugus.pleco.domain.User;
import gugus.pleco.domain.UserEco;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserEcoService {

    void performEco(String username, String ecoName);

    List<UserEco> findAll(String email);

    List<UserEco> UserEcoTime(String email, LocalDateTime now);

}
