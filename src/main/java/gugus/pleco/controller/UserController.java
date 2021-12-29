package gugus.pleco.controller;

import gugus.pleco.aop.aspect.annotation.Log;
import gugus.pleco.controller.userdto.DuplicateDto;
import gugus.pleco.controller.userdto.LoginDto;
import gugus.pleco.controller.userdto.SignUpDto;
import gugus.pleco.controller.userdto.UserDto;
import gugus.pleco.domain.User;
import gugus.pleco.excetion.UserDupulicatedException;
import gugus.pleco.jwt.JwtTokenProvider;
import gugus.pleco.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    @Log
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public SignUpDto signup(@RequestBody UserDto userDto)throws UserDupulicatedException {
        User user = userService.join(userDto);
        return new SignUpDto(user.getId(), true);
    }

    @Log
    @GetMapping("/duplicate")
    @ResponseStatus(HttpStatus.OK)
    public DuplicateDto checkEmail(@RequestParam String email) throws UserDupulicatedException  {
        return new DuplicateDto(userService.checkEmail(email));
    }
    @Log
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginDto login(@RequestBody UserDto userDto) throws UsernameNotFoundException, BadCredentialsException, Throwable {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(userService);
        User user = userService.login(userDto);
        return new LoginDto(jwtTokenProvider.createToken(user.getUsername(),user.getRoles()),true, 0L);
    }

}
