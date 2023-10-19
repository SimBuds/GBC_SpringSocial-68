package ca.georgebrown.userservice.service;

import ca.georgebrown.userservice.dto.UserRequest;
import ca.georgebrown.userservice.dto.UserResponse;
import ca.georgebrown.userservice.model.User;
import ca.georgebrown.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void createUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());  // Hashing needed
        user.setEmail(userRequest.getEmail());
        user.setFullName(userRequest.getFullName());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public String updateUser(String username, UserRequest userRequest) {
        User user = userRepository.getUserByUsername(username);
        if(user == null) {
            throw new RuntimeException("User not found");
        }
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());  // Hashing needed
        user.setEmail(userRequest.getEmail());
        user.setFullName(userRequest.getFullName());
        userRepository.save(user);
        return user.getUsername();
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.getUserByUsername(userId);
        if(user != null) {
            userRepository.delete(user);
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToUserResponse).collect(Collectors.toList());
    }

    @Override
    public String getUserId(String userId) {
        User user = userRepository.getUserByUsername(userId);
        if(user != null) {
            return user.getUsername();
        }
        return userId;
    }

    @Override
    public String loginUser(UserRequest userRequest) {
        User user = userRepository.getUserByUsername(userRequest.getUsername());
        if(user != null) {
            if(user.getPassword().equals(userRequest.getPassword())) {
                return user.getUsername();
            }
        }
        return null;
    }

    @Override
    public String logoutUser(UserRequest userRequest) {
        User user = userRepository.getUserByUsername(userRequest.getUsername());
        if(user != null) {
            if(user.getPassword().equals(userRequest.getPassword())) {
                return user.getUsername();
            }
        }
        return null;
    }

    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }
}