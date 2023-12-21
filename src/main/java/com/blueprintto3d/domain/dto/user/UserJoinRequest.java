package com.blueprintto3d.domain.dto.user;

import com.blueprintto3d.domain.entity.User;
import com.blueprintto3d.domain.enum_class.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequest {

    @NotBlank(message = "email을 입력해주세요.")
    @Email(message = "이메일 형식에 맞게 작성해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp="[a-zA-Z1-9]{6,12}", message = "비밀번호는 영어와 숫자로 포함해서 6~12자리 이내로 입력해주세요.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    private String code;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .name(this.name)
                .role(UserRole.ROLE_USER)
                .build();
    }
}
