package ca.gbc.postservice.service;

import ca.gbc.postservice.config.CommentServiceClient;
import ca.gbc.postservice.config.UserServiceClient;
import ca.gbc.postservice.dto.CommentResponse;
import ca.gbc.postservice.dto.PostRequest;
import ca.gbc.postservice.dto.PostResponse;
import ca.gbc.postservice.dto.UserResponse;
import ca.gbc.postservice.model.Post;
import ca.gbc.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MongoTemplate mongoTemplate;
    private final UserServiceClient userServiceClient;
    private final CommentServiceClient commentServiceClient;

    @Override
    public void createPost(PostRequest postRequest) {
        log.info("Creating post: {}", postRequest.getTitle());

        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .authorId(postRequest.getAuthorId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        postRepository.save(post);

        log.info("Post created successfully: {}", postRequest.getTitle());
    }

    @Override
    public String updatePost(String postId, PostRequest postRequest) {
        LocalDateTime now = LocalDateTime.now();
        log.info("Updating post: {}", postId);

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(postId));

        Post post = mongoTemplate.findOne(query, Post.class);
        if (post != null) {
            post.setTitle(postRequest.getTitle());
            post.setContent(postRequest.getContent());
            post.setAuthorId(postRequest.getAuthorId());
            post.setUpdatedAt(now);

            postRepository.save(post);
            log.info("Post updated successfully: {}", postId);
            return post.getId();
        } else {
            log.error("No post found with ID: {}", postId);
        }

        return null;
    }

    @Override
    public void deletePost(String postId) {
        log.info("Deleting post: {}", postId);
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
            log.info("Post deleted successfully: {}", postId);
        } else {
            log.error("No post found with ID: {}", postId);
        }
    }

    @Override
    public Flux<PostResponse> getAllPosts() {
        return Flux.fromIterable(postRepository.findAll())
                .flatMap(post -> Mono.zip(
                        Mono.just(post),
                        userServiceClient.getUserById(post.getAuthorId()).defaultIfEmpty(new UserResponse()),
                        commentServiceClient.getCommentsByPostId(post.getId()).collectList()
                ))
                .map(tuple -> {
                    Post post = tuple.getT1();
                    UserResponse userResponse = tuple.getT2();
                    List<CommentResponse> comments = tuple.getT3();
                    return mapToDto(post, userResponse, comments);
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    private PostResponse mapToDto(Post post, UserResponse userResponse, List<CommentResponse> comments) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorId(userResponse.getFullName() != null ? userResponse.getFullName() : "Unknown Author")
                .comments(comments != null ? comments : Collections.emptyList())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}