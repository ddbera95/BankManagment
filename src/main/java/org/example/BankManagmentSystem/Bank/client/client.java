package org.example.BankManagmentSystem.Bank.client;

import org.example.BankManagmentSystem.Bank.Utils.ConstantUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.regex.Pattern;

public class client
{

    public static  final int PORT = 9999;

 /*   public  static  int socketPort = 0;*/

    public static  String token = null;

    public  static  String customerId = "";

    public static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));

    public static  final String SIGNUP_SIGNIN_OPTIONS = "Now choose the option from below" + ConstantUtils.NEW_LINE +
                                                        " 1 : Login" + ConstantUtils.NEW_LINE +
                                                        " 2 : Registration ";

    public  static final  String LINE_SEPERATOR = "-".repeat(15);

    public  static  final  String OPTIONS = "Now choose the options from the below " + ConstantUtils.NEW_LINE +
                                            ConstantUtils.NEW_LINE +
                                            " 1 : Check Balance" + ConstantUtils.NEW_LINE +
                                            " 2 : Get Account Number" + ConstantUtils.NEW_LINE +
                                            " 3 : Withdraw" + ConstantUtils.NEW_LINE +
                                            " 4 : Deposite" + ConstantUtils.NEW_LINE +
                                            " 5 : Transfer" + ConstantUtils.NEW_LINE +
                                            " 6 : Get Profile" + ConstantUtils.NEW_LINE +
                                            " 7 : to see the Options again" + ConstantUtils.NEW_LINE +
                                            " Enter exit to logout or press --exit to exit the progrrame";

    public static void main(String[] args)
    {

/*
        clientSocket socket = new clientSocket();

        socket.start();
        Thread.sleep(10);*/


        try {


            System.out.println("Welcome to the Bank of India");

            System.out.println(ConstantUtils.NEW_LINE);


            String option = "";

            boolean afterLogedInFlag = false;

            while (true) {

                if (token == null || customerId.equals(""))
                {
                    System.out.println(SIGNUP_SIGNIN_OPTIONS);

                    System.out.print(":");

                    option = READER.readLine();

                    switch (option) {

                        case "1":

                            signIn();

                            break;

                        case "2":

                            signUp();

                            break;

                        default:

                            System.out.println("Please Enter the valid Input");

                            break;
                    }

                }
                else
                {

                    if(!afterLogedInFlag )
                    {
                        System.out.printf(OPTIONS);

                        System.out.println(":");

                        //option = READER.readLine();

                        afterLogedInFlag = true;

                        continue;
                    }
                    else
                    {
                        System.out.println(ConstantUtils.NEW_LINE + "Choose your options");

                        System.out.print(":");

                        option = READER.readLine();
                    }



                    if(option.equals("exit"))
                    {
                        client.customerId = "";

                        client.token = null;

                        afterLogedInFlag = false;

                        continue;

                    }
                    else if(option.equals("--exit"))
                    {
                        System.exit(0);
                    }

                    switch (option) {

                        case "1":

                            checkBalance();

                            break;

                        case "2":

                            getAccountNumber();

                            break;

                        case "3":

                            withdraw();

                            break;

                        case "4":

                            deposite();

                            break;

                        case "5":

                            transfer();

                            break;

                        case "6":

                            getProfile();

                            break;

                        case "7":

                            System.out.println(ConstantUtils.NEW_LINE + OPTIONS);


                            break;

                        default:

                            System.out.println("Please enter the valid input");

                            break;
                    }

                }


            }
        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());

        }

    }


    public static  void getProfile()
    {
        if(token == null || customerId.equals(""))
        {
            return;
        }

        try(SocketConnection connection = new SocketConnection(PORT))
        {
            connection.write("profile");

            JSONObject request = new JSONObject();

            request.put("customerId" , customerId);

            request.put("token" , token);

            connection.write(request.toString());

            String  response = connection.read();

            if(response == null)
            {
                System.out.println("There is something is wrong with connection");
            }

            JSONObject responseObject = new JSONObject(response);

            if(responseObject.getString("user").equals("invalid"))
            {
                System.out.println("Please login again");

                customerId = "";

                token = null;

                return;
            }

            String profile =  connection.read();

            if(profile == null)
            {
                System.out.println("There is something wrong with connection");

                return;
            }

            JSONObject profileObject = new JSONObject(profile);

            String   name = profileObject.getString("name");

            String   address = profileObject.getString("address");

            String   eMail = profileObject.getString("eMail");

            String   mobileNumber = profileObject.getString("mobileNumber");

            String   accountNumber = profileObject.getString("accountNumber");

            int    amount = profileObject.getInt("amount");

            System.out.println(ConstantUtils.NEW_LINE);

            System.out.println("Name : " + name);

            System.out.println("Address : " + address);

            System.out.println("EMail : " + eMail);

            System.out.println("Mobile Number : " + mobileNumber );

            System.out.println("Customer Id : " + client.customerId);

            System.out.println("Account Number : " + accountNumber);

            System.out.println("Balance : " + amount);



        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }
    }

    public static  void transfer()
    {
        String  amountString = "";

        String  recipientAccountNumber = "";

        int amount = 0;

        try(SocketConnection connection = new SocketConnection(PORT))
        {
            for(int iterator = 0 ; iterator < 3 ; iterator++)
            {
                System.out.println("Enter the account number");

                System.out.print(":");

                recipientAccountNumber = READER.readLine();

                if(recipientAccountNumber.trim().equals(""))
                {
                    System.out.println("You can not enter the empty value");

                    if(iterator == 2)
                    {
                        return;
                    }
                    continue;
                }
                else if(!Pattern.matches("\\d{12}" , recipientAccountNumber))
                {
                    System.out.println("Please enter the 12 digit account Number");

                    if(iterator == 2)
                    {
                        return;
                    }
                    continue;
                }
                else
                {
                    break;
                }

            }
            //reading amount
            for(int iterator = 0 ; iterator < 3 ; iterator++)
            {
                System.out.println("Enter the amout the withdraw");

                System.out.print(":");

                amountString = READER.readLine();

                if(amountString.trim().equals(""))
                {
                    System.out.println("You can not enter the empty value");

                    if(iterator == 2)
                    {
                        return;
                    }
                    continue;
                }
                else if(!Pattern.matches("\\d*" , amountString))
                {
                    System.out.println("Please Enter the digit only");

                    if(iterator == 2)
                    {
                        return;
                    }
                    continue;
                }
                else  if(Pattern.matches("0" , amountString))
                {
                    System.out.println("You can not enter value 0");
                    if(iterator ==2)
                    {
                        return;
                    }
                    continue;
                }
                else
                {
                    break;
                }

            }

            amount = Integer.parseInt(amountString);

            System.out.println("hello world");

            JSONObject request = new JSONObject();

            request.put("token" , token);

            request.put("customerId" , customerId);

            request.put("amount" , amount);

            request.put("accountNumber" , recipientAccountNumber);

            connection.write("transfer");

            connection.write(request.toString());


            String  response = connection.read();



            if(response == null)
            {
                System.out.println("There is something is wrong with connection");
            }

            JSONObject responseObject = new JSONObject(response);

            if(responseObject.getString("user").equals("invalid"))
            {
                System.out.println("Please login again");

                customerId = "";

                token = null;

                return;
            }

            if(responseObject.getString("accountNumber").equals("invalid"))
            {
                System.out.println("Given account number is invalid");

                return;
            }

            int finalAmount = responseObject.getInt("finalAmount");

            int remeiningAmount = responseObject.getInt("remainingAmount");

            if(finalAmount == -1)
            {
                System.out.println("You can not transfer the amount more than available balance");

                return;
            }

            if(finalAmount == -2)
            {
                System.out.println("Given account Number can not receive the amount more than deposite limit so try transfer lover amount");

                return;
            }

            System.out.println(ConstantUtils.NEW_LINE + "Successfully transfered the " + amount + " in " + recipientAccountNumber + " account");

            System.out.println(ConstantUtils.NEW_LINE + "Now your account has  " + finalAmount + " amount");


        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }



    }

    public static void deposite()
    {
        String  amountString = "";

        int amount = 0;

        try(SocketConnection connection = new SocketConnection(PORT))
        {

            //reading amount
            for(int iterator = 0 ; iterator < 3 ; iterator++)
            {
                System.out.println("Enter the amount to deposite");

                System.out.print(":");

                amountString = READER.readLine();

                if(amountString.trim().equals(""))
                {
                    System.out.println("You can not enter the empty value");

                    if(iterator == 2)
                    {
                        return;
                    }
                    continue;
                }
                else if(!Pattern.matches("\\d*" , amountString))
                {
                    System.out.println("Please Enter the digit only");

                    if(iterator == 2)
                    {
                        return;
                    }
                    continue;
                }
                else  if(Pattern.matches("0" , amountString))
                {
                    System.out.println("You can not enter value 0");
                    if(iterator ==2)
                    {
                        return;
                    }
                    continue;
                }
                else
                {
                    break;
                }

            }

            amount = Integer.parseInt(amountString);

            JSONObject request = new JSONObject();

            request.put("token" , token);

            request.put("customerId" , customerId);

            request.put("amount" , amount);

            connection.write("deposite");

            connection.write(request.toString());

            String  response = connection.read();

            System.out.println("hello");

            if(response == null)
            {
                System.out.println("There is something is wrong with connection");
            }

            JSONObject responseObject = new JSONObject(response);

            if(responseObject.getString("user").equals("invalid"))
            {
                System.out.println("Please login again");

                customerId = "";

                token = null;

                return;
            }

            int finalAmount = responseObject.getInt("finalAmount");

            int remeiningAmount = responseObject.getInt("remainingAmount");

            if(finalAmount == -1)
            {
                System.out.println("You can not enter the amount more than limit of deposit in your account");

                return;
            }
            if(finalAmount == -2)
            {
                System.out.println("You can not maintain more amount than limit of deposite amount ");

                return;
            }

            System.out.println(ConstantUtils.NEW_LINE + "Successfully credited the " + amount + " in your account");

            System.out.println(ConstantUtils.NEW_LINE + "Now your account has  " + finalAmount + " amount");

        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }



    }


    // withdraw
    public static  void withdraw()
    {
        String  amountString = "";

        int amount = 0;

       try(SocketConnection connection = new SocketConnection(PORT))
       {
           //reading amount
           for(int iterator = 0 ; iterator < 3 ; iterator++)
           {
               System.out.println("Enter the amout the withdraw");

               System.out.print(":");

               amountString = READER.readLine();

               if(amountString.trim().equals(""))
               {
                   System.out.println("You can not enter the empty value");

                   if(iterator == 2)
                   {
                       return;
                   }
                   continue;
               }
               else if(!Pattern.matches("\\d*" , amountString))
               {
                   System.out.println("Please Enter the digit only");

                   if(iterator == 2)
                   {
                       return;
                   }
                   continue;
               }
               else  if(Pattern.matches("0" , amountString))
               {
                   System.out.println("You can not enter value 0");

                   if(iterator ==2)
                   {
                       return;
                   }
                   continue;
               }
               else
               {
                   break;
               }

           }



           amount = Integer.parseInt(amountString);

           JSONObject request = new JSONObject();

           request.put("token" , token);

           request.put("customerId" , customerId);

           request.put("amount" , amount);

           connection.write("withdraw");

           connection.write(request.toString());


           String  response = connection.read();



           if(response == null)
           {
               System.out.println("There is something is wrong with connection");
           }

           JSONObject responseObject = new JSONObject(response);

           if(responseObject.getString("user").equals("invalid"))
           {
               System.out.println("Please login again");

               customerId = "";

               token = null;

               return;
           }

           int finalAmount = responseObject.getInt("finalAmount");

           int remeiningAmount = responseObject.getInt("remainingAmount");

           if(finalAmount == -1)
           {
               System.out.println("You can not withdraw the amount more than available balance");

               return;
           }

           System.out.println(ConstantUtils.NEW_LINE + "Successfully debited the " + amount + " in your account");

           System.out.println(ConstantUtils.NEW_LINE + "Now your account has  " + finalAmount + " amount");


       }
       catch (Exception exception)
       {
           System.out.println(exception.getMessage());
       }

    }

    public  static void  checkBalance()
    {

        /*if(token == null || customerId.equals(""))
        {
            return;
        }

        try(SocketConnection connection = new SocketConnection(PORT))
        {
            connection.write("profile");

            JSONObject request = new JSONObject();

            request.put("customerId" , customerId);
            request.put("token" , token);

            connection.write(request.toString());

            String  response = connection.read();

            if(response == null)
            {
                System.out.println("There is something is wrong with connection");
            }

            JSONObject responseObject = new JSONObject(response);

            if(responseObject.getString("user").equals("invalid"))
            {
                System.out.println("Please login again");
                customerId = "";
                token = null;

                return;
            }

            String profile =  connection.read();
            if(profile != null)
            {

                System.out.println("There is something wrong with connection");
                return;
            }
            JSONObject profileObject = new JSONObject(profile);

            String   name = profileObject.getString("name");
            String   address = profileObject.getString("address");
            String   eMail = profileObject.getString("eMail");
            String   mobileNumber = profileObject.getString("mobileNumber");
            String   accountNumber = profileObject.getString("accountNumber");
            int    amount = profileObject.getInt("amount");

            System.out.println(ConstantUtils.NEW_LINE);

            System.out.println("Name : " + name);
            System.out.println("Address : " + address);
            System.out.println("EMail : " + eMail);
            System.out.println("Mobile Number : " + mobileNumber );
            System.out.println("Account Number : " + accountNumber);
            System.out.println("Balance : " + amount);



        }
        catch (Exception exception)
        {

            System.out.println(exception.getMessage());
        }*/

           if(token == null || customerId.equals(""))
        {
            return;
        }

        try(SocketConnection connection = new SocketConnection(PORT))
        {
            connection.write("checkbalance");

            JSONObject request = new JSONObject();

            request.put("customerId" , customerId);

            request.put("token" , token);

            connection.write(request.toString());

            String  response = connection.read();

            if(response == null)
            {
                System.out.println("There is something is wrong with connection");
            }

            JSONObject responseObject = new JSONObject(response);

            if(responseObject.getString("user").equals("invalid"))
            {
                System.out.println("Please login again");

                customerId = "";

                token = null;

                return;
            }

            int  amount = responseObject.getInt("amount");

            System.out.println(ConstantUtils.NEW_LINE + "Your account has " + amount + " amount");

        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }

    }

    public  static  void getAccountNumber()
    {
        if(token == null || customerId.equals(""))
        {
            return;
        }

        try(SocketConnection connection = new SocketConnection(PORT))
        {
            connection.write("accountno");

            JSONObject request = new JSONObject();

            request.put("customerId" , customerId);

            request.put("token" , token);

            connection.write(request.toString());

            String  response = connection.read();

            if(response == null)
            {
                System.out.println("There is something is wrong with connection");
            }

            JSONObject responseObject = new JSONObject(response);

            if(responseObject.getString("user").equals("invalid"))
            {
                System.out.println("Please login again");

                customerId = "";

                token = null;

                return;
            }

            String   accountNumber = responseObject.getString("accountNumber");

            System.out.println(ConstantUtils.NEW_LINE + "Your account Number is  " + accountNumber + " amount");

        }
        catch (Exception exception)
        {

            System.out.println(exception.getMessage());
        }
    }

    public static void signUp() {
        try(SocketConnection connection = new SocketConnection(PORT))
        {
            String  name = "";

            String  eMailAddress = "";

            String  address = "";

            String  MobileNo = "";

            String  accountType = "";

            String  password = "";

            String customerId = "";

            String  accountNumber = "";

           // System.out.println(System.console().readPassword());

            //reading name
            for(int iterator  = 0 ; iterator < 3 ; iterator++)
            {
                System.out.println(ConstantUtils.NEW_LINE + "Please Enter your Name");

                System.out.print(":");

                name = READER.readLine();

                if(name.equals("") || name.trim().equals(""))
                {
                    System.out.println(ConstantUtils.NEW_LINE + "you can not enter empty name" + ConstantUtils.NEW_LINE);

                    if(iterator == 2)
                    {
                        return;
                    }

                    continue;
                }
                else if(!Pattern.matches("[a-zA-Z]*" , name))
                {
                    System.out.println("Please enter valid name with characters only and with no space");

                    if(iterator == 2)
                    {
                        return;
                    }
                    continue;
                }
                {
                    break;
                }

            }

            //reading email address
            for(int iterator  = 0 ; iterator < 3 ; iterator++)
            {

                System.out.println(ConstantUtils.NEW_LINE + "Now enter your eMailAdress");

                System.out.print(":");

                eMailAddress = READER.readLine();

                if (!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", eMailAddress))
                {
                    System.out.printf(ConstantUtils.NEW_LINE + "Please Enter valid E mail address");

                    if(iterator == 2)
                    {
                        return;
                    }
                    continue;

                }
                else
                {
                    break;
                }
            }

            //reading address
            for(int iterator = 0 ; iterator < 3 ; iterator++)
            {

                System.out.println("Please Enter the  adress");

                System.out.print(":");

                address = READER.readLine();

                if(address.trim().equals(""))
                {
                    System.out.println("Please enter the valid adress ");

                    if(iterator == 2)
                    {
                        return;
                    }
                    continue;
                }
                else
                {

                    break;
                }
            }

            //movile no.
            for(int iterator = 0 ; iterator < 3 ; iterator++)
            {
                System.out.printf("Please Enter the MovileNo.");

                System.out.print(":");

                MobileNo = READER.readLine();

                if(!Pattern.matches("\\d{10}" , MobileNo))
                {
                    System.out.println("you can enter only 10 digits ");

                    if(iterator ==2)
                    {
                        return;
                    }

                    continue;
                }
                else
                {
                    break;
                }
            }

            //account type
            for(int iterator = 0 ; iterator < 3 ; iterator++)
            {
                System.out.println("choose your account type" + ConstantUtils.NEW_LINE +
                        "1 : Saving" + ConstantUtils.NEW_LINE +
                        "2 : Current");

                System.out.print(":");

                accountType =  READER.readLine();

                if(accountType.equals("1") || accountType.equals("2"))
                {
                    if(accountType.equals("1"))
                    {
                        accountType = "saving";
                    }
                    else
                    {
                        accountType = "current";
                    }
                    break;
                }
                else
                {
                    System.out.println("Enter the valid options");

                    if(iterator == 2)
                    {
                        return;
                    }
                }
            }

            //reading passwornd
            for(int iterator = 0 ; iterator < 3 ; iterator++)
            {
                System.out.println("Please Enter your password");

                password = READER.readLine();


                if(password.equals("") || password.trim().equals("") || password.length() < 8)
                {
                    System.out.println("you cannot enter the empty password and password should be 8 characters");

                    if(iterator ==2)
                    {
                        return;
                    }
                }
                else
                {
                    System.out.println(password +"<--");

                    break;
                }
            }


            JSONObject request = new JSONObject();

            request.put("name" , name);

            request.put("address" , address);

            request.put("email" , eMailAddress);

            request.put("mobileNumber" , MobileNo);

            request.put("accountType" , accountType);

            request.put("password" , password);

            //JSONObject.valueToString(request);

            connection.write("signup");

            connection.write(request.toString());

            String response = connection.read();

            if(response.equals(""))
            {
                System.out.println("Something went wrong please try again");
                
                return;
            }
            JSONObject responseObject = new JSONObject(response);

            if(responseObject.getString("input").equals("invalid") )
            {
                System.out.println("Input is wrong try it again");

                return;
            }
            if(!responseObject.getBoolean("accountCreated"))
            {
                System.out.printf("Account is not created , there is some internal error , Please try it again");

                return;
            }
              customerId = responseObject.getString("customerId");

              accountNumber = responseObject.getString("accountNumber");


            System.out.println("Your account has been successfully created ");

            System.out.println(ConstantUtils.NEW_LINE + "Your customer Id :" + customerId);

            System.out.println("Your account number :" + accountNumber);

            System.out.println(ConstantUtils.NEW_LINE);




        }
        catch (Exception exception)
        {

            System.out.println("exception generated");

            System.out.println(exception.getMessage());
        }
    }

    public static void signIn() throws Exception {

        try(SocketConnection connection = new SocketConnection(PORT))
        {


            String customerId = "";

            String passsword = "";

            String clientIdTemp = "";


            //reading customer id
            for(int iterator = 0 ; iterator < 3 ; iterator++)
            {

                System.out.println(ConstantUtils.NEW_LINE + "Please enter the customer id");

                System.out.print(":");

                customerId = READER.readLine();

                if(customerId.trim().equals(""))
                {
                    System.out.println("You can not enter the empty customer Id");

                    if(iterator == 2)
                    {
                        return;
                    }
                    continue;
                }
                else  if(!Pattern.matches("\\d{6}" ,customerId ))
                {
                    System.out.println("You can enter only 6 digit in customer Id");

                    if(iterator == 2)
                    {
                        return;
                    }
                    continue;
                }
                else
                {
                    break;
                }


            }
            //reading password
            for(int iterator = 0 ; iterator < 3 ; iterator++)
            {

                System.out.println("Enter the password");

                System.out.print(":");

                passsword = READER.readLine();

                if(passsword.trim().equals(""))
                {
                    System.out.println("You can not enter the empty password");

                    if(iterator ==2)
                    {
                        return;

                    }
                    continue;
                }
                else if(passsword.length() < 8 )
                {
                    System.out.println("password must be of size 8 or above");
                    if(iterator == 2)
                    {
                        return;
                    }
                    continue;
                }
                else
                {
                    break;
                }
            }

            connection.write("signin");

            JSONObject request  = new JSONObject();

            request.put("customerId" , customerId);

            request.put("password" , passsword);

            connection.write(request.toString());

            String response = connection.read();

            if(response == null)
            {
                System.out.println("there is something wrong with communication");

                return;
            }

            JSONObject responseObject  = new JSONObject(response);

            if(responseObject.getString("account").equals("invalid"))
            {
                System.out.println( ConstantUtils.NEW_LINE + "Given customer id is invalid");

                return;
            }

            if(responseObject.getString("password").equals("invalid"))
            {
                System.out.println(ConstantUtils.NEW_LINE + "Your password is invalid");

                return;
            }

            String token = responseObject.getString("token");
            
            client.token = token;

            client.customerId = customerId;

            System.out.println(ConstantUtils.NEW_LINE  + "Now you have successfully loged in ");

        }
        catch (Exception exception)
        {
            System.out.println(exception);
        }

    }



}

class SocketConnection implements  AutoCloseable
{

    Socket socket ;
    DataInputStream connectionInputStream ;
    DataOutputStream connectionOutputStream;

    SocketConnection(int port) throws IOException {

       socket = new Socket("localhost" , port);

       connectionInputStream = new DataInputStream(socket.getInputStream());

       connectionOutputStream = new DataOutputStream(socket.getOutputStream());

    }

    public String  read() throws IOException
    {

            String  data = connectionInputStream.readUTF();

            return  data;

    }

    public  void write(String  message) throws IOException
    {
        connectionOutputStream.writeUTF(message);
    }

    public void writeInt(int i) throws IOException
    {
        connectionOutputStream.writeInt(i);
    }

    public  int readInt() throws IOException
    {
        return connectionInputStream.readInt();
    }


    @Override
    public void close() throws Exception
    {

        connectionInputStream.close();

        connectionOutputStream.close();

        socket.close();
    }
}



