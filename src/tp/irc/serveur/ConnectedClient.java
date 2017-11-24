/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author p1502985
 */
public class ConnectedClient implements Runnable{
    private static int idCounter;
    private int id;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Server server;
    
    ConnectedClient(Server server, Socket socket){
        try {
            this.server = server;
            this.socket = socket;
            this.id = this.idCounter;
            this.idCounter++;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream());
            System.out.println("Nouvelle connexion, id = " + id);
        } catch (IOException ex) {
            Logger.getLogger(ConnectedClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendMessage(String message){
        this.out.println(message);
        this.out.flush();
    }
    
    @Override
    public void run(){
        boolean isActive = true;
        while(isActive)
        {
            try {
                String message = in.readLine();
                if(message != null)
                {
                    server.broadcastMessage(message, id);
                }
                else{
                    isActive = false;
                }
            } catch (IOException ex) {
                isActive = false;
                Logger.getLogger(ConnectedClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        server.disconnectedClient(this);
    }
    
    public int getId(){
        return id;
    }
    
    public void closeClient(){
        try {
            this.in.close();
            this.out.close();
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ConnectedClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
