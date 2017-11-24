/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author p1502985
 */
public class Connection implements Runnable {
    private ServerSocket serverSocket;
    private Server server;
    private int nbClientsConnectes;
    
    Connection(Server server) throws IOException{
        this.server = server;
        this.serverSocket = new ServerSocket(server.getPort());
        this.nbClientsConnectes = server.getClients().size();
    }
    
    @Override
    public void run(){
        if(this.nbClientsConnectes != this.server.getClients().size())
        {
            try {
                Socket sockNewClient = serverSocket.accept();
                ConnectedClient newClient = new ConnectedClient(server, sockNewClient);
                server.addClient(newClient);
                Thread threadNewClient = new Thread(newClient);
                threadNewClient.start();
            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.run();
    }
}
