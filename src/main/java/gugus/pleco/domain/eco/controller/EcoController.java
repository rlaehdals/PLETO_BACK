package gugus.pleco.domain.eco.controller;

import gugus.pleco.util.aop.argumentresolver.JwtDto;
import gugus.pleco.util.aop.argumentresolver.LoginUser;
import gugus.pleco.util.aop.aspect.annotation.Log;
import gugus.pleco.domain.eco.dto.RestEcoTimeDto;
import gugus.pleco.domain.plee.dto.PerformDto;
import gugus.pleco.domain.user.dto.UserEcoDto;
import gugus.pleco.domain.user.dto.UserEcoListDto;
import gugus.pleco.domain.plee.domain.Plee;
import gugus.pleco.domain.eco.service.UserEcoService;
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
    @PostMapping("/eco")
    @ResponseStatus(HttpStatus.OK)
    public UserEcoDto perform(@RequestBody PerformDto performDto) throws RuntimeException, Throwable{
        Plee plee = userEcoService.performEco(performDto.getEmail(), performDto.getEcoName());
        return new UserEcoDto(plee.getPleeName(),plee.getPleeStatus().toString());
    }

    @Log
    @GetMapping("/eco")
    @ResponseStatus(HttpStatus.OK)
    public RestEcoTimeDto getEcoTime(@LoginUser JwtDto jwtDto, @RequestParam String ecoName) {
        LocalTime localTime = userEcoService.OneUserEcoTime(jwtDto.getEmail(), ecoName);
        return new RestEcoTimeDto(localTime);
    }

    @Log
    @GetMapping("/ecos")
    @ResponseStatus(HttpStatus.OK)
    public List<UserEcoListDto> getEcoTimeList(@LoginUser JwtDto jwtDto) {
        return userEcoService.UserEcoStatus(jwtDto.getEmail());
    }





}
