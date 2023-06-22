package algoritmoGreedy;


import comune.Farmer;
import comune.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TreeAssignment {
    public static void assignTrees(List<Farmer> farmers, List<Tree> trees) {
        for (Tree tree : trees) {
            // Creiamo una lista di contadini compatibili per ogni albero
            List<Farmer> compatibleFarmers = new ArrayList<>();
            for (Farmer farmer : farmers) {
                // Verifichiamo se il contadino è nel paese giusto
                if (farmer.getCountry().equals(tree.getCountry())) {
                    compatibleFarmers.add(farmer);
                }
            }

            // Ordiniamo i contadini compatibili in base al numero di alberi piantati e alle penalità
            Collections.sort(compatibleFarmers, new Comparator<Farmer>() {
                public int compare(Farmer f1, Farmer f2) {
                    if (f1.getPenalties() != f2.getPenalties()) {
                        return f1.getPenalties() - f2.getPenalties();
                    } else {
                        return f1.getTreesPlanted() - f2.getTreesPlanted();
                    }
                }
            });

            // Assegniamo l'albero al contadino con meno alberi piantati e penalità
            if (!compatibleFarmers.isEmpty()) {
                Farmer assignedFarmer = compatibleFarmers.get(0);
                assignedFarmer.plantTree();
                System.out.println("L'albero " + tree.getId() + " è stato assegnato al contadino " + assignedFarmer.getId());
            } else {
                System.out.println("Nessun contadino idoneo per l'albero " + tree.getId());
            }
        }
    }

    public static void main(String[] args) {
        List<Farmer> farmers = new ArrayList<>();
        farmers.add(new Farmer(1, "Italia", 20));
        farmers.add(new Farmer(2, "Italia", 3));
        farmers.add(new Farmer(3, "Perù", 1));
        farmers.add(new Farmer(4, "Argentina", 4));
        farmers.add(new Farmer(5, "Italia", 20));
        farmers.add(new Farmer(6, "Italia", 3));
        farmers.add(new Farmer(7, "Perù", 1));
        farmers.add(new Farmer(8, "Argentina", 4));

        List<Tree> trees = new ArrayList<>();
        trees.add(new Tree(1, "Italia"));
        trees.add(new Tree(2, "Italia"));
        trees.add(new Tree(3, "Italia"));
        trees.add(new Tree(4, "Perù"));
        trees.add(new Tree(5, "Guatemala"));
        trees.add(new Tree(6, "Argentina"));
        trees.add(new Tree(7, "Argentina"));
        trees.add(new Tree(8, "Italia"));
        trees.add(new Tree(9, "Italia"));
        trees.add(new Tree(10, "Italia"));
        trees.add(new Tree(11, "Perù"));
        trees.add(new Tree(12, "Guatemala"));
        trees.add(new Tree(13, "Argentina"));
        trees.add(new Tree(14, "Argentina"));

        System.out.println("Situazione iniziale:");
        for (Farmer farmer : farmers) {
            System.out.println(farmer);
        }

        System.out.println();

        assignTrees(farmers, trees);

        System.out.println("\nSituazione finale:");
        for (Farmer farmer : farmers) {
            System.out.println(farmer);
        }
    }
}