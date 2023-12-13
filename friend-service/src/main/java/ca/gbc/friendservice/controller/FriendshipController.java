package ca.gbc.friendservice.controller;

import ca.gbc.friendservice.dto.FriendshipRequest;
import ca.gbc.friendservice.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/friendship")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/request")
    public ResponseEntity<?> createFriendshipRequest(@RequestBody FriendshipRequest request) {
        friendshipService.createFriendship(request.getUserId(), request.getFriendId());
        return ResponseEntity.ok("Friendship request sent.");
    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptFriendship(@RequestBody FriendshipRequest request) {
        friendshipService.acceptFriendship(request.getUserId(), request.getFriendId());
        return ResponseEntity.ok("Friendship accepted.");
    }

    @PostMapping("/reject")
    public ResponseEntity<?> rejectFriendship(@RequestBody FriendshipRequest request) {
        friendshipService.rejectFriendship(request.getUserId(), request.getFriendId());
        return ResponseEntity.ok("Friendship rejected.");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> deleteFriendship(@RequestBody FriendshipRequest request) {
        friendshipService.deleteFriendship(request.getUserId(), request.getFriendId());
        return ResponseEntity.ok("Friendship removed.");
    }

    @GetMapping("/status")
    public ResponseEntity<?> getFriendshipStatus(@RequestParam String userId, @RequestParam String friendId) {
        Mono<String> status = friendshipService.getFriendshipStatus(userId, friendId);
        return ResponseEntity.ok("Friendship status: " + status);
    }
}