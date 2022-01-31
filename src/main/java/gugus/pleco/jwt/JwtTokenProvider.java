package gugus.pleco.jwt;

import gugus.pleco.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${accesstoken.secret}")
    private String accessKey;

    @Value("${refreshtoken.secret")
    private String refreshKey;

    private Long accessTokenTime = 30 * 60 * 1000L; //accessToken 유효시간 30분
    private Long refreshTokenTime = 60 * 60 * 24 * 30 * 1000L; //유효시간 1달


    private final UserService userService;


    @PostConstruct
    protected void init() {
        accessKey = Base64.getEncoder().encodeToString(accessKey.getBytes());
        refreshKey = Base64.getEncoder().encodeToString(accessKey.getBytes());
    }

    public Map<String, String> createToken(String UserPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(UserPk);
        claims.put("roles", roles);
        Date now = new Date();
        String accestToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenTime))
                .signWith(SignatureAlgorithm.HS256, accessKey)
                .compact();
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenTime))
                .signWith(SignatureAlgorithm.HS256, refreshKey)
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("access", accestToken);
        map.put("refresh", refreshToken);
        return map;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(accessKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validateToken(String jwtToken) {
        try {
            System.out.println(jwtToken);
            Jws<Claims> claims = Jwts.parser().setSigningKey(accessKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
