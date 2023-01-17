package algoritmoGenetico.versione3;


import comune.Farmer;
import comune.Tree;

import java.util.*;

public class GeneticAlgorithm {
    private List<Tree> trees;
    private List<Farmer> farmers;
    private List<Chromosome> population;
    private final int POPULATION_SIZE;
    private final int TOURNAMENT_SIZE;
    private final int NUMBER_OF_SELECTED;
    private final double MUTATION_RATE;
    private final int NUMBER_OF_ITERATIONS;
    private Chromosome bestSolution;

    private double bestFitness;


    public GeneticAlgorithm(List<Tree> trees, List<Farmer> farmers, int populationSize, int tournamentSize, int numberOfSelected, double mutationRate, int numberOfIterations) {
        this.trees = trees;
        this.farmers = farmers;
        this.POPULATION_SIZE = populationSize;
        this.TOURNAMENT_SIZE = tournamentSize;
        this.NUMBER_OF_SELECTED = numberOfSelected;
        this.MUTATION_RATE = mutationRate;
        this.NUMBER_OF_ITERATIONS = numberOfIterations;
    }

    public Chromosome start() {
        initializePopulation();
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            List<Chromosome> newPopulation = new ArrayList<>();
            for (int j = 0; j < POPULATION_SIZE; j++) {
                Chromosome offspring = evolve();
                newPopulation.add(offspring);
            }
            population = newPopulation;
            mutate();
            updateBestSolution();
        }
        return bestSolution;
    }

    private void initializePopulation() {
        population = new ArrayList<>();
        Chromosome validIndividual = generateValidIndividual();
        bestSolution = generateValidIndividual();
        population.add(validIndividual);
        for (int i = 1; i < POPULATION_SIZE; i++) {
            Chromosome newIndividual = population.get((int) (Math.random() * population.size())).permute(trees, farmers);
            population.add(newIndividual);
        }
    }

    private Chromosome generateValidIndividual() {
        Chromosome chromosome = new Chromosome(trees.size(), trees, farmers);
        int[] gene = new int[trees.size()];
        Map<Integer, Integer> farmerTreeCount = new HashMap<>();
        for (int i = 0; i < farmers.size(); i++) {
            farmerTreeCount.put(farmers.get(i).getId(), 0);
        }
        for (int i = 0; i < trees.size(); i++) {
            List<Integer> possibleFarmers = new ArrayList<>();
            for (int j = 0; j < farmers.size(); j++) {
                if (farmers.get(j).getCountry().equals(trees.get(i).getCountry()) && farmerTreeCount.get(farmers.get(j).getId()) < trees.size() / farmers.size()) {
                    possibleFarmers.add(j);
                }
            }
            if (possibleFarmers.size() > 0) {
                int randomIndex = (int) (Math.random() * possibleFarmers.size());
                int farmerId = possibleFarmers.get(randomIndex);
                gene[i] = farmerId;
                farmerTreeCount.put(farmers.get(farmerId).getId(), farmerTreeCount.get(farmers.get(farmerId).getId()) + 1);
            } else {
// Handle the case where there are no possible farmers for the current tree
            }
        }
        chromosome.setGene(gene);
        chromosome.evaluateFitness(trees, farmers);
        return chromosome;
    }

    private Chromosome evolve() {
        Chromosome parent1 = tournamentSelection();
        Chromosome parent2 = tournamentSelection();
        Chromosome offspring = parent1.crossover(parent2);
        return offspring;
    }

    private Chromosome tournamentSelection() {
        List<Chromosome> tournament = new ArrayList<>();
        for (int j = 0; j < TOURNAMENT_SIZE; j++) {
            int randomIndex = (int) (Math.random() * population.size());
            tournament.add(population.get(randomIndex));
        }
        Chromosome winner = tournament.stream().max(Comparator.comparingDouble(Chromosome::getFitness)).get();
        return winner;
    }

    private void mutate() {
        for (Chromosome chromosome : population) {
            double randomNum = Math.random();
            if (randomNum < MUTATION_RATE) {
                chromosome.mutate(trees, farmers);
                chromosome.evaluateFitness(trees, farmers);
            }
        }
    }

    private void updateBestSolution() {
        Chromosome currentBest = population.stream().max(Comparator.comparingDouble(Chromosome::getFitness)).get();
        if (currentBest.getFitness() > bestSolution.getFitness()) {
            bestSolution = currentBest;
        }
    }

}