package algoritmoHillClimbing;

import comune.Farmer;
import comune.Tree;

import java.util.*;


public class TreeAssignmentHillClimbing {
    public static Map<Farmer, List<Tree>> findOptimalAssignment(List<Farmer> farmers, List<Tree> trees) {
        // Generazione di una soluzione iniziale casuale
        Map<Farmer, List<Tree>> currentAssignment = generateRandomAssignment(farmers, trees);

        while (true) {
            // Calcolo della funzione obiettivo della soluzione corrente
            int currentCost = calculateCost(currentAssignment);

            // Generazione del vicinato della soluzione corrente
            List<Map<Farmer, List<Tree>>> neighbors = generateNeighbors(currentAssignment);

            // Trovare la migliore soluzione nel vicinato
            Map<Farmer, List<Tree>> bestNeighbor = null;
            int bestNeighborCost = Integer.MAX_VALUE;

            for (Map<Farmer, List<Tree>> neighbor : neighbors) {
                int neighborCost = calculateCost(neighbor);
                if (neighborCost < bestNeighborCost) {
                    bestNeighbor = neighbor;
                    bestNeighborCost = neighborCost;
                }
            }

            // Verifica se la soluzione migliore nel vicinato è migliore della soluzione corrente
            if (bestNeighborCost < currentCost) {
                currentAssignment = bestNeighbor;
            } else {
                // Nessun miglioramento trovato, restituisci la soluzione corrente
                return currentAssignment;
            }
        }
    }

    private static Map<Farmer, List<Tree>> generateRandomAssignment(List<Farmer> farmers, List<Tree> trees) {
        Map<Farmer, List<Tree>> assignment = new HashMap<>();

        for (Farmer farmer : farmers) {
            assignment.put(farmer, new ArrayList<>());
        }

        Random random = new Random();

        for (Tree tree : trees) {
            Farmer randomFarmer = farmers.get(random.nextInt(farmers.size()));
            assignment.get(randomFarmer).add(tree);
        }

        return assignment;
    }

    private static List<Map<Farmer, List<Tree>>> generateNeighbors(Map<Farmer, List<Tree>> assignment) {
        List<Map<Farmer, List<Tree>>> neighbors = new ArrayList<>();

        for (Farmer farmer : new ArrayList<>(assignment.keySet())) {
            List<Tree> assignedTrees = assignment.get(farmer);

            for (Tree tree : new ArrayList<>(assignedTrees)) {
                // Remove the tree from the current farmer
                assignedTrees.remove(tree);

                // Add the tree to a different random farmer
                List<Farmer> otherFarmers = new ArrayList<>(assignment.keySet());
                otherFarmers.remove(farmer);
                Farmer randomFarmer = otherFarmers.get(new Random().nextInt(otherFarmers.size()));
                assignment.get(randomFarmer).add(tree);

                // Create a neighbor with the modified assignment
                Map<Farmer, List<Tree>> neighbor = new HashMap<>(assignment);
                neighbors.add(neighbor);

                // Restore the assignment to its original state for the next iteration
                assignedTrees.add(tree);
                assignment.get(randomFarmer).remove(tree);
            }
        }

        return neighbors;
    }


    private static int calculateCost(Map<Farmer, List<Tree>> assignment) {
        int totalCost = 0;

        for (Farmer farmer : assignment.keySet()) {
            int farmerCost = 0;
            List<Tree> assignedTrees = assignment.get(farmer);
            farmerCost = assignedTrees.size() + farmer.getPenalties();
            totalCost += farmerCost;
        }

        return totalCost;
    }

    public static void main(String[] args) {
        // Creazione dei contadini
        Farmer farmer1 = new Farmer(1, "Italy");
        Farmer farmer2 = new Farmer(2, "Italy");
        Farmer farmer3 = new Farmer(3, "France");

        // Creazione degli alberi
        Tree tree1 = new Tree(1, "Italy"); // Albero con id, paese e penalità
        Tree tree2 = new Tree(2, "France");
        Tree tree3 = new Tree(3, "Italy");

        // Creazione delle liste di contadini e alberi
        List<Farmer> farmers = Arrays.asList(farmer1, farmer2, farmer3);
        List<Tree> trees = Arrays.asList(tree1, tree2, tree3);

        // Chiamata al metodo per risolvere il problema con l'algoritmo di ricerca locale
        Map<Farmer, List<Tree>> optimalAssignment = TreeAssignmentHillClimbing.findOptimalAssignment(farmers, trees);

        // Stampa l'assegnazione ottimale
        if (optimalAssignment != null) {
            for (Farmer farmer : optimalAssignment.keySet()) {
                List<Tree> assignedTrees = optimalAssignment.get(farmer);
                System.out.println("Contadino " + farmer.getId() + ": " + assignedTrees.size() + " alberi assegnati");
            }
        } else {
            System.out.println("Nessuna soluzione trovata.");
        }
    }
}
