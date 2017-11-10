/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.client;

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

            System.out.println("Demande de connexion");
            socket = new Socket(address, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            System.out.println("Connexion établie avec le serveur, authentification :");

            ClientReceive clientReceive = new ClientReceive(this, in);
            ClientSend clientSend = new ClientSend(out);

            clientReceive.run();
            clientSend.run();

        } catch (UnknownHostException e) {
            System.out.println("Impossible de se connecter à l'adresse " + address + "\n" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Aucun serveur à l'écoute du port " + port + "\n" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Fatal Error: " + e.getMessage());
        }

    }

    public void disconnectedServer() {
        try {
            in.close();
            out.close();
            socket.close();
            System.exit(0);
        } catch (IOException ex) {
            System.out.println("Error while disconnecting server :" + ex.getMessage());
        }
    }

}
