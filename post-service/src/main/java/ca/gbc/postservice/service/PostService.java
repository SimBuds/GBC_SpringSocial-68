package ca.gbc.postservice.service;

import ca.gbc.postservice.dto.PostRequest;
import ca.gbc.postservice.dto.PostResponse;
import reactor.core.publisher.Flux;

public interface PostService {
    void createPost(PostRequest postRequest);

    Flux<PostResponse> getAllPosts();

    String updatePost(String postId, PostRequest postRequest);

    void deletePost(String postId);
}