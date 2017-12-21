/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm;

import java.security.MessageDigest;
import java.net.URL;
import java.security.DigestException;
import java.time.Clock;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Proprietaire
 */
public class FXMLConnexionController implements Initializable {
    @FXML
    private TextField loginConnexion;
    @FXML
    private TextField mdpConnexion;
    @FXML
    private Button btnConnexion;
    
    @FXML
    private void handleButtonAction(ActionEvent event){
        System.out.println("Login: " + loginConnexion.getText() + "\nMdp: " + mdpConnexion.getText());
    };
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    
    
}
