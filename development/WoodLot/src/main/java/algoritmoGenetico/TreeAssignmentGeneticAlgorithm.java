package algoritmoGenetico;

import comune.Farmer;
import comune.Tree;

import java.util.*;

public class TreeAssignmentGeneticAlgorithm {
    private static final int POPULATION_SIZE = 50;
    private static final double MUTATION_RATE = 0.1;
    private static final int MAX_GENERATIONS = 100;

    public static Map<Farmer, List<Tree>> findOptimalAssignment(List<Farmer> farmers, List<Tree> trees) {
        // Creazione della popolazione iniziale
        List<Chromosome> population = generateInitialPopulation(farmers, trees);

        // Evoluzione della popolazione
        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            // Calcolo del fitness per ogni cromosoma
            evaluateFitness(population);

            // Selezione dei genitori per la riproduzione
            List<Chromosome> parents = selectParents(population);

            // Creazione della nuova generazione attraverso crossover e mutazione
            List<Chromosome> offspring = reproduce(parents);

            // Aggiornamento della popolazione
            population = offspring;

            // Verifica se esistono assegnazioni valide nella soluzione migliore
            Map<Farmer, List<Tree>> bestSolution = getBestSolution(population);
            if (hasValidAssignments(bestSolution, farmers.size(), trees.size())) {
                return bestSolution;
            }
        }

        // Restituzione di una soluzione di default o gestione dell'assenza di assegnazioni valide
        // ...

        return null;  // Esempio: restituisce null se non ci sono assegnazioni valide
    }

    private static boolean hasValidAssignments(Map<Farmer, List<Tree>> assignment, int numFarmers, int numTrees) {
        // Verifica se ciascun contadino ha almeno un albero assegnato
        if (assignment.values().stream().anyMatch(List::isEmpty)) {
            return false;
        }

        // Verifica se il numero totale di alberi assegnati corrisponde al numero totale di alberi disponibili
        int totalAssignedTrees = assignment.values().stream().mapToInt(List::size).sum();
        return totalAssignedTrees == numTrees;
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
        List<Tree> shuffledTrees = new ArrayList<>(trees);
        Collections.shuffle(shuffledTrees);

        Map<Farmer, List<Tree>> assignment = new HashMap<>();

        for (Farmer farmer : farmers) {
            List<Tree> assignedTrees = new ArrayList<>();
            for (Tree tree : shuffledTrees) {
                if (tree.getCountry().equals(farmer.getCountry())) {
                    assignedTrees.add(tree);
                    shuffledTrees.remove(tree);
                    break;
                }
            }
            assignment.put(farmer, assignedTrees);
        }

        // Distribute remaining trees randomly among farmers
        Random random = new Random();
        for (Tree tree : shuffledTrees) {
            Farmer randomFarmer = farmers.get(random.nextInt(farmers.size()));
            assignment.get(randomFarmer).add(tree);
        }

        return new Chromosome(assignment);
    }



    private static void evaluateFitness(List<Chromosome> population) {
        for (Chromosome chromosome : population) {
            int cost = calculateCost(chromosome.getAssignment());
            chromosome.setFitness(1.0 / (cost + 1)); // Higher fitness for lower cost
        }
    }

    private static int calculateCost(Map<Farmer, List<Tree>> assignment) {
        int totalCost = 0;

        for (Farmer farmer : assignment.keySet()) {
            int farmerCost = 0;
            List<Tree> assignedTrees = assignment.get(farmer);

            for (Tree tree : assignedTrees) {
                farmerCost += farmer.getPenalties();
            }

            totalCost += farmerCost;
        }

        return totalCost;
    }

    private static List<Chromosome> selectParents(List<Chromosome> population) {
        List<Chromosome> parents = new ArrayList<>();

        // Selection based on roulette wheel method
        double totalFitness = population.stream().mapToDouble(Chromosome::getFitness).sum();

        for (int i = 0; i < POPULATION_SIZE / 2; i++) {
            double randomValue = Math.random() * totalFitness;
            double partialSum = 0;
            int j = 0;

            while (partialSum < randomValue) {
                partialSum += population.get(j).getFitness();
                j++;
            }

            parents.add(population.get(j - 1));
        }

        return parents;
    }

    private static List<Chromosome> reproduce(List<Chromosome> parents) {
        List<Chromosome> offspring = new ArrayList<>();

        int numberOfParents = parents.size();
        int numberOfChildren = numberOfParents / 2 * 2;


        for (int i = 0; i < numberOfChildren; i++) {
            Chromosome parent1 = parents.get(i);
            Chromosome parent2 = parents.get(numberOfParents - i - 1);

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

        for (Farmer farmer : assignment1.keySet()) {
            List<Tree> parent1Trees = assignment1.get(farmer);
            List<Tree> parent2Trees = assignment2.get(farmer);
            List<Tree> childTrees = new ArrayList<>();

            int crossoverPoint = (int) (Math.random() * parent1Trees.size());

            for (int i = 0; i < parent1Trees.size() && i < parent2Trees.size(); i++) {
                if (i < crossoverPoint) {
                    childTrees.add(parent1Trees.get(i));
                } else {
                    childTrees.add(parent2Trees.get(i));
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

            for (int i = 0; i < trees.size(); i++) {
                if (Math.random() < MUTATION_RATE) {
                    Tree tree = trees.get(i);
                    trees.remove(i);

                    Farmer randomFarmer = getRandomFarmerExcept(farmer, assignment.keySet());
                    assignment.get(randomFarmer).add(tree);
                }
            }
        }
    }

    private static Farmer getRandomFarmerExcept(Farmer excludeFarmer, Set<Farmer> farmers) {
        List<Farmer> otherFarmers = new ArrayList<>(farmers);
        otherFarmers.remove(excludeFarmer);
        return otherFarmers.get((int) (Math.random() * otherFarmers.size()));
    }

    private static Map<Farmer, List<Tree>> getBestSolution(List<Chromosome> population) {
        Chromosome bestChromosome = Collections.max(population, Comparator.comparingDouble(Chromosome::getFitness));
        return bestChromosome.getAssignment();
    }

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
    }

    public static void main(String[] args) {

        // Esempio di utilizzo
        List<Farmer> farmers = new ArrayList<>();
        farmers.add(new Farmer(1, "Italia", 3));
        farmers.add(new Farmer(2, "Guatemala", 43));
        farmers.add(new Farmer(3, "Perù", 43));

        List<Tree> trees = new ArrayList<>();
        trees.add(new Tree(10, "Guatemala"));
        trees.add(new Tree(5, "Perù"));
        trees.add(new Tree(8, "Italia"));

        Map<Farmer, List<Tree>> optimalAssignment = findOptimalAssignment(farmers, trees);

// Stampa della soluzione ottimale
        for (Farmer farmer : optimalAssignment.keySet()) {
            List<Tree> assignedTrees = optimalAssignment.get(farmer);
            System.out.println("Farmer: " + farmer.getId() + "(" + farmer.getCountry() + ")" + ", Assigned Trees: " + assignedTrees);
        }

    }

}
