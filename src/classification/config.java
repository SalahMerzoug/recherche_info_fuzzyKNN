/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classification;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import pp.Preprocess;
import pp.documentMatrix;
import pp.reslt;

/**
 *
 * @author Salah_Mer
 */
public class config {

    public static BorderPane pane;
    public static int ppcount;
    public static String[] docNameArray;
    public static String path = "test/";
    public static ArrayList<String> dossiers;
    public static ArrayList<Preprocess> ppList;
    public static HashMap<String, String[]> folderTopicsMap;
    public static int[] documentTopicArray;
    public static String[] termColsArray;
    public static double[] idf;
    public static documentMatrix docMatrix;
    public static double[][] tfidfMatrix;
    public static int[] actualDocumentTopic;
    public static String[][] folderTopicArray;
    public static String[] directoryList;
    public static VBox box;
    public static boolean etat;
    public static ArrayList<reslt> result;
      public static reslt maxd;

    public static void init() throws FileNotFoundException {
        box =new VBox();
        //  box.getChildren().clear();
        // tab.getItems().clear();
        directoryList = new String[dossiers.size()];
        for (int i = 0; i < directoryList.length; i++) {
            directoryList[i] = dossiers.get(i);
        }
        docMatrix = new documentMatrix(Preprocess.globalWordCountMap, ppList, directoryList);
        //retourner tous les dossiers avec son identifiant java (ou le pointeur) du tableau tab
        folderTopicsMap = docMatrix.getFolderTopicMap();
        /*retourner l'identifiant java (ou le pointeur) du tableau de document car tab n'est pas affichable comme 
les types simples comme une chaine et caratère ou un entier, c'est un objet donc il affiche l'identifant 
du pointeur de l'objet qui affiche le nom de la classe, un @, et le hashCode de l'objet. */
        actualDocumentTopic = docMatrix.getDocumentTopicArray();
        folderTopicArray = docMatrix.getFolderTopicArray(); //retourner l'identifiant java (ou le pointeur) du tableau de dossier 
        //construction  d'une matrice tf-idf
        tfidfMatrix = docMatrix.getTFIDFMatrix();
        documentTopicArray = docMatrix.getDocumentTopicArray();
        termColsArray = docMatrix.getTermColsArray();
        idf = docMatrix.getIDF();
        //box.getChildren().add(new Label("la création d'une matrice de document et mot"));
        //box.getChildren().add(new Label("row  : "+docMatrix.getDocTermMatrix().length));
        //box.getChildren().add(new Label("cols : "+docMatrix.getDocTermMatrix()[2].length));

    }
}
