package gugus.pleco.service;

import gugus.pleco.domain.Plee;
import gugus.pleco.domain.PleeStatus;
import gugus.pleco.domain.User;
import gugus.pleco.excetion.AlreadyUserHaveGrowPlee;
import gugus.pleco.excetion.ExistSamePleeName;
import gugus.pleco.repositroy.PleeRepository;
import gugus.pleco.repositroy.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public void 그로우플리_생성() throws Throwable {
        //given
        String email = "rkdlem48@gmail.com";
        String pleeName = "장미";
        Long completeCount = 10L;

        //when
        pleeService.createGrowPlee(email, pleeName, completeCount);

        //then
        assertThat(pleeService.getGrowPlee(email).getPleeName())
                .isEqualTo(pleeName);
        assertThat(pleeService.getGrowPlee(email).getPleeStatus())
                .isEqualTo(PleeStatus.GROWING);
        assertThat(pleeService.getGrowPlee(email).getCompleteCount())
                .isEqualTo(completeCount);
    }

    @Test
    public void 모든_플리_조회() throws Throwable {
        //given
        String email1 = "rkdlem48@gmail.com";
        String email2 = "asf@gmail.com";

        pleeService.createGrowPlee(email1, "장미", 1L);
        Plee growPlee1 = pleeService.getGrowPlee(email1);
        growPlee1.addEcoCount();

        pleeService.createGrowPlee(email1, "호박", 1L);
        Plee growPlee2 = pleeService.getGrowPlee(email1);
        growPlee2.addEcoCount();

        pleeService.createGrowPlee(email2, "동백", 1L);

        //when
        List<Plee> plees1 = pleeService.findComplete(email1);
        List<Plee> plees2 = pleeService.findComplete(email2);

        //then
        assertThat(plees1.size()).isEqualTo(2);
        assertThat(plees2.size()).isEqualTo(0);

    }

    @Test
    public void 에코_증가_및_플리_COMPLETE() throws Throwable{
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

    @Test
    void user가_가지고_있는_플리의_이름이_이미_있을_때(){
        User user = userRepository.findByUsername("rkdlem48@gmail.com").get();

        pleeService.createGrowPlee(user.getUsername(), "장미", 10L);

        assertThrows(ExistSamePleeName.class, () ->         pleeService.createGrowPlee(user.getUsername(), "장미", 10L));
    }

    @Test
    void user가_이미_키우고_있는_플리가_있을_때(){
        User user = userRepository.findByUsername("rkdlem48@gmail.com").get();
        User user1 = userRepository.findByUsername("asd@gmail.com").get();

        pleeService.createGrowPlee(user.getUsername(), "장미", 10L);

        assertThrows(AlreadyUserHaveGrowPlee.class,
                () ->         pleeService.createGrowPlee(user.getUsername(), "꽃", 10L)
        );
    }
    @Test
    void 다른_사람이_같은_이름의_플리는_가져된다(){
        User user = userRepository.findByUsername("rkdlem48@gmail.com").get();
        User user1 = userRepository.findByUsername("asf@gmail.com").get();

        pleeService.createGrowPlee(user.getUsername(), "장미", 10L);
        pleeService.createGrowPlee(user1.getUsername(),"장미",10L);
    }
}