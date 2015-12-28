/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab_2_sd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

/**
 *
 * @author Nelson
 */
public class filtroStopWords {

    static String filtrar(String cadena) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader("stop-words-spanish.txt");
        
        if(fr == null){
            System.out.println("Archivo erroneo");
            System.exit(1);
        }
        BufferedReader bf = new BufferedReader(fr);
        
        ArrayList<String>palabras = new ArrayList();
        String aux1;
        
        while( (aux1 = bf.readLine() ) != null ){
            palabras.add(aux1);
        }
        
        bf.close();
        fr.close();
        
        String delim =".,:;\\]\\[\\}\\{-_";
        
        for (int i = 0; i < palabras.size(); i++) {
            //System.out.println(palabras.get(i));
        }
        
        
        String[] cadenaArray = cadena.split(delim);
        
        cadena = Arrays.toString(cadenaArray);
        
        System.out.println(cadena);
        
        for (int i = 0; i < palabras.size(); i++) {
            cadena = cadena.replace(palabras.get(i), "");
        }
        
        return cadena;
    }
    
}
