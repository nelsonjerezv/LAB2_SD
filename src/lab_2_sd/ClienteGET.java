
package lab_2_sd;

import java.io.*;
import java.net.*;

public class ClienteGET {
    
    public static void main(String args[]) throws Exception{
        //Variables
        String fromServer;
       
        //Buffer para recibir desde consola
        System.out.print("Consultar: ");
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        String sentence = inFromUser.readLine();
        
        // estructura improvisada
        sentence = "GET /consulta/" + sentence;
        String[] requests = {sentence};
        
        for (String request : requests) {
            //Socket para el cliente (host, puerto)
            Socket clientSocket = new Socket("localhost", 1235);

            //Buffer para enviar el dato al server
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            //Buffer para recibir dato del servidor
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //Leemos del cliente y lo mandamos al servidor
            outToServer.writeBytes(request + '\n');

            //Recibimos del servidor
            fromServer = inFromServer.readLine();
            System.out.println("Server response: " + fromServer);

            //Cerramos el socket
            clientSocket.close();
        }
    }    
}
