package ca.gbc.friendservice.service;

import ca.gbc.friendservice.model.Friendship;
import ca.gbc.friendservice.model.FriendshipStatus;
import ca.gbc.friendservice.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipRepository friendshipRepository;

    @Override
    public void createFriendship(String userId, String friendId) {
        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId(friendId);
        friendship.setStatus(FriendshipStatus.PENDING);
        friendshipRepository.save(friendship);
        log.info("Friendship request from {} to {}.", userId, friendId);
    }


    @Override
    public void acceptFriendship(String userId, String friendId) {
        Friendship friendship = friendshipRepository.findByUserIdAndFriendId(friendId, userId)
                .orElseThrow(() -> new RuntimeException("Friendship request not found"));
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(friendship);
        log.info("Friendship accepted between {} and {}.", userId, friendId);
    }


    @Override
    public void rejectFriendship(String userId, String friendId) {
        Friendship friendship = friendshipRepository.findByUserIdAndFriendId(friendId, userId)
                .orElseThrow(() -> new RuntimeException("Friendship request not found"));
        friendship.setStatus(FriendshipStatus.REJECTED);
        friendshipRepository.save(friendship);
        log.info("Friendship between {} and {} has been rejected.", userId, friendId);
    }

    @Override
    public void deleteFriendship(String userId, String friendId) {
        Friendship friendship = friendshipRepository.findByUserIdAndFriendId(userId, friendId)
                .orElseThrow(() -> new RuntimeException("Friendship not found"));
        friendshipRepository.delete(friendship);
        log.info("Friendship between {} and {} has been deleted.", userId, friendId);
    }

    @Override
    public String getFriendshipStatus(String userId, String friendId) {
        return friendshipRepository.findByUserIdAndFriendId(userId, friendId)
                .map(Friendship::getStatus)
                .map(Enum::name)
                .orElse("Not a friend");
    }
}