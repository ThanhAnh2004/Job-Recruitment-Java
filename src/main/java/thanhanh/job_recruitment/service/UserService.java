package thanhanh.job_recruitment.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import thanhanh.job_recruitment.domain.User;
import thanhanh.job_recruitment.dto.request.User.UserRequest;
import thanhanh.job_recruitment.dto.response.ApiResponse.ResultPagination;
import thanhanh.job_recruitment.dto.response.User.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest user);
    void deleteUser(long id);
    UserResponse fetchUserById (long id);
    ResultPagination fetchAllUser (Specification<User> spec, Pageable pageable);
    UserResponse updateUser (User user);
    User fetchUserByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsById(long id);
    void updateUserToken (String token, String email);
    User fetchUserByTokenAndEmail (String token, String email);
}
