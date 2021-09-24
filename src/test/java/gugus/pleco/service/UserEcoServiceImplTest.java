package gugus.pleco.service;

import gugus.pleco.controller.dto.UserDto;
import gugus.pleco.controller.dto.UserEcoListDto;
import gugus.pleco.domain.*;
import gugus.pleco.excetion.NotExistPlee;
import gugus.pleco.excetion.TimeDissatisfactionException;
import gugus.pleco.repositroy.EcoRepository;
import gugus.pleco.repositroy.PleeRepository;
import gugus.pleco.repositroy.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
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
    public void 에코_수행_성공() throws RuntimeException, Throwable{
        //given
        Eco tumbler = Eco.createEco("텀블러 사용하기", 1000L);
        ecoRepository.save(tumbler);

        String email = "rkdlem48@gmail.com";
        createUser(email, "1234");
        Long pleeId = pleeService.createGrowPlee(email, "장미", 10L);

        //when
        Plee plee = userEcoService.performEco(email, tumbler.getEcoName());


        //then
        // 플리의 에코 카운트가 증가해야한다.
        assertThat(plee.getEcoCount()).isEqualTo(1L);
        assertThat(plee.getPleeName()).isEqualTo("장미");
        assertThat(plee.getPleeStatus()).isEqualTo(PleeStatus.GROWING);
    }

    @Test
    public void 에코_수행실패_아직_쿨타임() throws Throwable{
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
    public void 에코_수행실패_키우고_있는_플리가_없음() throws RuntimeException{
        //given
        Eco tumbler = Eco.createEco("텀블러 사용하기", 1000L);
        ecoRepository.save(tumbler);

        String email = "rkdlem48@gmail.com";
        createUser(email, "1234");
        // 첫 번째 에코 수행

        //when
        // 첫 번째 에코 수행 후 바로 두 번째 에코 수행
        Throwable e = assertThrows(NotExistPlee.class,
                () -> userEcoService.performEco(email, tumbler.getEcoName()));

        //then
        String message = e.getMessage();
        assertThat(message).isEqualTo("버튼을 눌러 엔딩을 확인해주세요!!");
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
    @Test
    void userEco_모든_상태_출력_possible(){
        Eco tumbler = Eco.createEco("텀블러 사용하기", 1000L);
        Eco bucket = Eco.createEco("장바구니 사용하기", 2000L);


        ecoRepository.save(tumbler);
        ecoRepository.save(bucket);

        String email = "rkdlem48@gmail.com";
        createUser(email, "1234");

        User user = userRepository.findByUsername(email).get();

        List<UserEcoListDto> userEcoListDtoList = userEcoService.UserEcoStatus(email);

        assertThat(userEcoListDtoList.size()).isEqualTo(2);


        // 에코 이름
        assertThat(userEcoListDtoList.get(0).getEcoName()).isEqualTo(tumbler.getEcoName());
        assertThat(userEcoListDtoList.get(1).getEcoName()).isEqualTo(bucket.getEcoName());
        // 에코 고정 대기 시간
        assertThat(userEcoListDtoList.get(0).getCoolTime()).isEqualTo(SecondChangeToTime(Math.toIntExact(tumbler.getCoolTime())));
        assertThat(userEcoListDtoList.get(1).getCoolTime()).isEqualTo(SecondChangeToTime(Math.toIntExact(bucket.getCoolTime())));
        // 에코 수행 status 확인
        assertThat(userEcoListDtoList.get(0).getStatus()).isEqualTo("possible");
        assertThat(userEcoListDtoList.get(1).getStatus()).isEqualTo("possible");

    }
    @Test
    void userEco_모든_상태_출력_impossible(){
        Eco tumbler = Eco.createEco("텀블러 사용하기", 1000L);
        Eco bucket = Eco.createEco("장바구니 사용하기", 2000L);


        ecoRepository.save(tumbler);
        ecoRepository.save(bucket);

        String email = "rkdlem48@gmail.com";
        createUser(email, "1234");

        User user = userRepository.findByUsername(email).get();

        List<UserEco> userEcoList = userEcoService.findAll(email);

        // 마지막 수행 시간을 현재로 넣어서 불가능하게 설정
        userEcoList.get(0).setPerformTime(LocalDateTime.now());

        List<UserEcoListDto> userEcoListDtoList = userEcoService.UserEcoStatus(email);

        assertThat(userEcoListDtoList.size()).isEqualTo(2);


        // 에코 이름
        assertThat(userEcoListDtoList.get(0).getEcoName()).isEqualTo(tumbler.getEcoName());
        assertThat(userEcoListDtoList.get(1).getEcoName()).isEqualTo(bucket.getEcoName());
        // 에코 고정 대기 시간
        assertThat(userEcoListDtoList.get(0).getCoolTime()).isEqualTo(SecondChangeToTime(Math.toIntExact(tumbler.getCoolTime())));
        assertThat(userEcoListDtoList.get(1).getCoolTime()).isEqualTo(SecondChangeToTime(Math.toIntExact(bucket.getCoolTime())));
        // 에코 수행 status 확인
        assertThat(userEcoListDtoList.get(0).getStatus()).isEqualTo("impossible");
        assertThat(userEcoListDtoList.get(1).getStatus()).isEqualTo("possible");

    }

    @Test
    void 특정_userEco의_cooltime출력_쿨타임없을_때(){

        Eco tumbler = Eco.createEco("텀블러 사용하기", 1000L);
        Eco bucket = Eco.createEco("장바구니 사용하기", 2000L);


        ecoRepository.save(tumbler);
        ecoRepository.save(bucket);

        String email = "rkdlem48@gmail.com";
        createUser(email, "1234");

        User user = userRepository.findByUsername(email).get();

        LocalTime localTime = userEcoService.OneUserEcoTime(email, tumbler.getEcoName());

        assertThat(localTime).isEqualTo(LocalTime.of(0,0,0,0));
    }
    @Test
    void 특정_userEco의_cooltime출력_쿨타임있을_때(){

        Eco tumbler = Eco.createEco("텀블러 사용하기", 1000L);
        Eco bucket = Eco.createEco("장바구니 사용하기", 2000L);


        ecoRepository.save(tumbler);
        ecoRepository.save(bucket);

        String email = "rkdlem48@gmail.com";
        createUser(email, "1234");

        User user = userRepository.findByUsername(email).get();

        List<UserEco> userEcoList = userEcoService.findAll(email);

        UserEco userEco = userEcoList.get(0);

        //마지막 수행 시간을 현재 시간 -10분으로 설정
        userEco.setPerformTime(LocalDateTime.now().minusMinutes(10L));
        LocalTime localTime = userEcoService.OneUserEcoTime(email, tumbler.getEcoName());

        //쿨타임 - 마지막 수행시간만큼 남은 시간
        assertThat(localTime).isEqualTo(LocalTime.of(0,6,40));
    }




    private LocalTime SecondChangeToTime(int second){
        if(second<=0){
            return LocalTime.of(0,0,0,0);

        }
        LocalTime localTime = LocalTime.of(second/3600,(second%3600)/60,second%60,0);
        return localTime;
    }
    private void createUser(String email, String password) {

        UserDto userDto = new UserDto(email, password);
        userService.join(userDto);
    }
}