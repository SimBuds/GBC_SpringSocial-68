package ca.gbc.friendservice.controller;

import ca.gbc.friendservice.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friendship")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/request")
    public ResponseEntity<?> createFriendshipRequest(@RequestParam String userId, @RequestParam String friendId) {
        friendshipService.createFriendship(userId, friendId);
        return ResponseEntity.ok("Friendship request sent.");
    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptFriendship(@RequestParam String userId, @RequestParam String friendId) {
        friendshipService.acceptFriendship(userId, friendId);
        return ResponseEntity.ok("Friendship accepted.");
    }

    @PostMapping("/reject")
    public ResponseEntity<?> rejectFriendship(@RequestParam String userId, @RequestParam String friendId) {
        friendshipService.rejectFriendship(userId, friendId);
        return ResponseEntity.ok("Friendship rejected.");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> deleteFriendship(@RequestParam String userId, @RequestParam String friendId) {
        friendshipService.deleteFriendship(userId, friendId);
        return ResponseEntity.ok("Friendship removed.");
    }

    @GetMapping("/status")
    public ResponseEntity<?> getFriendshipStatus(@RequestParam String userId, @RequestParam String friendId) {
        String status = friendshipService.getFriendshipStatus(userId, friendId);
        return ResponseEntity.ok("Friendship status: " + status);
    }
}