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
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
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
        //On vérifie d'abord que l'onglet n'est pas déjà ouvert
        boolean tabOpen = false;
        for(Tab tab : tabPane.getTabs())
        {
            if(tab.getText().equals(pseudo))
            {
                tabOpen = true;
            }
        }
        if(!tabOpen) //si l'onglet n'est pas déjà ouvert, on en crée un avec la vue PrincipalView à l'intérieur
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
    
    //fonction pour récupérer l'historique des messages pour les conversations individuelles
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
            String sql = "SELECT id FROM utilisateur WHERE login = '" + client.getLogin() +"'"; //On récupère l'id en bdd du client
            rs = st.executeQuery(sql);
            if(rs.next())
            {
               int idClient = rs.getInt("id");
               
               sql = "SELECT id FROM utilisateur WHERE login = '" + loginCorrespondent +"'"; //On récupère l'id en bdd du correspondant
               rs = st.executeQuery(sql);
               if(rs.next())
               {
                   int idCorrespondent = rs.getInt("id");
                   
                   //On récupère l'id des messages liés au client et à son correspondant, ainsi que l'id de l'expéditeur pour savoir s'il s'agit d'un message reçu ou envoyé par ce client
                   sql = "SELECT message_id, expediteur_id FROM messages_utilisateurs WHERE expediteur_id IN(" + idClient + ", " + idCorrespondent + ") OR destinataire_id IN(" + idClient + ", " + idCorrespondent + ")";
                   rs = st.executeQuery(sql);
                   while(rs.next())
                   {
                       int idMessage = rs.getInt("message_id");
                       int idSenderMessage = rs.getInt("expediteur_id");
                       
                       //On récupère le message grâce à son id
                       String sql2 = "SELECT message FROM messages WHERE id = " + idMessage + " ORDER BY date DESC";
                       Statement st2 = cn.createStatement();
                       ResultSet rs2 = st2.executeQuery(sql2);
                       if(rs2.next())
                       {
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
    
    //fonction qui va s'occuper de réceptionner tous les messages à destination de ce client et de l'envoyer vers le bon onglet
    public void receiveMessage(Message message){
        if(message.isIdentification()) //S'il s'agit d'un message automatique
        {
            if(message.getConnectedUsers() != null) //Si connectedUsers n'est pas null, cela signifie que c'est un message pour récupérer la liste des utilisateurs actuellement connectés
            {
                for(Object lgn : message.getConnectedUsers()) //On va ajouter le login à la liste des utilisateurs connectés (liste à gauche), sauf s'il d'agit du login de ce client
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
            else if(message.isDisconnectedMessage()) //s'il s'agit d'un message de déconnexion (message envoyé par un client lorsqu'il se déconnecte pour prévenir les autres)
            {
                //Lorsq'un client se déconnecte on le supprime de la liste des utilisateurs connectés qui apparait à gauche de la vue
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
            else{ //S'il s'agit d'un message automatique en raison de la connexion d'un nouveau client
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
        else{ //S'il s'agit d'un message envoyé volontairement par un client
            if(message.getLoginRecipient() == null) //S'il n'y a pas de destinataire, cela signifie que ce message est à destination de tous les utilisateurs
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
            else{ //Sinon, il s'agit d'un message d'une conversation privée
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }   
    
}
