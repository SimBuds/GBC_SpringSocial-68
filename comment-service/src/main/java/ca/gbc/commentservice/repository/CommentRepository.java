package ca.gbc.commentservice.repository;

import ca.gbc.commentservice.model.Comment;
import org.springframework.data.mongodb.repository.JpaRepository;
import org.springframework.data.mongodb.repository.DeleteQuery;

public interface CommentRepository extends JpaRepository<Comment, String> {
    @DeleteQuery
    void deleteById(String id);

    Comment getById(String id);
}
