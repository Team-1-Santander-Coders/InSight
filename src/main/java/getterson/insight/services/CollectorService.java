package getterson.insight.services;

import getterson.insight.dtos.GeneratedSummaryDTO;
import getterson.insight.dtos.SummaryRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

@Service
public class CollectorService {

    private final WebClient webClient;

    public CollectorService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void collectData(String topic, String initialDate, String finalDate) {
        SummaryRequestDTO summaryRequest = new SummaryRequestDTO(topic, initialDate, finalDate);
        webClient.post()
                .uri("/summarize")
                .bodyValue(summaryRequest)
                .retrieve()
                .bodyToMono(GeneratedSummaryDTO.class)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        summarizedData -> System.out.println(summarizedData.summary()),
                        error -> error.printStackTrace());
    }
}
