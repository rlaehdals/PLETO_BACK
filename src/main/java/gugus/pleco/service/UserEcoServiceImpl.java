package gugus.pleco.service;

import gugus.pleco.domain.Plee;
import gugus.pleco.domain.User;
import gugus.pleco.domain.UserEco;
import gugus.pleco.excetion.TimeDissatisfactionException;
import gugus.pleco.repositroy.PleeRepository;
import gugus.pleco.repositroy.UserEcoRepository;
import gugus.pleco.repositroy.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.context.TenantIdentifierMismatchException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserEcoServiceImpl implements UserEcoService{

    private final UserEcoRepository userEcoRepository;

    private final PleeRepository pleeRepository;

    private final UserRepository userRepository;

    @Override
    public void confirmCoolTime(String username, String ecoName, LocalDateTime now, String pleename) throws TimeDissatisfactionException{
        //페치 조인써서 쿼리 최적화
        UserEco userEco = userEcoRepository.findByUserAndEcoName(username, ecoName).get();
        //controller에 들어온 순간을 기록
        LocalDateTime performTime = userEco.getPerformTime();
        //입력들어온 eco에 대하여 쿨타임 조회
        Long coolTime = userEco.getEco().getCoolTime();
        //마지막 성공 시간과 controller로 들어온 시간 + coolTime 더한 값의 시간 비교
        if(performTime.isBefore(now.plusMinutes(coolTime))){
            throw new TimeDissatisfactionException("아직 미션을 할 수 있는 시간이 아닙니다.");
        }
        // 쿨타임에 해당 안될경우 플리의 카운터 증가 후 새로운 시간 갱신
        Plee plee = pleeRepository.findByUser(userEco.getUser()).stream()
                .filter(
                        m -> m.getPleeName().equals(pleename)
                )
                .collect(Collectors.toList()).get(0);
        plee.addEcoCount();
        userEco.setPerformTime(now);
    }

    @Override
    public List<UserEco> UserEcoTime(String email, LocalDateTime now) {
        User user = userRepository.findByUsername(email).get();

        List<UserEco> userEcoList = user.getUserEcoList();

        userEcoList.stream().
    }

    //남은 시간만 담는 dto
    @Data
    @AllArgsConstructor
    class UserEcoTimeDto(LocalDateTime localDateTime){
        LocalDateTime coolTime;
    }
}
