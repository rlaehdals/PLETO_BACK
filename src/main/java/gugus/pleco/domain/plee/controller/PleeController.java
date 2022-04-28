package gugus.pleco.domain.plee.controller;

import gugus.pleco.util.aop.argumentresolver.JwtDto;
import gugus.pleco.util.aop.argumentresolver.LoginUser;
import gugus.pleco.util.aop.aspect.annotation.Log;
import gugus.pleco.domain.plee.dto.CreatePleeDto;
import gugus.pleco.domain.plee.dto.PleeDictDto;
import gugus.pleco.domain.plee.dto.PleeDto;
import gugus.pleco.domain.plee.domain.Plee;
import gugus.pleco.util.excetion.ExistSamePleeName;
import gugus.pleco.domain.plee.service.PleeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class PleeController{

    private final PleeService pleeService;
    @Log
    @GetMapping("/plee")
    @ResponseStatus(HttpStatus.OK)
    public PleeDto currentPleeCall(@LoginUser JwtDto jwtDto) throws Throwable{
        Plee growPlee = pleeService.getGrowPlee(jwtDto.getEmail());
        return new PleeDto(growPlee.getPleeName(),growPlee.getEcoCount());
    }
    @Log
    @GetMapping("/plees")
    @ResponseStatus(HttpStatus.OK)
    public List<PleeDictDto> pleeDictCall(@LoginUser JwtDto jwtDto){
        return pleeService.findComplete(jwtDto.getEmail()).stream()
                .map(m -> new PleeDictDto(m.getPleeName())).collect(Collectors.toList());
    }
    @Log
    @PostMapping("/plee")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createPlee(@RequestBody CreatePleeDto createPleeDto, @LoginUser JwtDto jwtDto) throws ExistSamePleeName {
        Long createPleeId = pleeService.createGrowPlee(jwtDto.getEmail(), createPleeDto.getPleeName(), createPleeDto.getCompleteCount());
        return new ResponseEntity<>(createPleeId, HttpStatus.OK);
    }


}
