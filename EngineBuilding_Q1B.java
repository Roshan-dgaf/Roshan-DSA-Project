/*
Question 1 (b)

You are the captain of a spaceship and you have been assigned a mission to explore a distant galaxy. Your
spaceship is equipped with a set of engines, where each engine represented by a block. Each engine requires a
specific amount of time to be built and can only be built by one engineer.
Your task is to determine the minimum time needed to build all the engines using the available engineers. The
engineers can either work on building an engine or split into two engineers, with each engineer sharing the
workload equally. Both decisions incur a time cost.
The time cost of splitting one engineer into two engineers is given as an integer split. Note that if two engineers
split at the same time, they split in parallel so the cost would be split.
Your goal is to calculate the minimum time needed to build all the engines, considering the time cost of splitting
engineers.
Input: engines= [1,2,3]
Split cost (k)=1
Output: 4
Example:
Imagine you need to build engine represented by an array [1,2,3] where ith element of an array i.e a[i] represents
unit time to build ith engine and the split cost is 1. Initially, there is only one engineer available.
The optimal strategy is as follows:
1. The engineer splits into two engineers, increasing the total count to two. (Split Time: 1) and assign first
engineer to build third engine i.e. which will take 3 unit of time.
2. Again, split second engineer into two (split time :1) and assign them to build first and second engine
respectively.
Therefore, the minimum time needed to build all the engines using optimal decisions on splitting engineers and
assigning them to engines. =1+ max (3, 1 + max (1, 2)) = 4.
Note: The splitting process occurs in parallel, and the goal is to minimize the total time required to build all the
engines using the available engineers while considering the time cost of splitting.


*/

package assignment;

public class EngineBuilding_Q1B {

    public static void main(String[] args) {
        int[] engines = {1, 2, 3};
        int splitCost = 1;
        System.out.println(calculateMinimumTime(engines, splitCost));
    }

    public static int calculateMinimumTime(int[] engines, int splitCost) {
        int maxTime = 0;
        int availableEngineers = 1; // Number of available engineers
        
        // Iterating through the engines in reverse order
        for (int i = engines.length - 1; i >= 0; i--) {
            int engineBuildTime = engines[i];
            
            // Loop until we have enough engineers to build the engine
            while (availableEngineers < engineBuildTime) {
                // Calculate the split time considering the available engineers and the split cost
                int splitTime = Math.min(engineBuildTime - availableEngineers, splitCost);
                availableEngineers += splitTime; // Increase the number of engineers by splitTime
                maxTime = Math.max(maxTime, engineBuildTime); // Update the maximum time
            }
            availableEngineers--; // One engineer starts working on the engine
        }

        return maxTime + 1; // Return the total time needed to build all engines
    }
}
