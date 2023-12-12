package ca.gbc.postservice.config;

import ca.gbc.postservice.dto.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserServiceClient {
    private final WebClient webClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceClient.class);

    public UserServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://user-service:8083").build();
    }

    public Mono<UserResponse> getUserById(Long userId) {
        return webClient.get()
                .uri("/api/users/" + userId)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .doOnError(error -> LOGGER.error("Error while calling user-service: {}", error.getMessage()));
    }
}

