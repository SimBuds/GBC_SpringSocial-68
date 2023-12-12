package ca.gbc.commentservice.controller;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public Mono<ResponseEntity<CommentResponse>> createComment(@RequestBody CommentRequest commentRequest) {
        return commentService.createComment(commentRequest)
                .map(response -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(response));
    }

    @GetMapping
    public Flux<CommentResponse> getAllComments() {
        return commentService.getAllComments();
    }

    @PutMapping("/{commentId}")
    public Mono<ResponseEntity<CommentResponse>> updateComment(@PathVariable("commentId") Long commentId,
                                                               @RequestBody CommentRequest commentRequest) {
        return commentService.updateComment(commentId, commentRequest)
                .map(updatedComment -> ResponseEntity
                        .ok()
                        .location(URI.create("/api/comments/" + updatedComment.getId()))
                        .body(updatedComment))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{commentId}")
    public Mono<ResponseEntity<CommentResponse>> getCommentById(@PathVariable("commentId") Long commentId) {
        return commentService.getCommentById(commentId)
                .map(comment -> ResponseEntity.ok().body(comment))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{commentId}")
    public Mono<ResponseEntity<Void>> deleteComment(@PathVariable("commentId") Long commentId) {
        return commentService.deleteComment(commentId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
