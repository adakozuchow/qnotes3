package ydgrun.info.qnotes3.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ydgrun.info.qnotes3.api.NotesApi;
import ydgrun.info.qnotes3.model.Note;
import ydgrun.info.qnotes3.model.NoteRequest;
import ydgrun.info.qnotes3.model.NotesResponse;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class NotesController implements NotesApi {

    @Override
    public ResponseEntity<Note> createNote(NoteRequest noteRequest) {
        Note note = createDummyNote(UUID.randomUUID());
        note.setTitle(noteRequest.getTitle());
        note.setContent(noteRequest.getContent());
        note.setPriority(Note.PriorityEnum.fromValue(noteRequest.getPriority().getValue()));
        return ResponseEntity.status(201).body(note);
    }

    @Override
    public ResponseEntity<Void> deleteNote(UUID id) {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Note> getNoteById(UUID id) {
        return ResponseEntity.ok(createDummyNote(id));
    }

    @Override
    public ResponseEntity<NotesResponse> getNotes(Integer page, String priority, String dateRange) {
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
    public ResponseEntity<Note> updateNote(UUID id, NoteRequest noteRequest) {
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