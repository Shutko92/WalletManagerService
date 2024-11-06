package com.example.depositeService.mapper;

import com.example.depositeService.dto.WalletInfoResponse;
import com.example.depositeService.entity.Wallet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    List<WalletInfoResponse> toWalletInfoResponse(List<Wallet> walletList);
}
