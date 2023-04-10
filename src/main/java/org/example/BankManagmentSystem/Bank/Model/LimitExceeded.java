package org.example.BankManagmentSystem.Bank.Model;

public class LimitExceeded extends RuntimeException {

    LimitExceeded(String message) {
        super(message);

    }
}
