package ydgrun.info.qnotes3.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ydgrun.info.qnotes3.api.StatisticsApi;
import ydgrun.info.qnotes3.model.Statistics;

@RestController
public class StatisticsController implements StatisticsApi {

    @Override
    public ResponseEntity<Statistics> getStatistics() {
        // TODO: Implement statistics calculation logic
        throw new UnsupportedOperationException("Not implemented yet");
    }
}