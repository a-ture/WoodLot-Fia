package algoritmoGenetico;

import comune.Farmer;
import comune.Tree;

import java.util.*;

public class TreeAssignmentGeneticAlgorithm {
    private static final int POPULATION_SIZE = 50;
    private static final double MUTATION_RATE = 0.5;
    private static final int MAX_GENERATIONS = 100;

    // Inner class representing a chromosome
    private static class Chromosome {
        private final Map<Farmer, List<Tree>> assignment;
        private double fitness;

        public Chromosome(Map<Farmer, List<Tree>> assignment) {
            this.assignment = assignment;
        }

        public Map<Farmer, List<Tree>> getAssignment() {
            return assignment;
        }

        public double getFitness() {
            return fitness;
        }

        public void setFitness(double fitness) {
            this.fitness = fitness;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Chromosome\n");
            for (Farmer farmer : assignment.keySet()) {
                sb.append("\nFarmer ").append(farmer.getId()).append(": ");
                List<Tree> assignedTrees = assignment.get(farmer);
                for (Tree tree : assignedTrees) {
                    sb.append(" Tree ").append(tree.getId()).append(" (").append(tree.getCountry()).append("), ");
                }
                sb.delete(sb.length() - 2, sb.length()); // Rimuove l'ultima virgola e spazio
            }
            sb.append("\n").append("Fitness: ").append(fitness).append("\n");

            return sb.toString();
        }


    }

    public static Map<Farmer, List<Tree>> findOptimalAssignment(List<Farmer> farmers, List<Tree> trees) {
        // Creazione della popolazione iniziale
        List<Chromosome> population = generateInitialPopulation(farmers, trees);
        Map<Farmer, List<Tree>> bestSolution = null;

        // Stampa la popolazione iniziale
        System.out.println("Popolazione iniziale:");
        for (Chromosome chromosome : population) {
            System.out.println(chromosome.toString());
        }
        System.out.println();

        // Evoluzione della popolazione
        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            // Calcolo del fitness per ogni cromosoma
            evaluateFitness(population);

            // Stampa la popolazione corrente
            System.out.println("\n\nPopolazione :" + generation);
            for (Chromosome chromosome : population) {
                System.out.println(chromosome.toString());
            }
            System.out.println();

            // Selezione dei genitori per la riproduzione
            List<Chromosome> parents = selectParents(population);

            // Creazione della nuova generazione attraverso crossover e mutazione
            List<Chromosome> offspring = reproduce(parents);

            // Aggiornamento della popolazione
            population.addAll(offspring);

            // Calcolo del fitness per ogni cromosoma nella nuova popolazione
            evaluateFitness(population);

            // Verifica se esistono assegnazioni valide nella soluzione migliore
            bestSolution = getBestSolution(population);
        }

        return bestSolution;  // Esempio: restituisce null se non ci sono assegnazioni valide
    }


    private static List<Chromosome> generateInitialPopulation(List<Farmer> farmers, List<Tree> trees) {
        List<Chromosome> population = new ArrayList<>();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            Chromosome chromosome = generateRandomChromosome(farmers, trees);
            population.add(chromosome);
        }

        return population;
    }

    private static Chromosome generateRandomChromosome(List<Farmer> farmers, List<Tree> trees) {
        List<Tree> availableTrees = new ArrayList<>(trees);
        Map<Farmer, List<Tree>> assignment = new HashMap<>();

        for (Farmer farmer : farmers) {
            assignment.put(farmer, new ArrayList<>());
        }

        Random random = new Random();
        int assignedTreesCount = 0;

        while (!availableTrees.isEmpty() && assignedTreesCount < trees.size()) {
            Tree selectedTree = null;
            Farmer selectedFarmer = null;

            // Cerca un albero disponibile che non sia stato ancora assegnato
            for (Tree tree : availableTrees) {
                boolean isAssigned = false;

                // Controlla se l'albero è già stato assegnato a un contadino
                for (List<Tree> assignedTrees : assignment.values()) {
                    if (assignedTrees.contains(tree)) {
                        isAssigned = true;
                        break;
                    }
                }

                if (!isAssigned) {
                    selectedTree = tree;
                    break;
                }
            }

            // Cerca un contadino disponibile con lo stesso paese dell'albero selezionato
            if (selectedTree != null) {
                List<Farmer> availableFarmers = new ArrayList<>();
                for (Farmer farmer : farmers) {
                    if (farmer.getCountry().equals(selectedTree.getCountry())) {
                        availableFarmers.add(farmer);
                    }
                }

                if (!availableFarmers.isEmpty()) {
                    int randomIndex = random.nextInt(availableFarmers.size());
                    selectedFarmer = availableFarmers.get(randomIndex);
                }
            }

            if (selectedTree != null && selectedFarmer != null) {
                assignment.get(selectedFarmer).add(selectedTree);
                availableTrees.remove(selectedTree);
                assignedTreesCount++;
            }
        }

        return new Chromosome(assignment);
    }

    private static void evaluateFitness(List<Chromosome> population) {
        for (Chromosome chromosome : population) {
            Map<Farmer, List<Tree>> assignment = chromosome.getAssignment();

            // Calcola il numero di alberi assegnati a ciascun contadino
            // Trova il valore minimo delle penalità
            int minPenalty = Integer.MAX_VALUE;
            Map<Farmer, Integer> assignedTreeCount = new HashMap<>();
            for (Farmer farmer : assignment.keySet()) {
                assignedTreeCount.put(farmer, assignment.get(farmer).size());
                if (farmer.getPenalties() < minPenalty)
                    minPenalty = farmer.getPenalties();
            }

            // Calcola la fitness basata sul numero di alberi assegnati ai contadini con penalità minore
            double fitness = 0.0;
            List<Farmer> farmersWithMinPenalty = new ArrayList<>();
            for (Farmer farmer : assignedTreeCount.keySet()) {
                if (assignedTreeCount.get(farmer) == minPenalty) {
                    farmersWithMinPenalty.add(farmer);
                }
            }

            // Distribuisci in modo equo gli alberi tra i contadini con penalità minore
            int minAssignedTrees = Integer.MAX_VALUE;
            for (Farmer farmer : farmersWithMinPenalty) {
                int farmerAssignedTrees = assignedTreeCount.get(farmer);
                if (farmerAssignedTrees < minAssignedTrees) {
                    minAssignedTrees = farmerAssignedTrees;
                }
            }

            // Somma il numero di alberi assegnati ai contadini con penalità minore e distribuzione equa
            for (Farmer farmer : farmersWithMinPenalty) {
                if (assignedTreeCount.get(farmer) == minAssignedTrees) {
                    fitness += assignedTreeCount.get(farmer);
                }
            }
            chromosome.setFitness(fitness);
        }
    }


    private static List<Chromosome> selectParents(List<Chromosome> population) {
        List<Chromosome> parents = new ArrayList<>();

        // Selection based on roulette wheel method
        double totalFitness = population.stream().mapToDouble(Chromosome::getFitness).sum();

        for (int i = 0; i < POPULATION_SIZE / 2; i++) {
            double randomValue = Math.random() * totalFitness;
            double partialSum = 0;
            int j = 0;

            while (partialSum < randomValue && j < population.size()) {
                partialSum += population.get(j).getFitness();
                j++;
            }

            if (j > 0) {
                parents.add(population.get(j - 1));
            }

        }

        return parents;
    }


    private static List<Chromosome> reproduce(List<Chromosome> parents) {
        List<Chromosome> offspring = new ArrayList<>();

        int numberOfParents = parents.size();
        int numberOfChildren = numberOfParents / 2 * 2;

        for (int i = 0; i < numberOfChildren; i += 2) {
            Chromosome parent1 = parents.get(i);
            Chromosome parent2 = parents.get(i + 1);

            Chromosome child1 = crossover(parent1, parent2);
            Chromosome child2 = crossover(parent2, parent1);

            mutate(child1);
            mutate(child2);

            offspring.add(child1);
            offspring.add(child2);
        }

        return offspring;
    }


    private static Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        Map<Farmer, List<Tree>> assignment1 = parent1.getAssignment();
        Map<Farmer, List<Tree>> assignment2 = parent2.getAssignment();
        Map<Farmer, List<Tree>> childAssignment = new HashMap<>();

        // Create a new child assignment by combining characteristics from the parents
        for (Farmer farmer : assignment1.keySet()) {
            List<Tree> trees1 = assignment1.get(farmer);
            List<Tree> trees2 = assignment2.get(farmer);
            List<Tree> childTrees = new ArrayList<>();

            // Combine trees from both parents while maintaining validity
            for (Tree tree : trees1) {
                if (!childTrees.contains(tree)) {
                    childTrees.add(tree);
                }
            }

            for (Tree tree : trees2) {
                if (!childTrees.contains(tree)) {
                    childTrees.add(tree);
                }
            }

            childAssignment.put(farmer, childTrees);
        }

        return new Chromosome(childAssignment);
    }

    private static void mutate(Chromosome chromosome) {
        Map<Farmer, List<Tree>> assignment = chromosome.getAssignment();

        for (Farmer farmer : assignment.keySet()) {
            List<Tree> trees = assignment.get(farmer);

            List<Tree> treesToRemove = new ArrayList<>();

            for (int i = 0; i < trees.size(); i++) {
                if (Math.random() < MUTATION_RATE) {
                    Tree tree = trees.get(i);
                    treesToRemove.add(tree);
                }
            }

            for (Tree tree : treesToRemove) {
                trees.remove(tree);

                Farmer randomFarmer = getRandomFarmerExcept(farmer, assignment.keySet());
                assignment.get(randomFarmer).add(tree);
            }
        }
    }


    private static Farmer getRandomFarmerExcept(Farmer excludeFarmer, Set<Farmer> farmers) {
        List<Farmer> otherFarmers = new ArrayList<>(farmers);
        otherFarmers.remove(excludeFarmer);
        return otherFarmers.get((int) (Math.random() * otherFarmers.size()));
    }

    private static Map<Farmer, List<Tree>> getBestSolution(List<Chromosome> population) {
        if (population.isEmpty()) {
            return null; // o gestione appropriata per il caso in cui la popolazione sia vuota
        }
        Chromosome bestChromosome = Collections.max(population, Comparator.comparingDouble(Chromosome::getFitness));
        return bestChromosome.getAssignment();
    }


    public static void main(String[] args) {

        // Esempio di utilizzo
        List<Farmer> farmers = new ArrayList<>();
        farmers.add(new Farmer(1, "Italia", 3));
        farmers.add(new Farmer(2, "Spagna", 5));
        farmers.add(new Farmer(3, "Francia", 4));
        farmers.add(new Farmer(4, "Italia", 3));
        farmers.add(new Farmer(5, "Spagna", 5));
        farmers.add(new Farmer(6, "Francia", 4));
        farmers.add(new Farmer(7, "Italia", 3));
        farmers.add(new Farmer(8, "Spagna", 5));
        farmers.add(new Farmer(9, "Francia", 4));

        List<Tree> trees = new ArrayList<>();
        trees.add(new Tree(1, "Italia"));
        trees.add(new Tree(2, "Italia"));
        trees.add(new Tree(3, "Spagna"));
        trees.add(new Tree(4, "Francia"));
        trees.add(new Tree(5, "Italia"));
        trees.add(new Tree(6, "Spagna"));
        trees.add(new Tree(7, "Francia"));

        Map<Farmer, List<Tree>> optimalAssignment = findOptimalAssignment(farmers, trees);

        if (optimalAssignment != null) {
            for (Farmer farmer : optimalAssignment.keySet()) {
                List<Tree> assignedTrees = optimalAssignment.get(farmer);
                System.out.println("\nFarmer " + farmer.getId() + ": from " + farmer.getCountry() + " penality:" + farmer.getPenalties());
                for (Tree tree : assignedTrees) {
                    System.out.println("  Tree " + tree.getId() + " from " + tree.getCountry());
                }
            }
        } else {
            System.out.println("No valid assignment found.");
        }
    }
}
