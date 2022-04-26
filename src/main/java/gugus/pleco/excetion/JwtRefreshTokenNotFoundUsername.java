package gugus.pleco.excetion;

public class JwtRefreshTokenNotFoundUsername extends RuntimeException{
    public JwtRefreshTokenNotFoundUsername(String message) {
        super(message);
    }
}
