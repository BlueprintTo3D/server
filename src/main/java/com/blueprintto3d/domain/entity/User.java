package com.blueprintto3d.domain.entity;

import com.blueprintto3d.domain.dto.user.UserJoinRequest;
import com.blueprintto3d.domain.enum_class.UserRole;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private String email;
    private String password;
    private String name;

    private String provider;
    private String providerId;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public static User of(UserJoinRequest userJoinRequest, String password) {
        return User.builder()
                .name(userJoinRequest.getName())
                .email(userJoinRequest.getEmail())
                .password(password)
                .role(UserRole.ROLE_USER)
                .build();
    }

    public void changePassword(String password) {
        this.password = password;
    }

}
