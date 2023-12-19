package com.blueprintto3d.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinResponse {

    private String email;
    private String role;

    public static UserJoinResponse of(UserDto userDto) {
        return UserJoinResponse.builder()
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .build();
    }

}
