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

/**
 *
 * User의 회원가입과 로그인을 담담하는 서비스 계층입니다.
 * OSIV가 켜져 있으므로 Controller 계층까지 영속성 컨텍스트가 살아있습니다.
 *
 * @see UserRepository
 * @see PasswordEncoder
 * @see EcoRepository
 * @see JwtTokenProvider
 */

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

    /**
     * Srping Security를 사용하기 위해서 UserDetailsService를 상속받았기에 메서드 오버라이딩을 진행했습니다.
     *
     * @param username username은 email로 유니크한 값입니다.
     * @return UserDetails를 반환합니다. User Entity는 UserDetails를 상속 받았습니다.
     * @throws UsernameNotFoundException UserRepository에서 username을 찾지 못했을 때 예외를 발생시킵니다.
     */
    @Log
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("등록되지 않은 아이디입니다. "));
    }

    /**
     *
     * {@link gugus.pleco.domain.user.dto.UserDto} 의 필드 값들을 받고 회원 가입을 진행하는 메서드 만약
     * 이미 email이 존재한다면 에외 발생
     *
     * {@link UserRepository} 저장된 User중에서 email이 같은지 확인하며, 없을 경우 저장을 진행
     * @see gugus.pleco.domain.user.domain.User#User(String, String, List) Builder 패턴을 이용해서 User엔티티를 만듬
     * @param email 유니크한 값을 가짐
     * @param password {@link PasswordEncoder}를 사용하여 저장할 때는 password를 encode하여 저장
     * @return User객체를 반환하고, {@link gugus.pleco.domain.user.controller.UserController}에서 id를 이용해서 응답함
     * @throws UserDuplicatedException 회원가입을 진행하려는 email이 이미 존재 시 발생하는 예외
     */
    @Log
    @Override
    public User join(String email, String password) throws UserDuplicatedException {
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

    /**
     *
     * User 회원 가입을 진행할 때 email 중복 체크를 해서 boolean 값으로 돌려주는 메서드
     *
     *
     * @param email 사용자의 email 값을 넘겨 받음
     * @return 이미 같은 email로 회원 가입한 사람이 있다면 false 없다면 true를 반환
     * @throws UserDuplicatedException 회원가입을 진행하려는 email이 이미 존재 시 발생하는 예외
     */
    @Log
    @Override
    public boolean checkEmail(String email) throws UserDuplicatedException {
        userRepository.findByUsername(email).ifPresent(
                m -> {
                    throw new UserDuplicatedException("이미 있는 아이디입니다.");
                }
        );
        return true;
    }

    /**
     *
     * 사용자의 로그인을 담당하는 메서드 로그인에 성공했다면, access token을 반환하고, refresh token은 DB에 저장하여
     * 다음 access token이 만료 되었을 때 refresh token을 사용해서 access token 재발급 진행
     *
     * @param email 사용자의 email 값을 넘겨 받음
     * @param password 사용자의 password 값을 넘겨 받음
     * @return 로그인이 성공했다면, access token String 값을 반환한다.
     * @throws UsernameNotFoundException 로그인을 진행하려는 email이 이미 존재 시 발생하는 예외
     * @throws BadCredentialsException 넘어온 password가 DB에 저장된 password가 일치하지 않을 때 발생하는 예외
     */
    @Log
    @Override
    public Map<String,String> login(String email, String password) throws UsernameNotFoundException, BadCredentialsException{
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
    public String useRefreshTokenForAccessToken(String refreshToken) throws UsernameNotFoundException, BadCredentialsException {
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
