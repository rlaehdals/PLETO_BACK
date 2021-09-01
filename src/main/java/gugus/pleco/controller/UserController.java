package gugus.pleco.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
    public String signup(@RequestBody UserDto userDto)throws UserDupulicatedException {
        Long userId = userService.join(userDto);
        User user = userService.findById(userId);
        return jwtTokenProvider.createToken(userDto.getEmail(),user.getRoles());
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDto userDto){
        Long userId = userService.login(userDto);
        User user = userService.findById(userId);
        return jwtTokenProvider.createToken(userDto.getEmail(),user.getRoles());
    }
}
