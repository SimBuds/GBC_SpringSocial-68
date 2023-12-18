package ca.gbc.postservice.config;

import ca.gbc.postservice.dto.UserResponse;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceClient {
    private final WebClient webClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceClient.class);
    private final Cache<String, Mono<UserResponse>> cache;

    public UserServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://user-service:8083").build();
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build();
    }

    public Mono<UserResponse> getUserById(String userId) {
        try {
            return cache.get(userId, () -> doGetUserById(userId));
        } catch (ExecutionException e) {
            throw new RuntimeException("Error while getting user", e);
        }
    }

    private Mono<UserResponse> doGetUserById(String userId) {
        return webClient.get()
                .uri("/api/users/" + userId)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .doOnError(error -> LOGGER.error("Error while calling user-service: {}", error.getMessage()))
                .retry(3)
                .timeout(Duration.ofSeconds(5));
    }
}