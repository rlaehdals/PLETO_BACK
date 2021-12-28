package gugus.pleco.controller.userdto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginDto {
    String token;
    boolean success;
    Long pleeSize;
}
