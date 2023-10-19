package ca.gbc.postservice.repository;

import ca.gbc.postservice.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.DeleteQuery;

public interface PostRepository extends MongoRepository<Post, String> {
    @DeleteQuery
    void deleteById(String id);
}
