/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.serveur;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author p1502985
 */
public class MainServer {

    /**
     * creates a new server
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            printUsage();
        } else {
            try {
                Integer port = new Integer(args[0]);
                Server server = new Server(port);
            } catch (IOException ex) {
                Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void printUsage() {
        System.out.println("java server.Server <port>");
        System.out.println("\t<port>: server's port");
    }

}
