/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controles;

import classification.config;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Salah_Mer
 */
public class FXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
     @FXML
    private JFXHamburger hamburger;
      @FXML
    public  BorderPane pane;
       @FXML
       private JFXDrawer panc;
    private  VBox v;
    private boolean  isopen;
      private JFXDrawer pan;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("resources/views/Accueil.fxml"));
              AnchorPane p=loader.load();
            panc.setSidePane(p);
            panc.open();
            } catch (IOException ex) {
            Logger.getLogger(controlepane.class.getName()).log(Level.SEVERE, null, ex);
        }
        pan =new JFXDrawer();
        pan.setDefaultDrawerSize(240);
       config.pane=pane;
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(-1);
       transition.setRate(transition.getRate() * -1);
        transition.play();
         try {
                    v=  (VBox) FXMLLoader.load(getClass().getClassLoader().getResource("resources/views/panel.fxml"));
                } catch (IOException ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
                 pane.setLeft(pan); 
                 pane.getLeft().prefWidth(240);
                   pan.setSidePane(v);
                pan.open();
                  isopen=true;
                  System.out.println("open");
       
         hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();
            if ( isopen ) {
                pan.close();
               pane.getChildren().remove(pan);
               isopen=false;
               System.out.println("close");
            } else {
                try {
                    v=  (VBox) FXMLLoader.load(getClass().getClassLoader().getResource("resources/views/panel.fxml"));
                } catch (IOException ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
                 pane.setLeft(pan); 
                 pane.getLeft().prefWidth(240);
                   pan.setSidePane(v);
                pan.open();
                  isopen=true;
                  System.out.println("open");
            }
        });
    }     
}
