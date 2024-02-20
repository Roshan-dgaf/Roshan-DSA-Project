
/*
Question 2 (b)

You are given an integer n representing the total number of individuals. Each individual is identified by a unique
ID from 0 to n-1. The individuals have a unique secret that they can share with others.
The secret-sharing process begins with person 0, who initially possesses the secret. Person 0 can share the secret
with any number of individuals simultaneously during specific time intervals. Each time interval is represented by
a tuple (start, end) where start and end are non-negative integers indicating the start and end times of the interval.
You need to determine the set of individuals who will eventually know the secret after all the possible secretsharing intervals have occurred.
Example:
Input: n = 5, intervals = [(0, 2), (1, 3), (2, 4)], firstPerson = 0
Output: [0, 1, 2, 3, 4]
Explanation:
In this scenario, we have 5 individuals labeled from 0 to 4.
The secret-sharing process starts with person 0, who has the secret at time 0. At time 0, person 0 can share the
secret with any other person. Similarly, at time 1, person 0 can also share the secret. At time 2, person 0 shares the
secret again, and so on.
Given the intervals [(0, 2), (1, 3), (2, 4)], we can observe that during these intervals, person 0 shares the secret with
every other individual at least once.
Hence, after all the secret-sharing intervals, individuals 0, 1, 2, 3, and 4 will eventually know the secret.

*/
package assignment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SecretSharingProcess_Q2B {

    // Method to determine individuals who will eventually know the secret
    public static List<Integer> determineIndividualsWithSecret(int totalIndividuals, int[][] timeIntervals, int initialPerson) {
        List<Integer> individualsWithSecret = new ArrayList<>();
        Set<Integer> sharedSet = new HashSet<>();
        sharedSet.add(initialPerson); // Initially, only the initial person has the secret

        // Iterate through the time intervals
        for (int[] interval : timeIntervals) {
            int startTime = interval[0];
            int endTime = interval[1];

            // Create a new set to store the individuals with the secret after the current interval
            Set<Integer> newSharedSet = new HashSet<>(sharedSet);

            // Iterate through the individuals with the secret
            for (int person : sharedSet) {
                // If it's the initial person, share the secret with others within the interval
                if (person == initialPerson) {
                    for (int i = startTime; i <= endTime; i++) {
                        newSharedSet.add(i % totalIndividuals); // Share the secret with other individuals
                    }
                } else {
                    newSharedSet.add(person); // Preserve the individuals who already have the secret
                }
            }

            sharedSet = newSharedSet; // Update the set of individuals with the secret after the current interval
        }

        individualsWithSecret.addAll(sharedSet); // Add all individuals with the secret to the result list
        return individualsWithSecret; // Return the list of individuals who eventually know the secret
    }

    // Main method to test the functionality
    public static void main(String[] args) {
        int totalIndividuals = 5;
        int[][] timeIntervals = {{0, 2}, {1, 3}, {2, 4}};
        int initialPerson = 0;

        // Determine individuals who eventually know the secret
        List<Integer> individualsWithSecret = determineIndividualsWithSecret(totalIndividuals, timeIntervals, initialPerson);

        // Output the result
        System.out.println("Individuals who eventually know the secret: " + individualsWithSecret);
    }
}
