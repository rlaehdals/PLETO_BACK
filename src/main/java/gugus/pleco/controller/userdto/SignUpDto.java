package gugus.pleco.controller.userdto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SignUpDto {
    Long userId;
    boolean success;
}
