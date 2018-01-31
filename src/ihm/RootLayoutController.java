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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import tp.irc.client.Client;

/**
 *
 * @author spada_000
 */
public class RootLayoutController implements Initializable{
    private Client client;
    
    @FXML
    TabPane tabPane;
    @FXML
    ScrollPane scrollConnected;
    @FXML
    TextFlow connected;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    
    //fonction qui s'exécute lorsque l'on clique sur le login d'un utilisateur connecté
    public void clickOnLogin(String pseudo)
    {
        boolean tabOpen = false;
        for(Tab tab : tabPane.getTabs())
        {
            if(tab.getText().equals(pseudo))
            {
                tabOpen = true;
            }
        }
        if(!tabOpen)
        {
            try{
                Tab tab = new Tab(pseudo);
                tab.setId(pseudo);

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(IHMConnexion.class.getResource("PrincipalView.fxml"));
                Pane principalView = (Pane) loader.load();
                PrincipalViewController principalViewController = loader.getController();
                principalViewController.setLoginCaller(pseudo);
                principalViewController.setClient(client);
                getPreviousMessages(pseudo, principalViewController);
                tab.setContent(principalView);

                tabPane.getTabs().add(tab);
            }catch(IOException e){
                e.printStackTrace();
            }
        }


    }
    
    public void getPreviousMessages(String loginCorrespondent, PrincipalViewController ctrl)
    {
        String url = "jdbc:mysql://localhost/java-tp-irc";
        String login = "root";
        String password = "";
        Connection cn = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection(url, login, password);
            st = cn.createStatement();
            String sql = "SELECT id FROM utilisateur WHERE login = '" + client.getLogin() +"'";
            rs = st.executeQuery(sql);
            if(rs.next())
            {
               System.out.println("SQL1");
               int idClient = rs.getInt("id");
               
               sql = "SELECT id FROM utilisateur WHERE login = '" + loginCorrespondent +"'";
               rs = st.executeQuery(sql);
               if(rs.next())
               {
                   System.out.println("SQL2");
                   int idCorrespondent = rs.getInt("id");
                   
                   sql = "SELECT message_id, expediteur_id FROM messages_utilisateurs WHERE expediteur_id IN(" + idClient + ", " + idCorrespondent + ") OR destinataire_id IN(" + idClient + ", " + idCorrespondent + ")";
                   rs = st.executeQuery(sql);
                   while(rs.next())
                   {
                       System.out.println("SQL3");
                       int idMessage = rs.getInt("message_id");
                       int idSenderMessage = rs.getInt("expediteur_id");
                       String sql2 = "SELECT message FROM messages WHERE id = " + idMessage + " ORDER BY date DESC";
                       Statement st2 = cn.createStatement();
                       ResultSet rs2 = st2.executeQuery(sql2);
                       if(rs2.next())
                       {
                           System.out.println("FIN SQL");
                           String msg = "";
                           if(idSenderMessage == idClient)
                           {
                               msg = "Moi: " + rs2.getString("message") + "\n";
                           }
                           else{
                               msg = loginCorrespondent + ": " + rs2.getString("message") + "\n";
                           }
                           final String messg = msg;
                           Platform.runLater(
                            () -> {                           
                                Text txt = new Text(messg);
                                ctrl.receivedText.getChildren().add(txt);
                            }
                          );
                       }
                   }
               }
            }
        }catch(Exception e){
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
    
    public void receiveMessage(Message message){
        System.out.println("MESSAGE RECU");
        if(message.isIdentification())
        {
            if(message.getConnectedUsers() != null)
            {
                for(Object lgn : message.getConnectedUsers())
                {
                    if(!lgn.equals(client.getLogin()))
                    {
                        String pseudo = (String)lgn;
                        Label thisLogin = new Label(pseudo);
                        thisLogin.setId(pseudo);
                        thisLogin.setOnMouseClicked((event) -> clickOnLogin(pseudo));
                        Platform.runLater(
                            () -> {
                                connected.getChildren().add(thisLogin);
                                connected.getChildren().add(new Text("\n"));
                            }
                          );
                    }
                    
                    
                    
                }
            }
            else if(message.isDisconnectedMessage())
            {
                System.out.println("DISCONNECTED MESSAGE");
                String loginDisconnectedUser = message.getLoginSender();
                int i = 0;
                for(Node n : connected.getChildren())
                {
                    i++;
                    final int num = i;
                    if(n.getId() != null && n.getId().equals(loginDisconnectedUser))
                    {
                        Platform.runLater(
                            () -> {
                                connected.getChildren().remove(num);
                                connected.getChildren().remove(n);
                            }
                          );
                        
                    }
                }
            }
            else{
                String pseudo = message.getLoginSender();
                Label thisLogin = new Label(pseudo);
                thisLogin.setId(pseudo);
                thisLogin.setOnMouseClicked((event) -> clickOnLogin(pseudo));
                Platform.runLater(
                    () -> {
                        connected.getChildren().add(thisLogin);
                        connected.getChildren().add(new Text("\n"));
                    }
                  );
            }

        }
        else{
            if(message.getLoginRecipient() == null)
            {
                for(Tab tab : tabPane.getTabs())
                {
                    if(tab.getText().equals("Général"))
                    {
                        Pane contenu = (Pane)tab.getContent();
                        for(Node n : contenu.getChildren())
                        {
                            if(n.getId().equals("scrollReceivedText"))
                            {

                                Platform.runLater(
                                () -> {
                                    ScrollPane sp = (ScrollPane)n;
                                    Text reveiveMessage = new Text(message.getLoginSender() + ": " + message.getMessage() + "\n");
                                    TextFlow tf = (TextFlow)sp.getContent();
                                    tf.getChildren().add(reveiveMessage);
                                }
                              );

                            }
                        }
                    }
                }
            }
            else{
                for(Tab tab : tabPane.getTabs())
                {
                    if(tab.getText().equals(message.getLoginSender()))
                    {
                        Pane contenu = (Pane)tab.getContent();
                        for(Node n : contenu.getChildren())
                        {
                            if(n.getId().equals("scrollReceivedText"))
                            {
                                Platform.runLater(
                                () -> {
                                    ScrollPane sp = (ScrollPane)n;
                                    Text reveiveMessage = new Text(message.getLoginSender() + ": " + message.getMessage() + "\n");
                                    TextFlow tf = (TextFlow)sp.getContent();
                                    tf.getChildren().add(reveiveMessage);
                                }
                              );
                            }
                        }
                    }
                }
            }
        }
        
    }
    
    
    
    public void initializeUsersList()
    {
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
                if(!client.getLogin().equals(rs.getString("login")))
                {
                    String pseudo = rs.getString("login");
                    Label thisLogin = new Label(pseudo);
                    thisLogin.setId(pseudo);
                    thisLogin.setOnMouseClicked((event) -> {
                        boolean tabOpen = false;
                        for(Tab tab : tabPane.getTabs())
                        {
                            if(tab.getText().equals(pseudo))
                            {
                                tabOpen = true;
                            }
                        }
                        if(!tabOpen)
                        {
                            try{
                                Tab tab = new Tab(pseudo);
                                tab.setId(pseudo);

                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(IHMConnexion.class.getResource("PrincipalView.fxml"));
                                Pane principalView = (Pane) loader.load();
                                PrincipalViewController principalViewController = loader.getController();
                                principalViewController.setLoginCaller(pseudo);
                                principalViewController.setClient(client);
                                tab.setContent(principalView);

                                tabPane.getTabs().add(tab);
                            }catch(IOException e){
                                e.printStackTrace();
                            }
                        }


                    });
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }   
    
}
