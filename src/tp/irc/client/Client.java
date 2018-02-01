/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.client;

import ihm.RootLayoutController;
import ihm.model.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

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
    private RootLayoutController rootController;
    private String login;
    
    public String getLogin()
    {
        return this.login;
    }

    public RootLayoutController getRootController() {
        return rootController;
    }

    public void setRootController(RootLayoutController rootController) {
        this.rootController = rootController;
    }

    public Client(String ipAdress, Integer ServPort, RootLayoutController rootController, String login) {
        this.port = ServPort;
        this.address = ipAdress;
        this.rootController = rootController;
        this.login = login;
        
        try {
            System.out.println("Demande de connexion");
            socket = new Socket(address, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connexion établie avec le serveur, authentification");

            ClientReceive clientReceive = new ClientReceive(this, in);
            ClientSend clientSend = new ClientSend(out);
            
            
            Thread threadReceive = new Thread(clientReceive);
            Thread threadSend = new Thread(clientSend);
            
            threadReceive.start();
            threadSend.start();
            
            //Lors de la création d'un client, on envoit un message au server pour que le ConnectedClient sache quel est le login du client qui lui est lié
            Message messageIdentification = new Message(login, null, "", true);
            out.writeObject(messageIdentification);
            out.flush();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
    

}
