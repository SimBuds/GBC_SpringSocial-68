package ca.gbc.commentservice;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.UserResponse;
import ca.gbc.commentservice.model.Comment;
import ca.gbc.commentservice.repository.CommentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CommentRepository commentRepository;

	@MockBean
	private WebClient userServiceWebClient;

	@BeforeEach
	void setUp() {
		UserResponse mockUserResponse = new UserResponse(1L, "username", "email", "fullName");
		Comment mockComment = new Comment(1L, "somePostId", "Sample Comment", "commentAuthor123", LocalDateTime.now(), LocalDateTime.now());

		// Mocking WebClient interactions
		WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
		WebClient.RequestHeadersSpec requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
		WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

		Mockito.when(userServiceWebClient.get()).thenReturn(requestHeadersUriSpec);
		Mockito.when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersSpec);
		Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		Mockito.when(responseSpec.bodyToMono(UserResponse.class)).thenReturn(Mono.just(mockUserResponse));

		// Mocking CommentRepository interactions
		Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(mockComment));
		Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(mockComment);
	}

	// Test methods
	void createComment() throws Exception {
		CommentRequest commentRequest = new CommentRequest("somePostId", "Sample Comment", "commentAuthor123", LocalDateTime.now(), LocalDateTime.now());
		String commentRequestString = objectMapper.writeValueAsString(commentRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(commentRequestString))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(containsString("Sample Comment")));
	}
}