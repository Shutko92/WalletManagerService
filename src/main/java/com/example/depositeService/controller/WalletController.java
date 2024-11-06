package com.example.depositeService.controller;

import com.example.depositeService.dto.OperationRequest;
import com.example.depositeService.dto.OperationResponse;
import com.example.depositeService.dto.WalletInfoResponse;
import com.example.depositeService.service.WalletService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping("/wallet/new")
    public ResponseEntity<OperationResponse> newWallet(@Validated @RequestHeader
                                                           @UUID(message = "Нарушен формат UUID")
                                                           @NotBlank(message = "Поле ID не должно быть пустым")
                                                           String clientId) {
        return ResponseEntity.ok(walletService.newWallet(clientId));
    }

    @PostMapping("/wallet")
    public ResponseEntity<OperationResponse> operation(@Validated @RequestBody OperationRequest request) {
        return ResponseEntity.ok(walletService.operation(request));
    }

    @GetMapping("/wallets/{clientId}")
    public ResponseEntity<List<WalletInfoResponse>> getWallets(@Validated @PathVariable
                                                                   @UUID(message = "Нарушен формат UUID")
                                                                   @NotBlank(message = "Поле ID не должно быть пустым")
                                                                   String clientId) {
        return ResponseEntity.ok(walletService.getWallets(clientId));
    }

    @PostMapping("/wallet/{walletId}")
    public ResponseEntity<OperationResponse> deleteWallet(@Validated @PathVariable
                                                              @UUID(message = "Нарушен формат UUID")
                                                              @NotBlank(message = "Поле ID не должно быть пустым")
                                                              String walletId) {
        return ResponseEntity.ok(walletService.deleteWallet(walletId));
    }
}
