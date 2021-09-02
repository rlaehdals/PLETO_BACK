package gugus.pleco.service;

import gugus.pleco.domain.Plee;
import gugus.pleco.domain.PleeStatus;
import gugus.pleco.domain.User;
import gugus.pleco.repositroy.PleeRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PleeServiceImplTest {

    @Autowired UserRepository userRepository;
    @Autowired PleeRepository pleeRepository;
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
        pleeService.createGrowPlee(email, pleeName, completeCount);

        //then
        assertThat(pleeService.getGrowPlee(email).get().getPleeName())
                .isEqualTo(pleeName);
        assertThat(pleeService.getGrowPlee(email).get().getPleeStatus())
                .isEqualTo(PleeStatus.GROWING);
        assertThat(pleeService.getGrowPlee(email).get().getCompleteCount())
                .isEqualTo(completeCount);
    }

    @Test
    public void 모든_플리_조회(){
        //given
        String email1 = "rkdlem48@gmail.com";
        String email2 = "asf@gmail.com";

        pleeService.createGrowPlee(email1, "장미", 10L);
        pleeService.createGrowPlee(email1, "호박", 10L);
        pleeService.createGrowPlee(email2, "동백", 10L);

        //when
        List<Plee> plees1 = pleeService.findAll(email1);
        List<Plee> plees2 = pleeService.findAll(email2);

        //then
        assertThat(plees1.size()).isEqualTo(2);
        assertThat(plees2.size()).isEqualTo(1);

    }

    @Test
    public void 에코_증가_및_플리_COMPLETE(){
        //given
        String email1 = "rkdlem48@gmail.com";
        Long pleeId = pleeService.createGrowPlee(email1, "장미", 3L);
        Plee plee = pleeRepository.getById(pleeId);

        //when
        for(int i=0; i<3; i++){
            plee.addEcoCount();
        }

        //then
        assertThat(plee.getEcoCount()).isEqualTo(3L);
        assertThat(plee.getPleeStatus()).isEqualTo(PleeStatus.COMPLETE);
    }

}