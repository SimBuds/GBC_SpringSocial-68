package ca.georgebrown.userservice.service;

import ca.georgebrown.userservice.dto.UserRequest;
import ca.georgebrown.userservice.dto.UserResponse;

import java.util.List;

public interface UserService {
    void createUser(UserRequest userRequest);

    String updateUser(String userId, UserRequest userRequest);

    void deleteUser(String userId);

    List<UserResponse> getAllUsers();

    UserResponse getUserByUsername(String userId);
    String loginUser(UserRequest userRequest);

    String logoutUser(UserRequest userRequest);
}
