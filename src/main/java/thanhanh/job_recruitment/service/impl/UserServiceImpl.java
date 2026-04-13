package thanhanh.job_recruitment.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import thanhanh.job_recruitment.domain.User;
import thanhanh.job_recruitment.dto.request.User.UserRequest;
import thanhanh.job_recruitment.dto.response.ApiResponse.Meta;
import thanhanh.job_recruitment.dto.response.ApiResponse.ResultPagination;
import thanhanh.job_recruitment.dto.response.User.UserResponse;
import thanhanh.job_recruitment.repository.UserRepository;
import thanhanh.job_recruitment.service.UserService;


import java.util.Optional;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserRequest user) {

        boolean checkExists = this.existsByEmail(user.getEmail());

        String hashPassword = passwordEncoder.encode(user.getPassword());

            User newUser = User.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .password(hashPassword)
                    .age(user.getAge())
                    .address(user.getAddress())
                    .gender(user.getGender())
                    .build();
            this.userRepository.save(newUser);

            return this.mapperUserToUserResponse(newUser);

    }

    public void deleteUser(long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found user"));

        this.userRepository.deleteById(id);
    }

    @Override
    public UserResponse fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);

        return this.mapperUserToUserResponse(userOptional.get());
    }

    @Override
    public ResultPagination fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);

        Page<UserResponse> pageUserResponse = pageUser.map(this::mapperUserToUserResponse);

        Meta meta = new Meta();
        ResultPagination resultPagination = new ResultPagination();

       meta.setPage(pageable.getPageNumber());
       meta.setPageSize(pageable.getPageSize());
       meta.setTotal(pageUserResponse.getTotalElements());
       meta.setPages(pageUserResponse.getTotalPages());

       resultPagination.setMeta(meta);
       resultPagination.setResult(pageUserResponse.getContent());

       return resultPagination;

    }

    @Override
    public UserResponse updateUser(User user) {
        User currentUser = this.userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Not found user"));

        if (currentUser != null) {
            currentUser.setName(user.getName());
            currentUser.setEmail(user.getEmail());
            currentUser.setPassword(user.getPassword());
            this.userRepository.save(currentUser);
        }

        return this.mapperUserToUserResponse(currentUser);
    }

    @Override
    public User fetchUserByEmail(String email) {
        Optional<User> userOptional = this.userRepository.findByEmail(email);
        return userOptional.get();
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsById(long id) {
        return this.userRepository.existsById(id);
    }

    @Override
    public void updateUserToken(String token, String email) {
        User currentUser = this.fetchUserByEmail(email);

        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    private UserResponse mapperUserToUserResponse (User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .gender(user.getGender())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .updatedAt(user.getUpdatedAt())
                .updatedBy(user.getUpdatedBy())
                .build();
    }

}
