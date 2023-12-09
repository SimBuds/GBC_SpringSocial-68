package ca.gbc.friendservice.service;

public interface FriendshipService {
    void createFriendship(String userId, String friendId);

    void acceptFriendship(String userId, String friendId);

    void rejectFriendship(String userId, String friendId);

    void deleteFriendship(String userId, String friendId);

    String getFriendshipStatus(String userId, String friendId);
}
