package ca.gbc.commentservice.service;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;

import java.util.List;

public interface CommentService {
    void createComment(CommentRequest commentRequest);

    List<CommentResponse> getAllComments();

    String updateComment(Long commentId, CommentRequest commentRequest);

    void deleteComment(Long commentId);

    String getCommentById(Long commentId);
}
