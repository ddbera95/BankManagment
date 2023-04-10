package org.example.BankManagmentSystem.Bank.Utils;

import java.math.BigInteger;
import java.util.UUID;

public class Utils
{
    public static String generateAccountNumber()
    {
        String  accountNumber;

         String lUUID = String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));

      accountNumber = lUUID.substring((lUUID.length() -12) , lUUID.length());


      return  accountNumber;

    }


    public  static  String generateCustomerId()
    {
        String  customerId;

        String lUUID = String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));

        customerId = lUUID.substring((lUUID.length() -6) , lUUID.length());

        return customerId;

    }
}
