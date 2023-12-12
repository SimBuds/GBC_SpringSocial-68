package ca.gbc.commentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    private Long id;

    @Column("post_id")
    private String postId;

    @Column("content")
    private String content;

    @Column("author_id")
    private String authorId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
