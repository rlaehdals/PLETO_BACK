package gugus.pleco.service;

import gugus.pleco.controller.dto.UserDto;
import gugus.pleco.domain.Eco;
import gugus.pleco.domain.User;
import gugus.pleco.domain.UserEco;
import gugus.pleco.repositroy.EcoRepository;
import gugus.pleco.repositroy.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserEcoServiceImplTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EcoRepository ecoRepository;

    @Autowired
    UserService userService;
    @Autowired
    UserEcoServiceImpl userEcoService;




    @Test
    public void userEco_모두_조회(){
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