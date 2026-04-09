package thanhanh.job_recruitment.service;

import thanhanh.job_recruitment.domain.User;
import thanhanh.job_recruitment.dto.request.UserRequest;

import java.util.List;

public interface UserService {
    User createUser(UserRequest user);
    void deleteUser(long id);
    User fetchUserById (long id);
    List<User> fetchAllUser ();
    User updateUser (User user);
    User fetchUserByEmail(String email);
}
