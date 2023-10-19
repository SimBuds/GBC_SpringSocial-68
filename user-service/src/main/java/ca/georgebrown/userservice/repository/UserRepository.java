package ca.georgebrown.userservice.repository;

import ca.georgebrown.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByUsername(String username);
    User getUserByEmail(String email);
}
