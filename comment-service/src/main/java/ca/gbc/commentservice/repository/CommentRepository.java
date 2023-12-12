package ca.gbc.commentservice.repository;

import ca.gbc.commentservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import reactor.core.publisher.Flux;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Flux<Comment> findByPostId(String postId);
}