package thanhanh.job_recruitment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import thanhanh.job_recruitment.util.constant.GenderEnum;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    long id;
    String name;
    String email;
    int age;
    GenderEnum gender;
    String address;
    Instant createdAt;
    String createdBy;
    Instant updatedAt;
    String updatedBy;
    String error;
}
