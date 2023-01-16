package algoritmoGenetico.versione3;


import comune.Farmer;
import comune.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneticAlgorithm {

    private List<Tree> trees;
    private List<Farmer> farmers;

    public GeneticAlgorithm(List<Tree> trees, List<Farmer> farmers) {
        this.trees = trees;
        this.farmers = farmers;
    }

    public List<Chromosome> initializePopulation(int populationSize) {
        List<Chromosome> population = new ArrayList<>();
        Chromosome validIndividual = generateValidIndividual();
        population.add(validIndividual);
        for (int i = 1; i < populationSize; i++) {
            Chromosome newIndividual = population.get((int) (Math.random() * population.size())).permute(trees, farmers);
            population.add(newIndividual);
        }
        return population;
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
            int randomIndex = (int) (Math.random() * possibleFarmers.size());
            int farmerId = possibleFarmers.get(randomIndex);
            gene[i] = farmerId;
            farmerTreeCount.put(farmers.get(farmerId).getId(), farmerTreeCount.get(farmers.get(farmerId).getId()) + 1);
        }
        chromosome.setGene(gene);
        chromosome.evaluateFitness(trees, farmers);
        return chromosome;
    }
}