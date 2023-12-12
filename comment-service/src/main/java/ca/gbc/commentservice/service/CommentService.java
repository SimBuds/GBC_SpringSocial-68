package ca.gbc.commentservice.service;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface CommentService {
    Mono<CommentResponse> createComment(CommentRequest commentRequest);
    Flux<CommentResponse> getAllComments();
    Mono<CommentResponse> updateComment(Long commentId, CommentRequest commentRequest);
    Mono<ResponseEntity<Void>> deleteComment(Long commentId);
    Mono<CommentResponse> getCommentById(Long commentId);

}

