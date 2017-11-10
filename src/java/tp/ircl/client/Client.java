/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java.tp.ircl.client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author p1502985
 */
public class Client {
    
    private int port;
    private String address;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    
    Client(String s,Integer i){
        
    }
    
    public void disconnectedServer(){
        
    }
    
    
}
