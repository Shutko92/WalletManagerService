package com.example.depositeService.service;

import com.example.depositeService.dto.OperationRequest;
import com.example.depositeService.dto.OperationResponse;
import com.example.depositeService.dto.OperationType;
import com.example.depositeService.dto.WalletInfoResponse;
import com.example.depositeService.entity.Wallet;
import com.example.depositeService.exception.UserNotFoundException;
import com.example.depositeService.mapper.WalletMapper;
import com.example.depositeService.repository.DepositRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {
    @Mock
    private DepositRepository depositRepository;
    @Mock
    private WalletMapper walletMapper;
    @InjectMocks
    private WalletService walletService;

    private OperationRequest operationRequest;
    private OperationResponse operationResponse;
    private WalletInfoResponse walletInfoResponse;
    private Wallet wallet;

    private final UUID walletId = UUID.fromString("78970304-2dbb-4a03-b468-81bb56696ad5");
    private final UUID clientId = UUID.fromString("5b054676-255d-4796-86f0-3afba6fa3673");
    private final LocalDate creationDate = LocalDate.of(2024, 1, 1);
    private final LocalDate deletionDate = LocalDate.of(2024, 2, 1);
    private final String ALTER_DEPOSIT = "Пополнен кошелек. Актуальный баланс: %f";
    private final String DELETION_SUCCESS = "Закрыт кошелек. Актуальный баланс: %f";
    private final String NEW_WALLET = "Открыт новый кошелек. Актуальный баланс: %f";

    @BeforeEach
    void setUp() {
        operationRequest = getTestOperationRequest();
        walletInfoResponse = getTestWalletInfoResponse();
        wallet = getTestWallet();
    }

    private WalletInfoResponse getTestWalletInfoResponse() {
        return WalletInfoResponse.builder()
                .id(walletId)
                .balance(BigDecimal.valueOf(100))
                .creationDate(creationDate)
                .ownerId(clientId).build();
    }

    private Wallet getTestWallet() {
        return Wallet.builder()
                .id(walletId)
                .balance(BigDecimal.valueOf(100))
                .creationDate(creationDate)
                .deletionDate(deletionDate)
                .ownerId(clientId)
                .deleted(false).build();
    }

    private OperationResponse getTestOperationResponse(String message, double amount) {
        return OperationResponse.builder()
                .id(walletId)
                .message(message.formatted(amount)).build();
    }

    private OperationRequest getTestOperationRequest() {
        return OperationRequest.builder()
                .walletId(walletId)
                .operationType(OperationType.DEPOSIT)
                .amount(BigDecimal.valueOf(100)).build();
    }

    @Test
    void operationShouldReturnOperationResponse() {
        operationResponse = getTestOperationResponse(ALTER_DEPOSIT, 200.00);
        when(depositRepository.findByIdAndDeletedIsFalse(walletId))
                .thenReturn(Optional.ofNullable(wallet));
        when(depositRepository.save(wallet)).thenReturn(wallet);

        assertEquals(operationResponse, walletService.operation(operationRequest));
    }

    @Test
    void newWalletShouldReturnOperationResponse() {
        operationResponse = getTestOperationResponse(NEW_WALLET, 0.00);
        wallet.setBalance(BigDecimal.valueOf(0));
        when(depositRepository.save(any(Wallet.class))).thenReturn(wallet);

        assertEquals(operationResponse, walletService.newWallet(String.valueOf(clientId)));
    }

    @Test
    void getWalletsShouldReturnWalletInfoResponseList() {
        depositRepository.save(wallet);
        when(depositRepository.findAllByOwnerIdAndDeletedIsFalse(clientId)).thenReturn(List.of(wallet));
        when(walletMapper.toWalletInfoResponse(List.of(wallet))).thenReturn(List.of(walletInfoResponse));

        assertEquals(List.of(walletInfoResponse), walletService.getWallets(String.valueOf(clientId)));
    }

    @Test
    void getWalletsShouldThrowUserNotFoundException() {
        depositRepository.save(wallet);
        when(depositRepository.findAllByOwnerIdAndDeletedIsFalse(clientId)).thenReturn(List.of());

        assertThrows(UserNotFoundException.class, () -> walletService.getWallets(String.valueOf(clientId)));
    }

    @Test
    void deleteWalletShouldReturnOperationResponse() {
        operationResponse = getTestOperationResponse(DELETION_SUCCESS, 100.00);

        when(depositRepository.findByIdAndDeletedIsFalse(walletId))
                .thenReturn(Optional.ofNullable(wallet));
        when(depositRepository.save(wallet)).thenReturn(wallet);

        assertEquals(operationResponse, walletService.deleteWallet(String.valueOf(walletId)));
    }
}