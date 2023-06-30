package algoritmoGreedy;


import comune.Farmer;
import comune.Tree;

import java.util.*;

public class TreeAssignment {
    public static void assignTrees(List<Farmer> farmers, List<Tree> trees) {
        Map<Farmer, Integer> assignedTrees = new HashMap<>(); // Mappa per memorizzare il numero di alberi assegnati a ciascun contadino

        for (Tree tree : trees) {
            // Creiamo una lista di contadini compatibili per ogni albero
            List<Farmer> compatibleFarmers = new ArrayList<>();
            for (Farmer farmer : farmers) {
                // Verifichiamo se il contadino è nel paese giusto e non ha già 4 alberi assegnati
                if (farmer.getCountry().equals(tree.getCountry()) && getAssignedTreeCount(assignedTrees, farmer) < 4) {
                    compatibleFarmers.add(farmer);
                }
            }

            // Ordiniamo i contadini compatibili in base al numero di alberi piantati e alle penalità
            Collections.sort(compatibleFarmers, new Comparator<Farmer>() {
                public int compare(Farmer f1, Farmer f2) {
                    int penalties1 = f1.getPenalties();
                    int penalties2 = f2.getPenalties();

                    // Ordinamento in base alle penalità
                    if (penalties1 != penalties2) {
                        return penalties1 - penalties2;
                    }

                    return 0;
                }
            });

            // Assegniamo l'albero al contadino con penalità minore e meno alberi assegnati
            if (!compatibleFarmers.isEmpty()) {
                Farmer assignedFarmer = compatibleFarmers.get(0);
                incrementAssignedTreeCount(assignedTrees, assignedFarmer); // Incrementiamo il numero di alberi assegnati al contadino
                System.out.println("L'albero " + tree.getId() + " è stato assegnato al contadino " + assignedFarmer.getId());
            } else {
                System.out.println("Nessun contadino idoneo per l'albero " + tree.getId());
            }
        }
    }

    private static int getAssignedTreeCount(Map<Farmer, Integer> assignedTrees, Farmer farmer) {
        Integer count = assignedTrees.get(farmer);
        return count != null ? count : 0;
    }

    private static void incrementAssignedTreeCount(Map<Farmer, Integer> assignedTrees, Farmer farmer) {
        int count = getAssignedTreeCount(assignedTrees, farmer);
        assignedTrees.put(farmer, count + 1);
    }


    public static void main(String[] args) {
        List<Farmer> farmers = new ArrayList<>();
        farmers.add(new Farmer(1, "Italia"));
        farmers.add(new Farmer(2, "Italia"));
        farmers.add(new Farmer(3, "Perù"));
        farmers.add(new Farmer(4, "Argentina"));
        farmers.add(new Farmer(5, "Italia"));
        farmers.add(new Farmer(6, "Italia"));
        farmers.add(new Farmer(7, "Perù"));
        farmers.add(new Farmer(8, "Argentina"));
        farmers.add(new Farmer(9, "Guatemala"));

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

    }
}