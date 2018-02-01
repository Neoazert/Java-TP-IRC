/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author p1502985
 */
public class IHMConnexion extends Application {
    private static Stage primaryStage;
    private static String address;
    private static int port;
    public static boolean isModeGraphique = false;
    public static FXMLConnexionController controllerConnexion;

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    
    
    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        IHMConnexion.isModeGraphique = true;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLConnexion.fxml"));
        Parent root = (Parent)loader.load();
        controllerConnexion = (FXMLConnexionController)loader.getController();
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        
        controllerConnexion.setIhmConnexion(this);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            printUsage();
        } else {
            address = args[0];
            port = new Integer(args[1]);
            launch(args);
        }
    }
    
    private static void printUsage() {
        System.out.println("java client.Client <address> <port>");
        System.out.println("\t<address>: server's ip address");
        System.out.println("\t<port>: server's port");
    }
    
    @Override
    public void stop(){
        System.out.println("Fermeture de l'application");
        System.exit(0);
    }
    
}
