
/*
Question 4 (b)

You are provided with balanced binary tree with the target value k. return x number of values that are closest to the
given target k. provide solution in O(n)
Note: You have only one set of unique values x in binary search tree that are closest to the target.

// Example tree:
        BinaryTree tree = new BinaryTree();
        tree.insert(4);
        tree.insert(2);
        tree.insert(5);
        tree.insert(1);
        tree.insert(3);

        double target(k) = 3.8;
        int x = 2;

        Find Output: 3, 4

*/

package assignment;

import java.util.*;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    public TreeNode(int val) {
        this.val = val;
    }
}

class BalancedBinaryTree {
    TreeNode root;

    public void insert(int val) {
        root = insertRec(root, val);
    }

    private TreeNode insertRec(TreeNode root, int val) {
        if (root == null) {
            root = new TreeNode(val);
            return root;
        }

        if (val < root.val)
            root.left = insertRec(root.left, val);
        else if (val > root.val)
            root.right = insertRec(root.right, val);

        return root;
    }

    public List<Integer> findClosestValues(int x, double target) {
        List<Integer> closestValues = new ArrayList<>();
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(x, (a, b) -> Double.compare(Math.abs(a - target), Math.abs(b - target)));

        inorderTraversal(root, minHeap, x, target);

        while (!minHeap.isEmpty()) {
            closestValues.add(minHeap.poll());
        }

        return closestValues;
    }

    private void inorderTraversal(TreeNode root, PriorityQueue<Integer> minHeap, int x, double target) {
        if (root == null)
            return;

        inorderTraversal(root.left, minHeap, x, target);

        if (minHeap.size() < x)
            minHeap.offer(root.val);
        else if (Math.abs(root.val - target) < Math.abs(minHeap.peek() - target)) {
            minHeap.poll();
            minHeap.offer(root.val);
        }

        inorderTraversal(root.right, minHeap, x, target);
    }
}

public class ClosestValuesInBST_Q4B {
    public static void main(String[] args) {
        BalancedBinaryTree tree = new BalancedBinaryTree();
        tree.insert(4);
        tree.insert(2);
        tree.insert(5);
        tree.insert(1);
        tree.insert(3);

        double target = 3.8;
        int x = 2;

        List<Integer> closestValues = tree.findClosestValues(x, target);
        System.out.println("Closest values to " + target + " with x = " + x + ": " + closestValues);
    }
}
