package com.example.depositeService.exception;

public class UserNotFoundException extends RuntimeException{
    private static final String WALLET_NOT_FOUND = "Пользователь с переданным id не найден";
    public UserNotFoundException() {super(WALLET_NOT_FOUND);}
}
