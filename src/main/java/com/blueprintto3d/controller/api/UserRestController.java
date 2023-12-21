package com.blueprintto3d.controller.api;

import com.blueprintto3d.domain.dto.user.*;
import com.blueprintto3d.exception.Response;
import com.blueprintto3d.service.EmailService;
import com.blueprintto3d.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Api(tags = "회원 가입 및 로그인")
@Validated
public class UserRestController {

    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/join")
    @ApiOperation(value = "회원가입")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest) {
        UserJoinResponse userJoinResponse = userService.join(userJoinRequest);
        return Response.success(userJoinResponse);
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
        UserLoginResponse userLoginResponse = userService.login(userLoginRequest, response);
        return Response.success(userLoginResponse);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃")
    public Response<UserLogoutResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        UserLogoutResponse userLogoutResponse = userService.logout(request, response);
        return Response.success(userLogoutResponse);
    }

    // 인증 메일 보내기
    @GetMapping("/send-auth-email")
    @ApiOperation(value = "인증 이메일 보내기", notes = "이메일 인증을 위한 인증 이메일을 보냅니다.")
    @ApiImplicitParam(name = "email", value = "이메일")
    public Response<String> sendAuthEmail(@RequestParam String email) throws Exception {
        return Response.success(emailService.sendLoginAuthMessage(email));
    }


    // 인증 메일 확인 하기
    @GetMapping("/check-auth-email")
    @ApiOperation(value = "이메일 인증 코드 확인", notes = "이메일의 인증 코드를 확인합니다.")
    @ApiImplicitParam(name = "code", value = "인증코드")
    public Response<Boolean> checkAuthEmail(@RequestParam String code) {
        System.out.println(code);
        if (emailService.getData(code) == null) return Response.success(false);
        else return Response.success(true);
    }

}
