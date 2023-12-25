package com.blueprintto3d.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class AppException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 여기를 수정
        this.errorCode = errorCode;
        this.message = super.getMessage(); // 여기를 수정
    }

    @Override
    public String toString() {
        return message;
    }
}
