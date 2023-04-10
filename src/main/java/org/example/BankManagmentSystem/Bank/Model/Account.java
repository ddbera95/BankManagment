package org.example.BankManagmentSystem.Bank.Model;

public interface Account
{

    int getAmount();

    int deposite(int amount);

    int withdraw(int amount);

    void transfer(int amount , Account account);

    String getAccountNumber();




}
