package com.leadiq.stock.repository;

import com.leadiq.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByCompanySymbolAndDate(String companySymbol, LocalDate date);
}
