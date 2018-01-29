/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp.irc.client;

import ihm.FXMLDocumentController;
//import ihm.IHM;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author p1502985
 */
public class ClientSend implements Runnable {

    private PrintWriter out;

    ClientSend(PrintWriter printWriter) {
        out = printWriter;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        
        String message = null;
        
         
        while (true) {
            
            message = sc.nextLine();
            
            out.println(message);
            out.flush();
            /*if(!IHM.isModeGraphique)
                out.flush();*/

        }

    }
}
