
/*
Question 3 (b)

Implement Kruskal algorithm and priority queue using minimum heap

*/

package assignment;

import java.util.Arrays;

public class KruskalMST_Q3B {
    public static class Edge implements Comparable<Edge> {
        int source;
        int destination;
        int weight;

        Edge(int source, int destination, int weight) {
            this.source = source;
            this.weight = weight;
            this.destination = destination;
        }

        @Override
        public int compareTo(Edge otherEdge) {
            return this.weight - otherEdge.weight;
        }
    }

    private int vertices;
    private Edge[] edges;
    private int[][] minimumSpanningTree;

    public KruskalMST_Q3B(int vertices) {
        this.vertices = vertices;
        this.edges = new Edge[vertices * (vertices - 1) / 2];  // Initialize edge array
        this.minimumSpanningTree = new int[vertices][vertices];  // Initialize MST array
    }

    private int edgeCount = -1;

    public void addEdge(int source, int destination, int weight) {
        if (edgeCount + 1 < vertices) {
            edges[++edgeCount] = new Edge(source, destination, weight);
        } else {
            System.out.println("Cannot add more edges. The array is full.");
        }
    }

    public void findMinimumSpanningTree() {
        Arrays.sort(edges, 0, edgeCount + 1);
        int edgesTaken = 1;
        int edgeCounter = -1;
        int[] size = new int[vertices];
        int[] parent = new int[vertices];
        Arrays.fill(parent, -1);

        while (edgesTaken <= vertices - 1) {
            Edge currentEdge = edges[++edgeCounter];
            int sourceAbsRoot = find(currentEdge.source, parent);
            int destAbsRoot = find(currentEdge.destination, parent);
            if (sourceAbsRoot == destAbsRoot) {
                continue;
            }
            minimumSpanningTree[currentEdge.source][currentEdge.destination] = currentEdge.weight;
            minimumSpanningTree[currentEdge.destination][currentEdge.source] = currentEdge.weight;
            edgesTaken++;
            union(sourceAbsRoot, destAbsRoot, size, parent);
        }
    }

    private int find(int x, int[] parent) {
        if (parent[x] == -1) {
            return x;
        }
        return parent[x] = find(parent[x], parent);
    }

    private void union(int sourceAbs, int destAbs, int[] size, int[] parent) {
        if (size[sourceAbs] > size[destAbs]) {
            parent[destAbs] = sourceAbs;
        } else if (size[sourceAbs] < size[destAbs]) {
            parent[sourceAbs] = destAbs;
        } else {
            parent[destAbs] = sourceAbs;
            size[sourceAbs]++;
        }
    }

    public static void main(String[] args) {
        KruskalMST_Q3B kruskal = new KruskalMST_Q3B(4);

        // Adding edges to the graph
        kruskal.addEdge(0, 1, 10);
        kruskal.addEdge(0, 2, 6);
        kruskal.addEdge(0, 3, 5);
        kruskal.addEdge(1, 3, 15);

        // Attempting to add another edge when the array is full
        kruskal.addEdge(2, 3, 4);

        // Finding the Minimum Spanning Tree using Kruskal's Algorithm
        kruskal.findMinimumSpanningTree();

        // Printing the Minimum Spanning Tree
        System.out.println("Minimum Spanning Tree using Kruskal's Algorithm:");
        for (int i = 0; i < kruskal.vertices; i++) {
            for (int j = 0; j < kruskal.vertices; j++) {
                System.out.print(kruskal.minimumSpanningTree[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
