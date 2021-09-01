package gugus.pleco.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UserDto {

    @NotNull
    @Email
    public String email;

    @NotNull
    @NotBlank
    public String password;
}
