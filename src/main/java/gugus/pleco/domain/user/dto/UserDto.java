package gugus.pleco.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class UserDto {

    @Email(message = "이메일 형식으로 입력해주세요.")
    public String email;

    @Size(min = 8, max = 20, message = "비밀번호는 8~20 범위로 입력해주세요")
    public String password;
}
