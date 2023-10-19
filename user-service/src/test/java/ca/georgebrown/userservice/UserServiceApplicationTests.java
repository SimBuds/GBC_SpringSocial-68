package ca.georgebrown.userservice;

import ca.georgebrown.userservice.dto.UserRequest;
import ca.georgebrown.userservice.dto.UserResponse;
import ca.georgebrown.userservice.model.User;
import ca.georgebrown.userservice.repository.UserRepository;
import ca.georgebrown.userservice.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceApplicationTests extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    UserRequest getUserRequest() {
        return UserRequest.builder()
                .username("john_doe")
                .password("password123")
                .email("john_doe@example.com")
                .fullName("John Doe")
                .build();
    }

    private List<User> getUserList() {
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setUsername("john_doe");
        user.setPassword("password123");
        user.setEmail("john_doe@example.com");
        user.setFullName("John Doe");
        userList.add(user);
        return userList;
    }

    private String convertObjectToJson(List<UserResponse> userList) throws Exception {
        return objectMapper.writeValueAsString(userList);
    }

    private List<UserResponse> convertJsonToObject(String jsonString) throws Exception {
        return objectMapper.readValue(jsonString, new TypeReference<List<UserResponse>>() {
        });
    }

    @Test
    void createUsers() throws Exception {
        UserRequest userRequest = getUserRequest();
        String userRequestString = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestString))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Assertions.assertTrue(userRepository.findAll().size() > 0);
    }

    @Test
    void getUserById() throws Exception {
        // Prepare saved user
        // check if user exists
        User user = getUserList().get(0);
        // if not, create user
        if(userRepository.getUserByUsername(user.getUsername()) == null) {
            userService.createUser(UserRequest.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .build());
        }

        // Action
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/" + user.getUsername())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Assert
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        UserResponse actualUserResponse = objectMapper.readValue(actualResponseBody, UserResponse.class);
        assertEquals(user.getUsername(), actualUserResponse.getUsername());
    }

    @Test
    void updateUser() throws Exception {
        // Prepare saved user
        User user = getUserList().get(0);
        if(userRepository.getUserByUsername(user.getUsername()) == null) {
            userService.createUser(UserRequest.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .build());
        }

        // Prepare updated user
        UserRequest updatedUserRequest = UserRequest.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();

        // Action
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/user/" + user.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteUser() throws Exception {
        // Prepare saved user
        User user = getUserList().get(0);
        if(userRepository.getUserByUsername(user.getUsername()) == null) {
            userService.createUser(UserRequest.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .build());
        }

        // Action
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/user/" + user.getUsername())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}