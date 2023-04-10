package org.example.BankManagmentSystem.Bank;

import org.example.BankManagmentSystem.Bank.DB.BankDB;
import org.example.BankManagmentSystem.Bank.Model.Customer;
import org.example.BankManagmentSystem.Bank.Model.InsufficientBalance;
import org.example.BankManagmentSystem.Bank.Model.LimitExceeded;
import org.example.BankManagmentSystem.Bank.Utils.AccountType;
import org.example.BankManagmentSystem.Bank.Utils.Utils;
import org.json.JSONObject;

public abstract class BankImpl implements  Bank
{

    int port = 0;

    BankDB database = new BankDB();


    public int getPort()
    {
        return  port;
    }

    public void setPort(int socketPort)
    {
        this.port = socketPort;
    }

    public int withdraw(int amount , String customerId)
    {
        try {

            return database.getCustomer(customerId).getAccount().withdraw(amount);

        }
        catch (InsufficientBalance exception)
        {
            return -1;
        }

    }

    public  int deposite(int amount , String customerId)
    {

        try
        {
          return   database.getCustomer(customerId).getAccount().deposite(amount);
        }
        catch (LimitExceeded exceeded)
        {
            if(exceeded.getMessage().equals("amountLimit"))
            {
                return -2;
            }
            return -1;
        }
    }

    public int transfer(int amount , String customerID , String accountNumber)
    {

        try
        {
             database.getCustomer(customerID).getAccount().transfer(amount , database.getAccount(accountNumber));

             return database.getCustomer(customerID).getAccount().getAmount();
        }
        catch (InsufficientBalance noBalanceException)
        {
            return -1;
        }
        catch (LimitExceeded exceeded)
        {
            return -2;
        }
    }

    public String  signUp(String name , String adress , String mobileNumber , String eMailAddress  , String password , String  accountType)
    {

        String  customerId = "";

        String accountNumber = "";

        boolean isCustomerIdGenerated = false;

        boolean isAccountNumberGenrated = false;

        while (!isCustomerIdGenerated)
        {
            customerId = Utils.generateCustomerId();

            if(database.getCustomer(customerId) == null)
            {
                isCustomerIdGenerated = true;
            }
        }

        while (!isAccountNumberGenrated)
        {
            accountNumber = Utils.generateAccountNumber();

            if(database.getAccount(accountNumber) == null)
            {
                isAccountNumberGenrated = true;
            }
        }

        Customer customer = null;

        if(accountType.equals("saving"))
        {
             customer = new Customer(name, adress , mobileNumber , eMailAddress , password ,  AccountType.Saving ,accountNumber ,customerId);
        }
        else  if(accountType.equals("current"))
        {
             customer = new Customer(name, adress , mobileNumber , eMailAddress , password ,  AccountType.Current ,accountNumber ,customerId);
        }


        database.addCustomer(customer);

        database.addAccount(customer.getAccount());

        System.out.println("new account with " + customerId + " " + " and " + database.getCustomer(customerId).getAccount().getAccountNumber() + " account number is created");


        return  customerId;

    }

    public boolean signIn(String customerId , String password)
    {

        if(database.getCustomer(customerId) != null && database.getCustomer(customerId).getPassword().equals(password))
        {
                return true;
        }
        else
        {
            return false;
            //not logedin
        }

    }

    public String  getAccountNumber(String customerId)
    {
        if(database.getCustomer(customerId) != null)
        return  database.getCustomer(customerId).getAccount().getAccountNumber();

        return null;
    }

    public String getProfile(String customerId)
    {
        if(database.getCustomer(customerId) != null)
        {
            Customer customer = database.getCustomer(customerId);

            JSONObject response = new JSONObject();

            response.put("name" , customer.getName());

            response.put("address" , customer.getAddress() );

            response.put("eMail" , customer.getEMailAddress());

            response.put("mobileNumber" , customer.getMobileNumber());

            response.put("accountNumber" , customer.getAccount().getAccountNumber() );

            response.put("amount" , customer.getAccount().getAmount());

            return response.toString();
        }

        return null;
    }

    public  int getAmount(String customerId)
    {
        return database.getCustomer(customerId).getAccount().getAmount();
    }

    public boolean checkAccount(String  accountNumber)
    {
        if(database.getAccount(accountNumber) != null)
        {
            return true;
        }
        return  false;
    }

}
