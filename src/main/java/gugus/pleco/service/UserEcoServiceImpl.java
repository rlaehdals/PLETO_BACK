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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserEcoServiceImpl implements UserEcoService {

    private final UserEcoRepository userEcoRepository;
    private final PleeRepository pleeRepository;
    private final UserRepository userRepository;
    private final PleeServiceImpl pleeService;

    @Override
    public void confirmCoolTime(String username, String ecoName) throws TimeDissatisfactionException {
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

        // 쿨타임에 해당 안될경우 플리의 카운터 증가 후 새로운 시간 갱신
        Plee plee = pleeService.getGrowPlee(username);
        plee.addEcoCount();
        userEco.setPerformTime(nowTime);
    }

    @Override
    public List<UserEco> findAll(String email) {

        User user = userRepository.findByUsername(email).get();
        return userEcoRepository.findByUser(user);
    }

    private void checkTime(LocalDateTime performTime, LocalDateTime nowTime, Long coolTime) {
        // 경과 시간
        Long elapsedTime = ChronoUnit.SECONDS.between(performTime, nowTime);

        if (elapsedTime < coolTime) {
            throw new TimeDissatisfactionException("아직 미션을 할 수 있는 시간이 아닙니다.");
        }
    }

    @Override
    public List<UserEco> UserEcoTime(String email, LocalDateTime now) {
        return null;
    }

}
