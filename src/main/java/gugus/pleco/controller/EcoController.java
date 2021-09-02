package gugus.pleco.controller;

import gugus.pleco.controller.dto.UserEcoListDto;
import gugus.pleco.domain.PleeStatus;
import gugus.pleco.domain.User;
import gugus.pleco.domain.UserEco;
import gugus.pleco.service.UserEcoService;
import gugus.pleco.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class EcoController {

    private final UserEcoService userEcoService;


    @PostMapping("/performEco")
    public UserEcoDto perform(@RequestBody PerformDto performDto){
        PleeStatus pleeStatus = userEcoService.performEco(performDto.getEmail(), performDto.getEcoName(), performDto.getPleeName());
        return new UserEcoDto(pleeStatus.toString());
    }

    @GetMapping("/ecoTime")
    public RestEcoTimeDto getEcoTime(@RequestParam String email, @RequestParam String ecoName){
        LocalTime localTime = userEcoService.OneUserEcoTime(email, ecoName);
        return new RestEcoTimeDto(localTime);
    }

    @GetMapping("/ecoList")
    public List<UserEcoListDto> getEcoTimeList(@RequestParam String email){
        return userEcoService.UserEcoStatus(email);
    }



    @Data
    @AllArgsConstructor
    class UserEcoDto{
        String status;
    }



    @Data
    @AllArgsConstructor
    class RestEcoTimeDto{
        LocalTime restCoolTime;
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
