/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mineria;

/**
 *
 * @author prayt
 */
public class Mineria {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        String [][] datos;
        Lector data = new Lector("test-2.txt");
        datos = data.lee();
        data.imprimir(datos);
        
        double[][] numbers, auxNum;
        String[][] nominals, auxNom;
        

        double[][] numericos; //limpios
        String[][] nominales; //limpios
        
        String[] clases;
        
        String[][] Datamart;
        
        int i = 0;
        int j = 0;
        int w = 0;
        
        int aux1, aux2;
        
        double[][] corrMatriz;
        double correlacion = 0.2;
        
        double[][] chiMatriz;
        double margenError = 0.05;
        
        int k_fold = 10;
        int k = 3;
        
        for (w = 0; w < data.clasificador.length - 1; w++){
            if (data.clasificador[w] == 0){
                i++;
            }else{
                j++;
            }
        }
        
        numbers = new double[i][datos[0].length];
        nominals = new String[j][datos[0].length];
        
        auxNum = new double[datos[0].length][i];
        auxNom = new String[datos[0].length][j];
        
        System.out.println("numbers:" + i + "a" + datos.length + "b" + datos[0].length);
        System.out.println("nominals:" + j);
        
        for (j = 0; j < datos[0].length; j++){
            aux1 = 0;
            aux2 = 0;
            for (i = 0; i < datos.length - 1; i++){
                
                if (data.clasificador[i] == 0){
                    numbers[aux1][j] = Double.parseDouble(((String)datos[i][j]));
                    aux1++;
                }else{
                    nominals[aux2][j] = datos[i][j];
                    aux2++;
                }
            }
        }
        
        clases = datos[datos.length - 1];
        
        for (i = 0; i < auxNum.length; i++){
            for (j = 0; j < auxNum[0].length; j++){
                auxNum[i][j] = numbers[j][i];
            }
        }
        
        for (i = 0; i < auxNom.length; i++){
            for (j = 0; j < auxNom[0].length; j++){
                auxNom[i][j] = nominals[j][i];
            }
        }
        
        System.out.println("NUMBERS EN BRUTO");
        data.imprimirDouble(auxNum);
        System.out.println("NOMINALS EN BRUTO");
        data.imprimir(auxNom);
        
        System.out.println("NOMINALS -----------------");
        data.imprimir(nominals);
        System.out.println("");
        System.out.println("NUMBERS -----------------");
        data.imprimirDouble(numbers);
        System.out.println("");
        
        System.out.println("CLASES ------------------");
        for (i = 0; i < clases.length; i++){
            System.out.print("[" + clases[i] + "]");
        }
        System.out.println("");
        System.out.println("");
        
        System.out.println("MATRIZ DE CORRELACION ----------------");
        Pearson pearson = new Pearson(numbers, correlacion);
        corrMatriz = pearson.matrizCorrelacion();
        data.imprimirDouble(corrMatriz);
        System.out.println("");
        
        System.out.println("MATRIZ CHI CUADRADA ------------------");
        Chi chi = new Chi(nominals, margenError);
        chiMatriz = chi.matrizChi();
        data.imprimirDouble(chiMatriz);
        System.out.println("");
        
        System.out.println("DATAMART -----------------------------");
        Datamart datamart = new Datamart(numbers, corrMatriz, correlacion, 1, nominals, chiMatriz);
        Datamart = datamart.Datamart();
        data.imprimir(Datamart);
        System.out.println("");
        
        System.out.println("NUMERICOS ----------------------------");
        numericos = datamart.matrizNumericos(clases);
        data.imprimirDouble(numericos);
        System.out.println("");
        
        System.out.println("NOMINALES ----------------------------");
        nominales = datamart.matrizNominales(clases);
        data.imprimir(nominales);
        System.out.println("");
        
        System.out.println("KNN ----------------------------------");
        Knn knn = new Knn(numericos, nominales, clases, k_fold, k);
        knn.KNN();
        System.out.println("");
        System.out.println("KNN NUMERICOS ------------------------");
        knn.numericosKNN();
        System.out.println("");
        System.out.println("KNN NOMINALES ------------------------");
        knn.nominalesKNN();
        System.out.println("");
    }
    
}
