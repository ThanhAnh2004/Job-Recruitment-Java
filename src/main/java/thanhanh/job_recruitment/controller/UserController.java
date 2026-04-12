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
import thanhanh.job_recruitment.dto.request.UserRequest;
import thanhanh.job_recruitment.dto.response.ResultPagination;
import thanhanh.job_recruitment.dto.response.UserResponse;
import thanhanh.job_recruitment.service.UserService;
import thanhanh.job_recruitment.util.annotation.ApiMessage;
import thanhanh.job_recruitment.util.exception.IdInvalidException;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping("/create")
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

    @GetMapping("/get-all")
    @ApiMessage("Fetch all user")
    public ResponseEntity<ResultPagination> fetchAllUser (
            @Filter Specification<User> spec,
            Pageable pageable
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser(spec, pageable)) ;
    }

    @PutMapping("/update")
    @ApiMessage("Update a user")
    public ResponseEntity<UserResponse> updateUser (@RequestBody User user) throws IdInvalidException {

        boolean checkId = this.userService.existsById(user.getId());
        if (!checkId) {
            throw new IdInvalidException("Not found user with id: " + user.getId());
        }

        UserResponse currentUser = this.userService.updateUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(currentUser);
    }
}
