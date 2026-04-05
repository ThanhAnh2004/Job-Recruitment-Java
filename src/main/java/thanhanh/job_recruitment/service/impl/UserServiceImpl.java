package thanhanh.job_recruitment.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import thanhanh.job_recruitment.domain.User;
import thanhanh.job_recruitment.repository.UserRepository;
import thanhanh.job_recruitment.service.UserService;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Override
    public User createUser(User user) {

            User newUser = User.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .build();
            return this.userRepository.save(newUser);

    }

    public void deleteUser(long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found user"));

        this.userRepository.deleteById(id);
    }

    @Override
    public User fetchUserById(long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found user"));

        return user;
    }

    @Override
    public List<User> fetchAllUser() {
        return this.userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        User currentUser = this.userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Not found user"));

        if (currentUser != null) {
            currentUser.setName(user.getName());
            currentUser.setEmail(user.getEmail());
            currentUser.setPassword(user.getPassword());
            this.userRepository.save(currentUser);
        }

        return currentUser;
    }

}
