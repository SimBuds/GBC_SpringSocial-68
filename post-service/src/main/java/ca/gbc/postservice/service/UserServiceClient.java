package ca.gbc.postservice.service;

import ca.gbc.postservice.dto.UserRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserServiceClient {
    private final WebClient.Builder webClientBuilder;

    public UserServiceClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<UserRequest> getUserById(String userId) {
        return webClientBuilder.build()
                .get()
                .uri("http://user-service/users/" + userId)
                .retrieve()
                .bodyToMono(UserRequest.class);
    }
}