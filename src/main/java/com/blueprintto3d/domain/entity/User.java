package com.blueprintto3d.domain.entity;

import com.blueprintto3d.domain.enum_class.UserRole;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private String email;
    private String password;

    private String image;
    private String provider;
    private String providerId;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
