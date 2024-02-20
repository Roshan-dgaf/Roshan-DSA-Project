/*
Question 5 (a)

Implement ant colony algorithm solving travelling a salesman problem.

 */
package assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntColony_TSP_Q5A {
    private double[][] pheromoneMatrix;
    private int[][] distanceMatrix;
    private int numOfNodes;
    private int numOfAnts;
    private int sourceNode;
    private int destinationNode;
    private double alpha;
    private double beta;
    private double evaporationRate;
    private double initialPheromone;

    public AntColony_TSP_Q5A(int numOfNodes, int numOfAnts, int sourceNode, int destinationNode,
                             double alpha, double beta, double evaporationRate, double initialPheromone) {
        this.numOfNodes = numOfNodes;
        this.numOfAnts = numOfAnts;
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporationRate = evaporationRate;
        this.initialPheromone = initialPheromone;
        pheromoneMatrix = new double[numOfNodes][numOfNodes];
        distanceMatrix = new int[numOfNodes][numOfNodes];
    }

    public void initializePheromoneMatrix() {
        for (int i = 0; i < numOfNodes; i++) {
            for (int j = 0; j < numOfNodes; j++) {
                pheromoneMatrix[i][j] = initialPheromone;
            }
        }
    }

    public void initializeDistanceMatrix(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

    public List<Integer> findShortestPath() {
        Random random = new Random();
        List<Integer> bestPath = null;
        int bestDistance = Integer.MAX_VALUE;

        for (int iteration = 0; iteration < numOfAnts; iteration++) {
            List<Integer> antPath = constructAntPath(sourceNode, destinationNode, random);
            int antDistance = calculatePathDistance(antPath);

            if (antDistance < bestDistance) {
                bestDistance = antDistance;
                bestPath = antPath;
            }

            updatePheromoneTrail(antPath, antDistance);
        }

        return bestPath;
    }

    private List<Integer> constructAntPath(int source, int destination, Random random) {
        List<Integer> antPath = new ArrayList<>();
        boolean[] visitedNodes = new boolean[numOfNodes];
        int currentNode = source;
        antPath.add(currentNode);
        visitedNodes[currentNode] = true;

        while (currentNode != destination) {
            int nextNode = selectNextNode(currentNode, visitedNodes, random);
            antPath.add(nextNode);
            visitedNodes[nextNode] = true;
            currentNode = nextNode;
        }

        return antPath;
    }

    private int selectNextNode(int currentNode, boolean[] visitedNodes, Random random) {
        double[] probabilities = new double[numOfNodes];
        double probabilitiesSum = 0.0;

        for (int node = 0; node < numOfNodes; node++) {
            if (!visitedNodes[node]) {
                double pheromoneLevel = Math.pow(pheromoneMatrix[currentNode][node], alpha);
                double distance = 1.0 / Math.pow(distanceMatrix[currentNode][node], beta);
                probabilities[node] = pheromoneLevel * distance;
                probabilitiesSum += probabilities[node];
            }
        }

        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;

        for (int node = 0; node < numOfNodes; node++) {
            if (!visitedNodes[node]) {
                double probability = probabilities[node] / probabilitiesSum;
                cumulativeProbability += probability;

                if (randomValue <= cumulativeProbability) {
                    return node;
                }
            }
        }

        return -1; // Error in Logic
    }

    private int calculatePathDistance(List<Integer> path) {
        int distance = 0;
        int pathSize = path.size();

        for (int i = 0; i < pathSize - 1; i++) {
            int currentNode = path.get(i);
            int nextNode = path.get(i + 1);
            distance += distanceMatrix[currentNode][nextNode];
        }

        return distance;
    }

    private void updatePheromoneTrail(List<Integer> path, int distance) {
        double pheromoneDeposit = 1.0 / distance;

        for (int i = 0; i < path.size() - 1; i++) {
            int currentNode = path.get(i);
            int nextNode = path.get(i + 1);

            pheromoneMatrix[currentNode][nextNode] = (1 - evaporationRate) *
                    pheromoneMatrix[currentNode][nextNode] + evaporationRate * pheromoneDeposit;
        }
    }

    public static void main(String[] args) {
        int[][] distanceMatrix = {
                {0, 2, 3, 4},
                {2, 0, 6, 1},
                {3, 6, 0, 2},
                {4, 1, 2, 0}
        };

        int numOfNodes = 4;
        int numOfAnts = 10;
        int sourceNode = 0;
        int destinationNode = 3;
        double alpha = 1.0;
        double beta = 2.0;
        double evaporationRate = 0.5;
        double initialPheromone = 0.1;

        AntColony_TSP_Q5A antColony = new AntColony_TSP_Q5A(numOfNodes, numOfAnts,
                sourceNode, destinationNode, alpha, beta, evaporationRate, initialPheromone);

        antColony.initializePheromoneMatrix();
        antColony.initializeDistanceMatrix(distanceMatrix);
        List<Integer> shortestPath = antColony.findShortestPath();

        System.out.println("Shortest path: " + shortestPath);
    }
}
