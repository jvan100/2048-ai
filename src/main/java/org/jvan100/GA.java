package org.jvan100;

import org.jvan100.jutils.iterable.AbstractEnumerator.EnumeratedItem;
import org.jvan100.jutils.iterable.Enumerator;

import java.util.Arrays;
import java.util.Random;

public class GA {

    private final int populationSize;
    private final int rounds;

    private GameAgent[] population;
    private int generation;

    private Random rand;

    private int bestFitness = 0;

    public static void main(String[] args) {
        final GA ga = new GA(10, 100);
        ga.evolve();
    }

    public GA(int populationSize, int rounds) {
        this.populationSize = populationSize;
        this.rounds = rounds;
        this.generation = 0;
        this.rand = new Random();
    }

    public void evolve() {
        // Create agents
        population = new GameAgent[populationSize];

        for (int i = 0; i < populationSize; i++)
            population[i] = new GameAgent();

        while (generation++ < rounds) {
            // Run games and calculate fitnesses
            Arrays.stream(population).forEach(GameAgent::run);

            // Keep top 2 performers
            GameAgent[] nextPopulation = new GameAgent[populationSize];

            System.arraycopy(Arrays.stream(population).sorted().limit(2).toArray(GameAgent[]::new), 0, nextPopulation, 0, 2);

            //double fitnessSum = Arrays.stream(population).mapToInt(GameAgent::getFitness).sum();
            double fitnessSum = Arrays.stream(nextPopulation).limit(2).mapToInt(GameAgent::getFitness).sum();

            bestFitness = nextPopulation[0].getFitness();

            System.out.println("Generation " + generation + " - Best: " + bestFitness + ", Avg: " + fitnessSum / populationSize);

            // Select agents to reproduce
            for (int i = 2; i < populationSize; i++) {
                final double randVal = rand.nextDouble();
                double runningFitness = 0;

                for (int j = 0; j < 2; j++) {
                    runningFitness += nextPopulation[j].getFitness() / fitnessSum;

                    if (runningFitness > randVal) {
                        // Chosen for reproduction
                        nextPopulation[i] = nextPopulation[j].cloneAgent();
                        break;
                    }
                }
            }

            // Mutate agents
            final double mutationRate = 0.15;
            final double mutationRange = 0.05;

            for (int i = 2; i < populationSize; i++) {
                final GameAgent agent = nextPopulation[i];

                for (int j = 0; j < 3; j++) {
                    final double randVal = rand.nextDouble();

                    if (randVal < mutationRate) {
                        double newWeight = agent.getWeight(j) + mutationRange * ((rand.nextDouble() * 2) - 1);
                        newWeight = Math.min(newWeight, 1);
                        newWeight = Math.max(newWeight, -1);
                        agent.setWeight(j, newWeight);
                    }
                }
            }

            population = nextPopulation;
        }
    }

}
