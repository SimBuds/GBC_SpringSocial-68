package ca.gbc.commentservice.service;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.model.Comment;
import ca.gbc.commentservice.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public void createComment(CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setPostId(commentRequest.getPostId());
        comment.setContent(commentRequest.getContent());
        comment.setAuthorId(commentRequest.getAuthorId());
        commentRepository.save(comment);
    }

    @Override
    public void updateComment(Long commentId, CommentRequest commentRequest) {
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("Comment not found");
        }
        Comment comment = commentRepository.getById(commentId);
        comment.setPostId(commentRequest.getPostId());
        comment.setContent(commentRequest.getContent());
        comment.setAuthorId(commentRequest.getAuthorId());
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("Comment not found");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentResponse> getAllComments() {
        return commentRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }
}