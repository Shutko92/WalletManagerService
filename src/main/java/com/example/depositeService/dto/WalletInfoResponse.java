package com.example.depositeService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletInfoResponse {
    private UUID id;
    private BigDecimal balance;
    private UUID ownerId;
    private LocalDate creationDate;
}
