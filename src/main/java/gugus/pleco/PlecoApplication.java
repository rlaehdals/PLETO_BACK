package gugus.pleco;
import gugus.pleco.controller.dto.UserDto;
import gugus.pleco.domain.Eco;
import gugus.pleco.domain.User;
import gugus.pleco.repositroy.EcoRepository;
import gugus.pleco.service.PleeService;
import gugus.pleco.service.UserEcoService;
import gugus.pleco.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RequiredArgsConstructor
public class PlecoApplication {

	private final UserService userService;
	private final UserEcoService userEcoService;
	private final EcoRepository ecoRepository;
	private final PleeService pleeService;

	public static void main(String[] args) {
		SpringApplication.run(PlecoApplication.class, args);
	}


//	@PostConstruct
//	public void dummy(){
//		Eco tumbler = Eco.createEco("텀블러", 3600L);
//		ecoRepository.save(tumbler);
//		UserDto userDto = new UserDto("rkdlem48@gmail.com","asdf");
//		User saveUser = userService.join(userDto);
//		pleeService.createGrowPlee(saveUser.getUsername(), "장미", 10L);
//	}
}
