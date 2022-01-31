package gugus.pleco.jwt;

import gugus.pleco.domain.User;
import gugus.pleco.repositroy.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        String email = jwtTokenProvider.getUserPk(token);
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if(token!=null){
            if(jwtTokenProvider.validateToken(token)){
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else{
                User user = userRepository.findByUsername(email).get();
                String refreshToken = user.getRefreshToken();
                if(jwtTokenProvider.validateToken(refreshToken)){
                    Map<String, String> map = jwtTokenProvider.createToken(email, user.getRoles());
                    httpResponse.setHeader("X-AUTH-TOKEN",map.get("access"));
                    user.setRefreshToken(map.get("refresh"));
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request,response);
    }
}
