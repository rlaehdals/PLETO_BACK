package gugus.pleco.domain.user.service;

import gugus.pleco.util.excetion.UserDupulicatedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import gugus.pleco.domain.user.domain.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

    User join(String email, String password) throws UserDupulicatedException;


    String login(String email, String password) throws UsernameNotFoundException, BadCredentialsException, Throwable;

    User findById(Long id);

    boolean checkEmail(String email) throws UserDupulicatedException;
}
