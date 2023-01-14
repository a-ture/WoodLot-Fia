package algoritmoGenetico.versione1;

import java.util.Random;

import java.util.Random;
// risolviamo il problema job assigbment in modo classico
public class JobAssignmentGA {
    // Number of workers and jobs
    private static final int N = 10;
    // Cost matrix
    private static final int[][] COST_MATRIX = new int[N][N];
    // Population size
    private static final int POPULATION_SIZE = 500;
    // Number of generations
    private static final int NUM_GENERATIONS = 50;
    // Probability of mutating a gene
    private static final double MUTATION_RATE = 0.8;


    public static void main(String[] args) {

        long startTime = System.nanoTime();

        // Initialize cost matrix
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                COST_MATRIX[i][j] = (int) (Math.random() * 100);
            }
        }

        // Initialize population
        int[][] population = new int[POPULATION_SIZE][N];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = generateRandomSolution();
        }

        System.out.println("Initial population:");
        for (int i = 0; i < POPULATION_SIZE; i++) {
            System.out.println(arrayToString(population[i]));
        }

        // Evaluate fitness of initial population
        int[] fitness = new int[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            fitness[i] = evaluateFitness(population[i]);
        }

        int bestIndex = getBest(population, fitness);
        int bestFitness = fitness[bestIndex];
        System.out.println("Best initial fitness: " + bestFitness);

        // Run the GA
        for (int generation = 0; generation < NUM_GENERATIONS; generation++) {
            // Select parents
            int[][] parents = selectParents(population, fitness);

            // Create children
            int[][] children = new int[POPULATION_SIZE][N];
            for (int i = 0; i < POPULATION_SIZE; i++) {
                int[] child = crossover(parents[i], parents[POPULATION_SIZE - i - 1]);
                children[i] = mutate(child);
            }

            // Evaluate fitness of children
            int[] childFitness = new int[POPULATION_SIZE];
            for (int i = 0; i < POPULATION_SIZE; i++) {
                childFitness[i] = evaluateFitness(children[i]);
            }

            // Replace the worst solutions in the population with the best children
            for (int i = 0; i < POPULATION_SIZE; i++) {
                int worstIndex = getWorst(population, fitness);
                int bestChildIndex = getBest(children, childFitness);
                if (fitness[worstIndex] > childFitness[bestChildIndex]) {
                    population[worstIndex] = children[bestChildIndex];
                    fitness[worstIndex] = childFitness[bestChildIndex];
                }
            }

            // Print the best solution of the current generation
            bestIndex = getBest(population, fitness);
            System.out.println("Generation " + generation + ": " + arrayToString(population[bestIndex]) + " (fitness: " + fitness[bestIndex] + ")");

            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;

            long elapsedTimeMilliseconds = elapsedTime / 1000000;
            System.out.println("Execution time: " + elapsedTimeMilliseconds + " ms");
        }
    }


    // Generates a random solution
    private static int[] generateRandomSolution() {
        int[] solution = new int[N];
        Random rng = new Random();
        for (int i = 0; i < N; i++) {
            solution[i] = rng.nextInt(N);
        }
        return solution;
    }

    // Evaluates the fitness of a solution
    private static int evaluateFitness(int[] solution) {
        int fitness = 0;
        for (int i = 0; i < N; i++) {
            fitness += COST_MATRIX[i][solution[i]];
        }
        return fitness;
    }

    // Selects parents using tournament selection
    private static int[][] selectParents(int[][] population, int[] fitness) {
        int[][] parents = new int[POPULATION_SIZE][N];
        Random rng = new Random();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            int p1 = rng.nextInt(POPULATION_SIZE);
            int p2 = rng.nextInt(POPULATION_SIZE);
            parents[i] = fitness[p1] < fitness[p2] ? population[p1] : population[p2];
        }
        return parents;
    }

    // Applies crossover to create a child from two parents
    private static int[] crossover(int[] p1, int[] p2) {
        int[] child = new int[N];
        Random rng = new Random();
        int crossoverPoint = rng.nextInt(N);
        for (int i = 0; i < crossoverPoint; i++) {
            child[i] = p1[i];
        }
        for (int i = crossoverPoint; i < N; i++) {
            child[i] = p2[i];
        }
        return child;
    }

    // Applies mutation to a child
    private static int[] mutate(int[] child) {
        Random rng = new Random();
        for (int i = 0; i < N; i++) {
            if (rng.nextDouble() < MUTATION_RATE) {
                child[i] = rng.nextInt(N);
            }
        }
        return child;
    }

    // Returns the index of the best solution in the population
    private static int getBest(int[][] population, int[] fitness) {
        int bestIndex = 0;
        for (int i = 1; i < POPULATION_SIZE; i++) {
            if (fitness[i] < fitness[bestIndex]) {
                bestIndex = i;
            }
        }
        return bestIndex;
    }

    // Returns the index of the worst solution in the population
    private static int getWorst(int[][] population, int[] fitness) {
        int worstIndex = 0;
        for (int i = 1; i < POPULATION_SIZE; i++) {
            if (fitness[i] > fitness[worstIndex]) {
                worstIndex = i;
            }
        }
        return worstIndex;
    }

    // Converts an array to a string
    // Converts an array to a string
    private static String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < N; i++) {
            sb.append(array[i]);
            if (i < N - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}


