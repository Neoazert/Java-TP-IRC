/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.serveur;

import ihm.model.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Server server;
    private String login;
    
    ConnectedClient(Server server, Socket socket){
        try {
            this.server = server;
            this.socket = socket;
            this.id = this.idCounter;
            this.idCounter++;
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Nouvelle connexion, id = " + id);
        } catch (IOException ex) {
            Logger.getLogger(ConnectedClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendMessage(Message message){
        try{
            this.out.writeObject(message);
            this.out.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public String getLogin()
    {
        return login;
    }
    
    
    
    @Override
    public void run(){
        boolean isActive = true;
        while(isActive)
        {
            try {
                System.out.println("CONNECTED CLIENT");
                Object obj = in.readObject();
                Message message = (Message) obj;
                if(message.isIdentification())
                {
                    this.login = message.getLoginSender();
                }
                else{
                    if(message != null)
                    {
                        server.broadcastMessage(message);
                    }
                    else{
                        isActive = false;
                    }
                }
            } catch (IOException ex) {
                isActive = false;
                Logger.getLogger(ConnectedClient.class.getName()).log(Level.SEVERE, null, ex);
            }catch(ClassNotFoundException e){
                e.printStackTrace();
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
