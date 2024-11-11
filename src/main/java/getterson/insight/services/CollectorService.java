package getterson.insight.services;

import getterson.insight.dtos.GeneratedSummary;
import getterson.insight.dtos.SummaryRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

import static getterson.insight.utils.DateUtil.ISO8601_DATE_PATTERN;
import static getterson.insight.utils.DateUtil.stringToDate;

@Service
public class CollectorService {

    private final WebClient summarizeClient;
    private final SummaryDataService summaryDataService;
    private static final Logger logger = LoggerFactory.getLogger(CollectorService.class);

    public CollectorService(WebClient summarizeClient, SummaryDataService summaryDataService) {
        this.summarizeClient = summarizeClient;
        this.summaryDataService = summaryDataService;
    }

    public void collectData(String topicTitle, String initialDate, String finalDate) {
        SummaryRequestDTO summaryRequest = new SummaryRequestDTO(topicTitle, initialDate, finalDate);
        summarizeClient.post()
                .uri("/summarize")
                .bodyValue(summaryRequest)
                .retrieve()
                .bodyToMono(GeneratedSummary.class)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        summarizedData -> summaryDataService.save(topicTitle, stringToDate(initialDate, ISO8601_DATE_PATTERN), stringToDate(finalDate, ISO8601_DATE_PATTERN), summarizedData, summaryRequest),
                        error -> logger.error("Erro ao tentar obter o resumo para o tópico {}: {}", topicTitle, error.getMessage()));
    }
}
