package com.trading.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeResponse {
    private Long transactionId;
    private boolean success;
    private String message;
    private BigDecimal executedPrice;
    private BigDecimal totalUsdt;
}
