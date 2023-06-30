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

public class TreeAssignmentAStar {

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
                for (Tree tree : trees) {
                    if (isValidAssignment(currentState, farmer, tree)) {
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

        // Nessuna soluzione trovata
        return null;
    }

    private static int calculateHeuristic(Map<Farmer, List<Tree>> state) {
        int totalAssignedTrees = 0;
        int maxPenalty = 0;
        int penalty = 0;

        for (List<Tree> assignedTrees : state.values()) {
            totalAssignedTrees += assignedTrees.size();
        }

        for (Farmer farmer : state.keySet()) {
            int farmerPenalty = farmer.getPenalties();
            int assignedTreesCount = state.get(farmer).size();

            // Trova la penalità massima tra tutti i contadini
            if (farmerPenalty > maxPenalty) {
                maxPenalty = farmerPenalty;
            }

            // Sottrai il punteggio di penalità del contadino dal punteggio totale
            penalty -= farmerPenalty;

            // Sottrai anche il numero di alberi già assegnati al contadino
            penalty -= assignedTreesCount;
        }

        // Aggiungi un termine basato sulla penalità massima per influenzare l'euristica
        penalty -= (maxPenalty * 10);

        return totalAssignedTrees + penalty;
    }


    private static boolean isValidAssignment(Map<Farmer, List<Tree>> state, Farmer farmer, Tree tree) {
        // Verifica se il contadino si trova nel luogo adatto alla crescita dell'albero
        if (!farmer.getCountry().equals(tree.getCountry())) {
            return false;
        }

        // Verifica se l'albero è già stato assegnato a un contadino diverso
        for (List<Tree> assignedTrees : state.values()) {
            if (assignedTrees.contains(tree)) {
                return false;
            }
        }

        return true;
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
        Farmer farmer4 = new Farmer(4, "Italy", 23);
        Farmer farmer5 = new Farmer(5, "Italy", 12);
        Farmer farmer6 = new Farmer(6, "France", 12);
        Farmer farmer7 = new Farmer(7, "Italy", 223);
        Farmer farmer8 = new Farmer(8, "Italy", 11);
        Farmer farmer9 = new Farmer(9, "France", 22);
        Farmer farmer10 = new Farmer(10, "Italy", 21);

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

        // Stampa l'assegnazione ottimale
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
