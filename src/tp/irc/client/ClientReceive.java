/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.client;

import ihm.FXMLDocumentController;
import ihm.IHMConnexion;
import ihm.PrincipalViewController;
import ihm.model.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author p1502985
 */
public class ClientReceive implements Runnable {

    private ObjectInputStream in;
    private Client client;

    public ClientReceive(Client client, ObjectInputStream bufferedReader) {
        this.client = client;
        in = bufferedReader;
    }

    @Override
    public void run() {

        boolean isActive = true;
        Message message = null;
        while (isActive) {
            try {
                Object obj = in.readObject();
                message = (Message)obj;
                if (message != null) {
                    //System.out.println(message);
                    client.getRootController().receiveMessage(message);
                } else {
                    isActive = false;
                }
                
            } catch (IOException ex) {
                ex.printStackTrace();
                //Client.write("Erreur while Reciving message : " + ex.getMessage());
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        client.disconnectedServer();
    }

}
