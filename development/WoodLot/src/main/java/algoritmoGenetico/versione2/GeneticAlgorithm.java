package algoritmoGenetico.versione2;


import java.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GeneticAlgorithm {
    private static final int POPULATION_SIZE = 50;
    private static final int NUM_ITERATIONS = 1000;
    private static final double MUTATION_RATE = 0.01;

    private static final Random rand = new Random();

    public static void main(String[] args) {
        List<Worker> workers = createWorkers();
        List<Job> jobs = createJobs();

        Solution bestSolution = null;
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            Solution solution = generateRandomSolution(workers, jobs);
            double fitness = solution.getFitness();

            if (bestSolution == null || fitness > bestSolution.getFitness()) {
                bestSolution = solution;
            }

            List<Solution> offspring = generateOffspring(solution);
            for (Solution child : offspring) {
                if (child.getFitness() > fitness) {
                    solution = child;
                    fitness = child.getFitness();
                }
            }
        }

        System.out.println("Best solution found:");
        for (Map.Entry<Worker, Job> entry : bestSolution.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    private static List<Worker> createWorkers() {
        // crea una lista di lavoratori con le loro qualifiche
        List<Worker> workers = new ArrayList<>();
        workers.add(new Worker("John", "plumbing", "electricity"));
        workers.add(new Worker("Jane", "plumbing"));
        workers.add(new Worker("Bill", "carpentry", "plumbing", "electricity"));
        workers.add(new Worker("Mary", "carpentry"));
        return workers;
    }

    private static List<Job> createJobs() {
        // crea una lista di lavori con i loro requisiti di qualifica
        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job("plumbing", 5));
        jobs.add(new Job("electricity", 4));
        jobs.add(new Job("carpentry", 3));
        return jobs;
    }

    private static Solution generateRandomSolution(List<Worker> workers, List<Job> jobs) {
        // crea una soluzione casuale assegnando a ciascun lavoratore un lavoro che può eseguire
        Solution solution = new Solution();
        for (Worker worker : workers) {
            for (Job job : jobs) {
                if (worker.canComplete(job)) {
                    solution.put(worker, job);
                    break;
                }
            }
        }
        return solution;
    }

    private static List<Solution> generateOffspring(Solution solution) {
        // crea figli mutando casualmente la soluzione attuale
        List<Solution> offspring = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Solution child = new Solution(solution);
            mutate(child);
            offspring.add(child);
        }
        return offspring;
    }

    private static void mutate(Solution solution) {
        // effettua una mutazione casuale della soluzione attuale, ad esempio scambiando due lavoratori o assegnando un lavoro a un lavoratore non qualificato
        if (rand.nextDouble() < MUTATION_RATE) {
            List<Worker> workers = new ArrayList<>(solution.keySet());
            Worker worker1 = workers.get(rand.nextInt(workers.size()));
            Worker worker2 = workers.get(rand.nextInt(workers.size()));
            Job job = solution.get(worker1);
            solution.put(worker1, solution.get(worker2));
            solution.put(worker2, job);
        }
    }

}

class Solution extends HashMap<Worker, Job> {
    // estende HashMap per memorizzare l'assegnamento di ciascun lavoratore a un lavoro

    public Solution() {
        super();
    }

    public Solution(Solution solution) {
        super(solution);
    }

    public double getFitness() {
        // calcola il valore di fitness della soluzione assegnando un punteggio più alto a soluzioni che utilizzano più lavoratori e che assegnano lavori a lavoratori con le qualifiche adeguate
        double fitness = 0;
        for (Map.Entry<Worker, Job> entry : this.entrySet()) {
            Worker worker = entry.getKey();
            Job job = entry.getValue();
            if (worker.canComplete(job)) {
                fitness += 1;
            }
        }
        return fitness;
    }
}

class Worker {
    private String name;
    private Set<String> qualifications;

    public Worker(String name, String... qualifications) {
        this.name = name;
        this.qualifications = new HashSet<>(Arrays.asList(qualifications));
    }

    public boolean canComplete(Job job) {
        return qualifications.contains(job.getQualification());
    }

    @Override
    public String toString() {
        return name;
    }
}

class Job {
    private String qualification;
    private int duration;


    public Job(String qualification, int duration) {
        this.qualification = qualification;
        this.duration = duration;
    }

    public String getQualification() {
        return qualification;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return qualification + " (" + duration + " days)";
    }

}
