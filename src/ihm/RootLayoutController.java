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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
                    if(n.getId().equals(loginDisconnectedUser))
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
