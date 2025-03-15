package com.leadiq.stock.service;

import com.leadiq.stock.entity.Stock;
import com.leadiq.stock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final RestTemplate restTemplate;

    @Value("${polygon.api.key}")
    private String polygonApiKey;

    public StockService(StockRepository stockRepository, RestTemplate restTemplate) {
        this.stockRepository = stockRepository;
        this.restTemplate = restTemplate;
    }

    public void fetchAndStoreStockPrices(String companySymbol, LocalDate fromDate, LocalDate toDate) {
        String url = UriComponentsBuilder
                .fromUriString("https://api.polygon.io/v2/aggs/ticker/{symbol}/range/1/day/{from}/{to}")
                .queryParam("adjusted", "true")
                .queryParam("sort", "asc")
                .queryParam("limit", "1000")
                .queryParam("apiKey", polygonApiKey)
                .build(companySymbol, fromDate, toDate)
                .toString();

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("results")) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
                results.forEach(data -> {
                    Stock stock = new Stock(
                            companySymbol,
                            LocalDate.ofEpochDay(((Number) data.get("t")).longValue() / 86400000),
                            BigDecimal.valueOf(((Number) data.get("o")).doubleValue()),
                            BigDecimal.valueOf(((Number) data.get("c")).doubleValue()),
                            BigDecimal.valueOf(((Number) data.get("h")).doubleValue()),
                            BigDecimal.valueOf(((Number) data.get("l")).doubleValue()),
                            ((Number) data.get("v")).longValue()
                    );
                    stockRepository.save(stock);
                });
            }
        } catch (Exception e) {
            System.err.println("API rate limit reached. Try again later.");
        }
    }

    public Stock getStockBySymbolAndDate(String companySymbol, LocalDate date) {
        return stockRepository.findByCompanySymbolAndDate(companySymbol, date)
                .orElseThrow(() -> new RuntimeException("Stock data not found."));
    }
}
