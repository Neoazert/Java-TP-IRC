/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.serveur;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author p1502985
 */
public class ConnectedClient implements Runnable{
    private int idCounter;
    private int id;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Server server;
    
    ConnectedClient(Server server, Socket socket){
        
    }
    
    public void sendMessage(String message){
        
    }
    
    @Override
    public void run(){
        
    }
    
    public int getId(){
        return id;
    }
    
    public void closeClient(){
        
    }
}
