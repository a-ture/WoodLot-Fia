package algoritmoGenetico.versione3;

import comune.Tree;
import comune.Farmer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * La stringa [2, 0, 1, 0] rappresenta la soluzione ottimale trovata dall'algoritmo genetico in base alla funzione
 * di fitness utilizzata. Ciascun numero in questa stringa rappresenta l'ID del contadino a cui è stato assegnato
 * un albero specifico. Ad esempio, l'albero con ID 2 è stato assegnato al contadino con ID 0, l'albero con ID 0
 * è stato assegnato al contadino con ID 1 e così via.
 */
public class Starter {
    public static void main(String[] args) {
        List<Tree> trees = new ArrayList<>();

        trees.add(new Tree(1, "Italia"));
        trees.add(new Tree(2, "Argentina")); //1
        trees.add(new Tree(3, "Perù"));   //1
        trees.add(new Tree(4, "Guatemala")); //1
        trees.add(new Tree(5, "Italia"));
        trees.add(new Tree(6, "Argentina")); //2
        trees.add(new Tree(7, "Perù")); //2
        trees.add(new Tree(8, "Guatemala")); //2
        trees.add(new Tree(1, "Italia"));
        trees.add(new Tree(9, "Argentina")); //3
        trees.add(new Tree(10, "Perù")); //3
        trees.add(new Tree(11, "Guatemala")); //3
        trees.add(new Tree(1, "Italia"));
        trees.add(new Tree(12, "Argentina")); //4
        trees.add(new Tree(13, "Perù")); //4
        trees.add(new Tree(14, "Guatemala")); //4

        List<Farmer> farmers = new ArrayList<>();
        farmers.add(new Farmer(1, "Argentina", 4));
        farmers.add(new Farmer(2, "Perù", 4));
        farmers.add(new Farmer(3, "Italia", 4));
        farmers.add(new Farmer(4, "Guatemala", 4));

        for (int i = 0; i < farmers.size(); i++)
            System.out.println(farmers.get(i));

        GeneticAlgorithm ga = new GeneticAlgorithm(trees, farmers,100,60,6,0.6,100);

        System.out.println("\n ---------\n");

        int[] gene =  ga.start().getGene() ;
        for (int i = 0; i < gene.length; i++) {
            int farmerId = gene[i];
            Farmer farmer = farmers.get(farmerId);
            farmer.plantTree();
        }

        for (int i = 0; i < farmers.size(); i++)
            System.out.println(farmers.get(i));

        System.out.println("Best solution: " + Arrays.toString(gene));

    }
}
