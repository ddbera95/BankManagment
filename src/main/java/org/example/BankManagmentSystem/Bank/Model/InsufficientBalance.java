package org.example.BankManagmentSystem.Bank.Model;

public class InsufficientBalance extends RuntimeException {

    InsufficientBalance(String message) {
        super(message);

    }
}
