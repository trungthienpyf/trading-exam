package com.trading.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletBalanceResponse {
    private Long userId;
    private String username;
    private BigDecimal usdtBalance;
    private BigDecimal ethBalance;
    private BigDecimal btcBalance;
}
