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

                   
        FXMLDocumentController.out = out;
        FXMLDocumentController.in = in;
        


    }
     

}
