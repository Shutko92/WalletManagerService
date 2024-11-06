package com.example.depositeService.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperationRequest {
    @NotNull(message = "Поле ID не должно быть пустым")
    private UUID walletId;
    @NotNull(message = "Поле типа операции не должно быть пустым")
    private OperationType operationType;
    @NotNull(message = "Поле суммы не должно быть пустым")
    private BigDecimal amount;
}
