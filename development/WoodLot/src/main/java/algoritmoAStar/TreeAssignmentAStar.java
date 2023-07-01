package algoritmoAStar;

import comune.Farmer;
import comune.Tree;

import java.util.*;

class State {
    Map<Farmer, List<Tree>> assignment; // Assegnazione degli alberi ai contadini

    public State(Map<Farmer, List<Tree>> assignment) {
        this.assignment = assignment;
    }

    public boolean isGoalState(List<Tree> trees) {
        // Verifica se tutti gli alberi sono stati assegnati
        for (Tree tree : trees) {
            boolean isAssigned = false;
            for (List<Tree> assignedTrees : assignment.values()) {
                if (assignedTrees.contains(tree)) {
                    isAssigned = true;
                    break;
                }
            }
            if (!isAssigned) {
                return false;
            }
        }
        return true;
    }
}

class Node implements Comparable<Node> {
    State state;
    Node parent;
    int gCost; // Costo g
    int hCost; // Costo h

    public Node(State state, int gCost, int hCost, Node parent) {
        this.state = state;
        this.gCost = gCost;
        this.hCost = hCost;
        this.parent = parent;
    }

    public State getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public int getCost() {
        return gCost + hCost;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(getCost(), other.getCost());
    }
}

public class TreeAssignmentAStar {

    public static Map<Farmer, List<Tree>> findOptimalAssignment(List<Farmer> farmers, List<Tree> trees) {
        // Inizializzazione dello stato iniziale
        Map<Farmer, List<Tree>> initialState = new HashMap<>();
        for (Farmer farmer : farmers) {
            initialState.put(farmer, new ArrayList<>());
        }

        // Creazione del nodo iniziale
        Node initialNode = new Node(new State(initialState), 0, calculateHeuristic(initialState, trees), null);

        // Inizializzazione delle strutture dati
        PriorityQueue<Node> openList = new PriorityQueue<>();
        Set<State> closedSet = new HashSet<>();

        // Aggiunta del nodo iniziale alla coda prioritaria
        openList.add(initialNode);

        while (!openList.isEmpty()) {
            // Estrazione del nodo con il costo più basso dalla coda prioritaria
            Node currentNode = openList.poll();
            State currentState = currentNode.getState();

            // Verifica se lo stato corrente è una soluzione
            if (currentState.isGoalState(trees)) {
                return currentState.assignment;
            }

            // Aggiunta dello stato corrente all'insieme dei nodi visitati
            closedSet.add(currentState);

            // Generazione dei successori

            for (Tree tree : trees) {
                if (!isTreeAssigned(currentState.assignment, tree)) {
                    Map<Farmer, List<Tree>> newState = new HashMap<>(currentState.assignment);

                    List<Farmer> eligibleFarmers = new ArrayList<>();

                    // Trova i contadini eleggibili con penalità simili
                    int minPenalties = Integer.MAX_VALUE;
                    for (Farmer farmer : farmers) {
                        if (farmer.getCountry().equals(tree.getCountry()) && newState.get(farmer).size() < 4) {
                            if (farmer.getPenalties() < minPenalties) {
                                eligibleFarmers.clear();
                                minPenalties = farmer.getPenalties();
                            }

                            if (farmer.getPenalties() == minPenalties) {
                                eligibleFarmers.add(farmer);
                            }
                        }
                    }

                    // Assegna l'albero al contadino con meno alberi assegnati
                    Farmer bestFarmer = null;
                    int minAssignedTrees = Integer.MAX_VALUE;

                    for (Farmer farmer : eligibleFarmers) {
                        int assignedTrees = newState.get(farmer).size();
                        if (assignedTrees < minAssignedTrees) {
                            bestFarmer = farmer;
                            minAssignedTrees = assignedTrees;
                        }
                    }

                    if (bestFarmer != null && isValidAssignment(currentState.assignment, bestFarmer, tree)) {
                        newState.get(bestFarmer).add(tree);
                        int gCost = currentNode.gCost + 1;
                        int hCost = calculateHeuristic(newState, trees);
                        Node newNode = new Node(new State(newState), gCost, hCost, currentNode);

                        if (!closedSet.contains(newNode.getState())) {
                            openList.add(newNode);
                        }
                    }
                }
            }
        }

        // Nessuna soluzione trovata
        return null;
    }

    private static boolean isTreeAssigned(Map<Farmer, List<Tree>> assignment, Tree tree) {
        for (List<Tree> assignedTrees : assignment.values()) {
            if (assignedTrees.contains(tree)) {
                return true;
            }
        }
        return false;
    }

    private static int calculateHeuristic(Map<Farmer, List<Tree>> assignment, List<Tree> trees) {
        int totalAssignedTrees = 0;
        int maxPenalty = 0;
        int penalty = 0;

        for (List<Tree> assignedTrees : assignment.values()) {
            totalAssignedTrees += assignedTrees.size();
        }

        for (Farmer farmer : assignment.keySet()) {
            int farmerPenalty = farmer.getPenalties();
            int assignedTreesCount = assignment.get(farmer).size();

            // Find the maximum penalty among all farmers
            if (farmerPenalty > maxPenalty) {
                maxPenalty = farmerPenalty;
            }

            // Subtract the farmer's penalty score from the total penalty
            penalty -= farmerPenalty;

            // Subtract the number of already assigned trees to the farmer
            penalty -= assignedTreesCount;
        }

        // Add a term based on the maximum penalty to influence the heuristic
        penalty -= (maxPenalty * 10);

        return totalAssignedTrees + penalty;
    }


    private static boolean isValidAssignment(Map<Farmer, List<Tree>> assignment, Farmer farmer, Tree tree) {
        // Verifica se il contadino si trova nel luogo adatto alla crescita dell'albero
        if (!farmer.getCountry().equals(tree.getCountry())) {
            return false;
        }

        // Verifica se l'albero è già stato assegnato a un contadino diverso
        for (List<Tree> assignedTrees : assignment.values()) {
            if (assignedTrees.contains(tree)) {
                return false;
            }
        }

        return true;
    }


    // Esempio di utilizzo
    public static void main(String[] args) {

        // Creazione dei contadini
        Farmer farmer1 = new Farmer(1, "Italy");
        Farmer farmer2 = new Farmer(2, "Italy");
        Farmer farmer3 = new Farmer(3, "France");
        Farmer farmer4 = new Farmer(4, "Italy");
        Farmer farmer5 = new Farmer(5, "Italy");
        Farmer farmer6 = new Farmer(6, "France");
        Farmer farmer7 = new Farmer(7, "Italy");
        Farmer farmer8 = new Farmer(8, "Italy");
        Farmer farmer9 = new Farmer(9, "France");
        Farmer farmer10 = new Farmer(10, "Italy");

        // Creazione degli alberi
        Tree tree1 = new Tree(1, "Italy");
        Tree tree2 = new Tree(2, "France");
        Tree tree3 = new Tree(3, "Italy");
        Tree tree4 = new Tree(4, "Italy");
        Tree tree5 = new Tree(5, "France");
        Tree tree6 = new Tree(6, "Italy");
        Tree tree7 = new Tree(7, "Italy");
        Tree tree8 = new Tree(8, "France");
        Tree tree9 = new Tree(9, "Italy");
        Tree tree10 = new Tree(10, "Italy");
        Tree tree11 = new Tree(11, "France");
        Tree tree12 = new Tree(12, "Italy");
        Tree tree13 = new Tree(13, "France");
        Tree tree14 = new Tree(14, "Italy");

        // Creazione delle liste di contadini e alberi
        List<Farmer> farmers = Arrays.asList(farmer1, farmer2, farmer3, farmer4, farmer5,
                farmer6, farmer7, farmer8, farmer9, farmer10);
        List<Tree> trees = Arrays.asList(tree1, tree2, tree3, tree4, tree5,
                tree6, tree7, tree8, tree9, tree10, tree11, tree12, tree14, tree13);

        // Trova l'assegnazione ottimale
        Map<Farmer, List<Tree>> optimalAssignment = findOptimalAssignment(farmers, trees);
        System.out.println("--Testiamo il caso in cui ci sono  più contadini che alberi--");
        System.out.println("I contadini usati sono: ");
        for (Farmer farmer : farmers) {
            System.out.println(farmer);
        }
        System.out.println("Gli alberi usati sono: ");
        for (Tree tree : trees) {
            System.out.println(tree);
        }
        // Stampa l'assegnazione ottimale
        printResult(optimalAssignment);
        System.out.println("\n");

        System.out.println("\n\n--Testiamo il caso in cui ci sono  più alberi che contadini--");
        // Trova l'assegnazione ottimale

        farmers = Arrays.asList(farmer1, farmer2, farmer3);
        optimalAssignment = findOptimalAssignment(farmers, trees);
        // Stampa l'assegnazione ottimale
        System.out.println("I contadini usati sono: ");
        for (Farmer farmer : farmers) {
            System.out.println(farmer);
        }
        System.out.println("Gli alberi usati sono: ");
        for (Tree tree : trees) {
            System.out.println(tree);
        }
        printResult(optimalAssignment);
        System.out.println("\n");
        System.out.println("Non viene prodotto un assegnamento poichè ci sono troppi alberi rispetto ai contadini, " +
                "ciascun contadino può avere al massimo 4 alberi");

        System.out.println("\n\n--Testiamo il caso in cui non ci sono contadini compatibili--");
        Farmer farmer11 = new Farmer(11, "Portugal");

        // Creazione delle liste di contadini e alberi
        List<Farmer> farmers1 = Arrays.asList(farmer11);

        System.out.println("I contadini usati sono: ");
        for (Farmer farmer : farmers1) {
            System.out.println(farmer);
        }
        System.out.println("Gli alberi usati sono: ");
        for (Tree tree : trees) {
            System.out.println(tree);
        }
        System.out.println("\n");
        // Trova l'assegnazione ottimale
        optimalAssignment = findOptimalAssignment(farmers1, trees);

        // Stampa l'assegnazione ottimale
        printResult(optimalAssignment);
        System.out.println("Non è presente una soluzione poichè il contadino inserito:" + farmer11 + " " +
                "\nnon è compatibile con nessun albero, poichè ha un paese diverso");

    }

    private static void printResult(Map<Farmer, List<Tree>> optimalAssignment) {
        if (optimalAssignment != null) {
            for (Farmer farmer : optimalAssignment.keySet()) {
                List<Tree> assignedTrees = optimalAssignment.get(farmer);
                System.out.print("Contadino " + farmer.getId() + ", del paese " + farmer.getCountry() + " con penalità: " + farmer.getPenalties() + "\n " + assignedTrees.size() + " alberi assegnati - Alberi: ");
                for (Tree tree : assignedTrees) {
                    System.out.print(tree.getId() + " (" + tree.getCountry() + "), ");
                }
                System.out.println();
            }
        } else {
            System.out.println("Nessuna soluzione trovata.");
        }
    }
}
