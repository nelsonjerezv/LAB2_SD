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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
 
public class InvertedIndex {
    
    static Mongo mongo = new Mongo("localhost",27017);
    
    static DB db = mongo.getDB("test");
    static DBCollection coleccion = db.getCollection("test");
    
     public  static ArrayList<String> palabras;
 
     public static List<String> stopwords;
 
	static Map<String, List<Tuple>> index = new HashMap<>();
	static List<String> files = new ArrayList<>();
 
	public void indexFile(String titulo, String cuerpo) throws IOException {
            
            // estructurra indice
            // (key,value) -> (palabra, [( documentoID, ocurrencias  ),...] )
            
            try (FileReader fr = new FileReader("stop-words-spanish.txt")) {//<editor-fold defaultstate="collapsed" desc="comment">
                
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
//</editor-fold>
            
            int fileno = files.indexOf(titulo);
            if (fileno == -1) {
                    files.add(titulo);
                    fileno = files.size() - 1;
            }

            int pos = 0;

            StringTokenizer st = new StringTokenizer(cuerpo);
            // iterate through st object to get more tokens from it
            while (st.hasMoreElements()) {
                String token = st.nextElement().toString();
                String word = token.toLowerCase();
                pos++;
                if (stopwords.contains(word))
                        continue;
                // cargo (palabra, [( doc, ocurrencias  ),...] )
                List<Tuple> idx = index.get(word);
                // si no existe en el indice la palabra, creo una lista de 
                // values(doc, ocurrencias) i agrego el doc,ocurrencia
                if (idx == null) {
                    idx = new LinkedList<>();
                    index.put(word, idx);
                    idx.add(new Tuple(fileno, 1));
                }
                // Si ya existe la palabra en el indice
                // busco el documento en los values asociados
                else{
                    // si ya existe el documento en el indice
                    // incremento las ocurrencias de la palabra
                    boolean flag = false;
                    for (int i = 0; i < idx.size(); i++) {
                        if( idx.get(i).fileno ==  fileno){
                            idx.get(i).count++;
                            flag = true;
                        }
                    }
                    // si no existe este documento en el indice
                    // agrego documento contando una ocurrencia
                    if(flag == false){
                        idx.add(new Tuple(fileno, 1));
                        flag = true;
                    }
                }
            }
            
            System.out.println("indexed " + titulo + " " + pos + " words");
	}
 
	public static void search(List<String> words) {
            List<Tuple3> answer = new LinkedList<>();
            
            for (int i=0; i<words.size(); i++) {
                String word = words.get(i).toLowerCase();
                // para cada palabra cargo sus values(doc, ocurrencias)
                List<Tuple> idx = index.get(word);
                // si existen values
                if (idx != null) {
                    // para cada tupla que existe
                    for (Tuple t : idx) {
                        Tuple3 e = new Tuple3(files.get(t.fileno),(int)t.fileno ,(int)t.count);
                        boolean presente = false;
                        for(int j=0; j<answer.size(); j++){
                            // si la estoy mostrando
                            if ( answer.get(j).titulo.equals(e.titulo) ) {
                                // sumo la cantidad de ocurrencias(por cada palabra de la query)
                                // a.k.a. query = "hoy mismo"
                                // -> hoy   -> doc3, 4 ocurrencias
                                // -> mismo -> doc3, 2 ocurrencias
                                // answer -> doc3, (4+2) ocurrencias
                                // asi rankeo las respuestas
                                answer.get(j).count = answer.get(j).count + e.count;
                                presente = true;
                            }
                        }                                                
                        // si no la estoy mostrando en mi respuesta
                        if(presente == false){
                            // la agrego
                            answer.add(e);  
                            presente = true;
                        }                        
                    }
                }
            }
            
            Collections.sort(answer, new Tuple3Comparator() );
            Collections.reverse(answer);
            
            System.out.print("Busqueda: " + words + "\nResultados: ");
            for (Tuple3 f : answer) {
                    System.out.print("\n   " + f.titulo + ", ocurrencias: " + f.count);
            }
            System.out.println("");
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
	}
        
        public void buscar(InvertedIndex idx, String query){
            search(Arrays.asList(query.split(",")));
        }

    static class Tuple3 {
        String titulo;
        int fileno;
        int count;

        public Tuple3(String titulo, int fileno, int count) {
            this.titulo = titulo;
            this.fileno = fileno;
            this.count = count;
        }
    }

    
 
    private class Tuple {
        private int fileno;
        private int count;

        public Tuple(int fileno, int count) {
            this.fileno = fileno;
            this.count = count;
        }
    }
    
    static class Tuple3Comparator implements Comparator<Tuple3> {
        @Override
        public int compare(Tuple3 t0, Tuple3 t1) {
            return Integer.valueOf(t0.count).compareTo(t1.count);
        }
    }
    
}