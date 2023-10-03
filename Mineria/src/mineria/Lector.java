/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mineria;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author prayt
 */
public class Lector {
    
    String fileName;
    String lines;
    StringTokenizer elementos;
    int [] clasificador;
    ArrayList<String> lineas;
    
    public Lector(String fileName){
        this.fileName = fileName;
        lines = new String();
        elementos = null;
        clasificador = null;
        lineas = new ArrayList();
    }
    
    public String[][] lee(){
        String[][] datos = null;
        
        int i = 0, j = 0;
        
        int bandera = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            lines = br.readLine(); // Guarda la primera línea en la posición 0 del array
            elementos = new StringTokenizer(lines, ",");
            clasificador = new int[elementos.countTokens()];
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        while(elementos.hasMoreElements()){
            clasificador[i] = Integer.parseInt((String) elementos.nextElement());
            i++;
        }

        System.out.println("La primera línea del archivo es: " + lines);
        
        for (i = 0; i < clasificador.length; i++){
            System.out.println("" + clasificador[i]);
        }
        
        
         try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((lines = br.readLine()) != null) { // Lee el archivo línea por línea hasta llegar al final
                if (bandera > 0){
                   //System.out.println(lines); // Imprime cada línea en la consola 
                   lineas.add(lines);
                   
                }
                
                bandera++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
         
        System.out.println("" +  bandera);
        
        datos = new String[clasificador.length][lineas.size()];
        
        for (j = 0; j < lineas.size(); j++){
            if (j > 0){
                String aux = lineas.get(j);
                elementos = new StringTokenizer(aux, ",");
            }
            for (i = 0; i < clasificador.length; i++){
                if (j == 0){
                    datos[i][j] = Integer.toString(clasificador[i]);
                    //System.out.print("[" + datos[i][j] + "]");
                }else{
                    datos[i][j] = (String) elementos.nextElement();
                }
            }
        }
        
        return datos;
    }
    
    public double[][] numericos(String[][] datos){
        double[][] numericos = null;
        
        return numericos;
    }
    
    public void imprimir(String[][] matriz){
        int i,j;
        for (i = 0; i < matriz.length; i++){
            for (j = 0; j < matriz[i].length; j++){
                System.out.print("[" + matriz[i][j] + "]");
            }
            System.out.println("");
        }
    }
    
    public void imprimirDouble(double[][] matriz){
        int i,j;
        for (i = 0; i < matriz.length; i++){
            for (j = 0; j < matriz[i].length; j++){
                System.out.print("[" + matriz[i][j] + "]");
            }
            System.out.println("");
        }
    }
    
}
