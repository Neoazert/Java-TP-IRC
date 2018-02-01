/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.client;

import java.io.ObjectOutputStream;
import java.util.Scanner;

/**
 *
 * @author p1502985
 */
public class ClientSend implements Runnable { //Classe obsolète pour lorsque l'on utilise l'interface graphique

    private ObjectOutputStream out;

    ClientSend(ObjectOutputStream printWriter) {
        out = printWriter;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        
        String message = null;
        
         
        while (true) {
            
            /*message = sc.nextLine();
            
            out.println(message);
            out.flush();*/

        }

    }
}
