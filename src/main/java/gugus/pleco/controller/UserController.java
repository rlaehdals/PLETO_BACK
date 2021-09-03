package gugus.pleco.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import gugus.pleco.controller.dto.UserDto;
import gugus.pleco.domain.User;
import gugus.pleco.excetion.UserDupulicatedException;
import gugus.pleco.jwt.JwtTokenProvider;
import gugus.pleco.service.UserService;



@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public signUpDto signup(@RequestBody UserDto userDto)throws UserDupulicatedException {
        User user = userService.join(userDto);
        return new signUpDto(user.getId(), true);
    }


    @GetMapping("/duplicate")
    @ResponseStatus(HttpStatus.OK)
    public duplicateDto checkEmail(@RequestParam String email) throws UserDupulicatedException  {
        return new duplicateDto(userService.checkEmail(email));
    }
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public loginDto login(@RequestBody UserDto userDto){
        User user = userService.login(userDto);
        return new loginDto(jwtTokenProvider.createToken(user.getUsername(),user.getRoles()),true, 0L);
    }

    @AllArgsConstructor
    @Data
    static class duplicateDto{
        boolean success;
    }

    @AllArgsConstructor
    @Data
    static class signUpDto{
        Long userId;
        boolean success;
    }

    @AllArgsConstructor
    @Data
    static class loginDto{
        String token;
        boolean success;
        Long pleeSize;
    }
}
