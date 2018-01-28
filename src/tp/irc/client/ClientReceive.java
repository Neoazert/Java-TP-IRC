/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.client;

import ihm.FXMLDocumentController;
import ihm.IHMConnexion;
import ihm.PrincipalViewController;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author p1502985
 */
public class ClientReceive implements Runnable {

    private BufferedReader in;
    private Client client;

    public ClientReceive(Client client, BufferedReader bufferedReader) {
        this.client = client;
        in = bufferedReader;
    }

    @Override
    public void run() {

        boolean isActive = true;
        String message = null;
        
        while (isActive) {
            try {
                
                message = in.readLine();
                
                if (message != null) {
                    //System.out.println(message);
                    client.getController().receiveMessage(message);
                } else {
                    isActive = false;
                }
                
            } catch (IOException ex) {
                Client.write("Erreur while Reciving message : " + ex.getMessage());
            }
        }
        client.disconnectedServer();
    }

}
