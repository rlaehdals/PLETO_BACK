package gugus.pleco.service;

import gugus.pleco.domain.user.dto.UserDto;
import gugus.pleco.domain.user.domain.User;
import gugus.pleco.domain.user.service.UserService;
import gugus.pleco.util.excetion.UserDuplicatedException;
import gugus.pleco.util.jwt.JwtTokenProvider;
import gugus.pleco.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplTest {


    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    JwtTokenProvider jwtTokenProvider;


    @BeforeEach
    public void init() {

        User user1 = User.builder()
                .password(passwordEncoder.encode("asdf"))
                .username("rkdlem48@gmail.com")
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        User user2 = User.builder()
                .password("asdf")
                .username("asf@gmail.com")
                .roles(Collections.singletonList("ROLE_USER"))
                .build();


        userRepository.save(user1);
        userRepository.save(user2);
    }

    @AfterEach
    public void destroy() {
        userRepository.deleteAll();
    }


    @Test
    void userRepositoryTest() {
        User saveUser = userRepository.findByUsername("rkdlem48@gmail.com").get();

        assertThat(saveUser.getUsername()).isEqualTo("rkdlem48@gmail.com");
    }

    @Test
    void UserList() {

        List<User> all = userRepository.findAll();

        assertThat(all.size()).isEqualTo(2);
    }

    @Test
    void join() {
        UserDto userDto = new UserDto("rlaehdals@gmail.com", "asdf");
        User user = userService.join(userDto.getEmail(), userDto.getPassword());


        assertThat(userDto.getEmail()).isEqualTo(user.getUsername());

    }

    @Test
    void joinError() {
        //생성
        UserDto userDto = new UserDto("rkdlem48@gmail.com", "asdf");
        //실행
        assertThrows(UserDuplicatedException.class, () -> userService.join(userDto.getEmail(), userDto.getPassword()));
    }

    @Test
    void loginSuccess() throws Throwable{
        UserDto userDto = new UserDto("rkdlem48@gmail.com", "asdf");
        User user = userRepository.findByUsername("rkdlem48@gmail.com").get();
        assertThat(1L).isEqualTo(user.getId());
    }

    @Test
    void loginCanNotFindEmail() {
        UserDto userDto = new UserDto("aaaa@gmail.com", "asdf");

        assertThrows(UsernameNotFoundException.class, () -> userService.login(userDto.getEmail(), userDto.getPassword()));


    }

    @Test
    void loginFailWrongPassword() {

        UserDto userDto = new UserDto("rkdlem48@gmail.com", "1234");

        assertThrows(BadCredentialsException.class, () -> userService.login(userDto.getEmail(), userDto.getPassword()));
    }
}