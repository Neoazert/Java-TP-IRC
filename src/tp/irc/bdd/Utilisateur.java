/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.bdd;

/**
 *
 * @author spada_000
 */
public class Utilisateur {
    private static int userCounter = 0;
    private int id;
    private String login;

    public Utilisateur(int id, String login) {
        this.id = id;
        this.login = login;
        userCounter++;
    }
    

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }
    
    
}
