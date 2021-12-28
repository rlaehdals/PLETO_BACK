package gugus.pleco.controller.userdto;

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
