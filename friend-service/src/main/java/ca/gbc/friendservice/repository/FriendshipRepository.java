package ca.gbc.friendservice.repository;

import ca.gbc.friendservice.model.Friendship;
import ca.gbc.friendservice.model.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByUserIdAndFriendId(String userId, String friendId);
}