package ydgrun.info.qnotes3.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ydgrun.info.qnotes3.domain.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}