package gugus.pleco.domain.user.service;

import gugus.pleco.util.aop.aspect.annotation.Log;
import gugus.pleco.domain.eco.domain.Eco;
import gugus.pleco.domain.user.domain.User;
import gugus.pleco.domain.eco.domain.UserEco;
import gugus.pleco.util.excetion.UserDuplicatedException;
import gugus.pleco.util.jwt.JwtTokenProvider;
import gugus.pleco.domain.eco.repository.EcoRepository;
import gugus.pleco.domain.eco.repository.UserEcoRepository;
import gugus.pleco.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEcoRepository userEcoRepository;
    private final EcoRepository ecoRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Log
    @Override
    public UserDetails loadUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("등록되지 않은 아이디입니다. "));
    }

    @Log
    @Override
    public User join(String email, String password){
        userRepository.findByUsername(email)
                .ifPresent(m->{
                    throw new UserDuplicatedException("이미 존재하는 아이디 입니다.");
                });
        User user = userRepository.save(User.builder()
                .username(email)
                .password(passwordEncoder.encode(password))
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        List<Eco> all = ecoRepository.findAll();
        for(Eco eco: all){
            UserEco eco1 = UserEco.createEco(user, eco);
            userEcoRepository.save(eco1);
        }
        return user;
    }

    @Log
    @Override
    public boolean checkEmail(String email){
        userRepository.findByUsername(email).ifPresent(
                m -> {
                    throw new UserDuplicatedException("이미 있는 아이디입니다.");
                }
        );
        return true;
    }

    @Log
    @Override
    public Map<String,String> login(String email, String password){
        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("등록되지 않은 아이디입니다.");
                });
        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new BadCredentialsException("잘못된 비밀번호입니다.");
        }
        List<Eco> all = ecoRepository.findAll();
        Map<String, String> map = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        return map;
    }

    @Override
    public String useRefreshTokenForAccessToken(String refreshToken){
        Map<String, String> token = new HashMap<>();
        if(jwtTokenProvider.validateRefreshToken(refreshToken)){
            String email = jwtTokenProvider.getUserPkRefreshToken(refreshToken);
            User user = userRepository.findByUsername(email)
                    .orElseThrow(() -> {
                        throw new UsernameNotFoundException("등록되지 않은 아이디입니다.");
                    });
            token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        }
        return token.get("ACCESS-TOKEN");
    }


}
