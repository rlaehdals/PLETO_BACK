package gugus.pleco.service;

import gugus.pleco.controller.userdto.UserEcoListDto;
import gugus.pleco.domain.Plee;
import gugus.pleco.domain.UserEco;

import java.time.LocalTime;
import java.util.List;

public interface UserEcoService {

    Plee performEco(String username, String ecoName)throws RuntimeException, Throwable;

    LocalTime OneUserEcoTime(String email, String ecoName);

    List<UserEcoListDto> UserEcoStatus(String email);

    List<UserEco> findAll(String email);
}
