package ydgrun.info.qnotes3.service;

import org.springframework.stereotype.Service;
import ydgrun.info.qnotes3.model.Statistics;

@Service
public class StatisticsService {
    private final NoteService noteService;

    public StatisticsService(NoteService noteService) {
        this.noteService = noteService;
    }

    public Statistics getStatistics(String userId) {
        Statistics stats = new Statistics();
        stats.setStaleNotesCount(noteService.countStaleNotes(userId));
        stats.setHighPriorityNotesCount(noteService.countHighPriorityNotes(userId));
        stats.setAverageCompletionTimeHours((float) noteService.calculateAverageCompletionTime(userId));
        stats.setAverageDeletionTimeHours((float) noteService.calculateAverageDeletionTime(userId));
        return stats;
    }
}