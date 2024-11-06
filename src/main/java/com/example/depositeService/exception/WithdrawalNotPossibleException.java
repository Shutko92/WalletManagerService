package com.example.depositeService.exception;

public class WithdrawalNotPossibleException extends RuntimeException{
    private static final String WITHDRAWAL_NOT_POSSIBLE = "Не хватает средств для снятия.";
    public WithdrawalNotPossibleException() {super(WITHDRAWAL_NOT_POSSIBLE);}
}
