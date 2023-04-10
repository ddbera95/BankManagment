package org.example.BankManagmentSystem.Bank.DB;

import java.util.HashMap;
import java.util.UUID;

public class TokenContainer
{

   private HashMap<String , String> customerTokenContainer = new HashMap<>();


   public  boolean isTokenValid(String  customerId , String token)
   {
       if(customerTokenContainer.get(customerId) != null)
       {
           if(customerTokenContainer.get(customerId).equals(token))
           {
               return true;
           }
       }

       return false;
   }

   public String addToken(String  customerId)
   {
       String  token = UUID.randomUUID().toString();

       customerTokenContainer.put(customerId , token);

       return token;
   }

   public  String  getToken(String  customerId)
   {
       return customerTokenContainer.get(customerId);
   }

}
