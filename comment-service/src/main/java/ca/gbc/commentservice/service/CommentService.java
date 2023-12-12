package ca.gbc.commentservice.service;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentService {
    Mono<CommentResponse> createComment(CommentRequest commentRequest);
    Flux<CommentResponse> getAllComments();
    Mono<CommentResponse> updateComment(Long commentId, CommentRequest commentRequest);
    Mono<Void> deleteComment(Long commentId);
    Mono<CommentResponse> getCommentById(Long commentId);

}

