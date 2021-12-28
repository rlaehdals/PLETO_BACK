package gugus.pleco.controller.pleedto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePleeDto {
    String pleeName;
    Long completeCount;
}
