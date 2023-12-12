package ca.gbc.commentservice.service;

import ca.gbc.commentservice.dto.UserRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserServiceClient {
    private final WebClient webClient;

    public UserServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://user-service").build();
    }

    public Mono<UserRequest> getUserDetails(String userId) {
        return webClient.get()
                .uri("/api/users/" + userId)
                .retrieve()
                .bodyToMono(UserRequest.class);
    }
}