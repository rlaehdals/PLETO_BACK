package gugus.pleco.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class he {

    @GetMapping("/asdf")
    public String goet(){
        return "asdf";
    }
}
