package com.leadiq.stock.controller;

import com.leadiq.stock.entity.Stock;
import com.leadiq.stock.service.StockService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/fetch")
    public Map<String, String> fetchStockData(
            @RequestParam String companySymbol,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        stockService.fetchAndStoreStockPrices(companySymbol, fromDate, toDate);
        return Map.of("message", "Stock data fetched and stored successfully.");
    }

    @GetMapping("/{companySymbol}")
    public Stock getStockBySymbolAndDate(
            @PathVariable String companySymbol,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return stockService.getStockBySymbolAndDate(companySymbol, date);
    }
}
