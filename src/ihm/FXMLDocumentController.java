/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 *
 * @author p1502985
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    public TextArea FXmessage;
    
    static private PrintWriter out;
    static private BufferedReader in;
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        String message = FXmessage.getText();
        if(message != null && !message.equals("")){
            out.println(message);
            out.flush();
            FXmessage.setText("");
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    static public void initConnectionInfo(PrintWriter out,BufferedReader in){
        FXMLDocumentController.out = out;
        FXMLDocumentController.in = in;
    }
    
}
