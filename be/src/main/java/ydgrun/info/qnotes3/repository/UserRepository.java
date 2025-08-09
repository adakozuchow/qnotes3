package ydgrun.info.qnotes3.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ydgrun.info.qnotes3.domain.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}