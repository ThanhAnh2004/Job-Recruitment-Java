package thanhanh.job_recruitment.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import thanhanh.job_recruitment.domain.User;
import thanhanh.job_recruitment.dto.request.UserRequest;
import thanhanh.job_recruitment.service.UserService;
import thanhanh.job_recruitment.service.impl.UserServiceImpl;
import thanhanh.job_recruitment.util.exception.IdInvalidException;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping("create")
    public ResponseEntity<User> createUser (@RequestBody UserRequest user) {
        User newUser = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser (@PathVariable("id") long id) throws IdInvalidException {
        if (id > 1500) {
            throw new IdInvalidException("Id khong hop le");
        }
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<User> fetchUserById (@PathVariable("id") long id) {
        User user = this.userService.fetchUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("get-all")
    public ResponseEntity<List<User>> fetchAllUser () {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser()) ;
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser (@RequestBody User user) {
        User currentUser = this.userService.updateUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(currentUser);

    }

}
