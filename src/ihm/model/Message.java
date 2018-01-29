/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm.model;

/**
 *
 * @author spada_000
 */
public class Message {
    private String loginSender;
    private String loginRecipient;
    private String message;
    
    public Message(String loginSender, String loginRecipient, String message)
    {
        this.loginSender = loginSender;
        this.loginRecipient = loginRecipient;
        this.message = message;
    }
}
