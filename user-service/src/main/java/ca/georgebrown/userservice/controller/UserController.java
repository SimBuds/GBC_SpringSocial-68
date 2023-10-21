package ca.georgebrown.userservice.controller;

import ca.georgebrown.userservice.dto.UserRequest;
import ca.georgebrown.userservice.dto.UserResponse;
import ca.georgebrown.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> getUser(@PathVariable String userId) {
        UserResponse user = userService.getUserByUsername(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UserRequest userRequest) {
        UserResponse updatedUser = userService.updateUser(userId, userRequest);
        if (updatedUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse username = userService.loginUser(userRequest);
        if (username == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(@Valid @RequestBody UserRequest userRequest) {
        userService.logoutUser(userRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}