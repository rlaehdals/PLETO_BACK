package gugus.pleco.controller;

import gugus.pleco.domain.User;
import gugus.pleco.service.UserEcoService;
import gugus.pleco.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class EcoController {

    private final UserEcoService userEcoService;

    @PostMapping("/performEco")
    public UserEcoDto perform(@RequestBody PerformDto performDto){
        LocalDateTime now = LocalDateTime.now();

//        userEcoService.confirmCoolTime(performDto.email, performDto.ecoName,now, performDto.getPleeName());

        return new UserEcoDto(HttpStatus.OK, "미션이 완료되었습니다.");
    }




    @Data
    @AllArgsConstructor
    class UserEcoDto{
        HttpStatus status;
        String message;
    }
    @Data
    @AllArgsConstructor
    class PerformDto{

        @Email
        @NotNull
        String email;

        @NotNull
        String ecoName;

        @NotNull
        String pleeName;
    }
}
