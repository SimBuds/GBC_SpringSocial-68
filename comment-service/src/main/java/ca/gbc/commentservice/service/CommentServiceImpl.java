package ca.gbc.commentservice.service;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.model.Comment;
import ca.gbc.commentservice.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void createComment(CommentRequest commentRequest) {
        log.info("Creating comment: {}", commentRequest.getContent());

        Comment comment = Comment.builder()
                .postId(commentRequest.getPostId())
                .content(commentRequest.getContent())
                .authorId(commentRequest.getAuthorId())
                .build();
        commentRepository.save(comment);

        log.info("Comment created successfully: {}", commentRequest.getId());
    }

    @Override
    public String updateComment(String commentId, CommentRequest commentRequest) {
        log.info("Updating comment: {}", commentId);

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(commentId));

        Comment comment = mongoTemplate.findOne(query, Comment.class);
        if (comment != null) {
            comment.setContent(commentRequest.getContent());
            comment.setAuthorId(commentRequest.getAuthorId());

            log.info("Comment updated successfully: {}", commentId);
            return commentRepository.save(comment).getId();
        }

        return commentId;
    }

    @Override
    public void deleteComment(String commentId) {
        log.info("Deleting comment {}", commentId);
        commentRepository.deleteById(commentId);
        log.info("Comment deleted successfully: {}", commentId);
    }

    private CommentResponse mapToDto(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .content(comment.getContent())
                .authorId(comment.getAuthorId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
