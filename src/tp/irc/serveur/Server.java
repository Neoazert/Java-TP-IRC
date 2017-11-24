/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.serveur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author p1502985
 */
public class Server {
    private int port;
    private ArrayList<ConnectedClient> clients;
    
    Server(int port) throws IOException{
        this.port = port;
        this.clients = new ArrayList<ConnectedClient>();
        Thread threadConnection = new Thread(new Connection(this));
        threadConnection.start();
    }
    
    public void addClient(ConnectedClient client){
        for(ConnectedClient ceClient : clients){
            ceClient.sendMessage("Le client " + client.getId() + " vient de se connecter");
        }
        this.clients.add(client);
    }
    
    public void broadcastMessage(String message, int id){
        for(ConnectedClient client : clients)
        {
            if(client.getId() != id)
            {
                client.sendMessage("Message de " + id + " : " + message);
            }
        }
    }
    
    public void disconnectedClient(ConnectedClient client){
        client.closeClient();
        this.clients.remove(client);
        for(ConnectedClient ceClient : clients)
        {
            client.sendMessage("Le client " + client.getId() + " nous a quitté");
        }
    }
    
    public int getPort(){
        return port;
    }

    public ArrayList<ConnectedClient> getClients() {
        return clients;
    }

    public void setClients(ArrayList<ConnectedClient> clients) {
        this.clients = clients;
    }
    
    
}
