package ca.gbc.friendservice.service;

import reactor.core.publisher.Mono;

public interface FriendshipService {
    Mono<Void> createFriendship(String userId, String friendId);

    Mono<Void> acceptFriendship(String userId, String friendId);

    Mono<Void> rejectFriendship(String userId, String friendId);

    Mono<Void> deleteFriendship(String userId, String friendId);

    Mono<String> getFriendshipStatus(String userId, String friendId);
}
