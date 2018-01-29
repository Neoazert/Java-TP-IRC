/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm;

import ihm.model.Message;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
    ScrollPane scrollConnected;
    @FXML
    TextFlow connected;
    @FXML
    Text textMembers;
    
    @FXML
    public void sendMessage()
    {
        
        if(!textToSend.getText().equals(null) && !textToSend.getText().equals(""))
        {
            Text newMesssage = new Text("Moi: " + textToSend.getText() + "\n");
            receivedText.getChildren().add(newMesssage);
            
            Message message = new Message(client.getLogin(), null, textToSend.getText());
            
            client.getOut().println(textToSend.getText());
            client.getOut().flush();
            textToSend.setText("");
        }
    }
    
    @FXML
    public void clearMessage()
    {
        textToSend.setText("");
    }
    
    public void receiveMessage(String message){
        Platform.runLater(
            () -> {
                Text reveiveMessage = new Text(message);
                Text newMesssage = new Text("Autrui: " + message + "\n");
                receivedText.getChildren().add(newMesssage);
            }
          );
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String link = "jdbc:mysql://localhost/java-tp-irc";
        String login = "root";
        String password = "";
        Connection cn = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection(link, login, password);
            st = cn.createStatement();
            String sql = "SELECT login FROM utilisateur";
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                Text thisLogin = new Text(rs.getString("login") + "\n");
                thisLogin.setOnMouseClicked((event) -> System.out.println(thisLogin));
                connected.getChildren().add(thisLogin);
                //System.out.println(rs.getString("login"));
            }
        }catch(SQLException e){
                e.printStackTrace();
        }catch (ClassNotFoundException e) {
                e.printStackTrace();
        }finally {
                try{
                        cn.close();
                        st.close();
                }catch(SQLException e){
                        e.printStackTrace();
                }
        }
    }    
    
}
