package ca.gbc.postservice.service;

import ca.gbc.postservice.dto.PostRequest;
import ca.gbc.postservice.dto.PostResponse;
import ca.gbc.postservice.model.Post;
import ca.gbc.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void createPost(PostRequest postRequest) {
        log.info("Creating post: {}", postRequest.getTitle());

        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .authorId(postRequest.getAuthorId())
                .build();
        postRepository.save(post);

        log.info("Post created successfully: {}", postRequest.getId());
    }

    @Override
    public String updatePost(String postId, PostRequest postRequest) {
        log.info("Updating post: {}", postId);

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(postId));

        Post post = mongoTemplate.findOne(query, Post.class);
        if (post != null) {
            post.setTitle(postRequest.getTitle());
            post.setContent(postRequest.getContent());
            post.setAuthorId(postRequest.getAuthorId());

            log.info("Post updated successfully: {}", postId);
            return postRepository.save(post).getId();
        }

        return postId;
    }

    @Override
    public void deletePost(String postId) {
        log.info("Deleting post: {}", postId);
        postRepository.deleteById(postId);
        log.info("Post deleted successfully: {}", postId);
    }

    private PostResponse mapToDto(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorId(post.getAuthorId())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

}
