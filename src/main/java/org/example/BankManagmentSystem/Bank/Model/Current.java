package org.example.BankManagmentSystem.Bank.Model;

import org.example.BankManagmentSystem.Bank.Utils.AccountType;

public class Current implements  Account
{

    private final String  accountNumber;

    int amount = 0;

    Customer customer ;

    AccountType type = AccountType.Saving;


    public Current(Customer customer , String accountNumber)
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
            throw new InsufficientBalance("insufficientBallance in your account");
        }

        return this.amount;
    }


    public  synchronized  int deposite(int dipositeAmmount)
    {



        this.amount += dipositeAmmount;

        return this.amount;

    }

    public  void transfer(int transferAmmount , Account account)
    {


        try
        {
            withdraw(transferAmmount);

            account.deposite(transferAmmount);
        }
        catch (LimitExceeded exceeded)
        {

            synchronized (this)
            {
                this.amount += transferAmmount;
            }

            throw new LimitExceeded("recipient can not receive more than 1000000");
        }

    }


}




