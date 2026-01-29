package com.trading.repository;

import com.trading.entity.Price;
import com.trading.enums.TradingPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    Optional<Price> findByTradingPair(TradingPair tradingPair);
}
