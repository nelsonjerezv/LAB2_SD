/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab_2_sd;

import org.xml.sax.Attributes;  
import org.xml.sax.SAXException;  
import org.xml.sax.helpers.DefaultHandler;  
  
public class ManejadorEjemplo extends DefaultHandler{  
    private String valor = "";
    private String cadena = "";
    private String titulo = "";

   @Override  
   public void startDocument() throws SAXException {  
      System.out.println("\nPrincipio del documento...");  
   }  
  
   @Override  
   public void endDocument() throws SAXException {  
      System.out.println("\nFin del documento...");  
   }  
  
   @Override  
   public void startElement(String uri, String localName, String name,  
        Attributes attributes) throws SAXException {
   }  
     
   @Override  
   public void characters(char[] ch, int start, int length)  
         throws SAXException {  
      //System.out.println("\nProcesando texto dentro de una etiqueta... ");
      
      cadena = new String(ch, start, length);
      valor = valor + cadena;
   }  
  
   @Override  
   public void endElement(String uri, String localName, String name)  
         throws SAXException {  
       if(localName.equals("title")){
           titulo = valor;
           System.out.println(titulo);
       }
       if (localName.equals("page")){
           System.out.println(valor);
           valor = "";
           System.out.println("----------------------");
        }
       if (localName.equals("siteinfo")){
           valor = "";
        }
       if (localName.equals("format")){
           valor = "";
        }


   }  
  
}