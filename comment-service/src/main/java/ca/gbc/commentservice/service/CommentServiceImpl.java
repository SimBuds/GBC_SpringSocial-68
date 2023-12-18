package ca.gbc.commentservice.service;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.dto.UserResponse;
import ca.gbc.commentservice.model.Comment;
import ca.gbc.commentservice.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final WebClient userServiceWebClient;

    @Override
    public Mono<CommentResponse> createComment(CommentRequest commentRequest) {
        return userServiceWebClient.get()
                .uri("/api/users/{id}", commentRequest.getAuthorId())
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse ->
                        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")))
                .bodyToMono(UserResponse.class)
                .flatMap(userResponse -> Mono
                        .fromCallable(() -> {
                            Comment newComment = new Comment();
                            LocalDateTime now = LocalDateTime.now();
                            newComment.setPostId(commentRequest.getPostId());
                            newComment.setContent(commentRequest.getContent());
                            newComment.setAuthorId(commentRequest.getAuthorId());
                            newComment.setCreatedAt(now);
                            newComment.setUpdatedAt(now);
                            // Save the comment and immediately map it to DTO
                            return mapToDto(commentRepository.save(newComment));
                        })
                        .subscribeOn(Schedulers.boundedElastic())
                );
    }

    @Override
    public Mono<CommentResponse> updateComment(Long commentId, CommentRequest commentRequest) {
        return Mono
                .fromCallable(() -> commentRepository.findById(commentId))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalComment -> {
                    if (optionalComment.isPresent()) {
                        Comment comment = optionalComment.get();
                        comment.setContent(commentRequest.getContent());
                        comment.setUpdatedAt(LocalDateTime.now());
                        return Mono.fromCallable(() -> commentRepository.save(comment))
                                .subscribeOn(Schedulers.boundedElastic());
                    } else {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
                    }
                })
                .map(this::mapToDto);
    }


    @Override
    public Mono<ResponseEntity<Void>> deleteComment(Long commentId) {
        return Mono
                .fromCallable(() -> commentRepository.findById(commentId))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalComment -> {
                    if (optionalComment.isPresent()) {
                        commentRepository.delete(optionalComment.get());
                        return Mono.just(ResponseEntity.ok().<Void>build());
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

    @Override
    public Mono<CommentResponse> getCommentById(Long commentId) {
        return Mono
                .fromCallable(() -> commentRepository.findById(commentId))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalComment -> optionalComment
                        .map(this::mapToDto)
                        .map(Mono::just)
                        .orElseGet(Mono::empty)
                );
    }

    @Override
    public Flux<CommentResponse> getAllComments() {
        return Flux.defer(() -> Flux.fromIterable(commentRepository.findAll()))
                .map(this::mapToDto)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<CommentResponse> getCommentsByPostId(String postId) {
        return Flux.defer(() -> {
                    List<Comment> comments = commentRepository.findByPostId(postId);
                    return Flux.fromIterable(comments);
                })
                .map(this::mapToDto)
                .subscribeOn(Schedulers.boundedElastic());
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