package ca.gbc.commentservice;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.dto.UserResponse;
import ca.gbc.commentservice.model.Comment;
import ca.gbc.commentservice.repository.CommentRepository;
import ca.gbc.commentservice.service.CommentServiceImpl;
import com.google.common.base.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CommentServiceApplicationTests {

	@Autowired
	private CommentServiceImpl commentService;

	@Mock
	private CommentRepository commentRepository;

	@Mock
	private WebClient userServiceWebClient;

	@BeforeEach
	void setUp() {
		UserResponse mockUserResponse = new UserResponse(1L, "username", "email", "fullName");
		Comment mockComment = new Comment(1L, "1", "content", "1", LocalDateTime.now(), LocalDateTime.now());

		WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
		WebClient.RequestHeadersSpec requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
		WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

		when(userServiceWebClient.get()).thenReturn(requestHeadersUriSpec);

		when(requestHeadersUriSpec.uri(anyString(), anyMap())).thenAnswer(new Answer<WebClient.RequestHeadersSpec>() {
			@Override
			public WebClient.RequestHeadersSpec answer(InvocationOnMock invocation) throws Throwable {
				return requestHeadersSpec;
			}
		});

		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

		when(responseSpec.bodyToMono(UserResponse.class)).thenReturn(Mono.just(mockUserResponse));

		when(commentRepository.save(any(Comment.class))).thenAnswer(new Answer<Comment>() {
			@Override
			public Comment answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return (Comment) args[0];
			}
		});

		when(commentRepository.findById(anyLong())).thenAnswer(new Answer<java.util.Optional<Comment>>() {
			@Override
			public java.util.Optional<Comment> answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return java.util.Optional.ofNullable(mockComment);
			}
		});

		when(commentRepository.findAll()).thenReturn(Flux.just(mockComment));

		when(commentRepository.deleteById(anyLong())).thenAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		});
	}
		@Test
	void createComment() {
		CommentRequest commentRequest = new CommentRequest("1", "content", "1", LocalDateTime.now(), LocalDateTime.now());
		CommentResponse expectedResponse = new CommentResponse(1L, "1", "content", "1", LocalDateTime.now(), LocalDateTime.now());
		CommentResponse actualResponse = commentService.createComment(commentRequest).block();

		assertEquals(expectedResponse, actualResponse);
	}

	// Similar structure for updateComment, deleteComment tests
}
