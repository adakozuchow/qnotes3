package ydgrun.info.qnotes3.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ydgrun.info.qnotes3.api.NotesApi;
import ydgrun.info.qnotes3.model.Note;
import ydgrun.info.qnotes3.model.NoteRequest;
import ydgrun.info.qnotes3.model.NotesResponse;

import java.util.UUID;

@RestController
public class NotesController implements NotesApi {

    @Override
    public ResponseEntity<Note> createNote(NoteRequest noteRequest) {
        // TODO: Implement note creation logic
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ResponseEntity<Void> deleteNote(UUID id) {
        // TODO: Implement soft delete logic
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ResponseEntity<Note> getNoteById(UUID id) {
        // TODO: Implement note retrieval logic
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ResponseEntity<NotesResponse> getNotes(Integer page, String priority, String dateRange) {
        // TODO: Implement notes listing logic with filtering
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ResponseEntity<Note> updateNote(UUID id, NoteRequest noteRequest) {
        // TODO: Implement note update logic
        throw new UnsupportedOperationException("Not implemented yet");
    }
}