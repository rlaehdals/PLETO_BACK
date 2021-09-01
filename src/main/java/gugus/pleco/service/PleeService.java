package gugus.pleco.service;

import gugus.pleco.domain.Plee;
import gugus.pleco.domain.User;

import java.util.List;

public interface PleeService {

    public Long createGrowPlee(String email,String pleeName, Long completeCount);

    public Plee getGrowPlee(String email);

    public List<Plee> findAll(String email);
}
