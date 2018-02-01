/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import tp.irc.client.Client;

/**
 * FXML Controller class
 *
 * @author Proprietaire
 */
public class FXMLConnexionController implements Initializable {
    private IHMConnexion ihmConnexion;
    private PrincipalViewController principalViewController;
    
    public void setIhmConnexion(IHMConnexion ihmConnexion)
    {
        this.ihmConnexion = ihmConnexion;
    }
    
    @FXML
    private Button btnConnexion;
    @FXML
    private TextField loginConnexion;
    @FXML
    private PasswordField mdpConnexion;
    
    @FXML
    private Button btnInscription;
    @FXML
    private TextField loginInscription;
    @FXML
    private PasswordField mdpInscription1;
    @FXML
    private PasswordField mdpInscription2;
    
    @FXML
    private void connexion(){
        String url = "jdbc:mysql://localhost/java-tp-irc";
        String login = "root";
        String password = "";
        Connection cn = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection(url, login, password);
            st = cn.createStatement();
            String sql = "SELECT * FROM utilisateur WHERE login = '" + loginConnexion.getText() + "' AND mdp = '" + mdpConnexion.getText() + "'";
            rs = st.executeQuery(sql);
            if(rs.next()) //Si les informations saisies correspondent à un utilisateur en bdd, on affiche le menu principal avec l'onglet de la conversation générale
            {
                try{
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(IHMConnexion.class.getResource("view/RootLayout.fxml"));
                    BorderPane borderPane = (BorderPane) loader.load();
                    RootLayoutController rootLayoutController = loader.getController();
                    
                    FXMLLoader loader2 = new FXMLLoader();
                    loader2.setLocation(IHMConnexion.class.getResource("view/PrincipalView.fxml"));
                    Pane principalView = (Pane) loader2.load();
                    
                    Tab tab = new Tab("Général");
                    tab.setContent(principalView);
                    rootLayoutController.tabPane.getTabs().add(tab);
                    
                    Scene scene = new Scene(borderPane);
                    ihmConnexion.getPrimaryStage().setScene(scene);
                    principalViewController = loader2.getController();
                    Client client = new Client(ihmConnexion.getAddress(), ihmConnexion.getPort(), rootLayoutController, rs.getString("login"));
                    principalViewController.setClient(client);
                    rootLayoutController.setClient(client);
                }catch(IOException e){
                    e.printStackTrace();
                }
                
                System.out.println("Connecter");
            }
            else{ //Sinon on affiche un message d'erreur à l'utilisateur
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Le Chat");
                alert.setHeaderText("Informations incorrectes");
                alert.setContentText("Veuillez entrer votre login et mot de passe");
                alert.showAndWait();
            }
        }catch(SQLException e){
                e.printStackTrace();
        }catch (ClassNotFoundException e) {
                e.printStackTrace();
        }finally {
            try{
                    cn.close();
                    st.close();
            }catch(SQLException e){
                    e.printStackTrace();
            }
        }
    }
    
    @FXML
	private void inscription(){
		if(mdpInscription1.getText().length() > 6)
		{
			if(mdpInscription1.getText().equals(mdpInscription2.getText()))
			{
				String url = "jdbc:mysql://localhost/java-tp-irc";
				String login = "root";
				String password = "";
				Connection cn = null;
				Statement st = null;
				try{
					Class.forName("com.mysql.jdbc.Driver");
					cn = DriverManager.getConnection(url, login, password);
					st = cn.createStatement();
					String sql = "INSERT INTO utilisateur(login, mdp, created, modified) VALUES('" + loginInscription.getText() + "', '" + mdpInscription1.getText() + "', NOW(), NOW())";
					st.executeUpdate(sql);
					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
					alert.setTitle("Le Chat");
					alert.setHeaderText("Iscription réussie");
					alert.setContentText("Félicitations, votre compte a été créé");
					alert.showAndWait();
				}catch(SQLException e){
					e.printStackTrace();
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Le Chat");
					alert.setHeaderText("Problème technique");
					alert.setContentText("Une erreur est survenue, veuillez réessayer plus tard");
					alert.showAndWait();
				}catch(ClassNotFoundException e){
					e.printStackTrace();
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Le Chat");
					alert.setHeaderText("Problème technique");
					alert.setContentText("Une erreur est survenue, veuillez réessayer plus tard");
					alert.showAndWait();
				}
			}
			else{
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Le Chat");
				alert.setHeaderText("Mots de passe différents");
				alert.setContentText("Veuillez entrer des mots de passe identiques");
				alert.showAndWait();
			}
		}
		else{
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Le Chat");
			alert.setHeaderText("Mots de passe trop courts");
			alert.setContentText("Veuillez entrer des mots de passe d'au moins 7 caractères");
			alert.showAndWait();
		}


	}
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}


