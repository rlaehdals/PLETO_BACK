package gugus.pleco.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class UserEcoListDto {
    String ecoName;
    LocalTime coolTime;
    String status;
}
