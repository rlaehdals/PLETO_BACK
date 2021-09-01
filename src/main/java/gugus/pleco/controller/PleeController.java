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
    public PleeDto currnetPleeCall(@RequestParam String email){
        Plee growPlee = pleeService.getGrowPlee(email);
        return new PleeDto(growPlee.getPleeName(),growPlee.getEcoCount());
    }

    @GetMapping("/pleeDict")
    public List<PleeDictDto> pleeDictCall(@RequestParam String email){
        return pleeService.findAll(email).stream()
                .map(m -> new PleeDictDto(m.getPleeName(),m.getPleeStatus().toString())).collect(Collectors.toList());
    }

    @PostMapping("/growPlee")
    public ResponseEntity<?> createPlee(@RequestBody createPleeDto createPleeDto, @RequestParam String email){
        Long createPleeId = pleeService.createGrowPlee(email, createPleeDto.getPleeName(), createPleeDto.getCompleteCount());
        return new ResponseEntity<>(createPleeId, HttpStatus.OK);
    }

    @Data
    @AllArgsConstructor
    class PleeDto{
        String pleeName;
        Long ecoCount;
    }

    @Data
    @AllArgsConstructor
    class PleeDictDto{
        String pleeName;
        String pleeStatus;
    }

    @Data
    @AllArgsConstructor
    class createPleeDto{
        String pleeName;
        Long completeCount;
    }
}
