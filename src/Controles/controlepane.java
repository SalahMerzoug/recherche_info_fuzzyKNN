/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controles;//

import classification.config;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Salah_Mer
 */
public class controlepane implements Initializable {
    
     private JFXDrawer pan;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
     //if(pan!=null)
       // config.pane.getChildren().remove(pan);
       pan= (JFXDrawer) config.pane.getCenter();
    // pan = new JFXDrawer();     
     //  pan.setDirection(JFXDrawer.DrawerDirection.RIGHT);
       // pan.setDefaultDrawerSize(760);
       // pan= (JFXDrawer) config.pane.getCenter();
         /*try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("resources/views/Accueil.fxml"));
              AnchorPane p=loader.load();
            pan.setSidePane(p);
            pan.open();
            } catch (IOException ex) {
            Logger.getLogger(controlepane.class.getName()).log(Level.SEVERE, null, ex);
        }*/
//To change body of generated methods, choose Tools | Templates.
    }
     @FXML
    private void changeColor(ActionEvent event) throws IOException {
        // config.pane.setCenter(pan);
       //  config.pane.getCenter().set
        Button btn = (Button) event.getSource();
        System.out.println(btn.getText());
        pan.close();
       
        switch (btn.getText()) {
            case "Accueil":
             try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("resources/views/Accueil.fxml"));
              AnchorPane p=loader.load();
            pan.setSidePane(p);
            pan.open();
            } catch (IOException ex) {
            Logger.getLogger(controlepane.class.getName()).log(Level.SEVERE, null, ex);
        }
                break;
            case "Corpus":
                try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("resources/views/Corpus.fxml"));
               AnchorPane p=loader.load();
            pan.setSidePane(p);
            pan.open();    
            
        } catch (IOException ex) {
            Logger.getLogger(controlepane.class.getName()).log(Level.SEVERE, null, ex);
        }
                break;
            case "Classification":
               //callback.updateColor("#FF0000");
               try {
               FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("resources/views/classification.fxml"));
               AnchorPane p=loader.load();
           ///    FXMLEtudiantController controller = loader.getController();               
               pan.setSidePane(p);
               pan.open();
           
            
        } catch (IOException ex) {
            Logger.getLogger(controlepane.class.getName()).log(Level.SEVERE, null, ex);
        }
                break;
          case "Settings": 
                //thrim.setEtat(false);
              // FXMLLoader loader = new FXMLLoader(getClass().getResource("/education/FXMLStatistique.fxml"));
               //AnchorPane p=loader.load();
              //FXMLEtudiantController controller = loader.getController();               
              // pan.setVisible(true);
              // pan.setSidePane(p);
               //pan.open();     
              break;
           case "Close":    
               System.exit(0);
              break;
          
       
        }
    }
    
}
