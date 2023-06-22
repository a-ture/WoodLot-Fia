package algoritmoAStar;

import comune.Farmer;
import comune.Tree;

import java.util.*;

// Classe rappresentante il nodo dello spazio di ricerca
 class Node implements Comparable<Node> {
    private Map<Farmer, List<Tree>> state;  // Stato del nodo
    private int gCost;  // Costo del percorso dal nodo iniziale al nodo corrente
    private int hCost;  // Euristiche del nodo corrente
    private Node parent;  // Nodo genitore nel percorso

    public Node(Map<Farmer, List<Tree>> state, int gCost, int hCost, Node parent) {
        this.state = state;
        this.gCost = gCost;
        this.hCost = hCost;
        this.parent = parent;
    }

    public Map<Farmer, List<Tree>> getState() {
        return state;
    }

    public int getGCost() {
        return gCost;
    }

    public int getHCost() {
        return hCost;
    }

    public int getFCost() {
        return gCost + hCost;
    }

    public Node getParent() {
        return parent;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.getFCost(), other.getFCost());
    }
}

public class TreeAssignmetAStar {

    public static Map<Farmer, List<Tree>> findOptimalAssignment(List<Farmer> farmers, List<Tree> trees) {
        // Inizializzazione dello stato iniziale
        Map<Farmer, List<Tree>> initialState = new HashMap<>();
        for (Farmer farmer : farmers) {
            initialState.put(farmer, new ArrayList<>());
        }

        // Creazione del nodo iniziale
        Node initialNode = new Node(initialState, 0, calculateHeuristic(initialState), null);

        // Inizializzazione delle strutture dati
        PriorityQueue<Node> openList = new PriorityQueue<>();
        Set<Map<Farmer, List<Tree>>> closedSet = new HashSet<>();

        // Aggiunta del nodo iniziale alla coda prioritaria
        openList.add(initialNode);

        while (!openList.isEmpty()) {
            // Estrazione del nodo con il costo più basso dalla coda prioritaria
            Node currentNode = openList.poll();
            Map<Farmer, List<Tree>> currentState = currentNode.getState();

            // Verifica se lo stato corrente è una soluzione
            if (isGoalState(currentState, trees)) {
                return currentState;
            }

            // Aggiunta dello stato corrente all'insieme dei nodi visitati
            closedSet.add(currentState);

            // Generazione dei successori
            for (Farmer farmer : farmers) {
                if (currentState.get(farmer).size() < farmer.getTreesPlanted()) {
                    for (Tree tree : trees) {
                        if (farmer.getCountry().equals(tree.getCountry()) && !currentState.get(farmer).contains(tree)) {
                            Map<Farmer, List<Tree>> newState = new HashMap<>(currentState);
                            newState.get(farmer).add(tree);
                            int gCost = currentNode.getGCost() + 1;
                            int hCost = calculateHeuristic(newState);
                            Node newNode = new Node(newState, gCost, hCost, currentNode);

                            if (!closedSet.contains(newState)) {
                                openList.add(newNode);
                            }
                        }
                    }
                }
            }
        }

        // Nessuna soluzione trovata
        return null;
    }

    private static int calculateHeuristic(Map<Farmer, List<Tree>> state) {
        int totalAssignedTrees = 0;
        for (List<Tree> assignedTrees : state.values()) {
            totalAssignedTrees += assignedTrees.size();
        }
        return totalAssignedTrees;
    }

    private static boolean isGoalState(Map<Farmer, List<Tree>> state, List<Tree> trees) {
        // Verifica se tutti gli alberi sono stati assegnati
        for (Tree tree : trees) {
            boolean isAssigned = false;
            for (List<Tree> assignedTrees : state.values()) {
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
    // Esempio di utilizzo
    public static void main(String[] args) {
        // Creazione dei contadini
        Farmer farmer1 = new Farmer(1, "Italy", 2);
        Farmer farmer2 = new Farmer(2, "Italy", 1);
        Farmer farmer3 = new Farmer(3, "France", 2);

        // Creazione degli alberi
        Tree tree1 = new Tree(1, "Italy");
        Tree tree2 = new Tree(2, "France");
        Tree tree3 = new Tree(3, "Italy");

        // Creazione delle liste di contadini e alberi
        List<Farmer> farmers = Arrays.asList(farmer1, farmer2, farmer3);
        List<Tree> trees = Arrays.asList(tree1, tree2, tree3);

        // Trova l'assegnazione ottimale
        Map<Farmer, List<Tree>> optimalAssignment = findOptimalAssignment(farmers, trees);

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
