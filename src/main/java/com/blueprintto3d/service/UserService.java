package com.blueprintto3d.service;

import com.blueprintto3d.domain.dto.user.*;
import com.blueprintto3d.domain.entity.User;
import com.blueprintto3d.exception.AppException;
import com.blueprintto3d.exception.ErrorCode;
import com.blueprintto3d.jwt.TokenProvider;
import com.blueprintto3d.repository.RefreshTokenRepository;
import com.blueprintto3d.repository.UserRepository;
import com.blueprintto3d.util.CookieUtil;
import com.blueprintto3d.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisService redisService;

    private final long accessTokenExpireTimeMs = 1000 * 60 * 30L;
    private final long refreshTokenExpireTimeMs = 1000 * 60 * 60 * 24L;

    @Value("${jwt.token.secret}")
    private String secretKey;

    public UserJoinResponse join(UserJoinRequest userJoinRequest) {
        //email 중복확인
        userRepository.findByEmail(userJoinRequest.getEmail()).ifPresent(user -> {
            throw new AppException(ErrorCode.DUPLICATED_USER_EMAIL);
        });

        //UserJoinRequest -> User
        User user = User.of(userJoinRequest, encoder.encode(userJoinRequest.getPassword()));

        //User 저장
        user = userRepository.save(user);
        //User -> UserJoinResponse 변환 후 반환
        return UserJoinResponse.of(user.getNo(), user.getName());
    }

    public UserLoginResponse login(UserLoginRequest userLoginRequest, HttpServletResponse response) {

        //이메일 체크
        User findUser = userRepository.findByEmail(userLoginRequest.getEmail()).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        });
        //비밀번호 체크
        if (!encoder.matches(userLoginRequest.getPassword(), findUser.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        //토큰 발행
        String accessToken = JwtTokenUtil.createToken(findUser.getNo(), secretKey, accessTokenExpireTimeMs);
        String refreshToken = JwtTokenUtil.createToken(findUser.getNo(), secretKey, refreshTokenExpireTimeMs);

        //레디스 저장
        redisService.setDataExpire(accessToken, refreshToken, refreshTokenExpireTimeMs / 1000);

        //쿠키 저장
        CookieUtil.saveCookie(response, "token", accessToken);

        return UserLoginResponse.of(accessToken);
    }

    /**
     * 로그아웃 메서드
     *
     * @param response 쿠키를 설정하기 위해 매개변수로 받은 response
     * @return 로그아웃 성공 여부 메세지를 반환한다.
     */
    public UserLogoutResponse logout(HttpServletRequest request, HttpServletResponse response) {
        //redis aceess token logout
        String accessToken = CookieUtil.getCookieValue(request, "token");
        redisService.deleteData(accessToken);
        redisService.setDataExpire(accessToken, "LOGOUT", accessTokenExpireTimeMs / 1000);
        //쿠키 초기화
        CookieUtil.initCookie(response, "token");
        return new UserLogoutResponse("로그아웃되었습니다.");
    }

    public boolean isReissueable(HttpServletRequest request, HttpServletResponse response) {
        log.info("토큰 재발급 시도");
        //accessToken 가져옴
        String accessToken = CookieUtil.getCookieValue(request, "token");
        //redis에서 refreshToken 가져오기 및 refresh 유효성 체크
        String refreshToken = redisService.getData(accessToken);
        if (refreshToken == null) {
            log.error("refresh 토큰이 없습니다.");
            return false;
        }
        if (!JwtTokenUtil.isValid(refreshToken, secretKey).equals("OK")){
            log.error("refresh 토큰이 유효하지 않습니다.");
            return false;
        }
        Long userNo = JwtTokenUtil.getUserNo(refreshToken, secretKey);
        //redis에서 기존 refresh 데이터 삭제
        redisService.deleteData(accessToken);
        //토큰 재발행 및 redis에 저장
        String newAccessToken = JwtTokenUtil.createToken(userNo, secretKey, accessTokenExpireTimeMs);
        String newRefreshToken = JwtTokenUtil.createToken(userNo, secretKey, refreshTokenExpireTimeMs);
        redisService.setDataExpire(newAccessToken, newRefreshToken, refreshTokenExpireTimeMs / 1000);
        //accessToken 쿠키에 저장
        if (request.getRequestURL().toString().contains("api"))
            CookieUtil.saveCookie(response, "token", newAccessToken);
        else CookieUtil.savePathCookie(response, "token", newAccessToken, "/");
        log.info(request.getRequestURL().toString());
        log.info("토큰 재발급 성공");
        return true;
    }
}
