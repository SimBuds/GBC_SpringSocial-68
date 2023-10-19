package ca.gbc.commentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {
    private String postId;
    private String content;
    private String authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
