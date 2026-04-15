package thanhanh.job_recruitment.dto.response.Auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    @JsonProperty("access_token")
    private String accessToken;
    private UserLoginResponse user;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class UserGetAccount {
        UserLoginResponse user;
    }
}
