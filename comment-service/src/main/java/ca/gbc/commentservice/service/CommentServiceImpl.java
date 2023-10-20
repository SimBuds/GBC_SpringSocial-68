package ca.gbc.commentservice.service;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.model.Comment;
import ca.gbc.commentservice.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

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
    public String updateComment(String commentId, CommentRequest commentRequest) {
        Comment comment = commentRepository.getById(commentId);
        comment.setPostId(commentRequest.getPostId());
        comment.setContent(commentRequest.getContent());
        comment.setAuthorId(commentRequest.getAuthorId());
        commentRepository.save(comment);
        return comment.getId().toString();
    }

    @Override
    public void deleteComment(String commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentResponse> getAllComments() {
        return commentRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public String getCommentById(String commentId) {
        Comment comment = commentRepository.getById(commentId);
        return comment.getAuthorId();
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