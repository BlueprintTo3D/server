package com.blueprintto3d.controller;

import com.blueprintto3d.domain.dto.user.UserJoinRequest;
import com.blueprintto3d.domain.dto.user.UserLoginRequest;
import com.blueprintto3d.exception.AppException;
import com.blueprintto3d.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@Slf4j
@ApiIgnore
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    /**
     * 로그인 페이지 불러오는 메서드
     *
     * @param model UserLoginRequest 객체를 타임리프로 넘긴다.
     * @return login 페이지를 반환한다.
     */
    @GetMapping("/users/login")
    public String login(Model model) {
        model.addAttribute("userLoginRequest", new UserLoginRequest());
        return "users/login";
    }

    /**
     * 로그인 페이지에서 입력한 정보를 가지고 로그인을 하는 메서드
     *
     * @param userLoginRequest 로그인 요청 dto
     * @param response         쿠키를 설정하기 위해 response를 서비스로 넘긴다.
     * @return 로그인 성공하면 login-test 페이지로 리다이렉트한다.
     */
    @PostMapping("/users/login")
    public String doLogin(@Validated @ModelAttribute UserLoginRequest userLoginRequest, BindingResult bindingResult, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return "users/login";
        }

        try {
            userService.login(userLoginRequest, response);
            log.info("🗃로그인 email : {}", userLoginRequest.getEmail());
            log.info("🗃로그인 password : {}", userLoginRequest.getPassword());
        } catch (AppException e) {
            log.info("🗃로그인 email : {}", userLoginRequest.getEmail());
            log.info("🗃로그인 password : {}", userLoginRequest.getPassword());
            log.info("🗃로그인 실패 {}", e.getMessage());
            bindingResult.reject("loginFail", e.getMessage());
        }

        if (bindingResult.hasErrors()) {
            return "users/login";
        }

        return "redirect:/";
    }

    /**
     * 로그아웃을 하는 메서드
     *
     * @param response 쿠키를 설정하기 위해 response를 서비스로 넘긴다.
     * @return 로그아웃 이후 test 페이지로 리다이렉트한다.
     */
    @PostMapping("/users/logout")
    public String doLogout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
        return "redirect:/";
    }

    /**
     * 회원 가입 폼 호출 메서드
     *
     * @param model userJoinRequest를 넘겨준다.
     * @return join 페이지를 반환한다.
     */
    @GetMapping("/users/join")
    public String join(@RequestParam(required = false) String email, Model model) {
        UserJoinRequest userJoinRequest = new UserJoinRequest();
        if (email != null) {
            userJoinRequest.setEmail(email);
        }
        model.addAttribute("userJoinRequest", userJoinRequest);
        return "users/join";
    }

    /**
     * 회원가입을 진행하는 메서드
     *
     * @param userJoinRequest 회원가입을 위한 사용자 정보를 담는 dto
     * @param bindingResult   사용자 정보 유효성 체크
     * @return 회원가입에 성공하면 로그인 페이지로 리다이렉트한다.
     */
    @PostMapping("/users/join")
    public String doJoin(@Validated @ModelAttribute UserJoinRequest userJoinRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "users/join";
        }

        try {
            userService.join(userJoinRequest);
        } catch (AppException e) {
            log.info("회원가입 실패 {}", e.getMessage());
            bindingResult.reject("joinFail", e.getMessage());
        }

        if (bindingResult.hasErrors()) {
            return "users/join";
        }

        return "redirect:/users/login";
    }





}
