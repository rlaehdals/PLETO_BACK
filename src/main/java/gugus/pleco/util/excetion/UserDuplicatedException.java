package gugus.pleco.util.excetion;

public class UserDuplicatedException extends RuntimeException{

    public UserDuplicatedException(String string) {
        super(string);
    }
}
