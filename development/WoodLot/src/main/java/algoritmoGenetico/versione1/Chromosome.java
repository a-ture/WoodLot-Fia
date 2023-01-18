package algoritmoGenetico.versione1;

import comune.Farmer;
import comune.Tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * n questa classe, si utilizza un array di interi per rappresentare un cromosoma,
 * dove ogni elemento dell'array rappresenta l'assegnazione di un albero a un contadino specifico.
 * La funzione di fitness valuta quanto bene una soluzione soddisfa i vincoli del problema,
 * ad esempio, premiando le soluzioni che assegnano gli alberi a contadini del paese corretto e penal
 * ho modificato la funzione di fitness per premiare le soluzioni che assegnano più alberi ai contadini che ne hanno
 * meno. Ho creato una mappa per tracciare quali contadini sono stati assegnati e ho calcolato il numero minimo di
 * alberi piantati tra tutti i contadini. Poi ho premiato ogni contadino che ha piantato alberi pari al numero minimo.
 */


public class Chromosome {
    private int[] gene;
    private double fitness;
    private List<Tree> trees;
    private List<Farmer> farmers;
    private Map<Integer, Boolean> farmersAssignedMap;

    public Chromosome(int size, List<Tree> trees, List<Farmer> farmers) {
        this.gene = new int[size];
        this.trees = trees;
        this.farmers = farmers;
        this.farmersAssignedMap = new HashMap<>();
        for (Farmer farmer : farmers) {
            farmersAssignedMap.put(farmer.getId(), false);
        }

    }

    public int[] getGene() {
        return gene;
    }

    public void setGene(int[] gene) {
        this.gene = gene;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void evaluateFitness(List<Tree> trees, List<Farmer> farmers) {
        int treesAssigned = 0;
        int farmersAssigned = 0;
        double fitnessScore = 0;
        Map<Integer, Integer> farmerTreeCount = new HashMap<>();
        for (int i = 0; i < farmers.size(); i++) {
            farmerTreeCount.put(farmers.get(i).getId(), farmers.get(i).getTreesPlanted());
        }

        for (int i = 0; i < this.gene.length; i++) {
            int farmerId = this.gene[i];
            if (farmerId != -1) {
                Farmer farmer = farmers.get(farmerId);

                int treeId = trees.get(i).getId() - 1;
                Tree tree = trees.get(treeId);
                if (farmer.getCountry().equals(tree.getCountry())
                        && farmerTreeCount.containsKey(farmerId) &&
                        farmerTreeCount.get(farmerId) < (trees.size() / farmers.size())) {
                    fitnessScore++;
                    farmerTreeCount.put(farmerId, farmerTreeCount.get(farmerId) + 1);
                    treesAssigned++;
                    if (!farmersAssignedMap.containsKey(farmerId) || !farmersAssignedMap.get(farmerId)) {
                        farmersAssigned++;
                        farmersAssignedMap.put(farmerId, true);
                    }
                } else {
                    fitnessScore--;
                }
            }
        }
        //premiare le soluzioni che assegnano più alberi ai contadini che ne hanno meno
        int minTreesPlanted = Integer.MAX_VALUE;
        for (Integer count : farmerTreeCount.values()) {
            if (count < minTreesPlanted) {
                minTreesPlanted = count;
            }
        }

        for (Integer count : farmerTreeCount.values()) {
            if (count == minTreesPlanted) {
                fitnessScore += 1;
            }
        }
        this.fitness = fitnessScore;
    }

    public Chromosome permute(List<Tree> trees, List<Farmer> farmers) {
        int[] gene = this.getGene();
        int randomIndex1 = (int) (Math.random() * gene.length);
        int randomIndex2 = (int) (Math.random() * gene.length);
        int temp = gene[randomIndex1];
        gene[randomIndex1] = gene[randomIndex2];
        gene[randomIndex2] = temp;
        Chromosome newChromosome = new Chromosome(gene.length, trees, farmers);
        newChromosome.setGene(gene);
        newChromosome.evaluateFitness(trees, farmers);
        return newChromosome;
    }

    public Chromosome crossover(Chromosome parent2) {
        Chromosome offspring = new Chromosome(gene.length, trees, farmers);
        int[] offspringGene = new int[gene.length];
        int crossoverPoint = (int) (Math.random() * gene.length);
        for (int i = 0; i < crossoverPoint; i++) {
            offspringGene[i] = this.gene[i];
        }
        for (int i = crossoverPoint; i < gene.length; i++) {
            offspringGene[i] = parent2.getGene()[i];
        }
        offspring.setGene(offspringGene);
        offspring.evaluateFitness(trees, farmers);
        return offspring;
    }


    public void mutate() {
        int randomIndex1 = (int) (Math.random() * gene.length);
        int randomIndex2 = (int) (Math.random() * gene.length);
        int temp = gene[randomIndex1];
        gene[randomIndex1] = gene[randomIndex2];
        gene[randomIndex2] = temp;
        evaluateFitness(trees, farmers);
    }

}