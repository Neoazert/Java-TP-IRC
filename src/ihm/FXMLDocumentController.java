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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import tp.irc.client.Client;
import tp.irc.serveur.ConnectedClient;
import tp.irc.serveur.Server;

/**
 *
 * @author p1502985
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    public TextArea FXmessage;
    @FXML
    public TextArea FXtextRecived;
    @FXML
    public TreeView <String> FXserverTree;
    TreeItem<String> rootItem;
    

    static private PrintWriter out;
    static private BufferedReader in;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        String message = FXmessage.getText();
        if (message != null && !message.equals("")) {
            out.println(message);
            IHM.controller.FXtextRecived.setText(IHM.controller.FXtextRecived.getText() + "\nMoi : " + message);
            out.flush();
            FXmessage.setText("");
        }
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    static public void initConnection(PrintWriter out, BufferedReader in, Client client) {
        
        IHM.controller.rootItem = new TreeItem<String> (client.getAddress() +":" + client.getPort());
        IHM.controller.rootItem.setExpanded(true);
        TreeItem<String> item = new TreeItem<String> ("Moi");
        IHM.controller.rootItem.getChildren().add(item);
        IHM.controller.FXserverTree.setRoot(IHM.controller.rootItem);

         
        //IHM.controller.refreshServerTree();
             
        FXMLDocumentController.out = out;
        FXMLDocumentController.in = in;
        

        GraphicClientReceive graphicClientReceive = new GraphicClientReceive(client, in);;
        //UpdateServerStatus updateServerStatus = new UpdateServerStatus();
        
        
        Thread threadReceive = new Thread(graphicClientReceive);
        //Thread threadUpdate = new Thread(updateServerStatus);
        
        threadReceive.start();
        //threadUpdate.start();

    }
    
    
    @Deprecated
    private void refreshServerTree(){
        
        rootItem.getChildren().clear();
        
        ArrayList<String> connectedClients = null; 
        if (connectedClients != null)
            for ( String c : connectedClients){
                TreeItem<String> item = new TreeItem<String> ("Client " + c);
                rootItem.getChildren().add(item);
            }

        IHM.controller.FXserverTree.setRoot(rootItem);
        
    }
    
    @Deprecated
    public static class UpdateServerStatus implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }
                IHM.controller.refreshServerTree();
            }
        }
        
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
                        IHM.controller.FXtextRecived.setText(IHM.controller.FXtextRecived.getText() + "\n" + message);
                        
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
