package gugus.pleco.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SignUpDto {
    Long userId;
    boolean success;
}
