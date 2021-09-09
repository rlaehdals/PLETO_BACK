package gugus.pleco.controller;

import gugus.pleco.controller.dto.UserEcoListDto;
import gugus.pleco.domain.Plee;
import gugus.pleco.domain.PleeStatus;
import gugus.pleco.service.UserEcoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class EcoController {

    private final UserEcoService userEcoService;


    @PostMapping("/performEco")
    @ResponseStatus(HttpStatus.OK)
    public UserEcoDto perform(@RequestBody PerformDto performDto) throws RuntimeException, Throwable{
        log.info("id: {}, location: {}",performDto.getEmail(),"EcoController.perform");
        Plee plee = userEcoService.performEco(performDto.getEmail(), performDto.getEcoName());
        return new UserEcoDto(plee.getPleeName(),plee.getPleeStatus().toString());
    }

    @GetMapping("/ecoTime")
    @ResponseStatus(HttpStatus.OK)
    public RestEcoTimeDto getEcoTime(@RequestParam String email, @RequestParam String ecoName) {
        log.info("id: {}, location: {}",email,"EcoController.getEcoTime");
        LocalTime localTime = userEcoService.OneUserEcoTime(email, ecoName);
        return new RestEcoTimeDto(localTime);
    }

    @GetMapping("/ecoList")
    @ResponseStatus(HttpStatus.OK)
    public List<UserEcoListDto> getEcoTimeList(@RequestParam String email) {
        log.info("id: {}, location: {}",email,"EcoController.getEcoTimeList");
        return userEcoService.UserEcoStatus(email);
    }

    @Data
    @AllArgsConstructor
    static class UserEcoDto {
        String pleeName;
        String status;
    }


    @Data
    @AllArgsConstructor
    static class RestEcoTimeDto {
        LocalTime restCoolTime;
    }

    @Data
    @AllArgsConstructor
    static class PerformDto {

        @Email
        @NotNull
        String email;

        @NotNull
        String ecoName;
    }
}
