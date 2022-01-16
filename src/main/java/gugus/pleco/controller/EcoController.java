package gugus.pleco.controller;

import gugus.pleco.aop.argumentresolver.JwtDto;
import gugus.pleco.aop.argumentresolver.LoginUser;
import gugus.pleco.aop.aspect.annotation.Log;
import gugus.pleco.controller.ecodto.RestEcoTimeDto;
import gugus.pleco.controller.pleedto.PerformDto;
import gugus.pleco.controller.userdto.UserEcoDto;
import gugus.pleco.controller.userdto.UserEcoListDto;
import gugus.pleco.domain.Plee;
import gugus.pleco.service.UserEcoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class EcoController {

    private final UserEcoService userEcoService;


    @Log
    @PostMapping("/performEco")
    @ResponseStatus(HttpStatus.OK)
    public UserEcoDto perform(@RequestBody PerformDto performDto) throws RuntimeException, Throwable{
        Plee plee = userEcoService.performEco(performDto.getEmail(), performDto.getEcoName());
        return new UserEcoDto(plee.getPleeName(),plee.getPleeStatus().toString());
    }

    @Log
    @GetMapping("/ecoTime")
    @ResponseStatus(HttpStatus.OK)
    public RestEcoTimeDto getEcoTime(@LoginUser JwtDto jwtDto, @RequestParam String ecoName) {
        LocalTime localTime = userEcoService.OneUserEcoTime(jwtDto.getEmail(), ecoName);
        return new RestEcoTimeDto(localTime);
    }

    @Log
    @GetMapping("/ecoList")
    @ResponseStatus(HttpStatus.OK)
    public List<UserEcoListDto> getEcoTimeList(@LoginUser JwtDto jwtDto) {
        return userEcoService.UserEcoStatus(jwtDto.getEmail());
    }





}
