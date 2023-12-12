package ca.gbc.commentservice;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.model.Comment;
import ca.gbc.commentservice.repository.CommentRepository;
import ca.gbc.commentservice.service.CommentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentServiceApplicationTests extends AbstractContainerBaseTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
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

	void getCommentById() throws Exception {
		Comment mockComment = new Comment();
		mockComment.setId(1L);
		mockComment.setPostId("somePostId");
		mockComment.setContent("Sample Comment");
		mockComment.setAuthorId("commentAuthor123");

		Mockito.when(commentRepository.findById(Mockito.anyLong()))
				.thenReturn(Mono.just(mockComment));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/{commentId}", 1L))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(containsString("Sample Comment")));
	}

	void deleteComment() throws Exception {
		Comment mockComment = new Comment();
		mockComment.setId(1L);
		mockComment.setPostId("somePostId");
		mockComment.setContent("Sample Comment");
		mockComment.setAuthorId("commentAuthor123");

		Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(Mono.just(mockComment));
		Mockito.when(commentRepository.deleteById(Mockito.anyLong())).thenReturn(Mono.empty());

		mockMvc.perform(MockMvcRequestBuilders
						.delete("/api/comments/" + mockComment.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	void createComments() throws Exception {
		CommentRequest commentRequest = getCommentRequest();
		String commentRequestString = objectMapper.writeValueAsString(commentRequest);

		Comment mockComment = new Comment();
		mockComment.setId(1L);
		mockComment.setPostId(commentRequest.getPostId());
		mockComment.setContent(commentRequest.getContent());
		mockComment.setAuthorId(commentRequest.getAuthorId());

		Mockito.when(commentRepository.save(Mockito.any(Comment.class)))
				.thenReturn(Mono.just(mockComment));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(commentRequestString))
				.andExpect(MockMvcResultMatchers.status().isOk());

		Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any(Comment.class));
	}

	void updateComment() throws Exception {
		Comment mockComment = new Comment();
		mockComment.setId(1L);
		mockComment.setPostId("somePostId");
		mockComment.setContent("Sample Comment");
		mockComment.setAuthorId("commentAuthor123");

		Mockito.when(commentRepository.findById(Mockito.anyLong()))
				.thenReturn(Mono.just(mockComment));

		Comment updatedComment = new Comment();
		updatedComment.setId(1L);
		updatedComment.setPostId("somePostId");
		updatedComment.setContent("Updated Comment Content");
		updatedComment.setAuthorId("commentAuthor123");

		Mockito.when(commentRepository.save(Mockito.any(Comment.class)))
				.thenReturn(Mono.just(updatedComment));

		CommentRequest updatedCommentRequest = CommentRequest.builder()
				.postId("somePostId")
				.content("Updated Comment Content")
				.authorId("commentAuthor123")
				.build();

		mockMvc.perform(MockMvcRequestBuilders.put("/api/comments/{commentId}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedCommentRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk());

		Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any(Comment.class));
	}
}