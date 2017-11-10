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
    }
    
    public void addClient(ConnectedClient client){
        
    }
    
    public void broadcastMessage(String s, int i){
        
    }
    
    public void disconnectedClient(ConnectedClient client){
        
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
