package gugus.pleco.service;

import gugus.pleco.domain.Plee;
import gugus.pleco.domain.PleeStatus;
import gugus.pleco.domain.User;
import gugus.pleco.excetion.AlreadyUserHaveGrowPlee;
import gugus.pleco.excetion.ExistSamePleeName;
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
    public Long createGrowPlee(String email, String pleeName, Long completeCount) throws ExistSamePleeName, AlreadyUserHaveGrowPlee{

        log.info("id: {}, location: {}", email, "PleeServiceImpl.createGrowPlee");

        User user = userRepository.findByUsername(email).get();

        pleeRepository.findByPleeNameAndUser(user,pleeName)
                .ifPresent(m -> {
                    throw new ExistSamePleeName("플리를 저장하려고 할 때 플리 이름이 중복되는 것이 있을 때");
                });

        // 사용자의 growPlee가 이미 있으면 예외
        pleeRepository.findByUser(user).stream().filter(m -> m.getPleeStatus()==PleeStatus.GROWING).findAny().ifPresent(
                m -> {throw new AlreadyUserHaveGrowPlee("플리를 새로 생성할 때 이미 플리가 있어서 터지는 예외"); }
        );


        Plee plee = pleeRepository.save(Plee.createPlee(user, pleeName, completeCount));

        log.info("id: {}, location: {}, status: {}", email, "PleeServiceImpl.createGrowPlee","Complete");

        return plee.getId();
    }

    @Override
    public Plee getGrowPlee(String email) throws NotExistPlee, Throwable{
        log.info("id: {}, location: {}", email, "PleeServiceImpl.getGrowPlee");
        User user = userRepository.findByUsername(email).get();
        List<Plee> Plees = pleeRepository.findByUser(user);

        // collect NULL 예외처리 필요
        return Plees.stream().filter(m -> m.getPleeStatus() == PleeStatus.GROWING).findAny().orElseThrow(
                () -> new NotExistPlee("현재는 플리가 없습니다.")
        );


    }

    @Override
    public List<Plee> findComplete(String email) {
        log.info("id: {}, location: {}", email, "PleeServiceImpl.findComplete");
        User user = userRepository.findByUsername(email).get();
        List<Plee> plees = pleeRepository.findByUser(user);

        return plees.stream().filter(m -> m.getPleeStatus() == PleeStatus.COMPLETE).collect(Collectors.toList());
    }
}
