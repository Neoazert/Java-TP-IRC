/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.client;

import com.sun.corba.se.pept.encoding.OutputObject;
import ihm.FXMLDocumentController;
//import ihm.IHM;
import ihm.PrincipalViewController;
import ihm.model.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author p1502985
 */
public class Client {

    private int port;
    private String address;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private PrincipalViewController controller;
    private String login;
    
    public String getLogin()
    {
        return this.login;
    }
    
    public void setController(PrincipalViewController controller)
    {
        this.controller = controller;
    }
    
    public PrincipalViewController getController()
    {
        return this.controller;
    }

    public Client(String ipAdress, Integer ServPort, PrincipalViewController controller, String login) {
        this.port = ServPort;
        this.address = ipAdress;
        this.controller = controller;
        this.login = login;
        
        try {

            //Client.write("Demande de connexion");
            System.out.println("Demande de connexion");
            socket = new Socket(address, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connexion établie avec le serveur, authentification");
            //Client.write("Connexion établie avec le serveur, authentification ");

            ClientReceive clientReceive = new ClientReceive(this, in);
            ClientSend clientSend = new ClientSend(out);
            
            
            Thread threadReceive = new Thread(clientReceive);
            Thread threadSend = new Thread(clientSend);
            
            threadReceive.start();
            threadSend.start();
            
            Message messageIdentification = new Message(login, null, "", true);
            out.writeObject(messageIdentification);
            out.flush();
            
            /*if(IHM.isModeGraphique){
                threadReceive.start();
                FXMLDocumentController.initConnection(out, in,this);
            }
                
            else{
                threadReceive.start();
                threadSend.start();
            }*/

        } catch (UnknownHostException e) {
            e.printStackTrace();
           // Client.write("Impossible de se connecter à l'adresse " + address + "\n" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            //Client.write("Aucun serveur à l'écoute du port " + port + "\n" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            //Client.write("Fatal Error: " + e.getMessage());
        }

    }

    public void disconnectedServer() {
        try {
            in.close();
            out.close();
            socket.close();
            System.exit(0);
        } catch (IOException ex) {
            ex.printStackTrace();
            //Client.write("Error while disconnecting server :" + ex.getMessage());
        }
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }
    
    /*public static void write(String m){
         if(IHMC.isModeGraphique)
             
             IHM.controller.FXtextRecived.setText(IHM.controller.FXtextRecived.getText() + "\n" + m);
         else
             System.out.println(m);
    }*/
    

}
