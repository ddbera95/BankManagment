package org.example.BankManagmentSystem.Bank.DB;

import java.net.Socket;
import java.util.HashMap;


///not used
public class ConnectionList
{

    HashMap<String , Socket> connections = new HashMap<>();

    public boolean containId(String id)
    {
        if(connections.get(id) != null )
        {
            if(connections.get(id).isClosed())
            {
                return true;
            }
        }

       return false;
    }

    public void add(String customerId , Socket socket)
    {
        connections.put(customerId , socket);
    }

    public  Socket getSocket(String customerId)
    {
        return connections.get(customerId);
    }


}
