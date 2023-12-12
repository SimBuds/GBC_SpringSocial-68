package ca.gbc.postservice.service;

import ca.gbc.postservice.dto.UserRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserServiceClient {
    private final WebClient webClient;

    public UserServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://user-service").build();
    }

    public Mono<UserRequest> getUserById(Long userId) {
        return webClient.get()
                .uri("/api/users/" + userId)
                .retrieve()
                .bodyToMono(UserRequest.class);
    }
}

