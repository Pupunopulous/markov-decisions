import java.text.*;
import java.util.*;

public class mdp {

    public static void main(String[] args) {
        // Default values for command line arguments
        double discountFactor = 1.0;
        boolean argMin = false;
        double tol = 0.01;
        int argIter = 100;
        String inputFileName = null;

        // Parse command line arguments
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-df" -> {
                        // Set discount factor if specified in command line arguments
                        if (i + 1 < args.length) {
                            discountFactor = Double.parseDouble(args[i + 1]);
                        }
                        i++;
                    }
                    // Set argMin to true if specified
                    case "-min" -> argMin = true;
                    case "-tol" -> {
                        // Set tolerance if specified in command line arguments
                        if (i + 1 < args.length) {
                            tol = Double.parseDouble(args[i + 1]);
                        }
                        i++;
                    }
                    case "-iter" -> {
                        // Set iteration count if specified in command line arguments
                        if (i + 1 < args.length) {
                            argIter = Integer.parseInt(args[i + 1]);
                        }
                        i++;
                    }
                    default -> {
                        if (args[i].contains(".txt")) {
                            inputFileName = args[i];
                        } else {
                            // Display an error message for incorrect input arguments
                            System.out.println("Wrong input arguments. Check README for more details.");
                            System.exit(0);
                        }
                    }
                }
            }
        } else {
            // Display an error message if no input arguments
            System.out.println("No arguments entered. Check README for more details.");
            System.exit(0);
        }

        // Validate the discount factor
        if (discountFactor > 1 || discountFactor < 0) {
            System.out.println("Discount factor not in [0, 1]. Terminating program.");
            System.exit(0);
        }

        // Read input data from file and create a map of nodes
        String input_data = readFile.fileReader(inputFileName);
        Map<String, graphNode> nodeList = readFile.setNodes(input_data, new TreeMap<>());
        // Process nodes to determine their types and relationships
        markov.processNodes(nodeList);

        // Solve the Markov decision process
        markov.markovSolver(nodeList, discountFactor, argMin, tol, argIter);

        // Print policies for decision nodes with more than one edge
        for (String key : nodeList.keySet()) {
            graphNode node = nodeList.get(key);
            if (node.isDecision && node.edges.size() > 1) {
                node.printPolicy();
            }
        }
        System.out.println();

        // Print the values of all nodes in the format "nodeName=value"
        DecimalFormat newFormat = new DecimalFormat("##0.000");
        for (String key : nodeList.keySet()) {
            graphNode node = nodeList.get(key);
            System.out.print(node.name + "=" + newFormat.format(node.value) + " ");
        }
        System.out.println();
    }

}
