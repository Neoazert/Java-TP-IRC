/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tp.irc.client.Client;
import tp.irc.client.MainClient;

/**
 *
 * @author p1502985
 */
public class IHM extends Application {
    
    public static boolean isModeGraphique = false;
    public static FXMLDocumentController controller;
    public static FXMLConnexionController controllerConnexion;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        IHM.isModeGraphique = true;
        
        FXMLLoader loaderConnexion = new FXMLLoader(getClass().getResource("FXMLConnexion.fxml"));
        Parent rootConnexion = (Parent)loaderConnexion.load();
        controllerConnexion = (FXMLConnexionController)loaderConnexion.getController();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = (Parent)loader.load();
        controller = (FXMLDocumentController)loader.getController();
        
        
        String[] args = {"127.0.0.1", "2000"};
        MainClient.main(args);
        
        
        
        Scene scene = new Scene(rootConnexion);
        
        
        
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void stop(){
        Client.write("Fermeture de l'application");
        System.exit(0);
    }
    
}
