package ydgrun.info.qnotes3.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ydgrun.info.qnotes3.domain.Note;
import ydgrun.info.qnotes3.domain.User;
import ydgrun.info.qnotes3.repository.NoteRepository;
import ydgrun.info.qnotes3.repository.UserRepository;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Configuration
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final Random random = new Random();

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, NoteRepository noteRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            logger.info("Starting data initialization...");

            // Clear existing data
            noteRepository.deleteAll();
            userRepository.deleteAll();
            logger.info("Cleared existing data");

            // Create test user
            User user = new User();
            user.setUsername("a@a.pl");
            user.setPassword(passwordEncoder.encode("dupadupa"));
            userRepository.save(user);
            logger.info("Created test user: {}", user.getUsername());

            // Create notes with different dates and random priorities
            List<Note> notes = Arrays.asList(
                createNote(user.getUsername(), "Very old note 1", "Content 1", OffsetDateTime.now().minusDays(12)),
                createNote(user.getUsername(), "Very old note 2", "Content 2", OffsetDateTime.now().minusDays(11)),
                createNote(user.getUsername(), "Old note 1", "Content 3", OffsetDateTime.now().minusDays(4)),
                createNote(user.getUsername(), "Old note 2", "Content 4", OffsetDateTime.now().minusDays(3)),
                createNote(user.getUsername(), "Recent note 1", "Content 5", OffsetDateTime.now().minusHours(12)),
                createNote(user.getUsername(), "Recent note 2", "Content 6", OffsetDateTime.now().minusHours(8)),
                createNote(user.getUsername(), "Recent note 3", "Content 7", OffsetDateTime.now().minusHours(6)),
                createNote(user.getUsername(), "Today's note 1", "Content 8", OffsetDateTime.now().minusHours(4)),
                createNote(user.getUsername(), "Today's note 2", "Content 9", OffsetDateTime.now().minusHours(2)),
                createNote(user.getUsername(), "Latest note", "Content 10", OffsetDateTime.now())
            );

            noteRepository.saveAll(notes);
            logger.info("Created {} test notes", notes.size());
            
            // Verify data
            long userCount = userRepository.count();
            long noteCount = noteRepository.count();
            logger.info("Data initialization completed. Users: {}, Notes: {}", userCount, noteCount);
        };
    }

    private Note createNote(String userId, String title, String content, OffsetDateTime createdAt) {
        Note note = new Note();
        note.setId(UUID.randomUUID());
        note.setUserId(userId);
        note.setTitle(title);
        note.setContent(content);
        note.setPriority(getRandomPriority());
        note.setCreatedAt(createdAt);
        note.setUpdatedAt(createdAt);
        return note;
    }

    private Note.Priority getRandomPriority() {
        Note.Priority[] priorities = Note.Priority.values();
        return priorities[random.nextInt(priorities.length)];
    }
}