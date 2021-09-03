package gugus.pleco.excetion;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDto {
    String message;
    boolean success;
}
