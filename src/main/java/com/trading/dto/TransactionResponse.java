package com.trading.dto;

import com.trading.enums.TransactionType;
import com.trading.enums.TradingPair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private TradingPair tradingPair;
    private TransactionType type;
    private BigDecimal amount;
    private BigDecimal price;
    private BigDecimal totalUsdt;
    private LocalDateTime createdAt;
}
