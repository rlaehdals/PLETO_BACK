package gugus.pleco.jwt;

import gugus.pleco.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        /*
        JwtProvider 의 경우 멤버 변수를 가지고 있으므로 bean 으로 등록하여 사용하는 것은 옳지 않다.
        하지만 UserService 를 주입해줘야 하기 때문에  ApplicationContext 에서 가져와서 주입해준다.
         */

        AnnotationConfigApplicationContext apc = new AnnotationConfigApplicationContext();
        UserService userService = (UserService) apc.getBean("userService");
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(userService);

        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        if(token!=null && jwtTokenProvider.validateToken(token)){
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request,response);
    }
}
