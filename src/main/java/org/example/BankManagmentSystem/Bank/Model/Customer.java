package org.example.BankManagmentSystem.Bank.Model;

import org.example.BankManagmentSystem.Bank.Bank;
import org.example.BankManagmentSystem.Bank.Utils.AccountType;

public class Customer
{
   private String  customerId ;

   private String  name;

   private String  address;

   private String mobileNumber;

   private String eMailAddress;

   private  String password;

   private Account account;

   private Bank bank;


   public  Customer(String  name , String address , String mobileNumber , String emailAddress , String password ,  AccountType accountType , String accountNumber , String  customerId)
   {

       switch (accountType)
       {
           case Saving ->
           {
               //System.out.println("saving is created");
               this.account = new Saving(this , accountNumber);
           }

           case Current ->
           {
               this.account = new Current(this , accountNumber);
           }


       }


       this.name = name;

        this.password = password;
       this.customerId = customerId;

       this.address = address;

       this.eMailAddress = emailAddress;

       this.mobileNumber = mobileNumber;

       //System.out.println(account);

   }

   public  String getPassword()
   {
       return  this.password;
   }

    public Account getAccount()
    {
        //System.out.println(this.account);
        return this.account;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public String getName()
    {
        return name;
    }

    public String getAddress()
    {
        return address;
    }

    public String getMobileNumber()
    {
        return mobileNumber;
    }

    public String getEMailAddress()
    {
        return eMailAddress;
    }
}
