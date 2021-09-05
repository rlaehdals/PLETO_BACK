package gugus.pleco.controller;

import gugus.pleco.domain.Plee;
import gugus.pleco.service.PleeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class PleeController {

    private final PleeService pleeService;

    @GetMapping("/growPlee")
    @ResponseStatus(HttpStatus.OK)
    public PleeDto currnetPleeCall(@RequestParam String email) throws Throwable{
        Plee growPlee = pleeService.getGrowPlee(email);
        return new PleeDto(growPlee.getPleeName(),growPlee.getEcoCount());
    }

    @GetMapping("/pleeDict")
    @ResponseStatus(HttpStatus.OK)
    public List<PleeDictDto> pleeDictCall(@RequestParam String email){
        return pleeService.findComplete(email).stream()
                .map(m -> new PleeDictDto(m.getPleeName())).collect(Collectors.toList());
    }

    @PostMapping("/growPlee")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createPlee(@RequestBody CreatePleeDto createPleeDto, @RequestParam String email){
        Long createPleeId = pleeService.createGrowPlee(email, createPleeDto.getPleeName(), createPleeDto.getCompleteCount());
        return new ResponseEntity<>(createPleeId, HttpStatus.OK);
    }

    @Data
    @AllArgsConstructor
    static class PleeDto{
        String pleeName;
        Long ecoCount;
    }

    @Data
    @AllArgsConstructor
    static class PleeDictDto{
        String pleeName;
    }

    @Data
    @AllArgsConstructor
    static class CreatePleeDto{
        String pleeName;
        Long completeCount;
    }
}
