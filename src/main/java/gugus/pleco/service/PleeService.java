package gugus.pleco.service;

import gugus.pleco.domain.Plee;
import gugus.pleco.domain.User;

import java.util.List;
import java.util.Optional;

public interface PleeService {

    Long createGrowPlee(String email, String pleeName, Long completeCount);

    Optional<Plee> getGrowPlee(String email);

    List<Plee> findComplete(String email);
}
