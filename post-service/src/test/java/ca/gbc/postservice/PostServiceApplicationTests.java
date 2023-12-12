package ca.gbc.postservice;

import ca.gbc.postservice.dto.PostRequest;
import ca.gbc.postservice.dto.PostResponse;
import ca.gbc.postservice.model.Post;
import ca.gbc.postservice.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class PostServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	PostRequest getPostRequest() {
		return PostRequest.builder()
				.title("Sample Title")
				.content("Sample Content")
				.authorId("12345")
				.build();
	}

	private List<Post> getPostList() {
		List<Post> postList = new ArrayList<>();
		UUID uuid = UUID.randomUUID();

		Post post = Post.builder()
				.id(uuid.toString())
				.title("Sample Title")
				.content("Sample Content")
				.authorId("12345")
				.build();

		postList.add(post);
		return postList;
	}

	private String convertObjectToJson(List<PostResponse> postList) throws Exception {
		return objectMapper.writeValueAsString(postList);
	}

	private List<PostResponse> convertJsonToObject(String jsonString) throws Exception {
		return objectMapper.readValue(jsonString, new TypeReference<List<PostResponse>>() {
		});
	}
	void createPost() throws Exception {
		// Action
		PostRequest postRequest = getPostRequest();
		String postRequestString = objectMapper.writeValueAsString(postRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(postRequestString))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		Assertions.assertTrue(postRepository.findAll().size() > 0);

		Query query = new Query();
		query.addCriteria(Criteria.where("title").is("Sample Title"));

		List<Post> posts = mongoTemplate.find(query, Post.class);
		Assertions.assertTrue(posts.size() > 0);
	}
	void getAllPosts() throws Exception {
		// Cleanup: Delete all posts before test
		postRepository.deleteAll();

		// Setup: Save some mock posts
		postRepository.saveAll(getPostList());

		// Action
		String response = mockMvc.perform(MockMvcRequestBuilders
						.get("/api/posts")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn().getResponse().getContentAsString();

		// Continue with assertion
		List<PostResponse> postList = convertJsonToObject(response);
		assertEquals(1, postList.size());
		assertEquals("Sample Title", postList.get(0).getTitle());
		assertEquals("Sample Content", postList.get(0).getContent());
		assertEquals("12345", postList.get(0).getAuthorId());
	}
	private PostResponse mapToDto(Post post) {
		return PostResponse.builder()
				.id(post.getId())
				.title(post.getTitle())
				.content(post.getContent())
				.authorId(post.getAuthorId())
				.build();
	}
	void updatePost() throws Exception {
		// Prepare saved post
		Post savedPost = postRepository.save(getPostList().get(0));

		// Prepare updated post and postRequest
		savedPost.setTitle("Updated Title");
		PostRequest postRequest = PostRequest.builder()
				.title(savedPost.getTitle())
				.content(savedPost.getContent())
				.authorId(savedPost.getAuthorId())
				.build();
		String postRequestString = objectMapper.writeValueAsString(postRequest);

		// Action
		mockMvc.perform(MockMvcRequestBuilders
						.put("/api/posts/" + savedPost.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(postRequestString))
				.andExpect(MockMvcResultMatchers.status().isNoContent());

		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(savedPost.getId()));
		Post updatedPost = mongoTemplate.findOne(query, Post.class);

		assertEquals("Updated Title", updatedPost.getTitle());
	}
	void deletePost() throws Exception {
		// Prepare saved post
		Post savedPost = postRepository.save(getPostList().get(0));

		// Action
		mockMvc.perform(MockMvcRequestBuilders
						.delete("/api/posts/" + savedPost.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNoContent());

		// Assert post is deleted
		Assertions.assertFalse(postRepository.existsById(savedPost.getId()));
	}

}