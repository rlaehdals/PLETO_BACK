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
    public Long createGrowPlee(String email, String pleeName, Long completeCount) throws ExistSamePleeName{

        log.info("id: {}, location: {}", email, "PleeServiceImpl.createGrowPlee");

        User user = userRepository.findByUsername(email).get();

        pleeRepository.findByPleeNameAndUser(user,pleeName)
                .ifPresent(m -> {
                    throw new ExistSamePleeName("유저에게 이미 존재 하는 플리");
                });

        // 사용자의 growPlee가 이미 있으면 예외
        pleeRepository.findByUser(user).stream().filter(m -> m.getPleeStatus()==PleeStatus.GROWING).findAny().ifPresent(
                m -> {throw new AlreadyUserHaveGrowPlee("이미 키우고 있는 플리가 있습니다."); }
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
                () -> new NotExistPlee("현재는플리가 없습니다.")
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
