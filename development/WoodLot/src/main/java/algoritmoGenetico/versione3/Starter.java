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
        trees.add(new Tree(3, "Argentina"));
        trees.add(new Tree(2, "Perù"));
        trees.add(new Tree(4, "Guatemala"));
        trees.add(new Tree(5, "Italia"));
        trees.add(new Tree(6, "Argentina"));
        trees.add(new Tree(7, "Perù"));
        trees.add(new Tree(8, "Guatemala")); trees.add(new Tree(1, "Italia"));
        trees.add(new Tree(9, "Argentina"));
        trees.add(new Tree(10, "Perù"));
        trees.add(new Tree(11, "Guatemala")); trees.add(new Tree(1, "Italia"));
        trees.add(new Tree(12, "Argentina"));
        trees.add(new Tree(13, "Perù"));
        trees.add(new Tree(14, "Guatemala"));

        List<Farmer> farmers = new ArrayList<>();
        farmers.add(new Farmer(1,"Argentina", 4));
        farmers.add(new Farmer(2,"Perù", 4));
        farmers.add(new Farmer(3,"Italia", 4));
        farmers.add(new Farmer(7,"Italia", 4));

        GeneticAlgorithm ga = new GeneticAlgorithm(trees, farmers);
        ga.initializePopulation(100);

        // loop per l'evoluzione della popolazione
        for (int i = 0; i < 100; i++) {
            List<Chromosome> newPopulation = new ArrayList<>();
            for (int j = 0; j < ga.getPopulation().size(); j++) {
                Chromosome evolved = ga.evolve(ga.getPopulation().get(j));
                newPopulation.add(evolved);
            }
            ga.setPopulation(newPopulation);
            ga.mutate(0.01);
        }

        // trovare il cromosoma con la migliore fitness
        Chromosome best = ga.getPopulation().stream().max(Comparator.comparingDouble(Chromosome::getFitness)).get();

        // utilizzare il gene di best per assegnare gli alberi ai contadini
        int[] gene = best.getGene();
        for (int i = 0; i < gene.length; i++) {
            int farmerId = gene[i];
            Farmer farmer = farmers.get(farmerId);
            farmer.plantTree();
        }
        System.out.println("Best solution: " + Arrays.toString(best.getGene()));

    }
}
