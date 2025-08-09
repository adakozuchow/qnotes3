package ydgrun.info.qnotes3.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import ydgrun.info.qnotes3.api.StatisticsApi;
import ydgrun.info.qnotes3.model.Statistics;
import ydgrun.info.qnotes3.service.StatisticsService;

@RestController
public class StatisticsController implements StatisticsApi {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    public ResponseEntity<Statistics> getStatistics() {
        logger.debug("Getting statistics for user: {}", getCurrentUserId());
        return ResponseEntity.ok(statisticsService.getStatistics(getCurrentUserId()));
    }
}