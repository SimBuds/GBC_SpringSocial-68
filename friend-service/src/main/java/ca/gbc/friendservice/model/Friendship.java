package ca.gbc.friendservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Friendship {
    private String id;
    private String userId;
    private String friendId;
    private FriendshipStatus status;
}