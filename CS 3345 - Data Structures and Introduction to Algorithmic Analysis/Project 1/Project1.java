//Programing Assignment 1
//Name: Chhay Lay Heng
//Class: CS3345.004

import java.util.*;

public class Project1 {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.print("Enter amount N: ");
        int N = in.nextInt();
        System.out.println("");

        SplayTree tree = new SplayTree();

        // insert node
        for (int i = 0; i < N; i++) {
            System.out.print("Enter the value: ");
            int insert = in.nextInt();
            tree.insert(insert);
        }
		
		System.out.println("");
        tree.printTree();
        System.out.println("");

        // search node
        System.out.print("Enter value you want to search: ");
        int search = in.nextInt();
        tree.search(search);
        System.out.println("Search " + search + ": " + tree.search(search) + "\n");

        // delete node
        System.out.print("Enter the node you want to delete: ");
        int delete = in.nextInt();
        tree.delete(delete);

        System.out.println("");
        tree.printTree();
    }
}

class Node {

    int key;
    Node left, right;

    Node(int key) {
        this.key = key;
        left = right = null;
    }
}

class SplayTree {

    Node root;

    Node rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    Node leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    Node splay(int key) {

        if (root == null || root.key == key) {
            return root;
        }

        Node dummy = new Node(-1);
        Node left = dummy, right = dummy;
        Node current = root;

        while (true) {
            if (key < current.key) {
                if (current.left == null) {
                    break;
                }
                if (key < current.left.key) {
                    current = rightRotate(current);
                    if (current.left == null) {
                        break;
                    }
                }
                right.left = current;
                right = current;
                current = current.left;
                right.left = null;
            } else if (key > current.key) {
                if (current.right == null) {
                    break;
                }
                if (key > current.right.key) {
                    current = leftRotate(current);
                    if (current.right == null) {
                        break;
                    }
                }
                left.right = current;
                left = current;
                current = current.right;
                left.right = null;
            } else {
                break;
            }
        }

        left.right = current.left;
        right.left = current.right;
        current.left = dummy.right;
        current.right = dummy.left;
        root = current;
        return root;
    }

    void insert(int key) {

        Node node = new Node(key);

        if (root == null) {
            root = node;
            return;
        }

        root = splay(key);

        if (root.key == key) {
            return;
        }

        if (key < root.key) {

            node.left = root.left;
            node.right = root;
            root.left = null;
        } else {

            node.right = root.right;
            node.left = root;
            root.right = null;
        }
        root = node;
    }

    void delete(int key) {

        if (root == null) {
            return;
        }

        root = splay(key);

        if (root.key != key) {
            return;
        }

        if (root.left == null) {
            root = root.right;

        } else if (root.right == null) {
            root = root.left;

        } else {
            Node leftTree = root.left;
            Node rightTree = root.right;
            root = leftTree;
            splay(key);
            root.right = rightTree;
        }
    }

    boolean search(int key) {

        if (root == null) {
            return false;
        }

        root = splay(key);
        return root.key == key;
    }

    void preorderTraversal(Node node) {

        if (node != null) {
            System.out.print(node.key + "RT ");
            preorderTraversal(node.left);

            if (node.left != null) {
                System.out.print(node.left.key + "L ");
            }

            if (node.right != null) {
                System.out.print(node.right.key + "R ");
            }

            preorderTraversal(node.right);
        }
    }

    void printTree() {
        preorderTraversal(root);
        System.out.println();
    }
}
