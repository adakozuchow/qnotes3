package ydgrun.info.qnotes3.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ydgrun.info.qnotes3.api.StatisticsApi;
import ydgrun.info.qnotes3.model.Statistics;

@RestController
public class StatisticsController implements StatisticsApi {

    @Override
    public ResponseEntity<Statistics> getStatistics() {
        Statistics stats = new Statistics();
        stats.setStaleNotesCount(5);
        stats.setHighPriorityNotesCount(3);
        stats.setAverageCompletionTimeHours(48.5f);
        stats.setAverageDeletionTimeHours(72.0f);
        return ResponseEntity.ok(stats);
    }
}