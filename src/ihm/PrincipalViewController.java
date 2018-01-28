/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import tp.irc.client.Client;

/**
 * FXML Controller class
 *
 * @author spada_000
 */
public class PrincipalViewController implements Initializable {
    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }
    
    @FXML
    TextArea textToSend;
    @FXML
    ScrollPane scrollReceivedText;
    @FXML
    TextFlow receivedText;
    @FXML
    Button sendBtn;
    @FXML
    Button clearBtn;
    @FXML
    TextArea connected;
    @FXML
    Text textMembers;
    
    @FXML
    public void sendMessage()
    {
        
        if(!textToSend.getText().equals(null) && !textToSend.getText().equals(""))
        {
            Text newMesssage = new Text("Moi: " + textToSend.getText() + "\n");
            receivedText.getChildren().add(newMesssage);
            client.getOut().println(textToSend.getText());
            client.getOut().flush();
            textToSend.setText("");
        }
    }
    
    public void receiveMessage(String message){
        Text reveiveMessage = new Text(message);
        System.out.println(reveiveMessage);
        //Text newMesssage = new Text("Autrui: " + message + "\n");
        //receivedText.getChildren().add(newMesssage);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
