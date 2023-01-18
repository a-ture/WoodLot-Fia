package algoritmoGenetico.versione1;

import comune.Tree;
import comune.Farmer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * La stringa [2, 0, 1, 0] rappresenta la soluzione ottimale trovata dall'algoritmo genetico in base alla funzione
 * di fitness utilizzata. Ciascun numero in questa stringa rappresenta l'ID del contadino a cui è stato assegnato
 * un albero specifico. Ad esempio, l'albero con ID 2 è stato assegnato al contadino con ID 0, l'albero con ID 0
 * è stato assegnato al contadino con ID 1 e così via.
 */

//TODO gli alberi devono essere esclusi a priori
public class Starter {
    public static void main(String[] args) {
        List<Tree> trees = new ArrayList<>();

        // ci sono 8 perù 8 argentina 6 italia 8 guatemala
        trees.add(new Tree(1, "Italia"));
        trees.add(new Tree(2, "Argentina"));
        trees.add(new Tree(3, "Perù")); //1
        trees.add(new Tree(4, "Guatemala"));
        trees.add(new Tree(5, "Italia"));
        trees.add(new Tree(6, "Argentina")); //2
        trees.add(new Tree(7, "Perù")); //2
        trees.add(new Tree(8, "Guatemala"));
        trees.add(new Tree(9, "Argentina")); //3
        trees.add(new Tree(10, "Perù")); //3
        trees.add(new Tree(11, "Guatemala"));
        trees.add(new Tree(12, "Italia"));
        trees.add(new Tree(13, "Argentina")); //4
        trees.add(new Tree(14, "Perù")); //4
        trees.add(new Tree(15, "Guatemala"));
        trees.add(new Tree(16, "Italia"));
        trees.add(new Tree(17, "Argentina")); //5
        trees.add(new Tree(18, "Perù")); //5
        trees.add(new Tree(19, "Guatemala"));
        trees.add(new Tree(20, "Italia"));
        trees.add(new Tree(21, "Argentina")); //6
        trees.add(new Tree(22, "Perù")); //6
        trees.add(new Tree(23, "Guatemala"));
        trees.add(new Tree(24, "Argentina")); //7
        trees.add(new Tree(25, "Perù")); //7
        trees.add(new Tree(26, "Guatemala"));
        trees.add(new Tree(27, "Italia"));
        trees.add(new Tree(28, "Argentina")); //8
        trees.add(new Tree(29, "Perù")); //8
        trees.add(new Tree(30, "Guatemala"));

        List<Farmer> farmers = new ArrayList<>();

        farmers.add(new Farmer(0, "Perù", 0));
        farmers.add(new Farmer(1, "Guatemala", 0));
        farmers.add(new Farmer(2, "Argentina", 0));
        farmers.add(new Farmer(3, "Italia", 0));
        farmers.add(new Farmer(4, "Perù", 0));
        farmers.add(new Farmer(5, "Guatemala", 0));
        farmers.add(new Farmer(6, "Argentina", 0));
        farmers.add(new Farmer(7, "Italia", 0));
        farmers.add(new Farmer(8, "Italia", 0));
        farmers.add(new Farmer(9, "Napoliii", 0));

        for (int i = 0; i < farmers.size(); i++)
            System.out.println(farmers.get(i));

        GeneticAlgorithm ga = new GeneticAlgorithm(trees, farmers,100,60,30,0.6,1000);

        System.out.println("\n ---------\n");

        int[] gene =  ga.start().getGene() ;
        for (int i = 0; i < gene.length; i++) {
            int farmerId = gene[i];
            if(farmerId !=-1){
            Farmer farmer = farmers.get(farmerId);
            farmer.plantTree();}
        }

        for (int i = 0; i < farmers.size(); i++)
            System.out.println(farmers.get(i));

        System.out.println("Best solution: " + Arrays.toString(gene));

    }
}
