import java.util.*;

public class graphNode {

    // Node attributes
    String name;
    boolean isChance = false;
    boolean isDecision = false;
    boolean isTerminal = false;
    double reward = 0;
    List<String> edges = new ArrayList<>();
    List<Double> probability = new ArrayList<>();
    double value = 0;
    String curPolicy;

    // Constructor to initialize a node with a given name
    public graphNode(String name) {
        this.name = name;
        this.curPolicy = name; // Default policy is the node's own name
    }

    // Add an edge to the node's list of edges
    public void addEdge(String edge) {
        edges.add(edge);
    }

    // Add a probability value to the node's list of probabilities
    public void addProb(double probValue) {
        probability.add(probValue);
    }

    // Determine the type of the node (chance, decision, terminal) based on its attributes
    public void nodeType() {
        if (edges.size() > 0 && edges.size() == probability.size()) {
            // Node is of type chance
            isChance = true;
        } else if (probability.size() == 1) {
            // Node is of type decision with a single probability
            isDecision = true;
        } else if (edges.size() > 0 && probability.size() == 0) {
            // Node is of type decision with default probability 1.0
            isDecision = true;
            addProb(1.0);
        } else if (edges.size() == 0) {
            // Node is of type terminal (no edges)
            isTerminal = true;
        }

        // Check if the sum of probabilities is 1 for Chance nodes
        double sumCheck = 0;
        if (isChance) {
            for (double curProb : probability) {
                sumCheck += curProb;
            }
            if (sumCheck != 1) {
                // Display an error message and terminate the program if the sum is not 1
                System.out.println("Sum of probabilities != 1 for chance node \"" + name + "\". Terminating program.");
                System.exit(0);
            }
        }
    }

    // Debug: Print the details of the node, including its attributes
    public void printNode() {
        System.out.println(name + " " + isChance + " " + isDecision + " " + isTerminal + " " + value + " " + edges + " " + probability + " " + curPolicy);
    }

    // Print the node's policy (used for decision nodes with more than one edge)
    public void printPolicy() {
        System.out.println(name + " -> " + curPolicy);
    }

}
