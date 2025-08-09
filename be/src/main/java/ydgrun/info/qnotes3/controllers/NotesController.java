package ydgrun.info.qnotes3.controllers;

import org.openapitools.jackson.nullable.JsonNullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ydgrun.info.qnotes3.api.NotesApi;
import ydgrun.info.qnotes3.model.Note;
import ydgrun.info.qnotes3.model.NoteRequest;
import ydgrun.info.qnotes3.model.NotesResponse;
import ydgrun.info.qnotes3.service.NoteService;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class NotesController implements NotesApi {
    private static final Logger logger = LoggerFactory.getLogger(NotesController.class);
    private final NoteService noteService;

    public NotesController(NoteService noteService) {
        this.noteService = noteService;
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    @PostMapping(value = "/notes", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Note> createNote(@RequestBody NoteRequest noteRequest) {
        logger.debug("Received request to create note: {}", noteRequest);
        try {
            ydgrun.info.qnotes3.domain.Note domainNote = noteService.createNote(
                getCurrentUserId(),
                noteRequest.getTitle(),
                noteRequest.getContent(),
                ydgrun.info.qnotes3.domain.Note.Priority.valueOf(noteRequest.getPriority().getValue())
            );
            return ResponseEntity.status(201).body(mapToApiNote(domainNote));
        } catch (Exception e) {
            logger.error("Error creating note", e);
            throw e;
        }
    }

    @Override
    @DeleteMapping(value = "/notes/{id}", produces = "application/json")
    public ResponseEntity<Void> deleteNote(@PathVariable("id") UUID id) {
        logger.debug("Deleting note with id: {}", id);
        noteService.deleteNote(getCurrentUserId(), id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping(value = "/notes/{id}", produces = "application/json")
    public ResponseEntity<Note> getNoteById(@PathVariable("id") UUID id) {
        logger.debug("Getting note with id: {}", id);
        ydgrun.info.qnotes3.domain.Note domainNote = noteService.getNote(getCurrentUserId(), id);
        return ResponseEntity.ok(mapToApiNote(domainNote));
    }

    @Override
    @GetMapping(value = "/notes", produces = "application/json")
    public ResponseEntity<NotesResponse> getNotes(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "priority", required = false) String priority,
            @RequestParam(value = "dateRange", required = false, defaultValue = "ALL") String dateRange) {
        logger.debug("Getting notes with page: {}, priority: {}, dateRange: {}", page, priority, dateRange);
        
        var notesPage = noteService.getNotes(
            getCurrentUserId(),
            page,
            priority != null ? ydgrun.info.qnotes3.domain.Note.Priority.valueOf(priority) : null,
            dateRange
        );

        NotesResponse response = new NotesResponse();
        response.setNotes(notesPage.getContent().stream().map(this::mapToApiNote).toList());
        response.setTotalPages(notesPage.getTotalPages());
        response.setCurrentPage(notesPage.getNumber());

        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping(value = "/notes/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Note> updateNote(@PathVariable("id") UUID id, @RequestBody NoteRequest noteRequest) {
        logger.debug("Updating note with id: {} and data: {}", id, noteRequest);
        ydgrun.info.qnotes3.domain.Note domainNote = noteService.updateNote(
            getCurrentUserId(),
            id,
            noteRequest.getTitle(),
            noteRequest.getContent(),
            ydgrun.info.qnotes3.domain.Note.Priority.valueOf(noteRequest.getPriority().getValue())
        );
        return ResponseEntity.ok(mapToApiNote(domainNote));
    }

    private Note mapToApiNote(ydgrun.info.qnotes3.domain.Note domainNote) {
        Note apiNote = new Note();
        apiNote.setId(domainNote.getId());
        apiNote.setTitle(domainNote.getTitle());
        apiNote.setContent(domainNote.getContent());
        apiNote.setPriority(Note.PriorityEnum.fromValue(domainNote.getPriority().name()));
        apiNote.setCreatedAt(domainNote.getCreatedAt());
        apiNote.setUpdatedAt(domainNote.getUpdatedAt());
        
        // Only set deletedAt if it's not null
        if (domainNote.getDeletedAt() != null) {
            apiNote.deletedAt(domainNote.getDeletedAt());
        }
        
        return apiNote;
    }
}