package gugus.pleco.util.jwt;

import gugus.pleco.domain.user.domain.User;
import gugus.pleco.util.excetion.JwtRefreshTokenNotFoundUsername;
import gugus.pleco.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hRequest = (HttpServletRequest) request;
        String accessToken = jwtTokenProvider.resolveToken(hRequest);
        if(accessToken!=null){
            String email = ((HttpServletRequest) request).getHeader("USERNAME");
            log.info("{}", email);
            User user = userRepository.findByUsername(email).orElseThrow(()->{
                throw new JwtRefreshTokenNotFoundUsername("없는 유저의 요청입니다. ");
            });
            if(jwtTokenProvider.validateAccessToken(accessToken) || jwtTokenProvider.validateRefreshToken(user.getRefreshToken())){
                Map<String, String> tokenMap = jwtTokenProvider.createToken(email, user.getRoles());
                HttpServletResponse hResponse = (HttpServletResponse) response;
                hResponse.setHeader("X-AUTH-TOKEN",tokenMap.get("access"));
                hRequest.setAttribute("X-AUTH-TOKEN",tokenMap.get("access"));
                Authentication authentication = jwtTokenProvider.getAuthentication(tokenMap.get("access"));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request,response);
    }
}