package gugus.pleco.domain.user.controller;

import gugus.pleco.util.aop.aspect.annotation.Log;
import gugus.pleco.domain.user.dto.DuplicateDto;
import gugus.pleco.domain.user.dto.UserDto;
import gugus.pleco.domain.user.domain.User;
import gugus.pleco.util.excetion.UserDuplicatedException;
import gugus.pleco.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @Log
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> signup(@Validated @RequestBody UserDto userDto, BindingResult bindingResult)throws UserDuplicatedException {
        Map<String, String> map =new HashMap<>();
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().stream()
                    .forEach(a -> {
                        String field = ((FieldError) a).getField();
                        String defaultMessage = a.getDefaultMessage();
                        map.put(field, defaultMessage);
                    });
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        User user = userService.join(userDto.getEmail(), userDto.getPassword());
        return new ResponseEntity<>(user.getId(),HttpStatus.CREATED);
    }

    @Log
    @GetMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public DuplicateDto checkEmail(@RequestParam String email) throws UserDuplicatedException {
        return new DuplicateDto(userService.checkEmail(email));
    }
    @Log
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestBody UserDto userDto, HttpServletResponse response) throws UsernameNotFoundException, BadCredentialsException {
        Map<String, String> map = userService.login(userDto.getEmail(), userDto.getPassword());
        response.setHeader("ACCESS-TOKEN",map.get("ACCESS-TOKEN"));
        response.setHeader("REFRESH-TOKEN",map.get("REFRESH-TOKEN"));
        return new ResponseEntity<String>("ok",HttpStatus.OK);
    }

    @Log
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> refreshToken(@RequestParam(name = "refreshToken") String refreshToken, HttpServletResponse response) throws UsernameNotFoundException, BadCredentialsException {
        String s = userService.useRefreshTokenForAccessToken(refreshToken);
        response.setHeader("ACCESS-TOKEN",s);
        return new ResponseEntity<String>("ok",HttpStatus.OK);
    }
}
