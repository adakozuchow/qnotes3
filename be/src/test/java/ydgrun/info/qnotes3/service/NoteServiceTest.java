package ydgrun.info.qnotes3.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ydgrun.info.qnotes3.domain.Note;
import ydgrun.info.qnotes3.repository.NoteRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    private NoteService noteService;

    private static final String USER_ID = "test-user";
    private static final UUID NOTE_ID = UUID.randomUUID();
    private static final String TITLE = "Test Title";
    private static final String CONTENT = "Test Content";
    private static final Note.Priority PRIORITY = Note.Priority.NOW;

    @BeforeEach
    void setUp() {
        noteService = new NoteService(noteRepository);
    }

    private Note createSampleNote() {
        Note note = new Note();
        note.setId(NOTE_ID);
        note.setUserId(USER_ID);
        note.setTitle(TITLE);
        note.setContent(CONTENT);
        note.setPriority(PRIORITY);
        note.setCreatedAt(OffsetDateTime.now());
        note.setUpdatedAt(OffsetDateTime.now());
        return note;
    }

    @Test
    void createNote_ShouldCreateAndReturnNewNote() {
        // Arrange
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Note createdNote = noteService.createNote(USER_ID, TITLE, CONTENT, PRIORITY);

        // Assert
        assertNotNull(createdNote);
        assertNotNull(createdNote.getId());
        assertEquals(USER_ID, createdNote.getUserId());
        assertEquals(TITLE, createdNote.getTitle());
        assertEquals(CONTENT, createdNote.getContent());
        assertEquals(PRIORITY, createdNote.getPriority());
        assertNotNull(createdNote.getCreatedAt());
        assertNotNull(createdNote.getUpdatedAt());
        assertNull(createdNote.getDeletedAt());

        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void updateNote_ShouldUpdateAndReturnNote() {
        // Arrange
        Note existingNote = createSampleNote();
        String newTitle = "Updated Title";
        String newContent = "Updated Content";
        Note.Priority newPriority = Note.Priority.LATER;

        when(noteRepository.findByUserIdAndIdAndNotDeleted(USER_ID, NOTE_ID))
            .thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Note updatedNote = noteService.updateNote(USER_ID, NOTE_ID, newTitle, newContent, newPriority);

        // Assert
        assertNotNull(updatedNote);
        assertEquals(NOTE_ID, updatedNote.getId());
        assertEquals(USER_ID, updatedNote.getUserId());
        assertEquals(newTitle, updatedNote.getTitle());
        assertEquals(newContent, updatedNote.getContent());
        assertEquals(newPriority, updatedNote.getPriority());
        assertNotNull(updatedNote.getUpdatedAt());
        assertNull(updatedNote.getDeletedAt());

        verify(noteRepository).findByUserIdAndIdAndNotDeleted(USER_ID, NOTE_ID);
        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void updateNote_ShouldThrowException_WhenNoteNotFound() {
        // Arrange
        when(noteRepository.findByUserIdAndIdAndNotDeleted(USER_ID, NOTE_ID))
            .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> noteService.updateNote(USER_ID, NOTE_ID, "title", "content", Note.Priority.NOW));
        assertEquals("Note not found", exception.getMessage());

        verify(noteRepository).findByUserIdAndIdAndNotDeleted(USER_ID, NOTE_ID);
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    void deleteNote_ShouldMarkNoteAsDeleted() {
        // Arrange
        Note existingNote = createSampleNote();
        when(noteRepository.findByUserIdAndIdAndNotDeleted(USER_ID, NOTE_ID))
            .thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        noteService.deleteNote(USER_ID, NOTE_ID);

        // Assert
        assertNotNull(existingNote.getDeletedAt());
        verify(noteRepository).findByUserIdAndIdAndNotDeleted(USER_ID, NOTE_ID);
        verify(noteRepository).save(existingNote);
    }

    @Test
    void deleteNote_ShouldThrowException_WhenNoteNotFound() {
        // Arrange
        when(noteRepository.findByUserIdAndIdAndNotDeleted(USER_ID, NOTE_ID))
            .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> noteService.deleteNote(USER_ID, NOTE_ID));
        assertEquals("Note not found", exception.getMessage());

        verify(noteRepository).findByUserIdAndIdAndNotDeleted(USER_ID, NOTE_ID);
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    void getNote_ShouldReturnNote() {
        // Arrange
        Note existingNote = createSampleNote();
        when(noteRepository.findByUserIdAndIdAndNotDeleted(USER_ID, NOTE_ID))
            .thenReturn(Optional.of(existingNote));

        // Act
        Note retrievedNote = noteService.getNote(USER_ID, NOTE_ID);

        // Assert
        assertNotNull(retrievedNote);
        assertEquals(NOTE_ID, retrievedNote.getId());
        assertEquals(USER_ID, retrievedNote.getUserId());
        assertEquals(TITLE, retrievedNote.getTitle());
        assertEquals(CONTENT, retrievedNote.getContent());
        assertEquals(PRIORITY, retrievedNote.getPriority());

        verify(noteRepository).findByUserIdAndIdAndNotDeleted(USER_ID, NOTE_ID);
    }

    @Test
    void getNote_ShouldThrowException_WhenNoteNotFound() {
        // Arrange
        when(noteRepository.findByUserIdAndIdAndNotDeleted(USER_ID, NOTE_ID))
            .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> noteService.getNote(USER_ID, NOTE_ID));
        assertEquals("Note not found", exception.getMessage());

        verify(noteRepository).findByUserIdAndIdAndNotDeleted(USER_ID, NOTE_ID);
    }

    @Test
    void getNotes_ShouldReturnAllNotes_WhenNoFilters() {
        // Arrange
        List<Note> notes = List.of(createSampleNote());
        Page<Note> notesPage = new PageImpl<>(notes);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        when(noteRepository.findAllByUserIdAndNotDeleted(USER_ID, pageRequest))
            .thenReturn(notesPage);

        // Act
        Page<Note> result = noteService.getNotes(USER_ID, null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(notes.get(0), result.getContent().get(0));

        verify(noteRepository).findAllByUserIdAndNotDeleted(USER_ID, pageRequest);
    }

    @Test
    void getNotes_ShouldReturnFilteredNotes_WhenPriorityProvided() {
        // Arrange
        List<Note> notes = List.of(createSampleNote());
        Page<Note> notesPage = new PageImpl<>(notes);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        when(noteRepository.findAllByUserIdAndPriorityAndNotDeleted(USER_ID, Note.Priority.NOW, pageRequest))
            .thenReturn(notesPage);

        // Act
        Page<Note> result = noteService.getNotes(USER_ID, null, Note.Priority.NOW, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(notes.get(0), result.getContent().get(0));

        verify(noteRepository).findAllByUserIdAndPriorityAndNotDeleted(USER_ID, Note.Priority.NOW, pageRequest);
    }

    @Test
    void getNotes_ShouldReturnFilteredNotes_WhenDateRangeIsToday() {
        // Arrange
        List<Note> notes = List.of(createSampleNote());
        Page<Note> notesPage = new PageImpl<>(notes);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        when(noteRepository.findAllByUserIdAndCreatedAtAfterAndNotDeleted(eq(USER_ID), any(OffsetDateTime.class), eq(pageRequest)))
            .thenReturn(notesPage);

        // Act
        Page<Note> result = noteService.getNotes(USER_ID, null, null, "TODAY");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(notes.get(0), result.getContent().get(0));

        verify(noteRepository).findAllByUserIdAndCreatedAtAfterAndNotDeleted(eq(USER_ID), any(OffsetDateTime.class), eq(pageRequest));
    }

    @Test
    void getNotes_ShouldReturnFilteredNotes_WhenDateRangeIsPastSevenDays() {
        // Arrange
        List<Note> notes = List.of(createSampleNote());
        Page<Note> notesPage = new PageImpl<>(notes);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        when(noteRepository.findAllByUserIdAndCreatedAtAfterAndNotDeleted(eq(USER_ID), any(OffsetDateTime.class), eq(pageRequest)))
            .thenReturn(notesPage);

        // Act
        Page<Note> result = noteService.getNotes(USER_ID, null, null, "PAST_SEVEN_DAYS");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(notes.get(0), result.getContent().get(0));

        verify(noteRepository).findAllByUserIdAndCreatedAtAfterAndNotDeleted(eq(USER_ID), any(OffsetDateTime.class), eq(pageRequest));
    }

    @Test
    void getNotes_ShouldReturnAllNotes_WhenInvalidDateRange() {
        // Arrange
        List<Note> notes = List.of(createSampleNote());
        Page<Note> notesPage = new PageImpl<>(notes);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        when(noteRepository.findAllByUserIdAndNotDeleted(USER_ID, pageRequest))
            .thenReturn(notesPage);

        // Act
        Page<Note> result = noteService.getNotes(USER_ID, null, null, "INVALID_RANGE");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(notes.get(0), result.getContent().get(0));

        verify(noteRepository).findAllByUserIdAndNotDeleted(USER_ID, pageRequest);
    }

    @Test
    void countStaleNotes_ShouldReturnCount() {
        // Arrange
        List<Note> staleNotes = List.of(createSampleNote());
        when(noteRepository.findStaleNotes(eq(USER_ID), any(OffsetDateTime.class)))
            .thenReturn(staleNotes);

        // Act
        int count = noteService.countStaleNotes(USER_ID);

        // Assert
        assertEquals(1, count);
        verify(noteRepository).findStaleNotes(eq(USER_ID), any(OffsetDateTime.class));
    }

    @Test
    void countHighPriorityNotes_ShouldReturnCount() {
        // Arrange
        List<Note> highPriorityNotes = List.of(createSampleNote());
        when(noteRepository.findHighPriorityNotes(USER_ID))
            .thenReturn(highPriorityNotes);

        // Act
        int count = noteService.countHighPriorityNotes(USER_ID);

        // Assert
        assertEquals(1, count);
        verify(noteRepository).findHighPriorityNotes(USER_ID);
    }

    @Test
    void calculateAverageCompletionTime_ShouldReturnAverage() {
        // Arrange
        Note note = createSampleNote();
        note.setCreatedAt(OffsetDateTime.now().minusHours(2));
        note.setUpdatedAt(OffsetDateTime.now());
        List<Note> completedNotes = List.of(note);

        when(noteRepository.findCompletedNotes(USER_ID))
            .thenReturn(completedNotes);

        // Act
        double average = noteService.calculateAverageCompletionTime(USER_ID);

        // Assert
        assertEquals(2.0, average, 0.1);
        verify(noteRepository).findCompletedNotes(USER_ID);
    }

    @Test
    void calculateAverageCompletionTime_ShouldReturnZero_WhenNoCompletedNotes() {
        // Arrange
        when(noteRepository.findCompletedNotes(USER_ID))
            .thenReturn(List.of());

        // Act
        double average = noteService.calculateAverageCompletionTime(USER_ID);

        // Assert
        assertEquals(0.0, average, 0.1);
        verify(noteRepository).findCompletedNotes(USER_ID);
    }

    @Test
    void calculateAverageDeletionTime_ShouldReturnAverage() {
        // Arrange
        Note note = createSampleNote();
        note.setCreatedAt(OffsetDateTime.now().minusHours(3));
        note.setDeletedAt(OffsetDateTime.now());
        List<Note> deletedNotes = List.of(note);

        when(noteRepository.findDeletedNotes(USER_ID))
            .thenReturn(deletedNotes);

        // Act
        double average = noteService.calculateAverageDeletionTime(USER_ID);

        // Assert
        assertEquals(3.0, average, 0.1);
        verify(noteRepository).findDeletedNotes(USER_ID);
    }

    @Test
    void calculateAverageDeletionTime_ShouldReturnZero_WhenNoDeletedNotes() {
        // Arrange
        when(noteRepository.findDeletedNotes(USER_ID))
            .thenReturn(List.of());

        // Act
        double average = noteService.calculateAverageDeletionTime(USER_ID);

        // Assert
        assertEquals(0.0, average, 0.1);
        verify(noteRepository).findDeletedNotes(USER_ID);
    }
}
