package com.trading.dto;

import com.trading.enums.TradingPair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceResponse {
    private TradingPair tradingPair;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private LocalDateTime timestamp;
}
