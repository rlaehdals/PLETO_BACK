package gugus.pleco.service;

import gugus.pleco.excetion.UserDupulicatedException;
import gugus.pleco.repositroy.EcoRepository;
import gugus.pleco.repositroy.UserEcoRepository;
import gugus.pleco.repositroy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import gugus.pleco.controller.dto.UserDto;
import gugus.pleco.domain.Eco;
import gugus.pleco.domain.User;
import gugus.pleco.domain.UserEco;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEcoRepository userEcoRepository;
    private final EcoRepository ecoRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("등록되지 않은 아이디입니다. "));
    }

    @Override
    public User join(UserDto userDto) throws UserDupulicatedException{
        userRepository.findByUsername(userDto.getEmail())
                .ifPresent(m->{
                    throw new UserDupulicatedException("이미 존재하는 아이디 입니다.");
                });
        User user = userRepository.save(User.builder()
                .username(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.password))
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        List<Eco> all = ecoRepository.findAll();
        for(Eco eco: all){
            UserEco eco1 = UserEco.createEco(user, eco);
            userEcoRepository.save(eco1);
        }
        return user;
    }

    @Override
    public boolean checkEmail(String email) throws UserDupulicatedException {
        User user = userRepository.findByUsername(email).get();

        if(user!=null){
            throw new UserDupulicatedException("이미 가입된 이메일 입니다.");
        }
        return true;
    }

    @Override
    public User login(UserDto userDto) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(userDto.getEmail())
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("등록되지 않은 아이디입니다.");
                });
        if(!passwordEncoder.matches(userDto.getPassword(),user.getPassword())){
            throw new BadCredentialsException("잘못된 비밀번호입니다.");
        }
        return user;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }
}
