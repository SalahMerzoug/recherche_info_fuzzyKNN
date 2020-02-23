package pp;

import java.io.File;//
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;



public class App 
{
	public static void main( String[] args ) throws IOException
	{

		String testFilePath = "test/resultat/";
		//String testFileName = null;
		int kValue;
                // Charge les documents devant être prétraités dans une liste
		String[] directoryList = {"test/dr/","test/dr1/"};
		HashMap <String, String[]> folderTopicsMap;
		int [] documentTopicArray;
		String [] termColsArray;
		double [] idf;
		String [] docNameArray;
		int fuzzyKnn;


               // Créer un objet scanner
		Scanner sc = new Scanner(System.in);
		System.out.println("Bienvenue dans le classificateur de documents K , le plus proche voisin");
		System.out.println("Veuillez entrer un nombre pour la valeur K ?");
                
                
              //lecture et l'ecriture de la valeur k
		System.out.print("Valeur K : ");
                kValue = sc.nextInt();
		System.out.println();
                
	      //lecture et l'ecriture de la valeur fuzzyknn (1 pour fuzzyKNN ou 0 pour KNN standard)  
		System.out.print("Entrée 1 pour FuzzyKNN ou 0 pour KNN standard: ");
                fuzzyKnn = sc.nextInt();
		System.out.println();

		System.out.println("\nCurrent test document directory: " +  testFilePath);


		//        
		//       while(!mode.equals("CS") && !mode.equals("EU")) {
		//    	   System.out.print("incorrect mode, please try again:");
		//    	    mode = sc.nextLine().toUpperCase();
		//  	     	
		//       } 

		//BasicConfigurator.configure();
		//Load documents that need to be preprocessed into a list
		//String[] directoryList = {"src/test/java/com/nlptools/corenlp/C1/"};
                
                
                

		//fonction pour obtenier une liste de tous les files d'un tous les repertoires devant être prétraités dans une liste
		ArrayList <Preprocess> ppList = loadAllFiles(directoryList);

		System.out.println("Le prétraitement commence, veuillez patienter \n");

		// prétraite chaque document
		int ppcount = 0;
               
		docNameArray = new String[ppList.size()];
		ppList.size();
                 //parcourir le nombre total de Fichiers texte prétraités 
		for(int i = 0; i < ppList.size(); i++){
                        //afficher l'indice de chaque document
			ppList.get(i).processDocument();
                        // afficher le nom de document selon l'indice
			docNameArray[i] = ppList.get(i).getDocName();
                        // incrémenter le nombre de document
			ppcount++;
			//System.out.println("\"" + ppList.get(i).getDocName() + "\" traité pour indexer: " + i);

		}
              // l'affichage de nombre total de fichiers
		System.out.println("Fichiers texte prétraités: " + ppcount);

		System.out.println("Prétraitement terminé");
                
                
                //l'affichage de total de mots des fichiers 
                System.out.println("Total de mots uniques: " + Preprocess.globalWordCountMap.size());

		

		//construction d'une matrice de document
		System.out.println("la création d'une matrice de document et mot");
		documentMatrix docMatrix = new documentMatrix(Preprocess.globalWordCountMap, ppList, directoryList);

   //retourner tous les dossiers avec son identifiant java (ou le pointeur) du tableau tab
                  folderTopicsMap = docMatrix.getFolderTopicMap();
/*retourner l'identifiant java (ou le pointeur) du tableau de document car tab n'est pas affichable comme 
les types simples comme une chaine et caratère ou un entier, c'est un objet donc il affiche l'identifant 
du pointeur de l'objet qui affiche le nom de la classe, un @, et le hashCode de l'objet. */

int[] actualDocumentTopic = docMatrix.getDocumentTopicArray();
String [][] folderTopicArray = docMatrix.getFolderTopicArray(); //retourner l'identifiant java (ou le pointeur) du tableau de dossier 
		//String [] folderTopicString = docMatrix.getFolderTopicString();

		//print2DMatrix(folderTopicArray);
		//System.out.println(documentTopic);
        //System.out.println("\"" + docMatrix.getFolderTopicMap() + "hhggff" + docMatrix.getFolderTopicArray());

         //construction  d'une matrice tf-idf
		double [][] tfidfMatrix = docMatrix.getTFIDFMatrix();
		documentTopicArray = docMatrix.getDocumentTopicArray();
		termColsArray = docMatrix.getTermColsArray();
		idf = docMatrix.getIDF();


		//System.out.println("Building Document-Word Matrix Complete");
		System.out.println("-------------------------\n");

		//    	for(int i = 0; i < actualDocumentTopic.length; i++) {
		//    		
		//    		System.out.println("docmatrix index " + i + " topic: " +actualDocumentTopic[i]);
		//    	}

		if(fuzzyKnn == 1) {
			System.out.println("Starting Fuzzy KNN \n");
		} else {
			System.out.println("Starting KNN \n");	
		}
		

		//System.out.println("The following files area available to test with");

		File folder = new File(testFilePath);
		File[] listOfFiles = folder.listFiles();


		for (File file : listOfFiles) {
			if (file.isFile()) {
				String textFileName = file.getName();
				System.out.println("\nTest Document: " + textFileName);
                                //afficher le chemin de folder et le nom de ficher
				
				String testFilePathTemp = testFilePath + textFileName;
				System.out.println(testFilePathTemp);
				KNN predict = new KNN(tfidfMatrix, documentTopicArray, folderTopicArray, termColsArray, idf, directoryList, docNameArray);
				predict.findNeighbor(kValue, testFilePathTemp, fuzzyKnn);

			}
		}
		






	}




	public static void print2DMatrix(double [][] inputMatrix){

		for (int i = 0; i < inputMatrix.length; i++){

			for(int j = 0; j < inputMatrix[0].length; j++){
				System.out.print(inputMatrix[i][j] + " ");
			}
			System.out.println();
		}

	} 

	public static void print2DMatrix(String [][] inputMatrix){

		for (int i = 0; i < inputMatrix.length; i++){

			for(int j = 0; j < inputMatrix[0].length; j++){
				System.out.print(inputMatrix[i][j] + " ");
			}
			System.out.println();
		}

	} 


	// charge tous les fichiers de chemins de fichiers dans un tableau
	public static ArrayList <Preprocess> loadAllFiles (String [] directoryList) throws IOException{

		ArrayList <Preprocess> documentObjectsList = new ArrayList<Preprocess>();

		for(int i = 0; i < directoryList.length; i++){

			File folder = new File(directoryList[i]);
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
				if (file.isFile()) {
					String textFileName = file.getName();
					System.out.println(textFileName);
					documentObjectsList.add(new Preprocess(directoryList[i] + textFileName, i, directoryList[i]));
				}
			}

		}	

		return documentObjectsList;
	}


}
