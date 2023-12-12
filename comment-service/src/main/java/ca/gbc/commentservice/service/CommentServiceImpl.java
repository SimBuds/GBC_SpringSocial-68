package ca.gbc.commentservice.service;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.model.Comment;
import ca.gbc.commentservice.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserServiceClient userServiceClient;

    @Override
    public Mono<CommentResponse> createComment(CommentRequest commentRequest) {
        Comment newComment = new Comment(); // Rename the variable to avoid conflict
        LocalDateTime now = LocalDateTime.now();
        newComment.setPostId(commentRequest.getPostId());
        newComment.setContent(commentRequest.getContent());
        newComment.setAuthorId(commentRequest.getAuthorId());
        newComment.setCreatedAt(now);
        newComment.setUpdatedAt(now);

        return commentRepository.save(newComment)
                .flatMap(savedComment -> this.mapToDtoWithUserDetails(savedComment)); // Avoid variable redefinition
    }
    @Override
    public Mono<CommentResponse> updateComment(Long commentId, CommentRequest commentRequest) {
        return commentRepository.findById(commentId)
                .map(comment -> {
                    comment.setContent(commentRequest.getContent());
                    comment.setUpdatedAt(LocalDateTime.now());
                    return comment;
                })
                .flatMap(commentRepository::save)
                .flatMap(this::mapToDtoWithUserDetails);
    }

    @Override
    public Mono<Void> deleteComment(Long commentId) {
        return commentRepository.deleteById(commentId);
    }

    @Override
    public Mono<CommentResponse> getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .flatMap(this::mapToDtoWithUserDetails);
    }

    @Override
    public Flux<CommentResponse> getAllComments() {
        return commentRepository.findAll()
                .flatMap(this::mapToDtoWithUserDetails);
    }

    private Mono<CommentResponse> mapToDtoWithUserDetails(Comment comment) {
        return userServiceClient.getUserDetails(comment.getAuthorId())
                .map(user -> CommentResponse.builder()
                        .id(comment.getId())
                        .postId(comment.getPostId())
                        .content(comment.getContent())
                        .authorId(comment.getAuthorId())
                        .createdAt(comment.getCreatedAt())
                        .updatedAt(comment.getUpdatedAt())
                        .build());
    }
}