package com.example.depositeService.service;

import com.example.depositeService.dto.OperationRequest;
import com.example.depositeService.dto.OperationResponse;
import com.example.depositeService.dto.WalletInfoResponse;
import com.example.depositeService.entity.Wallet;
import com.example.depositeService.exception.WalletNotFoundException;
import com.example.depositeService.exception.WithdrawalNotPossibleException;
import com.example.depositeService.mapper.WalletMapper;
import com.example.depositeService.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final DepositRepository depositRepository;
    private final WalletMapper walletMapper;

    private final BigDecimal DEFAULT_BALANCE = BigDecimal.valueOf(0.00);

    private final String ALTER_DEPOSIT = "Пополнен кошелек. Актуальный баланс: %f";
    private final String WITHDRAWAL_SUCCESS = "Средства сняты. Актуальный баланс: %f";
    private final String NEW_WALLET = "Открыт новый кошелек. Актуальный баланс: %f";
    private final String DELETION_SUCCESS = "Закрыт кошелек. Актуальный баланс: %f";

    public OperationResponse operation(OperationRequest request) {
        return switch (request.getOperationType()) {
            case DEPOSIT -> performDeposit(request);
            case WITHDRAWAL -> performWithdrawal(request);
        };
    }

    private OperationResponse performWithdrawal(OperationRequest request) {
        Wallet wallet = getExistingWallet(request.getWalletId());

        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new WithdrawalNotPossibleException();
        }
        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        Wallet savedWallet = depositRepository.save(wallet);

        return createOperationResponse(savedWallet, WITHDRAWAL_SUCCESS);
    }

    private OperationResponse performDeposit(OperationRequest request) {
        Wallet wallet = getExistingWallet(request.getWalletId());

        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        Wallet savedWallet = depositRepository.save(wallet);

        return createOperationResponse(savedWallet, ALTER_DEPOSIT);
    }

    public OperationResponse newWallet(String clientId) {
        Wallet savedWallet = depositRepository.save(Wallet.builder()
                .ownerId(UUID.fromString(clientId))
                .creationDate(LocalDate.now())
                .balance(DEFAULT_BALANCE).build());

        return createOperationResponse(savedWallet, NEW_WALLET);
    }

    private Wallet getExistingWallet(UUID walletId) {
        return depositRepository.findByIdAndDeletedIsFalse(walletId)
                .orElseThrow(WalletNotFoundException::new);
    }

    private OperationResponse createOperationResponse(Wallet wallet, String messageTemplate) {
        String message = messageTemplate.formatted(wallet.getBalance());
        return OperationResponse.builder()
                .message(message)
                .id(wallet.getId())
                .build();
    }

    public List<WalletInfoResponse> getWallets(String clientId) {
        List<Wallet> wallets = depositRepository.findAllByOwnerIdAndDeletedIsFalse(UUID.fromString(clientId));

        return walletMapper.toWalletInfoResponse(wallets);
    }

    public OperationResponse deleteWallet(String walletId) {
        Wallet wallet = getExistingWallet(UUID.fromString(walletId));

        wallet.setDeleted(true);
        wallet.setDeletionDate(LocalDate.now());
        Wallet savedWallet = depositRepository.save(wallet);

        return createOperationResponse(savedWallet, DELETION_SUCCESS);
    }
}