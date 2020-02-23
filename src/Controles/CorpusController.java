/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controles;

import classification.config;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import pp.Preprocess;
import pp.documentMatrix;

/**
 * FXML Controller class
 *
 * @author Salah_Mer
 */
public class CorpusController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    VBox box;
    @FXML
    JFXTreeView tree;
    @FXML
    CheckBoxTreeItem<String> rootItem;
    @FXML
    TableView<Preprocess> tab;
    @FXML
    private TableColumn<Preprocess, String> nomdoc;
    @FXML
    private TableColumn<Preprocess, String> patchd;
    @FXML
    private TableColumn<Preprocess, Integer> nbr_lines;
    @FXML
    private TableColumn<Preprocess, Integer> nbr_mots;
    int ppcount = 0;
    String [] docNameArray;
    String path = "test/";
    ArrayList<String> dossiers;
    ArrayList <Preprocess> ppList;
   // documentMatrix docMatrix;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       dossiers= new ArrayList<>();
        
         nomdoc.setCellValueFactory(new PropertyValueFactory<Preprocess, String>("nameOfDoc"));
          patchd.setCellValueFactory(new PropertyValueFactory<Preprocess, String>("folderName"));
          nbr_lines.setCellValueFactory(new PropertyValueFactory<Preprocess, Integer>("nbr_line"));
          nbr_mots.setCellValueFactory(new PropertyValueFactory<Preprocess, Integer>("nbr_mots"));
             tab.getItems().clear();
       settree();
        // TODO
    }

    public void settree() {
         rootItem = new CheckBoxTreeItem<String>(path);
        rootItem.setExpanded(true);
        tree.setRoot(rootItem);
        tree.setCellFactory(CheckBoxTreeCell.<String>forTreeView());
        CheckBoxTreeItem<String> checkBoxTreeItem;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isDirectory()) {
                String textFileName = file.getName();
                  checkBoxTreeItem = new CheckBoxTreeItem<String>(textFileName+"/");
                  rootItem.getChildren().add(checkBoxTreeItem);
            }
        }
         tree.setShowRoot(true);
    }
     @FXML
   public void effacer()
   {
         tab.getItems().clear();
          dossiers=new ArrayList<>();
          ppList= new ArrayList<>();
           config.etat=false;
   }
     @FXML
   public void recuperer_dossierselected() throws IOException
    {
         dossiers=new ArrayList<>();
         ppcount=0;
          tab.getItems().clear();
         CheckBoxTreeItem<String> t=(CheckBoxTreeItem<String>) tree.getRoot();
        ObservableList<TreeItem<String>> br = t.getChildren();
        for(int i=0;i<br.size();i++)
        {
           CheckBoxTreeItem<String> checkBoxTreeItem=(CheckBoxTreeItem<String>) br.get(i); 
           if(checkBoxTreeItem.isSelected())
            {
               
                dossiers.add(path +checkBoxTreeItem.getValue());
               
            }
         
        }
        loadAllFiles(dossiers);
        docNameArray = new String[ppList.size()];
        for(int i = 0; i < ppList.size(); i++){
                        //afficher l'indice de chaque document
			ppList.get(i).processDocument();
                        // afficher le nom de document selon l'indice
			docNameArray[i] = ppList.get(i).getDocName();
                        // incrémenter le nombre de document
			ppcount++;
			//System.out.println("\"" + ppList.get(i).getDocName() + "\" traité pour indexer: " + i);

		}
        if(ppList.size()>0)
        {
       config.docNameArray=docNameArray;
       config.dossiers=dossiers;
       config.path=path;
       config.ppList=ppList;
       config.ppcount=ppcount;
       config.etat=true;
        config.init();
        
        box.getChildren().add(new Label(" Fichiers texte prétraités:  " + ppcount)); 
        box.getChildren().add(new Label(" Total de mots uniques: " + Preprocess.globalWordCountMap.size()));
        box.getChildren().add(new Label("---------------------------------------"));
        box.getChildren().add(new Label("la création d'une matrice de document et mot"));
        box.getChildren().add(new Label("row  : "+ config.docMatrix.getDocTermMatrix().length));
        box.getChildren().add(new Label("cols : "+ config.docMatrix.getDocTermMatrix()[2].length));
        box.getChildren().add(new Label(" Prétraitement terminé"));
        config.box.getChildren().addAll(box.getChildren());
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, " Ne peut pas faire recuperation", ButtonType.YES);
                       alert.show();
        }
    }  
   
      public  void loadAllFiles (ArrayList<String> directoryList) throws IOException{

		ArrayList <Preprocess> documentObjectsList = new ArrayList<Preprocess>();

		for(int i = 0; i < directoryList.size(); i++){

			File folder = new File(directoryList.get(i));
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
				if (file.isFile()) {
					String textFileName = file.getName();
					System.out.println(textFileName);
                                        Preprocess p=new Preprocess(directoryList.get(i) + textFileName, i, directoryList.get(i));
                                       
                                        p.setNameOfDoc(textFileName);
					documentObjectsList.add(p);
                                          tab.getItems().add(p);
                                        
				}
			}

		}	

		ppList= documentObjectsList;
	}

    
}
