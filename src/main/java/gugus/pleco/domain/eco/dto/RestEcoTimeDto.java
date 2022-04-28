package gugus.pleco.domain.eco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class RestEcoTimeDto {
    LocalTime restCoolTime;
}
