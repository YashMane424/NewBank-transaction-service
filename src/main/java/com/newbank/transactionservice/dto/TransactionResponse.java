package com.newbank.transactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private Long id;
    private String transactionId;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private String type;
    private String status;
    private String description;
    private LocalDateTime transactionDate;
}
