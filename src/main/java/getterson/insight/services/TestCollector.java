package getterson.insight.services;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class TestCollector {
    private final CollectorService collectorService;

    public TestCollector(CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    @PostConstruct
    public void execute() {
        collectorService.collectData("Python", "2024-11-07", "2024-11-08");
    }
}
