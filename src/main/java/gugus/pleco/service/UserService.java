package gugus.pleco.service;

import gugus.pleco.excetion.UserDupulicatedException;
import org.springframework.security.core.userdetails.UserDetailsService;
import gugus.pleco.controller.dto.UserDto;
import gugus.pleco.domain.User;

public interface UserService extends UserDetailsService {

    Long join(UserDto userDto) throws UserDupulicatedException;


    Long login(UserDto userDto);

    User findById(Long id);
}
