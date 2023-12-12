package ca.gbc.commentservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    private Long id;

    @Column(name = "post_id")
    private String postId;

    @Column(name = "content")
    private String content;

    @Column(name = "author_id")
    private String authorId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}