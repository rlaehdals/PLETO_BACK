package gugus.pleco.controller.dto;

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
