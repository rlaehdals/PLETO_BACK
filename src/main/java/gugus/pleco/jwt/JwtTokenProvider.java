package gugus.pleco.jwt;

import gugus.pleco.repositroy.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
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
    private String accessToken;

    @Value("${refreshtoken.secret")
    private String refreshToken;

    private final Long ACCESS_TOKEN_TIME = 20000L; //accessToken 유효시간 30분
    private final Long REFRESH_TOKEN_TIME = 60 * 60 * 24 * 30 * 1000L; //유효시간 1달


    private final UserRepository userRepository;


    @PostConstruct
    protected void init() {
        accessToken = Base64.getEncoder().encodeToString(accessToken.getBytes());
        refreshToken = Base64.getEncoder().encodeToString(accessToken.getBytes());
    }

    public Map<String, String> createToken(String UserPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(UserPk);
        claims.put("roles", roles);
        Date now = new Date();
        String accestToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, accessToken)
                .compact();
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, this.refreshToken)
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("access", accestToken);
        map.put("refresh", refreshToken);
        return map;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = (UserDetails) userRepository.findByUsername(this.getUserPk(token)).get();
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserPk(String token){
        return Jwts.parser().setSigningKey(accessToken).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validateAccessToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(accessToken).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    public boolean validateRefreshToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(refreshToken).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
