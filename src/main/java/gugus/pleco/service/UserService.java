package gugus.pleco.service;

import gugus.pleco.excetion.UserDupulicatedException;
import javassist.bytecode.DuplicateMemberException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import gugus.pleco.controller.dto.UserDto;
import gugus.pleco.domain.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

    User join(UserDto userDto) throws UserDupulicatedException;


    User login(UserDto userDto) throws UsernameNotFoundException, BadCredentialsException;

    User findById(Long id);

    boolean checkEmail(String email) throws UserDupulicatedException;
}
