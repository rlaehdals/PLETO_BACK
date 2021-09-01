package gugus.pleco.service;

import gugus.pleco.controller.EcoController;
import gugus.pleco.controller.dto.UserEcoListDto;
import gugus.pleco.domain.PleeStatus;
import gugus.pleco.domain.User;
import gugus.pleco.domain.UserEco;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface UserEcoService {

    PleeStatus performEco(String username, String ecoName, String pleeName);

    LocalTime UserEcoTime(String email, String ecoName);

    List<UserEcoListDto> UserEcoStatus(String email);

    List<UserEco> findAll(String email);
}
