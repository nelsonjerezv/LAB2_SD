/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab_2_sd;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nelson
 */
public class MultiThreadServer implements Runnable {
    
    Socket csocket;
    // Arreglo con las particiones index
    ArrayList<Index> particiones;
    private String fromClient;

    MultiThreadServer(Socket csocket, ArrayList<Index> Particiones) {
        this.csocket = csocket;  
        this.particiones = Particiones;
    }

    @Override
    public void run() {
        try {
            //Buffer para leer al cliente
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
            //Buffer para enviar al cliente
            DataOutputStream outToClient = new DataOutputStream(csocket.getOutputStream());

            //Recibimos el dato del cliente y lo mostramos en el server
            fromClient =inFromClient.readLine();
            System.out.println("===== ===== ===== ===== =====");

            String[] tokens = fromClient.split(" ");
            String parametros = tokens[1];

            String http_method = tokens[0];

            String[] tokens_parametros = parametros.split("/");

            String resource = tokens_parametros.length > 1 ? tokens_parametros[1] : "";
            String id = tokens_parametros.length > 2 ? tokens_parametros[2] : "";

            //String meta_data = tokens.length > 2 ? tokens[2] : "";
            String meta_data = "";
            if(tokens.length > 2){
                for (int i = 2; i < tokens.length; i++) {
                    id = id + " " +tokens[i];
                }
            }

            System.out.println("CONSULTA:       " + fromClient);
            System.out.println("HTTP METHOD:    " + http_method);
            System.out.println("Resource:       " + resource);
            System.out.println("ID:             " + id);
            System.out.println("META DATA:      " + meta_data);  
            
            // Determinamos la particion a acceder con una funcion hash
            //int ParticionDestino = hash(id, particiones.size());
            //System.out.println("Particion destino: " + ParticionDestino);
            
            switch (http_method) {
                case "GET":
                    
                    System.out.println("Buscando en el index de '" + resource + "' el registro con id " + id);
                    /*
                    
                    
                    
                    // buscar en el cache estatico la respuesta a la query
                    String result = particiones.get(particiones.size()-1).getEntryFromCache(id);

                    if(result != null){//Si está
                        System.out.println("Entrada en el cache estático - Particion: "+(particiones.size()-1));
                        // Mostramos el cache(querys y answers)
                        particiones.get(particiones.size()-1).print(); System.out.println("");
                    }else{ //Si no está en el caché estático, verifico a la particion correspondiente
                        result = particiones.get(ParticionDestino).getEntryFromCache(id);
                        // Mostramos el cache(querys y answers)
                        particiones.get(ParticionDestino).print(); System.out.println("");
                    }
                    if (result == null) { // MISS
                        System.out.println("MISS");
                        //Enviamos miss al cliente
                        outToClient.writeBytes("MISS\n");
                    }else{
                        System.out.println("HIT");
                        // Enviamos hit al cliente
                        outToClient.writeBytes(result+"\n");
                    }
                    
                    
                    
                    */
                    break;
                default:
                    break;
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MultiThreadServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int hash(String id, int size) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
