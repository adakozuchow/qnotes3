package ydgrun.info.qnotes3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ydgrun.info.qnotes3.domain.Note;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteRepository extends MongoRepository<Note, UUID> {
    
    @Query("{ 'userId': ?0, 'deletedAt': null }")
    Page<Note> findAllByUserIdAndNotDeleted(String userId, Pageable pageable);

    @Query("{ 'userId': ?0, 'priority': ?1, 'deletedAt': null }")
    Page<Note> findAllByUserIdAndPriorityAndNotDeleted(String userId, Note.Priority priority, Pageable pageable);

    @Query("{ 'userId': ?0, 'createdAt': { $gte: ?1 }, 'deletedAt': null }")
    Page<Note> findAllByUserIdAndCreatedAtAfterAndNotDeleted(String userId, OffsetDateTime after, Pageable pageable);

    @Query("{ 'userId': ?0, 'id': ?1, 'deletedAt': null }")
    Optional<Note> findByUserIdAndIdAndNotDeleted(String userId, UUID id);

    @Query("{ 'userId': ?0, 'updatedAt': { $lt: ?1 }, 'priority': { $ne: 'DONE' }, 'deletedAt': null }")
    List<Note> findStaleNotes(String userId, OffsetDateTime staleThreshold);

    @Query("{ 'userId': ?0, 'priority': 'NOW', 'deletedAt': null }")
    List<Note> findHighPriorityNotes(String userId);

    @Query("{ 'userId': ?0, 'priority': 'DONE', 'deletedAt': null }")
    List<Note> findCompletedNotes(String userId);

    @Query("{ 'userId': ?0, 'deletedAt': { $ne: null } }")
    List<Note> findDeletedNotes(String userId);
}