package com.blueprintto3d.configuration;

import com.blueprintto3d.exception.ErrorCode;
import com.blueprintto3d.exception.ErrorResponse;
import com.blueprintto3d.exception.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_PERMISSION.name(), ErrorCode.INVALID_PERMISSION.getMessage());
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(ErrorCode.INVALID_PERMISSION.getStatus().value());

        try (OutputStream outputStream = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(outputStream, Response.error(errorResponse));
            outputStream.flush();
        }
    }
}
