/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm;

import ihm.model.Message;
import java.io.IOException;
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
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private String loginCaller; //login de la personne avec qui on est en conversation

    public void setClient(Client client) {
        this.client = client;
    }

    public void setLoginCaller(String loginCaller) {
        this.loginCaller = loginCaller;
    }

    public String getLoginCaller() {
        return loginCaller;
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
        System.out.println("SEND");
        if(textToSend.getText() != null && !textToSend.getText().equals(""))
        {
            try{
                Text newMesssage = new Text("Moi: " + textToSend.getText() + "\n");
                receivedText.getChildren().add(newMesssage);
                Message message = null;
                if(loginCaller == null)
                {
                    message = new Message(client.getLogin(), null, textToSend.getText(), false);
                    client.getOut().writeObject(message);
                    client.getOut().flush();
                }
                else{
                    message = new Message(client.getLogin(), loginCaller, textToSend.getText(), false);
                    client.getOut().writeObject(message);
                    client.getOut().flush();
                }

                /*client.getOut().println(textToSend.getText());
                client.getOut().flush();*/
                textToSend.setText("");
            }catch(IOException e){
                e.printStackTrace();
            }
            
        }
    }
    
    @FXML
    public void clearMessage()
    {
        textToSend.setText("");
    }
    
    public void receiveMessage(Message message){
        Platform.runLater(
            () -> {
                Text reveiveMessage = new Text(message.getLoginSender() + ": " + message.getMessage() + "\n");
                receivedText.getChildren().add(reveiveMessage);
            }
          );
    }
    
    /**
     * Initializes the controller class.
     */
   @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*String link = "jdbc:mysql://localhost/java-tp-irc";
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
                //Group group = new Group();
                Label thisLogin = new Label(rs.getString("login"));
                thisLogin.setId(rs.getString("login"));
                thisLogin.setOnMouseClicked((event) -> System.out.println(thisLogin));
                connected.getChildren().add(thisLogin);
                //group.getChildren().add(thisLogin);
                Label thisLoginConnected = new Label("(déconnecté)");
                thisLoginConnected.setId("conected_" + rs.getString("login"));
                connected.getChildren().add(thisLoginConnected);
                connected.getChildren().add(new Text("\n"));
                //group.getChildren().add(thisLoginConnected);
                //connected.getChildren().add(group);
                
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
        }*/
    }
    
}
