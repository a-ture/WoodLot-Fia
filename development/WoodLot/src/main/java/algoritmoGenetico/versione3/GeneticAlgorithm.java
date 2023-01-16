package algoritmoGenetico.versione3;


import comune.Farmer;
import comune.Tree;

import java.util.*;

public class GeneticAlgorithm {

    private List<Tree> trees;
    private List<Farmer> farmers;

    private List<Chromosome> population;

    public List<Tree> getTrees() {
        return trees;
    }

    public void setTrees(List<Tree> trees) {
        this.trees = trees;
    }

    public List<Farmer> getFarmers() {
        return farmers;
    }

    public void setFarmers(List<Farmer> farmers) {
        this.farmers = farmers;
    }

    public List<Chromosome> getPopulation() {
        return population;
    }

    public void setPopulation(List<Chromosome> population) {
        this.population = population;
    }

    public GeneticAlgorithm(List<Tree> trees, List<Farmer> farmers) {
        this.trees = trees;
        this.farmers = farmers;
    }

    public void initializePopulation(int populationSize) {
        population = new ArrayList<>();
        Chromosome validIndividual = generateValidIndividual();
        population.add(validIndividual);
        for (int i = 1; i < populationSize; i++) {
            Chromosome newIndividual = population.get((int) (Math.random() * population.size())).permute(trees, farmers);
            population.add(newIndividual);
        }

    }

    public Chromosome generateValidIndividual() {
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


    public List<Chromosome> tournamentSelection(int tournamentSize, int numberOfSelected) {
        List<Chromosome> selected = new ArrayList<>();
        for (int i = 0; i < numberOfSelected; i++) {
            List<Chromosome> tournament = new ArrayList<>();
            for (int j = 0; j < tournamentSize; j++) {
                int randomIndex = (int) (Math.random() * population.size());
                tournament.add(population.get(randomIndex));
            }
            Chromosome winner = tournament.stream().max(Comparator.comparingDouble(Chromosome::getFitness)).get();
            selected.add(winner);
        }
        return selected;
    }

    public Chromosome evolve(Chromosome chromosome) {
        Chromosome parent1 = chromosome.tournamentSelection(population);
        Chromosome parent2 = chromosome.tournamentSelection(population);
        Chromosome offspring = parent1.crossover(parent2);
        return offspring;
    }

    public void mutate(double mutationRate) {
        for (Chromosome chromosome : population) {
            double randomNum = Math.random();
            if (randomNum < mutationRate) {
                chromosome.mutate(trees, farmers);
            }
        }
    }

}