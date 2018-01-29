/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.serveur;

import ihm.model.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        System.out.println("Lancement du serveur sur le port: " + port);
        Thread threadConnection = new Thread(new Connection(this));
        
        threadConnection.start();
        
        
        
        while(threadConnection.isAlive()){
            try {
                System.out.println("Serveur en cour d'execution ...");
                TimeUnit.SECONDS.sleep(10);   
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        
        
        System.out.println("Serveur shutdown");
    }
    
    public void addClient(ConnectedClient client){
        /*for(ConnectedClient ceClient : clients){
            ceClient.sendMessage("Le client " + client.getId() + " vient de se connecter");
        }*/
        this.clients.add(client);
        
    }
    
    public void broadcastMessage(Message message){
        if(message.getLoginRecipient() == null)
        {
            for(ConnectedClient client : clients)
            {
                if(!client.getLogin().equals(message.getLoginSender()))
                { 
                    client.sendMessage(message);
                }
            }
        }
        else{
            for(ConnectedClient client : clients)
            {
                if(client.getLogin().equals(message.getLoginRecipient()))
                { 
                    client.sendMessage(message);
                }
            }
        }
        
    }
    
    public void disconnectedClient(ConnectedClient client){
        client.closeClient();
        this.clients.remove(client);
        for(ConnectedClient ceClient : clients)
        {
            Message message = new Message(null, null, "Le client " + client.getLogin() + " nous a quitté", false);
            client.sendMessage(message);
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
