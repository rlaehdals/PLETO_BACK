package gugus.pleco.controller.ecodto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class RestEcoTimeDto {
    LocalTime restCoolTime;
}
