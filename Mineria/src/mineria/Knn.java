/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mineria;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author prayt
 */
public class Knn {
    double[][] numericos;
    String[][] nominales;
    String[][] datamart;
    
    String[] clases;
    
    int k_fold;
    int k_neighbor;
    
    public Knn(double[][] numericos, String[][] nominales, String[] clases, int k_fold, int k_neighbor){
        this.numericos = numericos;
        this.nominales = nominales;
        this.clases = clases;
        this.k_fold = k_fold;
        this.k_neighbor = k_neighbor;
    }
    
    public double KNN(){
        double accuracy = 0;
        
        datamart = datamart();
        
        double numerator = 0;
        double denominator = k_fold;
        //Random rnd = new Random();
        int[][] folds = kfolds(datamart.length);
        int i, j, x, y, cont = 0;
        
        for (i = 0; i < folds.length; i++){
            //System.out.println(i + ": ITERACION ---------");
            cont = 0;
            
            String[][] test = new String[folds[i].length][datamart[0].length];
            int[] clasesTest = new int[folds[i].length];
            int[] clasesCorrectas = new int[folds[i].length];
            
            for (x = 0; x < test.length; x++){
                test[x] = datamart[folds[i][x]];
                clasesTest[x] = 0;
                clasesCorrectas[x] = Integer.parseInt(clases[folds[i][x]]);
            }
            
            /*System.out.println("TEST");
            for (x = 0; x < test.length; x++){
                for (y = 0; y < test[0].length; y++){
                    System.out.print("[" + test[x][y] + "]");
                }
                System.out.print(" - " + folds[i][x]);
                System.out.println("");
            }*/
            
            /*System.out.println("" + Arrays.toString(clasesTest));
            System.out.println("" + Arrays.toString(clasesCorrectas));*/
            
            
            for (j = 0; j < folds.length; j++){
                if (j != i){
                    cont += folds[j].length;
                }
            }
            
            String[][] entrenamiento = new String[cont][datamart[0].length];
            int[] clasesEntrenamiento = new int[cont];
            
            j = 0;
            while (j < entrenamiento.length){
                for (x = 0; x < folds.length; x++){
                    if (x != i){
                        for (y = 0; y < folds[x].length; y++){
                            entrenamiento[j] = datamart[folds[x][y]];
                            clasesEntrenamiento[j] = Integer.parseInt(clases[folds[x][y]]);
                            j++;
                        }
                    }
                }
            }
            
            //System.out.println("ENTRENAMIENTO");
            
            for (j = 0; j < folds[i].length; j++){
                String[] testPoint = test[j];
                clasesTest[j] = knn(entrenamiento, clasesEntrenamiento, testPoint);
            }
            
            /*System.out.println("" + Arrays.toString(clasesTest));
            System.out.println("" + Arrays.toString(clasesCorrectas));*/
            
            numerator += accuracy(clasesTest, clasesCorrectas);
            
        }
        
        accuracy = (numerator/denominator) * 100;
        
        System.out.println("ACCURACY final: " + accuracy);
        
        return accuracy;
    }
    
    public int[][] kfolds(int renglones){
        int n = renglones - 1;
        int[] indices = new int[n];

        // crea un arreglo con los índices de las filas
        for (int i = 0; i < n; i++) {
            indices[i] = i + 1;
        }
        
        /*for (int i = 0; i < n; i++) {
            System.out.print("[" + indices[i] + "]");
        }
        System.out.println("");*/

        // revuelve los índices de forma aleatoria
        Random r = new Random();
        for (int i = n - 1; i > 0; i--) {
            int j = r.nextInt(i + 1);
            int temp = indices[i];
            indices[i] = indices[j];
            indices[j] = temp;
        }

        // divide los índices en k folds
        int[][] folds = new int[k_fold][];
        int size = n / k_fold;
        int remainder = n % k_fold;
        int index = 0;

        for (int i = 0; i < k_fold; i++) {
            int foldSize = size + (i < remainder ? 1 : 0);
            folds[i] = Arrays.copyOfRange(indices, index, index + foldSize);
            index += foldSize;
        }

        // imprime los folds
        /*for (int i = 0; i < k_fold; i++) {
            System.out.println("Fold " + (i + 1) + ", " + folds[i].length + ": " + Arrays.toString(folds[i]));
        }*/
        
        return folds;
    }
    
    public double distancia(String[] entrenamiento, String[] prueba) {
        if (entrenamiento.length != prueba.length) {
            throw new IllegalArgumentException("Las matrices de entrenamiento y prueba deben tener la misma longitud");
        }

        double distancia = 0.0;
        double distanciaNum = 0.0;
        for (int i = 0; i < entrenamiento.length; i++) {
            if (esNumerico(entrenamiento[i]) && esNumerico(prueba[i])) {
                // Si los datos son numéricos, calcula la distancia euclidiana
                double diff = Double.parseDouble(entrenamiento[i]) - Double.parseDouble(prueba[i]);
                distanciaNum += diff * diff;
            } else {
                // Si los datos son nominales, agrega 1 si los valores son diferentes
                if (!entrenamiento[i].equals(prueba[i])) {
                    distancia += 1.0;
                } 
            }
        }
        
        distancia += Math.sqrt(distanciaNum);

        return distancia;
    }
    
    public double distanciaJaccard(String[] entrenamiento, String[] prueba) {
        int intersection = 0;
        int union = 0;
        for (int i = 0; i < entrenamiento.length; i++) {
            if (esNumerico(entrenamiento[i]) && esNumerico(prueba[i])) {
                union++;
                
            }
            if (entrenamiento[i].equals(prueba[i])) {
                intersection++;
            }
        }
        return 1.0 - ((double) intersection / (double) union);
    }

    public boolean esNumerico(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public int knn(String[][] entrenamiento, int[] clases, String[] objetoPrueba) {
        double[][] distanciasYClases = new double[entrenamiento.length][2];
        for (int i = 0; i < entrenamiento.length; i++) {
            double distancia = distancia(entrenamiento[i], objetoPrueba);
            distanciasYClases[i][0] = distancia;
            distanciasYClases[i][1] = clases[i];
        }
        Arrays.sort(distanciasYClases, Comparator.comparingDouble(a -> a[0]));
        
        int[] conteo = new int[clases.length];
        for (int i = 0; i < k_neighbor; i++) {
            int claseIndex = (int) distanciasYClases[i][1];
            conteo[claseIndex]++;
        }

        int claseIndexPredicha = 0;
        for (int i = 0; i < conteo.length; i++) {
            if (conteo[i] > conteo[claseIndexPredicha]) {
                claseIndexPredicha = i;
            }
        }
        
        return claseIndexPredicha;
    }
    
    public double accuracy(int[] test, int[] correctClasses){
        double accuracy = 0;
        int correct = 0;
        
        for (int i = 0; i < test.length; i++){
            if (test[i] == correctClasses[i]){
                correct++;
            }
        }
        
        
        accuracy = (double) correct/test.length;
        /*System.out.println("ACCURACY: " + accuracy);
        System.out.println("CORRECT: " + correct);
        System.out.println("DENOMINADOR: " + test.length);
        System.out.println("RESULTADO: " + correct/test.length);*/
        
        return accuracy;
    }
    
    public String[][] datamart(){
        String[][] data = new String[nominales.length][nominales[0].length + numericos[0].length];
        int i, j, z;
        
        for(i = 0; i < data.length; i++){
            for (j = 0; j < nominales[0].length; j++){
                data[i][j] = nominales[i][j];
            }
            for (z = nominales[0].length; z < nominales[0].length + numericos[0].length; z++){
                data[i][z] = Double.toString(numericos[i][z - nominales[0].length]);
            }
        }
        
        /*System.out.println("DATAMART DE KNN");
        for(i = 0; i < data.length; i++){
            for (j = 0; j < data[0].length; j++){
                System.out.print("[" + data[i][j] + "]");
            }
            System.out.println("");
        }*/
            
        return data;
    }
    
    public double numericosKNN(){
        double accuracy = 0;
        
        double numerator = 0;
        double denominator = k_fold;
        //Random rnd = new Random();
        int[][] folds = kfolds(numericos.length);
        int i, j, x, y, cont = 0;
        
        for (i = 0; i < folds.length; i++){
            //System.out.println(i + ": ITERACION ---------");
            cont = 0;
            
            double[][] test = new double[folds[i].length][numericos[0].length];
            int[] clasesTest = new int[folds[i].length];
            int[] clasesCorrectas = new int[folds[i].length];
            
            for (x = 0; x < test.length; x++){
                test[x] = numericos[folds[i][x]];
                clasesTest[x] = 0;
                clasesCorrectas[x] = Integer.parseInt(clases[folds[i][x]]);
            }
            
            /*System.out.println("TEST");
            for (x = 0; x < test.length; x++){
                for (y = 0; y < test[0].length; y++){
                    System.out.print("[" + test[x][y] + "]");
                }
                System.out.print(" - " + folds[i][x]);
                System.out.println("");
            }*/
            
            /*System.out.println("" + Arrays.toString(clasesTest));
            System.out.println("" + Arrays.toString(clasesCorrectas));*/
            
            
            for (j = 0; j < folds.length; j++){
                if (j != i){
                    cont += folds[j].length;
                }
            }
            
            double[][] entrenamiento = new double[cont][numericos[0].length];
            int[] clasesEntrenamiento = new int[cont];
            
            j = 0;
            while (j < entrenamiento.length){
                for (x = 0; x < folds.length; x++){
                    if (x != i){
                        for (y = 0; y < folds[x].length; y++){
                            entrenamiento[j] = numericos[folds[x][y]];
                            clasesEntrenamiento[j] = Integer.parseInt(clases[folds[x][y]]);
                            j++;
                        }
                    }
                }
            }
            
            //System.out.println("ENTRENAMIENTO");
            /*for (x = 0; x < entrenamiento.length; x++){
                for (y = 0; y < entrenamiento[x].length; y++){
                    System.out.print("[" + entrenamiento[x][y] + "]");
                }
                System.out.print(" - " + clasesEntrenamiento[x]);
                
                System.out.println("");
            }*/
            
            for (j = 0; j < folds[i].length; j++){
                double[] testPoint = test[j];
                clasesTest[j] = knnNumericos(entrenamiento, clasesEntrenamiento, testPoint);
            }
            
            /*System.out.println("" + Arrays.toString(clasesTest));
            System.out.println("" + Arrays.toString(clasesCorrectas));*/
            
            numerator += accuracy(clasesTest, clasesCorrectas);
            
        }
        
        accuracy = (numerator/denominator) * 100;
        
        System.out.println("ACCURACY final: " + accuracy);
        
        return accuracy;
    }
    
    public double distanciaEuclidiana(double[] puntoA, double[] puntoB) {
        double distancia = 0.0;
        for (int i = 0; i < puntoA.length; i++) {
            distancia += Math.pow((puntoA[i] - puntoB[i]), 2);
        }
        return Math.sqrt(distancia);
    }
    
    public int knnNumericos(double[][] entrenamiento, int[] clases, double[] objetoPrueba) {
        double[][] distanciasYClases = new double[entrenamiento.length][2];
        for (int i = 0; i < entrenamiento.length; i++) {
            double distancia = distanciaEuclidiana(entrenamiento[i], objetoPrueba);
            distanciasYClases[i][0] = distancia;
            distanciasYClases[i][1] = clases[i];
        }
        Arrays.sort(distanciasYClases, Comparator.comparingDouble(a -> a[0]));
        
        int[] count = new int[clases.length];
        for (int i = 0; i < k_neighbor; i++) {
            int classIndex = (int) distanciasYClases[i][1];
            count[classIndex]++;
        }

        int predictedClassIndex = 0;
        for (int i = 0; i < count.length; i++) {
            if (count[i] > count[predictedClassIndex]) {
                predictedClassIndex = i;
            }
        }
        
        return predictedClassIndex;
    }
    
    public double nominalesKNN(){
        double accuracy = 0;
        
        double numerator = 0;
        double denominator = k_fold;
        //Random rnd = new Random();
        int[][] folds = kfolds(numericos.length);
        int i, j, x, y, cont = 0;
        
        for (i = 0; i < folds.length; i++){
            //System.out.println(i + ": ITERACION ---------");
            cont = 0;
            
            String [][] test = new String[folds[i].length][nominales[0].length];
            int[] clasesTest = new int[folds[i].length];
            int[] clasesCorrectas = new int[folds[i].length];
            
            for (x = 0; x < test.length; x++){
                test[x] = nominales[folds[i][x]];
                clasesTest[x] = 0;
                clasesCorrectas[x] = Integer.parseInt(clases[folds[i][x]]);
            }
            
            /*System.out.println("TEST");
            for (x = 0; x < test.length; x++){
                for (y = 0; y < test[0].length; y++){
                    System.out.print("[" + test[x][y] + "]");
                }
                System.out.print(" - " + folds[i][x]);
                System.out.println("");
            }*/
            
            /*System.out.println("" + Arrays.toString(clasesTest));
            System.out.println("" + Arrays.toString(clasesCorrectas));*/
            
            
            for (j = 0; j < folds.length; j++){
                if (j != i){
                    cont += folds[j].length;
                }
            }
            
            String[][] entrenamiento = new String[cont][nominales[0].length];
            int[] clasesEntrenamiento = new int[cont];
            
            j = 0;
            while (j < entrenamiento.length){
                for (x = 0; x < folds.length; x++){
                    if (x != i){
                        for (y = 0; y < folds[x].length; y++){
                            entrenamiento[j] = nominales[folds[x][y]];
                            clasesEntrenamiento[j] = Integer.parseInt(clases[folds[x][y]]);
                            j++;
                        }
                    }
                }
            }
            
            //System.out.println("ENTRENAMIENTO");
            /*for (x = 0; x < entrenamiento.length; x++){
                for (y = 0; y < entrenamiento[x].length; y++){
                    System.out.print("[" + entrenamiento[x][y] + "]");
                }
                System.out.print(" - " + clasesEntrenamiento[x]);
                
                System.out.println("");
            }*/
            
            for (j = 0; j < folds[i].length; j++){
                String[] testPoint = test[j];
                clasesTest[j] = knnNominales(entrenamiento, clasesEntrenamiento, testPoint);
            }
            
            /*System.out.println("" + Arrays.toString(clasesTest));
            System.out.println("" + Arrays.toString(clasesCorrectas));*/
            
            numerator += accuracy(clasesTest, clasesCorrectas);
            
        }
        
        accuracy = (numerator/denominator) * 100;
        
        System.out.println("ACCURACY final: " + accuracy);
        
        return accuracy;
    }
    
    public double distanciaHamming(String[] puntoA, String[] puntoB) {
        if (puntoA.length != puntoB.length) {
            throw new IllegalArgumentException("Los puntos deben tener la misma longitud");
        }
        int distancia = 0;
        for (int i = 0; i < puntoA.length; i++) {
            if (!puntoA[i].equals(puntoB[i])) {
                distancia++;
            }
        }
        return distancia;
    }

    public int knnNominales(String[][] train, int[] classes, String[] test) {
        double[][] distanciasYClases = new double[train.length][2];
        for (int i = 0; i < train.length; i++) {
            double distancia = distanciaHamming(train[i], test);
            distanciasYClases[i][0] = distancia;
            distanciasYClases[i][1] = classes[i];
        }

        Arrays.sort(distanciasYClases, Comparator.comparingDouble(a -> a[0]));
        
        int[] count = new int[clases.length];
        for (int i = 0; i < k_neighbor; i++) {
            int classIndex = (int) distanciasYClases[i][1];
            count[classIndex]++;
        }

        int predictedClassIndex = 0;
        for (int i = 0; i < count.length; i++) {
            if (count[i] > count[predictedClassIndex]) {
                predictedClassIndex = i;
            }
        }
        
        return predictedClassIndex;
    }
}
