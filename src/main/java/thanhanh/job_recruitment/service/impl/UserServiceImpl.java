package thanhanh.job_recruitment.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import thanhanh.job_recruitment.domain.Company;
import thanhanh.job_recruitment.domain.Role;
import thanhanh.job_recruitment.domain.User;
import thanhanh.job_recruitment.dto.request.User.UpdateUserRequest;
import thanhanh.job_recruitment.dto.request.User.UserRequest;
import thanhanh.job_recruitment.dto.response.ApiResponse.Meta;
import thanhanh.job_recruitment.dto.response.ApiResponse.ResultPagination;
import thanhanh.job_recruitment.dto.response.Company.CompanyResponse;
import thanhanh.job_recruitment.dto.response.User.UserResponse;
import thanhanh.job_recruitment.repository.CompanyRepository;
import thanhanh.job_recruitment.repository.RoleRepository;
import thanhanh.job_recruitment.repository.UserRepository;
import thanhanh.job_recruitment.service.UserService;


import java.util.Optional;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    CompanyRepository companyRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserRequest user) {

        User newUser = new User();
        // Check company
        if(user.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(user.getCompany().getId());

            Company company = companyOptional.isPresent()
                    ? companyOptional.get() : null;
            newUser.setCompany(company);
        }

        // Check role
        if (user.getRole() != null) {
            Optional<Role> roleOptional = this.roleRepository.findById(user.getRole().getId());

            Role role = roleOptional.isPresent() ? roleOptional.get() : null;
            newUser.setRole(role);
        }

        String hashPassword = passwordEncoder.encode(user.getPassword());

        newUser = User.builder()
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
        String email = thanhanh.job_recruitment.util.SecurityUtil.getCurrentUserLogin().orElse("");
        if (!email.equals("admin@gmail.com") && !email.isEmpty()) {
            User currentUser = this.userRepository.findByEmail(email).orElse(null);
            if (currentUser != null && currentUser.getCompany() != null) {
                final long companyId = currentUser.getCompany().getId();
                Specification<User> companySpec = (root, query, cb) -> cb.equal(root.get("company").get("id"), companyId);
                spec = spec == null ? companySpec : spec.and(companySpec);
            } else {
                // If not global admin and has no company, return empty result
                return ResultPagination.builder()
                        .meta(new Meta())
                        .result(java.util.Collections.emptyList())
                        .build();
            }
        }

        Page<User> pageUser = this.userRepository.findAll(spec, pageable);

        Page<UserResponse> pageUserResponse = pageUser.map(this::mapperUserToUserResponse);

        Meta meta = new Meta();
        ResultPagination resultPagination = new ResultPagination();

       meta.setPage(pageable.getPageNumber() + 1);
       meta.setPageSize(pageable.getPageSize());
       meta.setTotal(pageUserResponse.getTotalElements());
       meta.setPages(pageUserResponse.getTotalPages());

       resultPagination.setMeta(meta);
       resultPagination.setResult(pageUserResponse.getContent());

       return resultPagination;

    }

    @Override
    public UserResponse updateUser(UpdateUserRequest user) {
        User currentUser = this.userRepository.findById(user.getId()).get();

        if(user.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(user.getCompany().getId());
            if (companyOptional.isPresent()) {
                Company company = companyOptional.get();
                currentUser.setCompany(company);
            }
        }

        if (user.getRole() != null) {
            Optional<Role> roleOptional = this.roleRepository.findById(user.getRole().getId());
            if (roleOptional.isPresent()) {
                Role role = roleOptional.get();
                currentUser.setRole(role);
            }
        }

        if (user.getName() != null) {
            currentUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            currentUser.setEmail(user.getEmail());
        }
        if (user.getAge() > 0) {
            currentUser.setAge(user.getAge());
        }
        if (user.getGender() != null) {
            currentUser.setGender(user.getGender());
        }
        if (user.getAddress() != null) {
            currentUser.setAddress(user.getAddress());
        }

        this.userRepository.save(currentUser);

        return this.mapperUserToUserResponse(currentUser);
    }

    @Override
    public User fetchUserByEmail(String email) {
        Optional<User> userOptional = this.userRepository.findByEmail(email);
        return userOptional.orElse(null);
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

    @Override
    public User fetchUserByTokenAndEmail(String token, String email) {
        Optional<User> userOptional = this.userRepository.findByRefreshTokenAndEmail(token, email);

        return userOptional.orElse(null);
    }

    private UserResponse mapperUserToUserResponse (User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .gender(user.getGender())
                .address(user.getAddress())
                .role(user.getRole() != null ? this.mapperRoleToRoleUser(user.getRole()) : null)
                .company(user.getCompany() != null ? this.mapperCompanyToCompanyUser(user.getCompany()) : null)
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .updatedAt(user.getUpdatedAt())
                .updatedBy(user.getUpdatedBy())
                .build();
    }

    private UserResponse.CompanyUser mapperCompanyToCompanyUser (Company company) {
        return UserResponse.CompanyUser.builder()
                .id(company.getId())
                .name(company.getName())
                .build();
    }

    private UserResponse.RoleUser mapperRoleToRoleUser(Role role) {
        return UserResponse.RoleUser.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
