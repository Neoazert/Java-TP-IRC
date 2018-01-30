/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author spada_000
 */
public class RootLayoutController implements Initializable{
    @FXML
    TabPane tabPane;
    @FXML
    ScrollPane scrollConnected;
    @FXML
    TextFlow connected;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String link = "jdbc:mysql://localhost/java-tp-irc";
        String login = "root";
        String password = "";
        Connection cn = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection(link, login, password);
            st = cn.createStatement();
            String sql = "SELECT login FROM utilisateur";
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                //Group group = new Group();
                Label thisLogin = new Label(rs.getString("login"));
                thisLogin.setId(rs.getString("login"));
                thisLogin.setOnMouseClicked((event) -> System.out.println(thisLogin));
                connected.getChildren().add(thisLogin);
                //group.getChildren().add(thisLogin);
                Label thisLoginConnected = new Label("(déconnecté)");
                thisLoginConnected.setId("conected_" + rs.getString("login"));
                connected.getChildren().add(thisLoginConnected);
                connected.getChildren().add(new Text("\n"));
                //group.getChildren().add(thisLoginConnected);
                //connected.getChildren().add(group);
                
                //System.out.println(rs.getString("login"));
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
    
}
