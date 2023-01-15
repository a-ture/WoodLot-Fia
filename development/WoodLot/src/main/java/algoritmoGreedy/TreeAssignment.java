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
            // creiamo una lista di contadini compatibili per ogni albero
            List<Farmer> compatibleFarmers = new ArrayList<>();
            for (Farmer farmer : farmers) {
                if (farmer.getCountry().equals(tree.getCountry())) {
                    compatibleFarmers.add(farmer);
                }
            }
            // ordiniamo i contadini compatibili in base al numero di alberi piantati
            Collections.sort(compatibleFarmers, new Comparator<Farmer>() {
                public int compare(Farmer f1, Farmer f2) {
                    return f1.getTreesPlanted() - f2.getTreesPlanted();
                }
            });
            // assegniamo l'albero al contadino con meno alberi piantati solo se ci sono contadini compatibili
            if (compatibleFarmers.size() > 0) {
                Farmer assignedFarmer = compatibleFarmers.get(0);
                compatibleFarmers.get(0).plantTree();
                System.out.println("L'albero " + tree.getId() + " è stato assegnato al contadino " + assignedFarmer.getId());
            }
        }
    }


    public static void main(String[] args) {
        List<Farmer> farmers = new ArrayList<>();
        farmers.add(new Farmer(1, "Italia", 20));
        farmers.add(new Farmer(2, "Italia", 3));
        farmers.add(new Farmer(3, "Perù", 1));
        farmers.add(new Farmer(4, "Argentina", 4));
        System.out.println("Situazione iniziale:");
        for (Farmer farmer : farmers) {
            System.out.println("Farmer ID: " + farmer.getId() + " Country: " + farmer.getCountry() + " Trees Planted: " + farmer.getTreesPlanted());
        }

        System.out.println("\n");

        List<Tree> trees = new ArrayList<>();
        trees.add(new Tree(1, "Italia"));
        trees.add(new Tree(2, "Italia"));
        trees.add(new Tree(3, "Italia"));
        trees.add(new Tree(4, "Perù"));
        trees.add(new Tree(5, "Guatemala"));
        trees.add(new Tree(6, "Argentina"));
        trees.add(new Tree(7, "Argentina"));
        for (Tree tree : trees) {
            System.out.println("Tree ID: " + tree.getId() + " Country: " + tree.getCountry());
        }

        System.out.println("\n");

        assignTrees(farmers, trees);

        System.out.println("\n");
        for (Farmer farmer : farmers) {
            System.out.println("Farmer ID: " + farmer.getId() + " Country: " + farmer.getCountry() + " Trees Planted: " + farmer.getTreesPlanted());
        }
    }
}

