package ca.gbc.postservice.service;

import ca.gbc.postservice.dto.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserServiceClient {
    private final WebClient.Builder webClientBuilder;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceClient.class);

    public UserServiceClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<UserRequest> getUserById(String userId) {
        return webClientBuilder.build()
                .get()
                .uri(uriBuilder -> uriBuilder.path("http://user-service/api/users/{id}").build(userId))
                .retrieve()
                .bodyToMono(UserRequest.class)
                .doOnSuccess(response -> LOGGER.info("Successfully retrieved user with ID: " + userId))
                .doOnError(error -> LOGGER.error("Error retrieving user with ID: " + userId, error));
    }
}
