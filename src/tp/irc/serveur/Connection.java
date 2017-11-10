/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.serveur;

import java.net.ServerSocket;

/**
 *
 * @author p1502985
 */
public class Connection implements Runnable {
    private ServerSocket serverSocket;
    private Server server;
    
    Connection(Server server){
        
    }
    
    @Override
    public void run(){
        
    }
}
