package gugus.pleco.service;

import gugus.pleco.aop.aspect.annotation.Log;
import gugus.pleco.controller.dto.UserEcoListDto;
import gugus.pleco.domain.Plee;
import gugus.pleco.domain.PleeStatus;
import gugus.pleco.domain.UserEco;
import gugus.pleco.excetion.NotExistPlee;
import gugus.pleco.excetion.TimeDissatisfactionException;
import gugus.pleco.repositroy.PleeRepository;
import gugus.pleco.repositroy.UserEcoRepository;
import gugus.pleco.repositroy.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserEcoServiceImpl implements UserEcoService {

    private final UserEcoRepository userEcoRepository;
    private final UserRepository userRepository;
    private final PleeRepository pleeRepository;

    @Log
    @Override
    public Plee performEco(String username, String ecoName) throws RuntimeException, Throwable {

        //페치 조인써서 쿼리 최적화
        UserEco userEco = userEcoRepository.findByUserAndEcoName(username, ecoName).get();
        // 최근에 수행한 시간 performTime 가져오기
        LocalDateTime performTime = userEco.getPerformTime();
        // 현재 시간 nowTime 가져오기
        LocalDateTime nowTime = LocalDateTime.now();
        //입력들어온 eco에 대하여 쿨타임 조회
        Long coolTime = userEco.getEco().getCoolTime();

        // 수행할 수 있는지 계산
        checkTime(performTime, nowTime, coolTime);


        Plee plee = pleeRepository.findPleeByUsername(username).stream()
                .filter(m -> m.getPleeStatus() == PleeStatus.GROWING)
                .findFirst()
                .orElseThrow(
                        () -> {
                            throw new NotExistPlee("버튼을 눌러 엔딩을 확인해주세요!!");
                        }
                );
        plee.addEcoCount();
        userEco.setPerformTime(nowTime);
        return plee;
    }

    //UserEco 수행할 수 있을때까지 남은시간 알려주는 함수
    @Log
    @Override
    public LocalTime OneUserEcoTime(String email, String ecoName) {
        // 현재 시간
        LocalDateTime now = LocalDateTime.now();
        // UserEco 확인
        UserEco userEco = userEcoRepository.findByUserAndEcoName(email, ecoName).get();
        // 마지막 실행시간 확인
        LocalDateTime performTime = userEco.getPerformTime();
        // Eco 의 coolTime - (마지막 성공시간 - 현지새간) ->  UserEco 의 남은 coolTime 확인
        // 초를 시간으로 바꿈
         return SecondChangeToTime(userEco.getEco().getCoolTime() - ChronoUnit.SECONDS.between(performTime, now));

    }


    // UserEco들에 관하여 실행 가능한 여부를 알려주는 로직
    @Log
    @Override
    public List<UserEcoListDto> UserEcoStatus(String email) {
        log.info("id: {}, location: {}", email, "UserEcoServiceImpl.UserEcoStatus");
        //현재 시간
        LocalDateTime now = LocalDateTime.now();
        //UserEco를 user_eco_id순으로 정렬
        List<UserEco> userEcoList = userEcoRepository.findByUsername(email);
        //dto를 담기위해 선언
        List<UserEcoListDto> userEcoListDtos = new ArrayList<>();

        for(UserEco userEco: userEcoList){
            //마지막 수행 시간과 현재시간을 비교하여 cooltime보다 크다면 possible 작다면 impossible
            String status = checkStatus(userEco.getPerformTime(), now, userEco.getEco().getCoolTime());
            // 반환을 위한 DTO 변환 dto클래스는 controller/dto 패키지에 있음
            userEcoListDtos.add(new UserEcoListDto(userEco.getEco().getEcoName()
                    ,SecondChangeToTime(userEco.getEco().getCoolTime())
                    ,status));
        }
        log.info("id: {}, location: {}, status: {}", email, "UserEcoServiceImpl.UserEcoStatus","Complete");
        return userEcoListDtos;
    }

    @Override
    public List<UserEco> findAll(String email) {
        return userEcoRepository.findByUsername(email);
    }

    private String checkStatus(LocalDateTime performTime, LocalDateTime nowTime, Long coolTime){
        // 경과 시간
        Long elapsedTime = ChronoUnit.SECONDS.between(performTime, nowTime);

        if (elapsedTime < coolTime) {
            return "impossible";
        }
        return "possible";
    }

    private LocalTime SecondChangeToTime(Long second){
        if(second<=0){
            return LocalTime.of(0,0,0,0);

        }
        LocalTime localTime = LocalTime.of(second.intValue()/3600,(second.intValue()%3600)/60,second.intValue()%60,0);
        return localTime;
    }

    private void checkTime(LocalDateTime performTime, LocalDateTime nowTime, Long coolTime) throws RuntimeException{
        // 경과 시간
        Long elapsedTime = ChronoUnit.SECONDS.between(performTime, nowTime);

        if (elapsedTime < coolTime) {
            throw new TimeDissatisfactionException("아직 미션을 할 수 있는 시간이 아닙니다.");
        }
    }


}
