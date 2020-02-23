package pp;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Properties;
import java.util.Scanner;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

public class Preprocess {
	 public static HashMap<String, Integer> globalWordCountMap = new HashMap<>();
	private HashMap<String, Integer> documentWordCount = new HashMap<>();//word with count
	private ArrayList <String> stopWordsList;
	private String docString;
	private String originalDocString;
	private ArrayList <String> processedWordList;
	private HashMap<String, Integer> ngramsMap = new HashMap<String, Integer>();
	 String nameOfDoc;
         String folderName; 
         int nbr_line = 0;
         int nbr_mots=0;

//
	 public Preprocess(String textFile, int folderIndex, String path) throws IOException{
                  //charger les mots vide dans un ficher
		this.stopWordsList = readFileStopWords("test/stopwords.txt");
		this.docString = docToString(textFile).toLowerCase();
		this.originalDocString = docToString(textFile).toLowerCase();//transforme le texte en miniscule
		this.processedWordList = new ArrayList<String>();// tableau de mot prétraité
		this.folderName = path;

	}
	
	 public Preprocess(String textFile) throws IOException{

		this.stopWordsList = readFileStopWords("test/stopwords.txt");
		this.docString = docToString(textFile).toLowerCase();
		this.originalDocString = docToString(textFile).toLowerCase();
		this.processedWordList = new ArrayList<String>();
//		this.folderName = path;

	}

	public void processDocument() {
		removeStopWords();//suprimer les mots vide
		tokenLemmaNer();//supression de préfixe et suffixe

	}
	
	
             //lire le ficher des mots vide
	private ArrayList <String> readFileStopWords(String fileName) throws IOException {
		Scanner s = new Scanner(new File(fileName));
		ArrayList<String> list = new ArrayList<String>();
               //afficher la liste de mot
		while (s.hasNext()){
			list.add(s.next());
		}
		s.close();

		return list;
	}

     public void setNameOfDoc(String nameOfDoc) {
        this.nameOfDoc = nameOfDoc;
    }

    public int getNbr_line() {
        return nbr_line;
    }

    public void setNbr_line(int nbr_line) {
        this.nbr_line = nbr_line;
    }

    public int getNbr_mots() {
        return nbr_mots;
    }

    public void setNbr_mots(int nbr_mots) {
        this.nbr_mots = nbr_mots;
    }

        
	private String docToString(String filename) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(filename));//le nom de document
		String line;
		String outputLine = " ";
		int lineCount = 0;
                int motCount = 0;
		while((line = in.readLine()) != null)//si la document n'est pas vide
		{
			if(lineCount == 0) {
				//nameOfDoc = line;
			}
			
			//System.out.println(line);//affichage le document
                        motCount+=line.split(" ").length;
			outputLine = outputLine + line + "  ";//affichage de tous les documents
			lineCount++;//incrémenter le nombre de doc
                        
		}
		in.close();
                 nbr_line=lineCount;
                 nbr_mots=motCount;
		//suprimer les espaces
		outputLine = outputLine.replaceAll("\\s+", " ");// suprimer les espaces entre les lignes
                //System.out.println(outputLine);afficher le texte dans un ligne
		return outputLine;		
	}
        //suprimer les mots vide

	private void removeStopWords() {

		for(int i = 0; i < stopWordsList.size(); i++) {//parcourir la liste des mots vides

			String currentStopWord = stopWordsList.get(i);//chaque mot prend sa valeur selon l'indice
			//System.out.println(currentStopWord);//affichage de mots vide

			if(i < 128) {
				docString = docString.replace(currentStopWord, "");//remplacer le mots vides par autre mots de doc

			} else {

				String regex = "\\s*\\b" + currentStopWord + "\\b\\s*";//
				//System.out.println(regex);
				docString = docString.replaceAll(regex, " ");
			}

		}

	}
        
//suprimer les ngram
	private void removeNgrams(){

		Document docNGrams = new Document(docString);
		
		//fill ngram freqency hashmap. this is acutally one sentence. .sentence creates a list.
		for (Sentence sent : docNGrams.sentences()) {
			
			//this is a sliding window. since I converted the entire doc into a single string.
			for(int i = 0; i < sent.length()-1; i++) {
				String currentWord = sent.lemma(i) + " " + sent.lemma(i+1);// afficher le radical(nouveau mot)
				ngramsMap.merge(currentWord, 1, Integer::sum);// applique le ngram n=1
				//System.out.println(currentWord);
				
				if(ngramsMap.get(currentWord) > 1) {
					processedWordList.add(currentWord);//ajouter les le mot dans un liste
					String regex = currentWord;
					documentWordCount.put(currentWord,ngramsMap.get(currentWord));//claculer les nouveau mots de doc

					if(globalWordCountMap.get(currentWord) == null) {
						globalWordCountMap.put(currentWord, ngramsMap.get(currentWord));//tester si le doc est null afficher le nouveau mot
					} else {
						//int curGlobalValue = globalWordCountMap.get(key);
						globalWordCountMap.put(currentWord, ngramsMap.get(currentWord) + globalWordCountMap.get(currentWord));//continuation le ngram si les mot de doc n'est pas null	
					}
					//System.out.println(regex);
					docString = docString.replace(regex, " ");//afficher tous le doc par string 
					//System.out.println(docString);
				}


			}
		}
		
		
	}


	private void tokenLemmaNer() {

		//token and lemma text
		Document tokenized = new Document(docString);
		String lemmaedString = "";
		
		for (Sentence sent : tokenized.sentences()) {
			for(int i = 0; i < sent.length(); i++) {
				String currentWord = sent.lemma(i);
				lemmaedString = lemmaedString + currentWord + " ";

			}

		}
		//udpate docString as with lemmanized string
		docString = lemmaedString;
		
		
 // build pipeline to get sentences and do basic tagging
 //// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
                // On crée un objet Properties
		Properties props = new Properties();
 // On définit les propriétés du pipeline : on annote, on tokenise, on découpe les phrases, on tague le type de mot, on lemmatise, on détecte les entités nommées
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
		props.setProperty("ner.applyFineGrained", "false");
// déclaration du pipeline, en lui passant les propriétés
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
                //// create an empty Annotation just with the given text
                //Annotation document = new Annotation(text);
                // On crée un objet Annotation en lui passant le texte, puis on appelle la fonction
		CoreDocument doc = new CoreDocument(docString);
                // run all Annotators on this text
                //pipeline.annotate(document);
		pipeline.annotate(doc);

                
              // these are all the sentences in this document
        
                
		//add name entities to document/global word count hashmaps. then removed from docString
		for (CoreEntityMention em : doc.entityMentions()) {
			globalWordCountMap.merge(em.text(), 1, Integer::sum);
			documentWordCount.merge(em.text(), 1, Integer::sum);
			processedWordList.add(em.text());  //add to list of words processed
			String regex = "\\s*\\b" + em.text() + "\\b\\s*";
			//remove name entities from docString
			//System.out.println("Entity text "+ regex);
			docString = docString.replaceAll(regex, " ");

		}

		//remove ngrams from docString, add ngrams to processedWordList, add ngrams to global/document wordlist hashmap
		removeNgrams();


		//reload docSstring
		Document doc2 = new Document(docString);

		//add remaining words to hashmap
		for (Sentence sent : doc2.sentences()) {
			for(int i = 0; i < sent.length(); i++) {
				String currentWord = sent.lemma(i);
				processedWordList.add(currentWord);
				globalWordCountMap.merge(currentWord, 1, Integer::sum);
				documentWordCount.merge(currentWord, 1, Integer::sum);

			}

		}

	}

    public String getNameOfDoc() {
        return nameOfDoc;
    }

	public String getDocName() {
		return nameOfDoc;
	}
	
	public HashMap<String, Integer> getDocumentWordMap(){
		return documentWordCount;
	}

	public ArrayList<String> getStopWords() {
		return stopWordsList;
	}

	public ArrayList<String> getProcessedWordsList() {
		return processedWordList;
	}

	public String getOriginalDocString() {
		return originalDocString;
	}

	public HashMap<String, Integer> getNgramsCount(){
		return ngramsMap;
	}

	public String getFolderName() {
		return folderName;
	}
	
	public void printCounts() {
		
		System.out.println("Processed word count: " +processedWordList.size());
		
		int count = 0;
		
		for (String key : documentWordCount.keySet()) {
			count += documentWordCount.get(key);
			if(!processedWordList.contains(key)) {
				System.out.println("Missing: " + key);
			}
			
		}
		System.out.println("Doc count: " + count);
		
		
	}

}
