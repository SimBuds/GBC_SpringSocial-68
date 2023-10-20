package ca.georgebrown.userservice.service;

import ca.georgebrown.userservice.dto.UserRequest;
import ca.georgebrown.userservice.dto.UserResponse;

import java.util.List;

public interface UserService {
    void createUser(UserRequest userRequest);

    UserResponse updateUser(String userId, UserRequest userRequest);

    void deleteUser(String userId);

    List<UserResponse> getAllUsers();

    UserResponse getUserByUsername(String username);

    UserResponse loginUser(UserRequest userRequest);

    UserResponse logoutUser(UserRequest userRequest);
}
