package gugus.pleco.controller.userdto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    public String email;

    public String password;
}
