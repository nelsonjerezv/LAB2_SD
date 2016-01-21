/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab_2_sd;

import java.net.ServerSocket;
import java.net.Socket;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Nelson
 */
public class IndexStart {

    public static DB db;
    public static DBCollection coleccion;
    public static DBCollection index_db;
    public static Mongo mongo;
    public static String nombre_BD = null;
    public static String nombre_coleccion_DB = null;
    public static String nombre_coleccion_DB_Index = null;
    public static int puerto_mongoDB;
    public static int puerto_cache;
    public static int nParticiones;
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, Exception {
        
        // Lectura de parametros
        
        FileReader fr = new FileReader("IndexConf.ini");
        
        if(fr == null){
            System.out.println("Archivo erroneo");
            System.exit(1);
        }
        BufferedReader bf = new BufferedReader(fr);
        String parametro;
        int counter = 0;
        int miPuerto = 0;
        
        while( (parametro = bf.readLine() ) != null ){
            if(parametro.charAt(0) != '/'){
                switch (counter) {
                    case 0:
                        miPuerto = Integer.parseInt(parametro);
                        System.out.println("parametro " + counter + ": " + miPuerto);
                        break;
                    case 1:
                        nParticiones = Integer.parseInt(parametro);
                        System.out.println("parametro " + counter + ": " + nParticiones);
                        break;
                    case 2:
                        nombre_BD = parametro;
                        System.out.println("parametro " + counter + ": " + nombre_BD);
                        break;
                    case 3:
                        nombre_coleccion_DB = parametro;
                        System.out.println("parametro " + counter + ": " + nombre_coleccion_DB);
                        break;
                    case 4:
                        nombre_coleccion_DB_Index = parametro;
                        System.out.println("parametro " + counter + ": " + nombre_coleccion_DB_Index);
                        break;
                    case 5:
                        puerto_mongoDB = Integer.parseInt(parametro);
                        System.out.println("parametro " + counter + ": " + puerto_mongoDB);
                        break;
                    case 6:
                        puerto_cache = Integer.parseInt(parametro);
                        System.out.println("parametro " + counter + ": " + puerto_cache);
                        break;
                    default:
                        System.out.println("parametro " + counter + ": " + parametro + "no es usado");
                        break;
                }
                counter++;            
            }
        }
        mongo = new Mongo("localhost",puerto_mongoDB);
        db = mongo.getDB(nombre_BD);
        coleccion = db.getCollection(nombre_coleccion_DB);
        index_db = db.getCollection(nombre_coleccion_DB_Index);
        
        ArrayList<Index> Particiones = new ArrayList();
        // tam particion = asdasdasd
        for (int i = 0; i < nParticiones; i++) {
//            Index index = new Index(/* var tamano */);
//            Particiones.add(index);
        }
        
        ServerSocket ssock = new ServerSocket(miPuerto);
        System.out.println("Listening...");
        while (true) {
           Socket sock = ssock.accept();
           System.out.println("Connected");
           new Thread(new MultiThreadServer(sock, Particiones)).start();
        }
        
    }
    
}
