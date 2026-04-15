package thanhanh.job_recruitment.dto.request.User;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thanhanh.job_recruitment.domain.Company;
import thanhanh.job_recruitment.util.constant.GenderEnum;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String name;
    String email;
    String password;
    int age;
    GenderEnum gender;
    String address;
    Company company;
}
