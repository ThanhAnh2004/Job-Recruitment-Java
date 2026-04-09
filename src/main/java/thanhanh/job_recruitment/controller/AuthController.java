package thanhanh.job_recruitment.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import thanhanh.job_recruitment.dto.request.LoginRequest;
import thanhanh.job_recruitment.dto.response.LoginResponse;
import thanhanh.job_recruitment.util.SecurityUtil;

@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AuthController {

    AuthenticationManagerBuilder authenticationManagerBuilder;
    SecurityUtil securityUtil;

    @PostMapping("/login")
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
        String accessToken = this.securityUtil.createToken(authentication);

        LoginResponse response = LoginResponse.builder()
                .accessToken(accessToken)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
