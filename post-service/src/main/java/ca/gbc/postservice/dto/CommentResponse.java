package ca.gbc.postservice.dto;

import java.time.LocalDateTime;

public class CommentResponse {
    private String postId;
    private String content;
    private String authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
