/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm.model;

import java.io.Serializable;

/**
 *
 * @author spada_000
 */
public class Message implements Serializable{
    private String loginSender;
    private String loginRecipient;
    private String message;
    private boolean identification; //Variable qui va servir à ConnectedClient de connaitre le login du client lorsque ce dernier se connecte
    
    public Message(String loginSender, String loginRecipient, String message, boolean identification)
    {
        this.identification = identification;
        this.loginSender = loginSender;
        this.loginRecipient = loginRecipient;
        this.message = message;
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
