package gugus.pleco.service;

import gugus.pleco.domain.Plee;
import gugus.pleco.domain.PleeStatus;
import gugus.pleco.domain.User;
import gugus.pleco.excetion.NotExistPlee;
import gugus.pleco.repositroy.PleeRepository;
import gugus.pleco.repositroy.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PleeServiceImpl implements PleeService {

    private final UserRepository userRepository;
    private final PleeRepository pleeRepository;


    @Override
    public Long createGrowPlee(String email, String pleeName, Long completeCount) {

        log.info("call_service");

        User user = userRepository.findByUsername(email).get();

        // 사용자의 growPlee가 이미 있으면 예외
        // ...

        log.info("savePlee");
        Plee plee = pleeRepository.save(Plee.createPlee(user, pleeName, completeCount));


        log.info("savePlee complete");
        return plee.getId();
    }

    @Override
    public Plee getGrowPlee(String email) throws NotExistPlee, Throwable{

        User user = userRepository.findByUsername(email).get();
        List<Plee> Plees = pleeRepository.findByUser(user);

        // collect NULL 예외처리 필요
        return Plees.stream().filter(m -> m.getPleeStatus() == PleeStatus.GROWING).findAny().orElseThrow(
                () -> new NotExistPlee("현재는플리가 없습니다.")
        );


    }

    @Override
    public List<Plee> findComplete(String email) {

        User user = userRepository.findByUsername(email).get();
        List<Plee> plees = pleeRepository.findByUser(user);

        return plees.stream().filter(m -> m.getPleeStatus() == PleeStatus.COMPLETE).collect(Collectors.toList());
    }
}
