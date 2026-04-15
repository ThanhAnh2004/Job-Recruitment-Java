package thanhanh.job_recruitment.dto.response.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import thanhanh.job_recruitment.dto.response.Company.CompanyResponse;
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
    CompanyResponse company;
    Instant createdAt;
    String createdBy;
    Instant updatedAt;
    String updatedBy;
}
