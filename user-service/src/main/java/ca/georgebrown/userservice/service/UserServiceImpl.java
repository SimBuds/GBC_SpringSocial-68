package ca.georgebrown.userservice.service;

import ca.georgebrown.userservice.dto.UserRequest;
import ca.georgebrown.userservice.dto.UserResponse;
import ca.georgebrown.userservice.model.User;
import ca.georgebrown.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @Transactional
    public void createUser(UserRequest userRequest) {
        User existingUser = userRepository.getUserByUsername(userRequest.getUsername());
        if (existingUser != null) {
            logger.error("User already exists with username: {}", userRequest.getUsername());
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setId(userRequest.getId());
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setEmail(userRequest.getEmail());
        user.setFullName(userRequest.getFullName());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(String username, UserRequest userRequest) {
        // Check if a user with the same username already exists
        User existingUser = userRepository.getUserByUsername(username);
        if (existingUser == null) {
            logger.error("User not found with username: {}", username);
            throw new RuntimeException("User not found");
        }

        existingUser.setUsername(userRequest.getUsername());
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setFullName(userRequest.getFullName());
        userRepository.save(existingUser);
        return convertToUserResponse(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.getUserByUsername(userId);
        if (user == null) {
            logger.error("User not found with username: {}", userId);
            throw new RuntimeException("User not found");
        } else {
            userRepository.delete(user);
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToUserResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserByUsername(String userId) {
        // Return the user if found, otherwise throw an exception
        User user = userRepository.getUserByUsername(userId);
        if (user == null) {
            logger.error("User not found with username: {}", userId);
            throw new RuntimeException("User not found");
        }
        // Return the username
        return convertToUserResponse(user);
    }

    @Override
    public UserResponse loginUser(UserRequest userRequest) {
        User user = userRepository.getUserByUsername(userRequest.getUsername());
        if(user != null) {
            if (userRequest.getPassword().equals(user.getPassword())) {
                return convertToUserResponse(user);
            }
        }
        return null;
    }

    @Override
    public UserResponse logoutUser(UserRequest userRequest) {
        User user = userRepository.getUserByUsername(userRequest.getUsername());
        if(user != null) {
            return convertToUserResponse(user);
        }
        return null;
    }

    private UserResponse convertToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setFullName(user.getFullName());
        return userResponse;
    }
}