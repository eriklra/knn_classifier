/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mineria;

/**
 *
 * @author prayt
 */
public class Pearson {
    double[][] corrMatrix;
    double correlacion;
    double[][] numbers;
    
    public Pearson(double[][] numbers, double correlacion){
        this.numbers = numbers;
        this.correlacion = correlacion;
        corrMatrix = new double[numbers.length][numbers.length];
    }
    
    public double pearsonCorrelacion(double[] x, double[] y){
        if (x.length != y.length) {
            throw new IllegalArgumentException("Los arreglos deben tener la misma longitud.");
        }

        int n = x.length;
        double sumX = 0.0;
        double sumY = 0.0;
        double sumXY = 0.0;
        double sumX2 = 0.0;
        double sumY2 = 0.0;

        for (int i = 1; i < n; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXY += x[i] * y[i];
            sumX2 += Math.pow(x[i], 2);
            sumY2 += Math.pow(y[i], 2);
        }

        double numerador = n * sumXY - sumX * sumY;
        double denominador = Math.sqrt((n * sumX2 - Math.pow(sumX, 2)) * (n * sumY2 - Math.pow(sumY, 2)));

        if (denominador == 0) {
            return 0.0;
        }

        return numerador / denominador;
    }
    
    public double[][] matrizCorrelacion(){
        int i, j;
        
        for (i = 0; i < numbers.length; i++) {
            for (j = i; j < numbers.length; j++) {
                double[] x = numbers[i];
                double[] y = numbers[j];

                // Calcula el coeficiente de correlación entre los arreglos i y j
                double corr = pearsonCorrelacion(x, y);

                // Almacena el coeficiente de correlación en la matriz
                corrMatrix[i][j] = corr;
                corrMatrix[j][i] = corr;
            }
        }
        
        return corrMatrix;
    }
}
