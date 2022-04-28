package gugus.pleco.domain.eco.service;

import gugus.pleco.domain.user.dto.UserEcoListDto;
import gugus.pleco.domain.plee.domain.Plee;
import gugus.pleco.domain.eco.domain.UserEco;

import java.time.LocalTime;
import java.util.List;

public interface UserEcoService {

    Plee performEco(String username, String ecoName)throws RuntimeException, Throwable;

    LocalTime OneUserEcoTime(String email, String ecoName);

    List<UserEcoListDto> UserEcoStatus(String email);

    List<UserEco> findAll(String email);
}
