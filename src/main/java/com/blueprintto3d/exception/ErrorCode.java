package com.blueprintto3d.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB에러"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자가 권한이 없습니다."),
    DUPLICATED_USER_EMAIL(HttpStatus.CONFLICT, "User Email이 중복됩니다."),
    USER_NOT_FOUNDED(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "패스워드가 잘못되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "Refresh Token이 유효하지 않습니다."),
    LOGOUT_USER(HttpStatus.NOT_FOUND, "로그아웃 된 사용자입니다."),
    TOKEN_NOT_MATCH(HttpStatus.UNAUTHORIZED,"토큰의 유저 정보가 일치하지 않습니다"),



    ;

    private HttpStatus status;
    private String message;
}
