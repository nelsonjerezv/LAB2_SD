/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab_2_sd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Nelson
 */
public class IndexStart {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        // Lectura de parametros
        
        FileReader fr = new FileReader("IndexConf.ini");
        
        if(fr == null){
            System.out.println("Archivo erroneo");
            System.exit(1);
        }
        BufferedReader bf = new BufferedReader(fr);
        String parametro;
        int counter = 0;
        int miPuerto;
        int nParticiones;
        
        while( (parametro = bf.readLine() ) != null ){
            switch (counter) {
                case 0:
                    miPuerto = Integer.parseInt(parametro);
                    System.out.println("parametro " + counter + ": " + miPuerto);
                    break;
                case 1:
                    nParticiones = Integer.parseInt(parametro);
                    System.out.println("parametro " + counter + ": " + nParticiones);
                    break;
                default:
                    System.out.println("parametro " + counter + ": " + parametro);
                    break;
            }
            counter++;            
        }
        
        // ************************************************ \\
        
        
    }
    
}
