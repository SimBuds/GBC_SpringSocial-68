package ca.gbc.friendservice.controller;

import ca.gbc.friendservice.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/friendship")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;
}
