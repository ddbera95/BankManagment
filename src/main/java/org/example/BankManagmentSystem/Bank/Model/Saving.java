package org.example.BankManagmentSystem.Bank.Model;

import org.example.BankManagmentSystem.Bank.Utils.AccountType;

public class Saving implements  Account
{

    private final String  accountNumber;

    int amount = 0;

    Customer customer ;

    AccountType type = AccountType.Saving;


    public Saving(Customer customer , String  accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber()
    {
        return  this.accountNumber;
    }


    public int getAmount() {
        return amount;
    }

    public AccountType getType() {
        return type;
    }

    public synchronized int  withdraw(int withdrawAmount)
    {


        if(withdrawAmount <= this.amount)
        {


            this.amount -= withdrawAmount;


        }
        else
        {
            throw new InsufficientBalance("insufficient Balance in your account");
        }

        return this.amount;
    }


    public  synchronized  int deposite(int depositeAmount)
    {

        if(depositeAmount > 100000)
        {
            throw  new LimitExceeded("you can not deposite more than 1 million in your saving account");
        }
        if((this.amount + depositeAmount) > 1000000)
        {
            throw  new LimitExceeded("amountLimit");
        }

        this.amount += depositeAmount;

        return this.amount;

    }

    public  void transfer(int transferAmount , Account account)
    {

        if(transferAmount > this.amount)
        {
            throw new InsufficientBalance("nobalance");


        }
        try
        {
            withdraw(transferAmount);

            account.deposite(transferAmount);
        }


        catch (LimitExceeded exceeded)
        {

            synchronized (this)
            {
                this.amount += transferAmount;
            }

            throw new LimitExceeded("limitcrosed");
        }

    }


}



