/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm;

import java.util.Arrays;
import javafx.application.Application;
import static javafx.application.Application.launch;
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
public class IHMConnexion extends Application {
    
    public static boolean isModeGraphique = false;
    public static FXMLConnexionController controllerConnexion;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        
        IHMConnexion.isModeGraphique = true;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLConnexion.fxml"));
        Parent root = (Parent)loader.load();
        controllerConnexion = (FXMLConnexionController)loader.getController();
        
        
        String[] args = {"127.0.0.1", "2000"};
        System.out.println(Arrays.toString(args));
        MainClient.main(args);
        
        Scene scene = new Scene(root);
        
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
