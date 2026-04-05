package thanhanh.job_recruitment.service;

import thanhanh.job_recruitment.domain.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    void deleteUser(long id);
    User fetchUserById (long id);
    List<User> fetchAllUser ();
    User updateUser (User user);
}
