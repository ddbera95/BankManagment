package org.example;

import org.example.BankManagmentSystem.Bank.Implementaion.BankService;
import org.example.BankManagmentSystem.Bank.Server.Server;

public class Main {
    public static void main(String[] args)
    {

        BankService bank = new BankService(9999);

        Server server = new Server(bank);

        server.start();

    }
}