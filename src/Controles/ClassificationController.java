/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controles;

import classification.config;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import pp.KNN;
import pp.Preprocess;
import pp.documentMatrix;
import pp.reslt;

/**
 * FXML Controller class
 *
 * @author Salah_Mer
 */
public class ClassificationController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    VBox box;
    @FXML
    JFXTextField path;
    @FXML
    TableView<reslt> tab;
    @FXML
    private TableColumn<reslt, String> dossier;
    @FXML
    private TableColumn<reslt, String> documnt;
    @FXML
    private TableColumn<reslt, String> percent;
     @FXML
      JFXComboBox bk;   
      @FXML
      JFXComboBox bkf;
    String testFilePath = "test/resultat/";
    int kValue;
    HashMap<String, String[]> folderTopicsMap;
    int[] documentTopicArray;
    String[] termColsArray;
    double[] idf;
    String[] docNameArray;
    int fuzzyKnn;
    int ppcount;
    documentMatrix docMatrix;
    ArrayList<String> dossiers;
    ArrayList<Preprocess> ppList;
    double[][] tfidfMatrix;
    int[] actualDocumentTopic;
    String[][] folderTopicArray;
    String[] directoryList;
        int maxk;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
         documnt.setCellValueFactory(new PropertyValueFactory<reslt, String>("document"));
        dossier.setCellValueFactory(new PropertyValueFactory<reslt, String>("dossier"));
         percent.setCellValueFactory(new PropertyValueFactory<reslt, String>("percent"));
         bkf.getItems().addAll(0, 1);
          bkf.getSelectionModel().selectFirst();
        if (config.etat) {
            docNameArray = config.docNameArray;
            dossiers = config.dossiers;
            ppList = config.ppList;
            ppcount = config.ppcount;
              maxk=config.docMatrix.getDocTermMatrix().length;
              for(int i=1;i<=maxk;i++)
                  bk.getItems().add(i);
             bk.getSelectionModel().selectFirst();
            box.getChildren().addAll(config.box.getChildren());
        } else {
            box.getChildren().add(new Label("corpus est  vide"));
                maxk=-1;
        }
    }
     @FXML
    public void select_dossier()
    {
        
                  DirectoryChooser directoryChooser = new DirectoryChooser(); 
                  directoryChooser.setTitle("select dossier test");
                  //Show open file dialog
                  File file = directoryChooser.showDialog(null);

                 if(file!=null){

                      path.setText(file.getPath()+"/");

                 }

              


              
    }
    @FXML
    public void runKnn() throws IOException {

        if (config.etat) {
            config.result =new ArrayList<>();
            // init();
            if (!path.getText().isEmpty()) {
                testFilePath = path.getText();
            }
            kValue =  (int) bk.getSelectionModel().getSelectedItem();
            if(kValue<=maxk && kValue>0)
            { 
            fuzzyKnn =(int) bk.getSelectionModel().getSelectedItem();
            File folder = new File(testFilePath);
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    String textFileName = file.getName();
                    System.out.println("\nTest Document: " + textFileName);
                    //afficher le chemin de folder et le nom de ficher
                    String testFilePathTemp = testFilePath + textFileName;
                    System.out.println(testFilePathTemp);
                    KNN predict = new KNN(config.tfidfMatrix, config.documentTopicArray, config.folderTopicArray, config.termColsArray, config.idf, config.directoryList, config.docNameArray);
                    predict.findNeighbor(kValue, testFilePathTemp, fuzzyKnn);

                }
                
            }
            tab.getItems().clear();
          for(reslt t:config.result)
          {
               tab.getItems().add(t);
          }
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "le nombre  K est plus que le nombre de documents", ButtonType.YES);
                       alert.show();
            }
        } else {
             Alert alert = new Alert(Alert.AlertType.ERROR, "corpus est vide . Ne peut pas faire classification", ButtonType.YES);
                       alert.show();
        }
    }

}
