/*
Question 1 (a)

You are a planner working on organizing a series of events in a row of n n. Each venue can be decorated with
one of the k available k. However, adjacent n should not have the same theme. The cost of decorating
each venue with a certain theme varies.
The costs of decorating each venue with a specific theme are represented by an n x k cost matrix. For example,
costs [0][0] represents the cost of decorating venue 0 with theme 0, and costs[1][2] represents the cost of
decorating venue 1 with theme 2. Your task is to find the minimum cost to decorate all the n while adhering
to the adjacency constraint.
For example, given the input costs = [[1, 5, 3], [2, 9, 4]], the minimum cost to decorate all the n is 5. One
possible arrangement is decorating venue 0 with theme 0 and venue 1 with theme 2, resulting in a minimum cost of
1 + 4 = 5. Alternatively, decorating venue 0 with theme 2 and venue 1 with theme 0 also yields a minimum cost of
3 + 2 = 5.
Write a function that takes the cost matrix as input and returns the minimum cost to decorate all the n while
satisfying the adjacency constraint.
Please note that the costs are positive integers.
Example: Input: [[1, 3, 2], [4, 6, 8], [3, 1, 5]] Output: 7
Explanation: Decorate venue 0 with theme 0, venue 1 with theme 1, and venue 2 with theme 0. Minimum cost: 1 +
6 + 1 = 7.

*/
package assignment;

public class EventDecorator_Q1 {

    // Method to calculate the minimum cost of decorating all venues
    public static int calculateMinimumCost(int[][] decorations) {
        // Check if decorations array is valid
        if (decorations == null || decorations.length == 0 || decorations[0].length == 0) {
            return 0; // If invalid, return 0
        }

        int venues = decorations.length; // Number of venues
        int themes = decorations[0].length; // Number of available themes

        // Array to store minimum costs for each venue and theme combination
        int[][] minCosts = new int[venues][themes];

        // Initialize the first row with the costs of decorating the first venue
        for (int themeIndex = 0; themeIndex < themes; themeIndex++) {
            minCosts[0][themeIndex] = decorations[0][themeIndex];
        }

        // Iterate through the venues
        for (int venueIndex = 1; venueIndex < venues; venueIndex++) {
            // Iterate through the available themes for each venue
            for (int themeIndex = 0; themeIndex < themes; themeIndex++) {
                // Find the minimum cost for the current venue and theme combination
                minCosts[venueIndex][themeIndex] = decorations[venueIndex][themeIndex] 
                                                    + findMinimumExcept(minCosts[venueIndex - 1], themeIndex);
            }
        }

        // Find the minimum cost from the last row
        return findMinimum(minCosts[venues - 1]);
    }

    // Helper method to find the minimum value in an array, excluding the value at a specific index
    private static int findMinimumExcept(int[] array, int excludeIndex) {
        int min = Integer.MAX_VALUE;
        // Iterate through the array
        for (int i = 0; i < array.length; i++) {
            // Exclude the value at the specified index
            if (i != excludeIndex) {
                // Update the minimum value if a smaller value is found
                min = Math.min(min, array[i]);
            }
        }
        return min;
    }

    // Helper method to find the minimum value in an array
    private static int findMinimum(int[] array) {
        int min = Integer.MAX_VALUE;
        // Iterate through the array
        for (int value : array) {
            // Update the minimum value if a smaller value is found
            min = Math.min(min, value);
        }
        return min;
    }

    // Main method to test the functionality
    public static void main(String[] args) {
        // Example decorations array
        int[][] decorations = {{2, 4, 1}, {3, 7, 5}, {6, 2, 4}}; // Modified decorations array
        // Calculate the minimum cost
        int minimumCost = calculateMinimumCost(decorations);
        // Output the result
        System.out.println("Minimum cost: " + minimumCost); // Output: 6
    }
}
