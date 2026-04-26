package thanhanh.job_recruitment.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import thanhanh.job_recruitment.domain.User;
import thanhanh.job_recruitment.dto.request.User.UpdateUserRequest;
import thanhanh.job_recruitment.dto.request.User.UserRequest;
import thanhanh.job_recruitment.dto.response.ApiResponse.ResultPagination;
import thanhanh.job_recruitment.dto.response.User.UserResponse;
import thanhanh.job_recruitment.service.UserService;
import thanhanh.job_recruitment.util.annotation.ApiMessage;
import thanhanh.job_recruitment.util.exception.IdInvalidException;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping
    @ApiMessage("Create a new user")
    public ResponseEntity<UserResponse> createUser (@Valid @RequestBody UserRequest user) throws IdInvalidException {
        // Check email exists
        boolean checkEmail = this.userService.existsByEmail(user.getEmail());
        if (checkEmail) {
            throw new IdInvalidException("Email " + user.getEmail() + " has existed");
        }

        UserResponse newUser = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteUser (@PathVariable("id") long id) throws IdInvalidException {
        boolean checkId = this.userService.existsById(id);
        if (!checkId) {
            throw new IdInvalidException("Not found user with id: " + id);
        }
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @ApiMessage("Fetch a user")
    public ResponseEntity<UserResponse> fetchUserById (@PathVariable("id") long id) throws IdInvalidException {
        boolean checkId = this.userService.existsById(id);
        if (!checkId) {
            throw new IdInvalidException("Not found user with id: " + id);
        }
        UserResponse user = this.userService.fetchUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping
    @ApiMessage("Fetch all user")
    public ResponseEntity<ResultPagination> fetchAllUser (
            @Filter Specification<User> spec,
            Pageable pageable
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser(spec, pageable)) ;
    }

    @PutMapping
    @ApiMessage("Update a user")
    public ResponseEntity<UserResponse> updateUser (@Valid @RequestBody UpdateUserRequest user) throws IdInvalidException {

        boolean checkId = this.userService.existsById(user.getId());
        if (!checkId) {
            throw new IdInvalidException("Not found user with id: " + user.getId());
        }

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.updateUser(user));
    }
}
