/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java.tp.irc.serveur;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author p1502985
 */
public class Server {
    private int port;
    private List<ConnectedClient> ConnectedClients = new ArrayList<>();
    
    Server(int port){
        this.port = port;
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
}
