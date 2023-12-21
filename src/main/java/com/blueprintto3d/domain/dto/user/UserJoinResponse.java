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

    private Long userNo;
    private String email;


    public static UserJoinResponse of(Long userNo, String email) {
        return UserJoinResponse.builder()
                .userNo(userNo)
                .email(email)
                .build();
    }

}
