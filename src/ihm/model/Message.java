/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author spada_000
 */
public class Message implements Serializable{
    private String loginSender;
    private String loginRecipient;
    private String message;
    private boolean identification; //Variable qui va servir à ConnectedClient de connaitre le login du client lorsque ce dernier se connecte
    private ArrayList<String> connectedUsers; //Liste des utilisateurs connectes: sera utilisé une seule fois, lorsque le client se connecte pour récupérer la liste des login des utilisateurs connectés
    private boolean disconnectedMessage; //Pour savoir si ce message est un message de déconnexion (message envoyé lorsqu'un utilisateur se déconnecte pour prévenir les autres utilisateurs de cette déconnexion)
    
    public Message(String loginSender, String loginRecipient, String message, boolean identification)
    {
        this.identification = identification;
        this.loginSender = loginSender;
        this.loginRecipient = loginRecipient;
        this.message = message;
        this.disconnectedMessage = false;
    }

    public boolean isDisconnectedMessage() {
        return disconnectedMessage;
    }

    public void setDisconnectedMessage(boolean disconnectedMessage) {
        this.disconnectedMessage = disconnectedMessage;
    }

    

    public ArrayList getConnectedUsers() {
        return connectedUsers;
    }

    public void setConnectedUsers(ArrayList connectedUsers) {
        this.connectedUsers = connectedUsers;
    }
    
    

    public String getLoginSender() {
        return loginSender;
    }

    public String getLoginRecipient() {
        return loginRecipient;
    }

    public String getMessage() {
        return message;
    }

    public boolean isIdentification() {
        return identification;
    }
    
    
}
