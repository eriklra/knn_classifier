/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mineria;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

/**
 *
 * @author prayt
 */
public class Chi {
    double[][] chiMatriz;
    double margenError;
    String[][] nominals;
    
    public Chi(String[][] nominals, double margenError){
        this.nominals = nominals;
        this.margenError = margenError;
        chiMatriz = new double[nominals.length][nominals.length];
    }
    
    public double chiCuadrada(String[] x, String[] y){
        double chi = 0;
        
        int i = 0;
        int j = 0;
        
        int w = 0;
        int z = 0;
        
        
        int xElementos = Integer.parseInt(x[0]);
        int yElementos = Integer.parseInt(y[0]);
        int[][] tablaContigencia = new int[xElementos + 1][yElementos + 1];
        double[][] tablaEsperados = new double[xElementos][yElementos];
        
        for (i = 0; i < xElementos + 1; i++){
            for (j = 0; j < yElementos + 1; j++){
                tablaContigencia[i][j] = 0;
            }
        }
       
        for (w = 0; w < xElementos; w++){
            for (z = 0; z < yElementos; z++){
                for (i = 1; i < x.length; i++){
                    for (j = 1; j < y.length; j++){
                        if (w == Integer.parseInt(x[i]) && z == Integer.parseInt(y[j]) && i == j){
                              tablaContigencia[w][z]++;
                        }
                    }
                }
            }
        }
        
        for (i = 0; i < xElementos; i++){
            for (j = 0; j < yElementos; j++){
                tablaContigencia[i][yElementos] += tablaContigencia[i][j];
            }
        }
        
        for (j = 0; j < yElementos; j++){
            for (i = 0; i < xElementos; i++){
                tablaContigencia[xElementos][j] += tablaContigencia[i][j];
            }
        }
        
        for (i = 0; i < xElementos; i++){
            tablaContigencia[xElementos][yElementos] += tablaContigencia[i][yElementos];
        }
        
        for (i = 0; i < xElementos; i++){
            for (j = 0; j < yElementos; j++){
                tablaEsperados[i][j] = (double) (tablaContigencia[i][yElementos] * tablaContigencia[xElementos][j])/tablaContigencia[xElementos][yElementos];
                double aux = tablaContigencia[i][j] - tablaEsperados[i][j];
                aux = aux*aux;
                if (tablaEsperados[i][j] == 0){
                    chi += 0;
                }else{
                    chi += aux/tablaEsperados[i][j];
                }
            }
        }
        
        return chi;
    }
    
    public int validacionChi(double chi, double margenError, int gradoLibertad){
        int validado = 0;
        
        ChiSquaredDistribution Valor = new ChiSquaredDistribution(gradoLibertad);
        double x = Valor.inverseCumulativeProbability(1 - margenError);
        /*System.out.println("Grado: " + gradoLibertad + " Margen: " + margenError);
        System.out.printf("\n\n%.3f\t\t", x);*/
        if (chi > x){
            validado = 1;
        }else{
            validado = 0;
        }
        
        return validado;
    }
    
    public double[][] matrizChi(){
        double chi;
        int gradoLibertad;
        int i, j;
        
        for (i = 0; i < nominals.length; i++) {
            for (j = i; j < nominals.length; j++) {
                
                
                
                
                // Calcula el chi los arreglos i y j
                chi = chiCuadrada(nominals[i], nominals[j]);
                gradoLibertad = (Integer.parseInt(nominals[i][0]) - 1)*(Integer.parseInt(nominals[j][0]) - 1);
                int a = validacionChi(chi, margenError, gradoLibertad);
                // Almacena el chi en la matriz
                chiMatriz[i][j] = a;
                chiMatriz[j][i] = a;
            }
        }
        
        return chiMatriz;
    }
}
