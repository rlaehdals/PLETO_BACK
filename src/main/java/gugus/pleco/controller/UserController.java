package gugus.pleco.controller;

import gugus.pleco.aop.aspect.annotation.Log;
import gugus.pleco.controller.userdto.DuplicateDto;
import gugus.pleco.controller.userdto.SignUpDto;
import gugus.pleco.controller.userdto.UserDto;
import gugus.pleco.domain.User;
import gugus.pleco.excetion.UserDupulicatedException;
import gugus.pleco.jwt.JwtTokenProvider;
import gugus.pleco.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

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
    public ResponseEntity<String> login(@RequestBody UserDto userDto, HttpServletRequest request, HttpServletResponse response) throws UsernameNotFoundException, BadCredentialsException, Throwable {
        String accessToken = userService.login(userDto);
        response.setHeader("X-AUTH-TOKEN",accessToken);
        response.setHeader("USERNAME", userDto.getEmail());
        return new ResponseEntity<String>("ok",HttpStatus.OK);
    }
}
