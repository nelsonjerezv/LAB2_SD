/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab_2_sd;
 
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
 
public class InvertedIndex {
    
    static Mongo mongo = new Mongo("localhost",27017);
    
    static DB db = mongo.getDB("test");
    static DBCollection coleccion = db.getCollection("test");
    
     public  static ArrayList<String> palabras;
  
        
        

 
     public static List<String> stopwords;// = ProcesaXML.palabras;
//                = Arrays.asList("a", "able", "about",
//			"across", "after", "all", "almost", "also", "am", "among", "an",
//			"and", "any", "are", "as", "at", "be", "because", "been", "but",
//			"by", "can", "cannot", "could", "dear", "did", "do", "does",
//			"either", "else", "ever", "every", "for", "from", "get", "got",
//			"had", "has", "have", "he", "her", "hers", "him", "his", "how",
//			"however", "i", "if", "in", "into", "is", "it", "its", "just",
//			"least", "let", "like", "likely", "may", "me", "might", "most",
//			"must", "my", "neither", "no", "nor", "not", "of", "off", "often",
//			"on", "only", "or", "other", "our", "own", "rather", "said", "say",
//			"says", "she", "should", "since", "so", "some", "than", "that",
//			"the", "their", "them", "then", "there", "these", "they", "this",
//			"tis", "to", "too", "twas", "us", "wants", "was", "we", "were",
//			"what", "when", "where", "which", "while", "who", "whom", "why",
//			"will", "with", "would", "yet", "you", "your");
 
	static Map<String, List<Tuple>> index = new HashMap<String, List<Tuple>>();
	static List<String> files = new ArrayList<String>();
 
	public void indexFile(String titulo, String cuerpo) throws IOException {
            
            try (FileReader fr = new FileReader("stop-words-spanish.txt")) {
            if(fr == null){
                System.out.println("Archivo erroneo");
                System.exit(1);
            }
            try (BufferedReader bf = new BufferedReader(fr)) {
                palabras = new ArrayList();
                String aux1;
                
                while( (aux1 = bf.readLine() ) != null ){
                    palabras.add(aux1);
                }
            }
        }
            
            
            
            
            
            
            
            
		int fileno = files.indexOf(titulo);
		if (fileno == -1) {
			files.add(titulo);
			fileno = files.size() - 1;
		}
 
		int pos = 0;
		//BufferedReader reader = new BufferedReader(new FileReader(file));
		//for (String line = reader.readLine(); line != null; line = reader
//		for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                StringTokenizer st =new StringTokenizer(cuerpo);
                // iterate through st object to get more tokens from it
                while (st.hasMoreElements()) {
                    String token = st.nextElement().toString();
                    String word = token.toLowerCase();
                    pos++;
                    if (stopwords.contains(word))
                            continue;
                    List<Tuple> idx = index.get(word);
                    if (idx == null) {
                            idx = new LinkedList<Tuple>();
                            index.put(word, idx);
                    }
                    idx.add(new Tuple(fileno, pos));
                }

			for (String _word : cuerpo.split("\\W+")) {
				String word = _word.toLowerCase();
				pos++;
				if (stopwords.contains(word))
					continue;
				List<Tuple> idx = index.get(word);
				if (idx == null) {
					idx = new LinkedList<Tuple>();
					index.put(word, idx);
				}
				idx.add(new Tuple(fileno, pos));
			}
//		}
		System.out.println("indexed " + titulo + " " + pos + " words");
	}
 
	public static void search(List<String> words) {
		for (String _word : words) {
			Set<String> answer = new HashSet<String>();
			String word = _word.toLowerCase();
			List<Tuple> idx = index.get(word);
			if (idx != null) {
				for (Tuple t : idx) {
					answer.add(files.get(t.fileno));
				}
			}
			System.out.print("Busqueda: " + word + "\nResultados: ");
			for (String f : answer) {
				System.out.print("\n   " + f);
			}
			System.out.println("");
		}
	}
 
	public static void indexar(InvertedIndex idx) throws IOException {
            
            try (FileReader fr = new FileReader("stop-words-spanish.txt")) {
                if(fr == null){
                    System.out.println("Archivo erroneo");
                    System.exit(1);
                }
                try (BufferedReader bf = new BufferedReader(fr)) {
                    palabras = new ArrayList();
                    String aux1;

                    while( (aux1 = bf.readLine() ) != null ){
                        palabras.add(aux1);
                    }
                }
            }
            
            stopwords = palabras;
            DBCursor cursor;
            cursor = coleccion.find();

            while( cursor.hasNext() ){
                DBObject documento = cursor.next();
                String titulo = (String) documento.get("titulo");
                String cuerpo = (String) documento.get("cuerpo");

                idx.indexFile(titulo, cuerpo);
            }

            //idx.search(Arrays.asList(args[0].split(",")));
		
	}
        
        public void buscar(InvertedIndex idx, String query){
            idx.search(Arrays.asList(query.split(",")));
        }
 
	private class Tuple {
		private int fileno;
		private int position;
 
		public Tuple(int fileno, int position) {
			this.fileno = fileno;
			this.position = position;
		}
	}
}