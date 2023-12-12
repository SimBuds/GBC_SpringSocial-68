package ca.gbc.postservice.config;

import ca.gbc.postservice.dto.CommentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class CommentServiceClient {
    private final WebClient webClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceClient.class);

    public CommentServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://comment-service:8082").build();
    }

    public Flux<CommentResponse> getCommentsByPostId(String postId) {
        return webClient.get()
                .uri("/api/comments/post/" + postId)
                .retrieve()
                .bodyToFlux(CommentResponse.class)
                .doOnError(error -> LOGGER.error("Error while calling comment-service: {}", error.getMessage()));
    }
}
