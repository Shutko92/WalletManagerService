package com.example.depositeService.repository;

import com.example.depositeService.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepositRepository extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> findByIdAndDeletedIsFalse(UUID walletId);

    List<Wallet> findAllByOwnerIdAndDeletedIsFalse(UUID uuid);
}
