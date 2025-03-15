package com.leadiq.stock.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_symbol", length = 10, nullable = false)
    private String companySymbol;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "open_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal openPrice;

    @Column(name = "close_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal closePrice;

    @Column(name = "high_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal highPrice;

    @Column(name = "low_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal lowPrice;

    @Column(name = "volume", nullable = false)
    private Long volume;

    public Stock() {}

    public Stock(String companySymbol, LocalDate date, BigDecimal openPrice, BigDecimal closePrice,
                 BigDecimal highPrice, BigDecimal lowPrice, Long volume) {
        this.companySymbol = companySymbol;
        this.date = date;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.volume = volume;
    }
}
