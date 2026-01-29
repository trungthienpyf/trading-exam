package com.trading.dto;

import com.trading.enums.TransactionType;
import com.trading.enums.TradingPair;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeRequest {
    
    @NotNull
    private Long userId;
    
    @NotNull
    private TradingPair tradingPair;
    
    @NotNull
    private TransactionType type;
    
    @NotNull
    private BigDecimal amount;
}
