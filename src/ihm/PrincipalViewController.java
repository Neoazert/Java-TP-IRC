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
    private String loginCaller; //login du correspondant

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
        if(textToSend.getText() != null && !textToSend.getText().equals(""))
        {
            try{
                Text newMesssage = new Text("Moi: " + textToSend.getText() + "\n");
                receivedText.getChildren().add(newMesssage);
                Message message = null;
                
                if(loginCaller == null) //S'il n'y a pas de destinataire (il est donc à destination de tout le monde)
                {
                    message = new Message(client.getLogin(), null, textToSend.getText(), false);
                    client.getOut().writeObject(message);
                    client.getOut().flush();
                }
                else{ //Sinon on envoi le message à l'utilisateur concerné et on enregistre le message en bdd
                    message = new Message(client.getLogin(), loginCaller, textToSend.getText(), false);
                    client.getOut().writeObject(message);
                    client.getOut().flush();
                    
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
                        String sql = "INSERT INTO messages(message, date) VALUES('" + textToSend.getText() + "', NOW())";
                        st.executeUpdate(sql);
                        sql = " SELECT LAST_INSERT_ID() id";
                        rs = st.executeQuery(sql);
                        if(rs.next())
                        {
                            int idMessage = rs.getInt("id");
                            String sql2 = "SELECT id FROM utilisateur WHERE login = '" + client.getLogin() + "'";
                            rs = st.executeQuery(sql2);
                            if(rs.next())
                            {
                                int idSender = rs.getInt("id");
                                String sql3 = "SELECT id FROM utilisateur WHERE login = '" + loginCaller + "'";
                                rs = st.executeQuery(sql3);
                                if(rs.next())
                                {
                                    int idRecipient = rs.getInt("id");
                                    String sql4 = "INSERT INTO messages_utilisateurs(message_id, expediteur_id, destinataire_id) VALUES(" + idMessage + ", " + idSender + ", " + idRecipient + ")";
                                    st.executeUpdate(sql4);
                                }
                            }
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
    
    /**
     * Initializes the controller class.
     */
   @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
}
