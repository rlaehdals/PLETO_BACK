package gugus.pleco.domain.plee.service;

import gugus.pleco.domain.plee.domain.Plee;
import gugus.pleco.util.excetion.AlreadyUserHaveGrowPlee;
import gugus.pleco.util.excetion.ExistSamePleeName;
import gugus.pleco.util.excetion.NotExistPlee;

import java.util.List;

public interface PleeService {

    Long createGrowPlee(String email, String pleeName, Long completeCount) throws ExistSamePleeName, AlreadyUserHaveGrowPlee;

    Plee getGrowPlee(String email) throws NotExistPlee, Throwable;

    List<Plee> findComplete(String email);
}
