package ydgrun.info.qnotes3.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ydgrun.info.qnotes3.domain.Note;
import ydgrun.info.qnotes3.repository.NoteRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private static final int PAGE_SIZE = 10;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note createNote(String userId, String title, String content, Note.Priority priority) {
        Note note = new Note();
        note.setId(UUID.randomUUID());
        note.setUserId(userId);
        note.setTitle(title);
        note.setContent(content);
        note.setPriority(priority);
        note.setCreatedAt(OffsetDateTime.now());
        note.setUpdatedAt(OffsetDateTime.now());
        return noteRepository.save(note);
    }

    public Note updateNote(String userId, UUID noteId, String title, String content, Note.Priority priority) {
        Note note = noteRepository.findByUserIdAndIdAndNotDeleted(userId, noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));

        note.setTitle(title);
        note.setContent(content);
        note.setPriority(priority);
        note.setUpdatedAt(OffsetDateTime.now());
        return noteRepository.save(note);
    }

    public void deleteNote(String userId, UUID noteId) {
        Note note = noteRepository.findByUserIdAndIdAndNotDeleted(userId, noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));

        note.setDeletedAt(OffsetDateTime.now());
        noteRepository.save(note);
    }

    public Note getNote(String userId, UUID noteId) {
        return noteRepository.findByUserIdAndIdAndNotDeleted(userId, noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));
    }

    public Page<Note> getNotes(String userId, Integer page, Note.Priority priority, String dateRange) {
        PageRequest pageRequest = PageRequest.of(
            page != null ? page : 0,
            PAGE_SIZE,
            Sort.by(Sort.Direction.DESC, "createdAt")
        );

        if (priority != null) {
            return noteRepository.findAllByUserIdAndPriorityAndNotDeleted(userId, priority, pageRequest);
        }

        if (dateRange != null) {
            OffsetDateTime after = switch (dateRange) {
                case "TODAY" -> OffsetDateTime.now().withHour(0).withMinute(0).withSecond(0);
                case "PAST_SEVEN_DAYS" -> OffsetDateTime.now().minusDays(7);
                default -> null;
            };
            
            if (after != null) {
                return noteRepository.findAllByUserIdAndCreatedAtAfterAndNotDeleted(userId, after, pageRequest);
            }
        }

        return noteRepository.findAllByUserIdAndNotDeleted(userId, pageRequest);
    }

    public int countStaleNotes(String userId) {
        OffsetDateTime staleThreshold = OffsetDateTime.now().minusDays(2);
        return noteRepository.findStaleNotes(userId, staleThreshold).size();
    }

    public int countHighPriorityNotes(String userId) {
        return noteRepository.findHighPriorityNotes(userId).size();
    }

    public double calculateAverageCompletionTime(String userId) {
        List<Note> completedNotes = noteRepository.findCompletedNotes(userId);
        if (completedNotes.isEmpty()) {
            return 0.0;
        }

        double totalHours = completedNotes.stream()
            .mapToDouble(note -> {
                OffsetDateTime start = note.getCreatedAt();
                OffsetDateTime end = note.getUpdatedAt();
                return (end.toEpochSecond() - start.toEpochSecond()) / 3600.0;
            })
            .sum();

        return totalHours / completedNotes.size();
    }

    public double calculateAverageDeletionTime(String userId) {
        List<Note> deletedNotes = noteRepository.findDeletedNotes(userId);
        if (deletedNotes.isEmpty()) {
            return 0.0;
        }

        double totalHours = deletedNotes.stream()
            .mapToDouble(note -> {
                OffsetDateTime start = note.getCreatedAt();
                OffsetDateTime end = note.getDeletedAt();
                return (end.toEpochSecond() - start.toEpochSecond()) / 3600.0;
            })
            .sum();

        return totalHours / deletedNotes.size();
    }
}