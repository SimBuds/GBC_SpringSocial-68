package ca.gbc.commentservice;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.model.Comment;
import ca.gbc.commentservice.repository.CommentRepository;
import ca.gbc.commentservice.service.CommentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentServiceApplicationTests extends AbstractContainerBaseTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private CommentService commentService;

	CommentRequest getCommentRequest() {
		return CommentRequest.builder()
				.postId("somePostId")
				.content("Sample Comment")
				.authorId("commentAuthor123")
				.build();
	}

	private List<Comment> getCommentList() {
		List<Comment> commentList = new ArrayList<>();
		Comment comment = new Comment();
		comment.setPostId("somePostId");
		comment.setContent("Sample Comment");
		comment.setAuthorId("commentAuthor123");
		commentList.add(comment);
		return commentList;
	}

	private String convertObjectToJson(List<CommentResponse> commentList) throws Exception {
		return objectMapper.writeValueAsString(commentList);
	}

	private List<CommentResponse> convertJsonToObject(String jsonString) throws Exception {
		return objectMapper.readValue(jsonString, new TypeReference<List<CommentResponse>>() {
		});
	}

	@Test
	void createComments() throws Exception {
		CommentRequest commentRequest = getCommentRequest();
		String commentRequestString = objectMapper.writeValueAsString(commentRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(commentRequestString))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		Long count = commentRepository.findAll().count().block();
		Assertions.assertTrue(count != null && count > 0);
	}

	@Test
	void getCommentById() throws Exception {
		// Arrange
		Comment comment = getCommentList().get(0);

		if(comment == null) {
			throw new Exception("Comment is null");
		}

		// Action
		commentRepository.save(comment);
	}

	@Test
	void updateComment() throws Exception {
		commentService.createComment(getCommentRequest());

		Mono<Comment> savedCommentMono = commentRepository.findAll().next();
		Comment savedComment = savedCommentMono.block();
		Assertions.assertNotNull(savedComment, "Expected saved comment not to be null");

		CommentRequest updatedCommentRequest = CommentRequest.builder()
				.postId(savedComment.getPostId())
				.content("Updated Comment Content")
				.authorId(savedComment.getAuthorId())
				.build();

		mockMvc.perform(MockMvcRequestBuilders
						.put("/api/comments/" + savedComment.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedCommentRequest)))
				.andExpect(MockMvcResultMatchers.status().isNoContent());
	}

	@Test
	void deleteComment() throws Exception {
		commentService.createComment(getCommentRequest());

		Mono<Comment> savedCommentMono = commentRepository.findAll().next();
		Comment savedComment = savedCommentMono.block();
		Assertions.assertNotNull(savedComment, "Expected saved comment not to be null");

		mockMvc.perform(MockMvcRequestBuilders
						.delete("/api/comments/" + savedComment.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
}