package com.blueprintto3d.domain.dto.user;

import com.blueprintto3d.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long no;
    private String email;
    private String password;
    private String role;

    public static UserDto of (User user) {
        return UserDto.builder()
                .no(user.getNo())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole().name())
                .build();
    }
}
