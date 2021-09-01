package gugus.pleco.service;

import gugus.pleco.controller.dto.UserDto;
import gugus.pleco.domain.Eco;
import gugus.pleco.domain.User;
import gugus.pleco.domain.UserEco;
import gugus.pleco.excetion.TimeDissatisfactionException;
import gugus.pleco.repositroy.EcoRepository;
import gugus.pleco.repositroy.PleeRepository;
import gugus.pleco.repositroy.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class UserEcoServiceImplTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PleeRepository pleeRepository;
    @Autowired
    EcoRepository ecoRepository;

    @Autowired
    UserService userService;
    @Autowired
    PleeServiceImpl pleeService;
    @Autowired
    UserEcoServiceImpl userEcoService;


    @Test
    public void 에코_수행_성공() {
        //given
        Eco tumbler = Eco.createEco("텀블러 사용하기", 1000L);
        ecoRepository.save(tumbler);

        String email = "rkdlem48@gmail.com";
        createUser(email, "1234");
        Long pleeId = pleeService.createGrowPlee(email, "장미", 10L);

        //when
        userEcoService.performEco(email, tumbler.getEcoName());

        //then
        // 플리의 에코 카운트가 증가해야한다.
        assertThat(pleeRepository.findById(pleeId).get().getEcoCount()).isEqualTo(1);

    }

    @Test
    public void 에코_수행실패() {
        //given
        Eco tumbler = Eco.createEco("텀블러 사용하기", 1000L);
        ecoRepository.save(tumbler);

        String email = "rkdlem48@gmail.com";
        createUser(email, "1234");
        Long pleeId = pleeService.createGrowPlee(email, "장미", 10L);

        // 첫 번째 에코 수행
        userEcoService.performEco(email, tumbler.getEcoName());

        //when
        // 첫 번째 에코 수행 후 바로 두 번째 에코 수행
        Throwable e = assertThrows(TimeDissatisfactionException.class,
                () -> userEcoService.performEco(email, tumbler.getEcoName()));

        //then
        String message = e.getMessage();
        assertThat(message).isEqualTo("아직 미션을 할 수 있는 시간이 아닙니다.");
    }

    @Test
    public void userEco_모두_조회() {
        //given
        Eco tumbler = Eco.createEco("텀블러 사용하기", 1000L);
        Eco bucket = Eco.createEco("장바구니 사용하기", 2000L);

        ecoRepository.save(tumbler);
        ecoRepository.save(bucket);

        String email = "rkdlem48@gmail.com";
        createUser(email, "1234");

        User user = userRepository.findByUsername(email).get();
//        System.out.println("실제 에코 : " + user.getUserEcoList().get(0).getEco().getEcoName());

        //when
        List<UserEco> userEcos = userEcoService.findAll(email);

        //then

        assertThat(userEcos.size()).isEqualTo(2);

    }

    private void createUser(String email, String password) {

        UserDto userDto = new UserDto(email, password);
        userService.join(userDto);
    }
}