package ca.gbc.commentservice.repository;

import ca.gbc.commentservice.model.Comment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.Optional;

public interface CommentRepository extends ReactiveCrudRepository<Comment, Long> {
}