package com.example.depositeService.exception;

public class WalletNotFoundException extends RuntimeException{
    private static final String WALLET_NOT_FOUND = "Кошелек с переданным id не найден";
    public WalletNotFoundException() {super(WALLET_NOT_FOUND);}

}
