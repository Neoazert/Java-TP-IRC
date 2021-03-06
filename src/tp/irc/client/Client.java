/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.client;

import ihm.FXMLDocumentController;
import ihm.IHM;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private PrintWriter out;
    private BufferedReader in;

    Client(String ipAdress, Integer ServPort) {
        this.port = ServPort;
        this.address = ipAdress;
        

        try {

            Client.write("Demande de connexion");
            socket = new Socket(address, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            Client.write("Connexion établie avec le serveur, authentification ");

            ClientReceive clientReceive = new ClientReceive(this, in);
            ClientSend clientSend = new ClientSend(out);
            
            
            Thread threadReceive = new Thread(clientReceive);
            Thread threadSend = new Thread(clientSend);
            
            
            
            if(IHM.isModeGraphique){
                threadReceive.start();
                FXMLDocumentController.initConnection(out, in,this);
            }
                
            else{
                threadReceive.start();
                threadSend.start();
            }

        } catch (UnknownHostException e) {
            Client.write("Impossible de se connecter à l'adresse " + address + "\n" + e.getMessage());
        } catch (IOException e) {
            Client.write("Aucun serveur à l'écoute du port " + port + "\n" + e.getMessage());
        } catch (Exception e) {
            Client.write("Fatal Error: " + e.getMessage());
        }

    }

    public void disconnectedServer() {
        try {
            in.close();
            out.close();
            socket.close();
            System.exit(0);
        } catch (IOException ex) {
            Client.write("Error while disconnecting server :" + ex.getMessage());
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

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }
    
    public static void write(String m){
         if(IHM.isModeGraphique)
             
             IHM.controller.FXtextRecived.setText(IHM.controller.FXtextRecived.getText() + "\n" + m);
         else
             System.out.println(m);
    }
    

}
