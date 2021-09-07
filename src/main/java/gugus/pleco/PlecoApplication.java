package gugus.pleco;


import gugus.pleco.controller.dto.UserDto;
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


	public static void main(String[] args) {
		SpringApplication.run(PlecoApplication.class, args);
	}




}
