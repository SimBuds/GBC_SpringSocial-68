package ca.georgebrown.userservice;

import ca.georgebrown.userservice.dto.UserRequest;
import ca.georgebrown.userservice.dto.UserResponse;
import ca.georgebrown.userservice.model.User;
import ca.georgebrown.userservice.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceImplTests extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    UserRequest getUserRequest() {
        return UserRequest.builder()
                .username("john_doe")
                .password("secure123")
                .email("john@example.com")
                .fullName("John Doe")
                .build();
    }

    private List<User> getUserList() {
        List<User> userList = new ArrayList<>();
        User user = User.builder()
                .username("john_doe")
                .password("secure123")
                .email("john@example.com")
                .fullName("John Doe")
                .build();
        userList.add(user);
        return userList;
    }

    private String convertObjectToJson(List<UserResponse> userList) throws Exception {
        return objectMapper.writeValueAsString(userList);
    }

    private List<UserResponse> convertJsonToObject(String jsonString) throws Exception {
        return objectMapper.readValue(jsonString, new TypeReference<List<UserResponse>>() {});
    }

    @Test
    void createUser() throws Exception {
        UserRequest userRequest = getUserRequest();
        String userRequestString = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestString))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Assertions.assertTrue(userRepository.findAll().size() > 0);
    }

    @Test
    void getUserById() throws Exception {
        // Setup
        userRepository.saveAll(getUserList());

        // Action
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/users/1") // Assuming the ID of the created user is 1 for simplicity
                .accept(MediaType.APPLICATION_JSON));

        // Verify
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andDo(MockMvcResultHandlers.print());

        MvcResult result = response.andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNodes = new ObjectMapper().readTree(jsonResponse);

        int actualSize = jsonNodes.size();
        int expectedSize = getUserList().size();

        assertEquals(expectedSize, actualSize);
    }

    // ... Include the update and delete tests similarly
}
