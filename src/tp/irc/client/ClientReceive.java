/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.client;

import ihm.model.Message;
import java.io.IOException;
import java.io.ObjectInputStream;

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
                    client.getRootController().receiveMessage(message);
                } else {
                    isActive = false;
                }
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        client.disconnectedServer();
    }

}
