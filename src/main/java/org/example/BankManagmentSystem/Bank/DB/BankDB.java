package org.example.BankManagmentSystem.Bank.DB;

import org.example.BankManagmentSystem.Bank.Model.Account;
import org.example.BankManagmentSystem.Bank.Model.Customer;

import java.util.HashMap;

public class BankDB
{

    private  HashMap<String , Customer> customers = new HashMap<>();

    private HashMap<String , Account> accounts = new HashMap<>();

    public Customer getCustomer(String  customerId)
    {
        return customers.get(customerId);
    }

    public  Account getAccount(String accountNumber)
    {

        return accounts.get(accountNumber);
    }

    public void addAccount(Account account)
    {
        accounts.put(account.getAccountNumber() , account);
    }

    public  void  addCustomer(Customer customer)
    {
        customers.put(customer.getCustomerId() , customer);
    }
}
