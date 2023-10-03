/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mineria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author prayt
 */
public class Datamart {
    String[][] Datamart;
    
    double[][] numbers;
    double[][] corrMatriz;
    double correlacion;
    int caso;
    
    String[][] nominals;
    double[][] chiMatriz;
    double chi;
    
    public Datamart(double[][] numbers, double[][] corrMatriz, double correlacion, int caso, String[][] nominals, double[][] chiMatriz){
        this.numbers = numbers;
        this.corrMatriz = corrMatriz;
        this.correlacion = correlacion;
        this.caso = caso;
        
        this.nominals = nominals;
        this.chiMatriz = chiMatriz;
        chi = 1.0;
    }
    
    public String[][] Datamart(){
        int[] numericos, nominales;
        int i, j;
        
        int contNum, contNom;
        
        numericos = numericos();
        nominales = nominales();
        
        contNum = conteoNum(numericos);
        contNom = conteo(nominales);
        
        Datamart = new String[contNum + contNom][numbers[0].length];
        System.out.println("DATA: " + Datamart.length);
        i = 0;
        j = 0;
        while( i < contNum){
            if (numericos[j] >= total(numericos)){
                for (int w = 0; w < Datamart[0].length; w++){
                    Datamart[i][w] = Double.toString(numbers[j][w]);
                }
                i++;
            }
            j++;
        }
        
        i = contNum;
        j = i - contNum;
        while( i < contNum + contNom){
            if (nominales[j] >= total(nominales)){
                Datamart[i] = nominals[j];
                i++;
            }
            j++;
        }
        
        return Datamart;
    }
    
    public int[] numericos(){
        int[] conjuntos = new int[corrMatriz.length ];
        int i,j;
        Arrays.fill(conjuntos, 0);
        
        for (i = 0; i < corrMatriz.length; i++){
            for (j = i + 1; j < corrMatriz[i].length; j++){
                
                switch(caso){
                    case 0:
                            if (corrMatriz[i][j] >= correlacion){
                                conjuntos[i] += 1;
                                conjuntos[j] += 1;
                            }
                        break;

                    case 1:
                            if (corrMatriz[i][j] <= (-1*correlacion)){
                                conjuntos[i] += 1;
                                conjuntos[j] += 1;
                            }
                        break;

                    case 2:
                            if (corrMatriz[i][j] >= correlacion || corrMatriz[i][j] <= (-1*correlacion)){
                                conjuntos[i] += 1;
                                conjuntos[j] += 1;
                            }
                        break;
                }
                
            }
        }
        
        for (i = 0; i < conjuntos.length; i++){
            System.out.print("[" + conjuntos[i] + "]");
        }
        System.out.println("");
        return conjuntos;
    } 
    
    public int[] nominales(){
        int[] conjuntos = new int[chiMatriz.length];
        int i,j;
        Arrays.fill(conjuntos, 0);
        
        for (i = 0; i < chiMatriz.length; i++){
            for (j = i + 1; j < chiMatriz[i].length; j++){
                if (chiMatriz[i][j] > 0){
                    conjuntos[i] += 1;
                    conjuntos[j] += 1;
                }
            }
        }
        
        for (i = 0; i < conjuntos.length; i++){
            System.out.print("[" + conjuntos[i] + "]");
        }
        System.out.println("");
        return conjuntos;
    }
    
    public double total(int[] arreglo){
        double total = 0;
        for (int i = 0; i < arreglo.length; i++){
            total += arreglo[i];
        }
        
        total = total/arreglo.length;
        
        return total;
    }
    
    public int conteo(int[] arreglo){
        int conteo = 0;
        System.out.println("");
        for (int i = 0; i < arreglo.length; i++){
            System.out.print("[" + arreglo[i] + "]");
            if (arreglo[i] > total(arreglo)){
                conteo += 1;
            }
        }
        System.out.println("");
        return conteo;
    }
    
    public int conteoNum(int[] arreglo){
        int conteo = 0;
        System.out.println("");
        for (int i = 0; i < arreglo.length; i++){
            System.out.print("[" + arreglo[i] + "]");
            if (arreglo[i] >= total(arreglo)){
                conteo += 1;
            }
        }
        System.out.println("");
        return conteo;
    }
    
    public double[][] matrizNumericos(String[] clases){
        double[][] numericos;
        int cont = 0;
        int i, j;
        
        for (i = 0; i < Datamart.length; i++){
            if (Datamart[i][0].equals("0.0")){
                cont++;
            }
        }
        
        numericos = new double[Datamart[0].length][cont];
        
        for (i = 0; i < Datamart[0].length; i++){
            for (j = 0; j < cont; j++){
                numericos[i][j] = Double.parseDouble(Datamart[j][i]);
            }
        }
        
        /*for (i = 0; i < Datamart[0].length; i++){
            numericos[i][cont] = Double.parseDouble(clases[i]);
        }*/
        
        return numericos;
    }
    
    public String[][] matrizNominales(String[] clases){
        String[][] nominales;
        int cont = 0, contNom;
        int i, j;
        
        for (i = 0; i < Datamart.length; i++){
            if (Datamart[i][0].equals("0.0")){
                cont++;
            }
        }
        
        contNom = Datamart.length - cont;
        
        nominales = new String[Datamart[0].length][contNom];
        
        for (i = 0; i < Datamart[0].length; i++){
            for (j = 0; j < contNom; j++){
                nominales[i][j] = Datamart[j + cont][i];
            }
        }
        
        /*for (i = 0; i < Datamart[0].length; i++){
            nominales[i][contNom] = clases[i];
        }*/
        
        return nominales;
    }
}
