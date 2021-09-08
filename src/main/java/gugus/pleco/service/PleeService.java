package gugus.pleco.service;

import gugus.pleco.domain.Plee;
import gugus.pleco.domain.User;
import gugus.pleco.excetion.AlreadyUserHaveGrowPlee;
import gugus.pleco.excetion.ExistSamePleeName;
import gugus.pleco.excetion.NotExistPlee;

import java.util.List;
import java.util.Optional;

public interface PleeService {

    Long createGrowPlee(String email, String pleeName, Long completeCount) throws ExistSamePleeName, AlreadyUserHaveGrowPlee;

    Plee getGrowPlee(String email) throws NotExistPlee, Throwable;

    List<Plee> findComplete(String email);
}
