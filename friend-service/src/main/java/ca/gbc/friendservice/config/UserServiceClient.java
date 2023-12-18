package ca.gbc.friendservice.config;

import ca.gbc.friendservice.dto.UserRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserServiceClient {
    private final WebClient webClient;

    public UserServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://user-service:8083").build();
    }

    @Cacheable("users")
    public Mono<UserRequest> getUserDetails(String userId) {
        return webClient.get()
                .uri("/api/users" + userId)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error(new RuntimeException("Failed to fetch user details"))
                )
                .bodyToMono(UserRequest.class);
    }
}