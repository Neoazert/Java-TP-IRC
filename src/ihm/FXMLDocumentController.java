/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import tp.irc.client.Client;

/**
 *
 * @author p1502985
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    public TextArea FXmessage;

    @FXML
    public TextArea FXtextRecived;

    static private PrintWriter out;
    static private BufferedReader in;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        String message = FXmessage.getText();
        if (message != null && !message.equals("")) {
            out.println(message);
            out.flush();
            FXmessage.setText("");
        }
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    static public void initConnection(PrintWriter out, BufferedReader in, Client client) {
        FXMLDocumentController.out = out;
        FXMLDocumentController.in = in;

        GraphicClientReceive graphicClientReceive;
        graphicClientReceive = new GraphicClientReceive(client, in);

        Thread threadReceive = new Thread(graphicClientReceive);

        threadReceive.start();
        

    }

    public static class GraphicClientReceive implements Runnable {

        private BufferedReader in;
        private Client client;

        public GraphicClientReceive(Client client, BufferedReader bufferedReader) {
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
                        IHM.controller.FXtextRecived.setText(IHM.controller.FXtextRecived.getText() + "\nMessage reçu : " + message);
                        
                    } else {
                        isActive = false;
                    }

                } catch (IOException ex) {
                    System.err.println("Erreur");
                }
            }
            client.disconnectedServer();
        }
    }

}
