package com.blueprintto3d.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {

    private String jwt;

    public static UserLoginResponse of(String token) {
        return UserLoginResponse.builder()
                .jwt(token)
                .build();
    }

}
