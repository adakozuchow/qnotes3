package ydgrun.info.qnotes3.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ydgrun.info.qnotes3.api.NotesApi;
import ydgrun.info.qnotes3.model.Note;
import ydgrun.info.qnotes3.model.NoteRequest;
import ydgrun.info.qnotes3.model.NotesResponse;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class NotesController implements NotesApi {
    private static final Logger logger = LoggerFactory.getLogger(NotesController.class);

    @Override
    @PostMapping(value = "/notes", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Note> createNote(@RequestBody NoteRequest noteRequest) {
        logger.debug("Received request to create note: {}", noteRequest);
        try {
            Note note = createDummyNote(UUID.randomUUID());
            note.setTitle(noteRequest.getTitle());
            note.setContent(noteRequest.getContent());
            note.setPriority(Note.PriorityEnum.fromValue(noteRequest.getPriority().getValue()));
            logger.debug("Created note: {}", note);
            return ResponseEntity.status(201).body(note);
        } catch (Exception e) {
            logger.error("Error creating note", e);
            throw e;
        }
    }

    @Override
    @DeleteMapping(value = "/notes/{id}", produces = "application/json")
    public ResponseEntity<Void> deleteNote(@PathVariable("id") UUID id) {
        logger.debug("Deleting note with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping(value = "/notes/{id}", produces = "application/json")
    public ResponseEntity<Note> getNoteById(@PathVariable("id") UUID id) {
        logger.debug("Getting note with id: {}", id);
        return ResponseEntity.ok(createDummyNote(id));
    }

    @Override
    @GetMapping(value = "/notes", produces = "application/json")
    public ResponseEntity<NotesResponse> getNotes(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "priority", required = false) String priority,
            @RequestParam(value = "dateRange", required = false, defaultValue = "ALL") String dateRange) {
        logger.debug("Getting notes with page: {}, priority: {}, dateRange: {}", page, priority, dateRange);
        
        List<Note> notes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            notes.add(createDummyNote(UUID.randomUUID()));
        }

        NotesResponse response = new NotesResponse();
        response.setNotes(notes);
        response.setTotalPages(3);
        response.setCurrentPage(page != null ? page : 0);

        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping(value = "/notes/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Note> updateNote(@PathVariable("id") UUID id, @RequestBody NoteRequest noteRequest) {
        logger.debug("Updating note with id: {} and data: {}", id, noteRequest);
        Note note = createDummyNote(id);
        note.setTitle(noteRequest.getTitle());
        note.setContent(noteRequest.getContent());
        note.setPriority(Note.PriorityEnum.fromValue(noteRequest.getPriority().getValue()));
        note.setUpdatedAt(OffsetDateTime.now());
        return ResponseEntity.ok(note);
    }

    private Note createDummyNote(UUID id) {
        Note note = new Note();
        note.setId(id);
        note.setTitle("Sample Note");
        note.setContent("This is a sample note content for testing purposes.");
        note.setPriority(Note.PriorityEnum.NOW);
        note.setCreatedAt(OffsetDateTime.now().minusDays(1));
        note.setUpdatedAt(OffsetDateTime.now());
        return note;
    }
}