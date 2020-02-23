package pp;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class documentMatrix {


	private HashMap <String, Integer> globaWordFrequencyMap;// collection des mots frequencies avec son valeur
	private ArrayList <Preprocess> preprocessedList; //liste des objets de document prétraités
	private int[][] docTermMatrix;// matrice
	private String [] termColsArray;  //tableau des mots
	int [] termColsCountArray;// nombre total de termes pour l'espace de document entier
	private double [][] tfidfMatrix;//matrice tf-idf
	private double [] wordsPerDoc; //indice correspond a la ligne de matrice
	private int [] topWordIndex; //indice pour les mots du haut. correspondent aux tableau des mots et matrice tf-idf
	private String [] folderName;//tableau de nom des dossiers
	private HashMap <String,String[]> folderTopicMap;
	private int [] documentTopic; //chaque élément pointe vers l'indice du tableau des dossiers
	private String [][] folderTopicArray;
	private double [] idf;
	//private String [] docNames;
	

	// constructeur requiert le nombre de mots et une liste des documents traités
        //hachmap est une collection en Java qui associe une clé à une valeur
      // Une Map est une classe qui permet d'associer une clé à un objet. Ainsi, il suffit de récupérer la clé (souvent un string ou un int) d'une base de données pour récupérer l'objet associé.
	public documentMatrix(HashMap<String, Integer> globalWordMap, ArrayList<Preprocess> ppList, String[] directoryList) throws FileNotFoundException{
		this.globaWordFrequencyMap = globalWordMap;
		this.preprocessedList = ppList;
		this.folderName = directoryList;
	
		buildDocMatrix();
		buildTFIDFMatrix();
	        generateTopics();
		
		
	}

	public void buildDocMatrix(){

		int rows = preprocessedList.size();//la ligne de matrice contient la liste de document prétraité
		int cols = globaWordFrequencyMap.size();// le colonne d'une matrice contient les mots

		//creation d'une matrice
		docTermMatrix = new int [rows][cols];//document Matrice de termes

		termColsArray = new String [cols];  //tableau avec tous les termes
		termColsCountArray = new int [cols]; //tableau avec toutes les fréquences des termes
		wordsPerDoc = new double[cols];//word par document
               // Retourne un objet de type Set<Key> représentant la liste des clés contenues dans la collection(les mots frequencies).
		for (String key : globaWordFrequencyMap.keySet()) {
			termColsArray[cols-1] = key;//remplir le tableau de mot par les cles de chaque mot
			termColsCountArray[cols-1] = globaWordFrequencyMap.get(key);//afficher la clé de chaque mot( les mots frquencies) 
			cols--;
		}


		System.out.println("rows: " + docTermMatrix.length);// afficher le nombre de document
		System.out.println("cols: " + docTermMatrix[2].length);// afficher le nombre des mots


// remplit la matrice avec la fréquence en faisant correspondre l'indice avec les mots dans tableau des mots avec le hashmap de la fréquence des mots du document prétraité
		for (int i = 0; i < rows; i++) {	

			for(int j = 0; j < termColsArray.length; j++) {
                            //on va parcourir le nouveau matrice qui contient les clés
				String currentCol = termColsArray[j];
                                // la recherche de mot avec son clé
				if(preprocessedList.get(i).getDocumentWordMap().containsKey(currentCol) == true) {
		System.out.println("Clé trouvée: "+ currentCol);
		System.out.println("la Valeur: " + preprocessedList.get(i).getDocumentWordMap().get(currentCol));
                // afficher la matrice avec ces valeurs 
		docTermMatrix[i][j] = preprocessedList.get(i).getDocumentWordMap().get(currentCol);

		//remplir tous les lignes par les valeurs
		wordsPerDoc[i] += preprocessedList.get(i).getDocumentWordMap().get(currentCol); 

				} else {
					//System.out.println("Non trouvée: "+ currentCol);
				}
			}
		}

		System.out.println("matrice fini");
	
	}



	public void buildTFIDFMatrix() {

		int[] numDocsWithTerms = new int[termColsArray.length]; // nombre de documents avec le terme correspondant le tableau des mots
		idf = new double [termColsArray.length];// le nmobre des mots


		double docSpace = docTermMatrix.length;// le nombre des documents

		// recherche le nombre de documents avec terme
		for (int col = 0; col <termColsArray.length; col++ ) {//parcourir la colonne(les mots)
			int count = 0;

			for (int row = 0; row < docSpace; row++) { //parcourir la ligne(les documents)
				if(docTermMatrix[row][col] > 0) {
					count++;// afficher le frequence de mots par documents sup a 0
				}	
			}
			numDocsWithTerms[col] = count;		
		}

		for (int col = 0; col <termColsArray.length; col++) {
             //calculer idf=log10*(le nombre de doc/la valeur )
			idf[col] = Math.log10(docSpace/(numDocsWithTerms[col]));

			if(idf[col]< 0 || Double.isInfinite(idf[col])) {
				System.out.println("error with idf at idf array index: " + idf[col]);
			}


		}


		if(docTermMatrix != null) {
			tfidfMatrix = new double[docTermMatrix.length][docTermMatrix[0].length];// creation la matrice tf-idf
		}

		for(int row = 0; row < docTermMatrix.length; row++ ) {

			for(int col = 0; col < docTermMatrix[0].length; col++) {
                           // la valeur de mots dans la matrice/le nombre total de mot dans le document 
	//System.out.println(docTermMatrix[row][col] + "/" + wordsPerDoc[row] +": "+ (docTermMatrix[row][col]/wordsPerDoc[row]));
       // idf*la valeur de mots dans la matrice/le nombre total de mot dans le document 
		tfidfMatrix[row][col] = idf[col]*(docTermMatrix[row][col]/wordsPerDoc[row]);//calcule tf-idf matrice
			}
		}

	}
	
	


	public void generateTopics() throws FileNotFoundException {

		folderTopicMap = new HashMap<String, String[]>();
		// additionne des vecteurs. l'indice de ce tableau correspondra toujours à l'original, termsCol
		
		documentTopic = new int [tfidfMatrix.length];//
		
		
		folderTopicArray = new String [folderName.length][];//le nombre de dossier

		// boucle tf-idf Matrix ligne par ligne et ajoute auTDTDF combiné, pour chaque dossier
		
		String text = "";
		
		for(int folderIndex = 0; folderIndex < folderName.length; folderIndex++) {
			
			String [] folderTopic = new String[10];
			
			double [] combinedTFIDF = new double [tfidfMatrix[0].length];
			
			for(int row = 0; row <tfidfMatrix.length; row++ ) {
				for(int col = 0; col <tfidfMatrix[0].length; col++ ) {
					
					
					if(folderName[folderIndex].equals(preprocessedList.get(row).folderName)) {
						//System.out.println(folderName[folderIndex]);
						//System.out.println(preprocessedList.get(row));
						combinedTFIDF[col] = combinedTFIDF[col] + tfidfMatrix[row][col];
					}
					
				}

			}	
			
			topWordIndex = TopKWords(combinedTFIDF, 10); 
			
		
			text = text + "\nTopics for " + folderName[folderIndex]+"\n";
			//System.out.println("\nTopics " + topKtopics + " of " + folderName[folderIndex]);
			for(int index = 0; index < topWordIndex.length; index++) {
				text = text + termColsArray[topWordIndex[index]] + "\n";
				folderTopic[index]=termColsArray[topWordIndex[index]];
				//System.out.println(termColsArray[topWordIndex[index]] +": " + combinedTFIDF[topWordIndex[index]]);
			}	
			
			//Arrays.sort(folderTopic);
			
			folderTopicArray[folderIndex] = folderTopic;
			folderTopicMap.put(folderName[folderIndex],folderTopic);
                        System.out.print(folderName[folderIndex]+"-"+folderTopic[folderIndex]);
		}
		try (PrintWriter out = new PrintWriter("test/topic.txt")) {
		    out.println(text);
		}
		
		// remplit le tableau document Topic avec le sujet du document
		for(int row = 0; row <documentTopic.length; row++ ) {
			String[] topic = folderTopicMap.get(preprocessedList.get(row).folderName);
			for(int i = 0; i <folderTopicArray.length; i++) {
				if(Arrays.equals(folderTopicArray[i], topic)) {
					documentTopic[row] = i;
				}
			}
	
			//System.out.println("Doc " + row + ": " + preprocessedList.get(row).folderName);
		}
	}
	
	


	// trouve les 10 premières valeurs, retourne un tableau d'indice
	private int [] TopKWords(double input[], int k) 
	{ 
		
		double [] arr = input.clone();
		int [] wordIndex = new int[k];
               // rechercher le maximum valeurs
		for(int iter = 0; iter < k; iter++) {
			
			double maxValue = arr[0];
			int maxIndex = 0;
			
			for (int col = 1; col < arr.length; col++) {
				
				//System.out.println(col);
				if(arr[col] > maxValue) {
					//System.out.println(col);
					//System.out.println(arr[col]);
					maxValue = arr[col];
					maxIndex = col;//l'indice de maximum valeurs
					arr[col] = -1.0;
				}

			}

			wordIndex[iter] = maxIndex;//retourne l'indice

		}


		return wordIndex;// retourner e premier mots selon le maxumum indice

	} 



	//setters

	public int [][] getDocTermMatrix() {
		return docTermMatrix;
	}

	public String [] getTermColsArray() {
		return termColsArray;
	}

	public int [] getTermColsCountArray() {
		return termColsCountArray;
	}

	public double [][]getTFIDFMatrix() {
		return tfidfMatrix;
	}
	
	public HashMap<String, String []> getFolderTopicMap() {
		return folderTopicMap;
	}
	
	public int [] getDocumentTopicArray(){
		
//		for(int i = 0; i <documentTopic.length; i++) {
//			System.out.println(documentTopic[i]);
//		}
		return documentTopic;
	}
	
	public String [][] getFolderTopicArray(){
//		
//		for(int i = 0; i < folderTopicArray.length; i++) {
//			System.out.print("Topics for folder " + folderName[i] + ": ");
//			for(int j = 0; j < folderTopicArray[0].length; j++) {
//				System.out.print(folderTopicArray[i][j] +" ");
//			}
//			System.out.println();
//		}
//		
		
		return folderTopicArray;
	}
	
	public double [] getIDF() {
		return idf;
	}
	
}
	



