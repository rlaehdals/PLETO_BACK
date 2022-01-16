package gugus.pleco.controller;

import gugus.pleco.aop.argumentresolver.JwtDto;
import gugus.pleco.aop.argumentresolver.LoginUser;
import gugus.pleco.aop.aspect.annotation.Log;
import gugus.pleco.controller.pleedto.CreatePleeDto;
import gugus.pleco.controller.pleedto.PleeDictDto;
import gugus.pleco.controller.pleedto.PleeDto;
import gugus.pleco.domain.Plee;
import gugus.pleco.excetion.ExistSamePleeName;
import gugus.pleco.service.PleeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class PleeController{

    private final PleeService pleeService;
    @Log
    @GetMapping("/growPlee")
    @ResponseStatus(HttpStatus.OK)
    public PleeDto currentPleeCall(@LoginUser JwtDto jwtDto) throws Throwable{
        Plee growPlee = pleeService.getGrowPlee(jwtDto.getEmail());
        return new PleeDto(growPlee.getPleeName(),growPlee.getEcoCount());
    }
    @Log
    @GetMapping("/pleeDict")
    @ResponseStatus(HttpStatus.OK)
    public List<PleeDictDto> pleeDictCall(@LoginUser JwtDto jwtDto){
        return pleeService.findComplete(jwtDto.getEmail()).stream()
                .map(m -> new PleeDictDto(m.getPleeName())).collect(Collectors.toList());
    }
    @Log
    @PostMapping("/growPlee")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createPlee(@RequestBody CreatePleeDto createPleeDto, @LoginUser JwtDto jwtDto) throws ExistSamePleeName {
        Long createPleeId = pleeService.createGrowPlee(jwtDto.getEmail(), createPleeDto.getPleeName(), createPleeDto.getCompleteCount());
        return new ResponseEntity<>(createPleeId, HttpStatus.OK);
    }


}
