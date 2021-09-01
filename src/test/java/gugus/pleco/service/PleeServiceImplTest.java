package gugus.pleco.service;

import gugus.pleco.domain.PleeStatus;
import gugus.pleco.domain.User;
import gugus.pleco.repositroy.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PleeServiceImplTest {

    @Autowired UserRepository userRepository;
    @Autowired PleeServiceImpl pleeService;
    @Autowired PasswordEncoder passwordEncoder;

    @BeforeEach
    public void init(){

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
    public void destroy(){
        userRepository.deleteAll();
    }

    @Test
    public void 그로우플리_생성(){
        //given
        String email = "rkdlem48@gmail.com";
        String pleeName = "장미";
        Long completeCount = 10L;

        //when
        Long pleeId = pleeService.createGrowPlee(email, pleeName, completeCount);

        //then
        assertThat(pleeService.getGrowPlee(email).getPleeName())
                .isEqualTo(pleeName);
        assertThat(pleeService.getGrowPlee(email).getPleeStatus())
                .isEqualTo(PleeStatus.GROWING);
        assertThat(pleeService.getGrowPlee(email).getCompleteCount())
                .isEqualTo(completeCount);
    }

}