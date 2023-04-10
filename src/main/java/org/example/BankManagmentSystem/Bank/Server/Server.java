package org.example.BankManagmentSystem.Bank.Server;

import org.example.BankManagmentSystem.Bank.Bank;
import org.example.BankManagmentSystem.Bank.DB.ConnectionList;
import org.example.BankManagmentSystem.Bank.DB.TokenContainer;
import org.example.BankManagmentSystem.Bank.Utils.ConstantUtils;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends  Thread
{
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    TokenContainer tokenContainer = new TokenContainer();
    ConnectionList connectionList =  new ConnectionList();



    Bank bank;

    public Server(Bank bank)
    {
        this.bank = bank;
    }


    public void run()
    {

        try(ServerSocket socket = new ServerSocket(bank.getPort()))
        {

            while (true)
            {
                Socket connection =  socket.accept();

                System.out.println(connection.getRemoteSocketAddress() + " is connected");

                executorService.execute(()->{

                    try(DataOutputStream connectionOutputStream = new DataOutputStream(connection.getOutputStream());
                            DataInputStream connectionInputStream = new DataInputStream(connection.getInputStream());
                        )
                    {

                        String command = connectionInputStream.readUTF();

                        switch (command)
                        {
                            case "signup":
                            {

                                    String request = connectionInputStream.readUTF();

                                JSONObject requestObject = new JSONObject(request);

                                JSONObject responseObject = new JSONObject();

                                String  signUpName =  requestObject.getString("name");

                                String signUpAddress = (String) requestObject.getString("address");

                                String signUpEMailAddress =  (String) requestObject.getString("email");

                                String signUpMobileNumber =  (String) requestObject.getString("mobileNumber");

                                String signUpPassword =  (String) requestObject.getString("password");

                                String signUpAccountType =  (String) requestObject.getString("accountType");

                                if(signUpName.equals(ConstantUtils.EMPTY_STRING) || signUpEMailAddress.equals(ConstantUtils.EMPTY_STRING) || signUpAddress.equals(ConstantUtils.EMPTY_STRING)
                                   || signUpAccountType.equals(ConstantUtils.EMPTY_STRING) || signUpPassword.equals(ConstantUtils.EMPTY_STRING) || signUpMobileNumber.equals(ConstantUtils.EMPTY_STRING))
                                {
                                    responseObject.put("input" , "invlid");

                                    connectionOutputStream.writeUTF(requestObject.toString());

                                    return;
                                }

                                String signUpcustomerId = "";

                                String signUpAccountNumber = "";

                                responseObject.put("input" , "valid");

                                if (signUpAccountType.equals("saving"))
                                {

                                    signUpcustomerId = bank.signUp(signUpName, signUpAddress, signUpMobileNumber, signUpEMailAddress, signUpPassword, "saving");
                                } else {
                                    signUpcustomerId = bank.signUp(signUpName, signUpAddress, signUpMobileNumber, signUpEMailAddress, signUpPassword, "current");
                                }

                                if (signUpcustomerId.trim().equals(""))
                                {
                                    responseObject.put("accountCreated" ,false);

                                    connectionOutputStream.writeUTF(responseObject.toString());

                                    return;
                                } else
                                {
                                    responseObject.put("accountCreated" , true);
                                    //connectionOutputStream.writeUTF("success");

                                    responseObject.put("customerId" , signUpcustomerId);
                                    //connectionOutputStream.writeUTF(signUpcustomerId);

                                    signUpAccountNumber = bank.getAccountNumber(signUpcustomerId);

                                    System.out.println(signUpAccountNumber);

                                    responseObject.put("accountNumber" , bank.getAccountNumber(signUpcustomerId));
                                    //connectionOutputStream.writeUTF(bank.getAccountNumber(signUpcustomerId));

                                    connectionOutputStream.writeUTF(responseObject.toString());


                                    System.out.println("hello");
                                }





                            }
                                break;
                            case "signin":
                            {
                                String  request = connectionInputStream.readUTF();

                                JSONObject requestObject = new JSONObject(request);

                                JSONObject responseObject = new JSONObject();

                                String  customerId = requestObject.getString("customerId");

                                String  password = requestObject.getString("password");

                                if(!(bank.getAccountNumber(customerId) != null))
                                {
                                    responseObject.put("account" , "invalid");

                                    connectionOutputStream.writeUTF(responseObject.toString());

                                    return;
                                }

                                responseObject.put("account" , "valid");

                                if(!bank.signIn(customerId , password))
                                {
                                    responseObject.put("password" , "invalid");

                                    connectionOutputStream.writeUTF(responseObject.toString());

                                    return;
                                }

                                responseObject.put("password" , "valid");

                                String token =   tokenContainer.addToken(customerId);

                                responseObject.put("token" , token);

                                responseObject.put("customerId" , customerId);

                                connectionOutputStream.writeUTF(responseObject.toString());


                            }
                                break;

                            case  "withdraw":

                            {
                                String request = connectionInputStream.readUTF();

                                JSONObject requestObject = new JSONObject(request);

                                JSONObject responseObject = new JSONObject();

                                String  token = requestObject.getString("token");
                                String customerId = requestObject.getString("customerId");

                                if(!tokenContainer.isTokenValid(customerId , token))
                                {
                                    responseObject.put("user" , "invalid");

                                    connectionOutputStream.writeUTF(responseObject.toString());

                                    return;
                                }

                                responseObject.put("user" , "valid");

                                int amount = requestObject.getInt("amount");


                                int finalAmount =  bank.withdraw(amount , customerId);

                                int remainingAmount = bank.getAmount(customerId);

                                responseObject.put("finalAmount" , finalAmount);

                                responseObject.put("remainingAmount" , remainingAmount);

                                connectionOutputStream.writeUTF(responseObject.toString());

                            }
                                break;
                            case  "deposite":
                            {
                                String request = connectionInputStream.readUTF();

                                JSONObject requestObject = new JSONObject(request);

                                JSONObject responseObject = new JSONObject();

                                String  token = requestObject.getString("token");

                                String customerId = requestObject.getString("customerId");

                                if(!tokenContainer.isTokenValid(customerId , token))
                                {
                                    responseObject.put("user" , "invalid");

                                    connectionOutputStream.writeUTF(responseObject.toString());

                                    return;
                                }

                                responseObject.put("user" , "valid");

                                int amount = requestObject.getInt("amount");


                                int finalAmount =  bank.deposite(amount , customerId);

                                int remainingAmount = bank.getAmount(customerId);

                                responseObject.put("finalAmount" , finalAmount);

                                responseObject.put("remainingAmount" , remainingAmount);

                                connectionOutputStream.writeUTF(responseObject.toString());




                            }

                                break;
                            case "transfer":
                            {
                                String request = connectionInputStream.readUTF();

                                JSONObject requestObject = new JSONObject(request);

                                JSONObject responseObject = new JSONObject();

                                String  token = requestObject.getString("token");

                                String customerId = requestObject.getString("customerId");

                                if(!tokenContainer.isTokenValid(customerId , token))
                                {
                                    responseObject.put("user" , "invalid");

                                    connectionOutputStream.writeUTF(responseObject.toString());

                                    return;
                                }

                                responseObject.put("user" , "valid");

                                int amount = requestObject.getInt("amount");

                                String  recipientAccountNumber = requestObject.getString("accountNumber");

                                if(!bank.checkAccount(recipientAccountNumber))
                                {
                                    responseObject.put("accountNumber" , "invalid");

                                    connectionOutputStream.writeUTF(responseObject.toString());

                                    return;
                                }

                                responseObject.put("accountNumber" , "valid");


                                int finalAmount =  bank.transfer(amount , customerId , recipientAccountNumber);

                                int remainingAmount = bank.getAmount(customerId);

                                responseObject.put("finalAmount" , finalAmount);

                                responseObject.put("remainingAmount" , remainingAmount);

                                connectionOutputStream.writeUTF(responseObject.toString());
                            }
                                break;
                            case "accountno":
                            {
                                String request = connectionInputStream.readUTF();

                                JSONObject requestObject = new JSONObject(request);

                                JSONObject responseObject = new JSONObject();

                                String  token = requestObject.getString("token");

                                String customerId = requestObject.getString("customerId");

                                if(!tokenContainer.isTokenValid(customerId , token))
                                {
                                    responseObject.put("user" , "invalid");

                                    connectionOutputStream.writeUTF(responseObject.toString());

                                    return;
                                }

                                responseObject.put("user" , "valid");

                                responseObject.put("accountNumber" , bank.getAccountNumber(customerId));

                                connectionOutputStream.writeUTF(responseObject.toString());
                            }

                                break;
                            case  "profile":
                                    {
                                        String request = connectionInputStream.readUTF();

                                        JSONObject requestObject = new JSONObject(request);

                                        JSONObject responseObject = new JSONObject();

                                        String  token = requestObject.getString("token");

                                        String customerId = requestObject.getString("customerId");

                                        if(!tokenContainer.isTokenValid(customerId , token))
                                        {
                                            responseObject.put("user" , "invalid");

                                            connectionOutputStream.writeUTF(responseObject.toString());

                                            return;
                                        }

                                        responseObject.put("user" , "valid");

                                        connectionOutputStream.writeUTF(responseObject.toString());

                                        String  profile = bank.getProfile(customerId);

                                        connectionOutputStream.writeUTF(profile);

                                    }
                                    break;
                            case  "checkbalance":

                            {

                                String request = connectionInputStream.readUTF();

                                JSONObject requestObject = new JSONObject(request);

                                JSONObject responseObject = new JSONObject();

                                String  token = requestObject.getString("token");

                                String customerId = requestObject.getString("customerId");

                                if(!tokenContainer.isTokenValid(customerId , token))
                                {
                                    responseObject.put("user" , "invalid");

                                    connectionOutputStream.writeUTF(responseObject.toString());

                                    return;
                                }

                                responseObject.put("user" , "valid");

                                responseObject.put("amount" , bank .getAmount(customerId));

                                connectionOutputStream.writeUTF(responseObject.toString());

                            }

                                break;

                            default:
                                bank.Optional();
                                break;
                        }



                    }
                    catch (Exception exception)
                    {

                        System.out.println(exception.getMessage());

                        System.out.printf(connection.getInetAddress().getHostName() + " is disconnected with server ");
                    }


                });

            }


        }
        catch (Exception exception)
        {
            System.out.println("server is shutting down ...");
        }




    }
}
