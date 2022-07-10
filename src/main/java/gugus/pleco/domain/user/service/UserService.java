package gugus.pleco.domain.user.service;

import gugus.pleco.util.excetion.UserDuplicatedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import gugus.pleco.domain.user.domain.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

public interface UserService extends UserDetailsService {

    User join(String email, String password);


    Map<String,String> login(String email, String password) ;
    String useRefreshTokenForAccessToken(String refreshToken);

    boolean checkEmail(String email);
}
