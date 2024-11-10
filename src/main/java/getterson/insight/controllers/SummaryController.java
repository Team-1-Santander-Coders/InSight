package getterson.insight.controllers;

import getterson.insight.dtos.SummaryDataDTO;
import getterson.insight.services.SummaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/summarize")
public class SummaryController {
    private final SummaryService summaryService;


    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @PostMapping
    public ResponseEntity<String> summarizeTopic(@RequestParam String term,
                                                 @RequestParam String start_date,
                                                 @RequestParam String end_date) {
        try {
            if(summaryService.isRequestInProgress(term, start_date, end_date)) {
                return ResponseEntity.status(HttpStatus.PROCESSING)
                        .body("Esta requisição já está sendo processada pelo servidor e seu resultado estará disponível o mais rápido possível.");
            }

            boolean enqueued = summaryService.enqueueSummarizationRequest(term, start_date, end_date);
            if(enqueued) {
                return ResponseEntity.ok("Resumo em processamento");
            } else {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Serviço temporariamente indisponível.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar a solicitação");
        }
    }

    @GetMapping
    public ResponseEntity<List<SummaryDataDTO>> getSummaries(@RequestParam String userId) {
        try {
            List<SummaryDataDTO> summaries = summaryService.getSummariesForUser(userId);
            return ResponseEntity.ok(summaries);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


}
