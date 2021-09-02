package gugus.pleco.service;

import gugus.pleco.domain.Plee;
import gugus.pleco.domain.PleeStatus;
import gugus.pleco.domain.User;
import gugus.pleco.repositroy.PleeRepository;
import gugus.pleco.repositroy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PleeServiceImpl implements PleeService {

    private final UserRepository userRepository;
    private final PleeRepository pleeRepository;


    @Override
    public Long createGrowPlee(String email, String pleeName, Long completeCount) {

        User user = userRepository.findByUsername(email).get();

        // 사용자의 growPlee가 이미 있으면 예외
        // ...

        Plee plee = pleeRepository.save(Plee.createPlee(user, pleeName, completeCount));

        return plee.getId();
    }

    @Override
    public Optional<Plee> getGrowPlee(String email) {

        User user = userRepository.findByUsername(email).get();
        List<Plee> Plees = pleeRepository.findByUser(user);

        // collect NULL 예외처리 필요
        return Plees.stream().filter(m -> m.getPleeStatus() == PleeStatus.GROWING).findAny();

    }

    @Override
    public List<Plee> findAll(String email) {

        User user = userRepository.findByUsername(email).get();
        return pleeRepository.findByUser(user);
    }
}
