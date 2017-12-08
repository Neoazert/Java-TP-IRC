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
import tp.irc.client.MainClient;

/**
 *
 * @author p1502985
 */
public class IHM extends Application {
    
    public static boolean isModeGraphique = false;
    public static FXMLDocumentController controller;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        
        IHM.isModeGraphique = true;
        
        String[] args = {"127.0.0.1", "2000"};
        MainClient.main(args);
        
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = (Parent)loader.load();
        controller = (FXMLDocumentController)loader.getController();
        
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
    
}
