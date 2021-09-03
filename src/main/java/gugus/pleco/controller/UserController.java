package gugus.pleco.controller;

import javassist.bytecode.DuplicateMemberException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import gugus.pleco.controller.dto.UserDto;
import gugus.pleco.domain.User;
import gugus.pleco.excetion.UserDupulicatedException;
import gugus.pleco.jwt.JwtTokenProvider;
import gugus.pleco.service.UserService;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto userDto)throws UserDupulicatedException {
        User user = userService.join(userDto);
        return new ResponseEntity<>(user.getId(),HttpStatus.OK);
    }

    @GetMapping("/duplicate")
    public duplicateDto checkEmail(@RequestParam String email) throws UserDupulicatedException  {
        return new duplicateDto(userService.checkEmail(email));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto){
        User user = userService.login(userDto);
        return new ResponseEntity<>( jwtTokenProvider.createToken(user.getUsername(), user.getRoles()),HttpStatus.OK);
    }



    @AllArgsConstructor
    @Data
    static class duplicateDto{
        boolean confirm;
    }

}
