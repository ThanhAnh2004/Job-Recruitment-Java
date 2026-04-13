package thanhanh.job_recruitment.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import thanhanh.job_recruitment.domain.User;
import thanhanh.job_recruitment.dto.request.Auth.LoginRequest;
import thanhanh.job_recruitment.dto.response.Auth.LoginResponse;
import thanhanh.job_recruitment.dto.response.Auth.UserLoginResponse;
import thanhanh.job_recruitment.service.UserService;
import thanhanh.job_recruitment.util.SecurityUtil;
import thanhanh.job_recruitment.util.annotation.ApiMessage;

@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthController {

    AuthenticationManagerBuilder authenticationManagerBuilder;
    SecurityUtil securityUtil;
    UserService userService;

    @NonFinal
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @PostMapping("/login")
    @ApiMessage("Login success")
    public ResponseEntity<LoginResponse> login (@Valid @RequestBody LoginRequest loginRequest ) {

        // add input include username/password into Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUserName(),
                loginRequest.getPassword()
        );

        // authenticate user => write function loadUserByUsername
        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(authenticationToken);

        // set user information log into context (will maybe use)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = this.securityUtil.createAccessToken(authentication);
        String refreshToken = this.securityUtil.createRefreshToken(authentication);

        // Update refresh token into database
        this.userService.updateUserToken(refreshToken, loginRequest.getUserName());

        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token",refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        User currentUser = this.userService.fetchUserByEmail(loginRequest.getUserName());
        UserLoginResponse user = UserLoginResponse.builder()
                .id(currentUser.getId())
                .email(currentUser.getEmail())
                .name(currentUser.getName())
                .build();

            LoginResponse response = LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(user)
                    .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(response);
    }
}
