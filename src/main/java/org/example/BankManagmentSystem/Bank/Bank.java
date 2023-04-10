package org.example.BankManagmentSystem.Bank;

public interface Bank
{

    int getPort();

    void setPort(int port);

    int withdraw(int amount , String  customerId);

    int deposite(int amount , String  customerId );

    int transfer(int amount , String  customerId , String accountNumber);

    boolean signIn(String  customerId , String password);

    public String  signUp(String name , String adress , String mobileNumber , String eMailAddress  , String password , String  accountType);

    public  int getAmount(String customerId);

    public String  getAccountNumber(String customerId);

    public String getProfile(String customerId);

    void Optional();

    boolean checkAccount(String  accountNumber);


}
