package ca.gbc.friendservice.service;

import reactor.core.publisher.Mono;

public interface FriendshipService {
    Mono<Void> createFriendship(String userId, String friendId);

    void acceptFriendship(String userId, String friendId);

    void rejectFriendship(String userId, String friendId);

    void deleteFriendship(String userId, String friendId);

    String getFriendshipStatus(String userId, String friendId);
}
