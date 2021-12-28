package gugus.pleco.controller;

import gugus.pleco.aop.aspect.annotation.Log;
import gugus.pleco.controller.pleedto.CreatePleeDto;
import gugus.pleco.controller.pleedto.PleeDictDto;
import gugus.pleco.controller.pleedto.PleeDto;
import gugus.pleco.domain.Plee;
import gugus.pleco.excetion.ExistSamePleeName;
import gugus.pleco.service.PleeService;
import lombok.AllArgsConstructor;
import lombok.Data;
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
    public PleeDto currentPleeCall(@RequestParam String email, HttpServletRequest request) throws Throwable{
        Plee growPlee = pleeService.getGrowPlee(email);
        return new PleeDto(growPlee.getPleeName(),growPlee.getEcoCount());
    }
    @Log
    @GetMapping("/pleeDict")
    @ResponseStatus(HttpStatus.OK)
    public List<PleeDictDto> pleeDictCall(@RequestParam String email){
        return pleeService.findComplete(email).stream()
                .map(m -> new PleeDictDto(m.getPleeName())).collect(Collectors.toList());
    }
    @Log
    @PostMapping("/growPlee")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createPlee(@RequestBody CreatePleeDto createPleeDto, @RequestParam String email) throws ExistSamePleeName {
        Long createPleeId = pleeService.createGrowPlee(email, createPleeDto.getPleeName(), createPleeDto.getCompleteCount());
        return new ResponseEntity<>(createPleeId, HttpStatus.OK);
    }


}
