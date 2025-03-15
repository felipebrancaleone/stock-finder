package com.leadiq.stock.service;

import com.leadiq.stock.entity.Stock;
import com.leadiq.stock.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StockService stockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStockBySymbolAndDate_Success() {
        // Given
        Stock stock = new Stock("AAPL", LocalDate.of(2025, 3, 1),
                new BigDecimal("174.25"), new BigDecimal("176.10"),
                new BigDecimal("178.00"), new BigDecimal("172.80"), 58900000L);

        when(stockRepository.findByCompanySymbolAndDate("AAPL", LocalDate.of(2025, 3, 1)))
                .thenReturn(Optional.of(stock));

        // When
        Stock retrievedStock = stockService.getStockBySymbolAndDate("AAPL", LocalDate.of(2025, 3, 1));

        // Then
        assertNotNull(retrievedStock);
        assertEquals("AAPL", retrievedStock.getCompanySymbol());
        assertEquals(new BigDecimal("176.10"), retrievedStock.getClosePrice());
    }

    @Test
    void testGetStockBySymbolAndDate_NotFound() {
        // Given
        when(stockRepository.findByCompanySymbolAndDate("AAPL", LocalDate.of(2025, 3, 1)))
                .thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () ->
                stockService.getStockBySymbolAndDate("AAPL", LocalDate.of(2025, 3, 1))
        );

        assertEquals("Stock data not found.", exception.getMessage());
    }

    @Test
    void testFetchAndStoreStockPrices_Success() {
        // Given: Mock API response in Java format (Map)
        Map<String, Object> mockApiResponse = Map.of(
                "results", List.of(
                        Map.of(
                                "t", 1746057600000L,
                                "o", 174.25,
                                "c", 176.10,
                                "h", 178.00,
                                "l", 172.80,
                                "v", 58900000
                        )
                )
        );

        // Mock API call returning a deserialized response
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenReturn(mockApiResponse);

        // When: Call the method
        stockService.fetchAndStoreStockPrices("AAPL", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 1));

        // Then: Verify that stockRepository.save() was called once
        verify(stockRepository, times(1)).save(any(Stock.class));
    }
}
