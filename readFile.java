import java.io.*;
import java.util.*;

public class readFile {

    // Parse graph data and create a map of nodes
    public static Map<String, graphNode> setNodes(String graphData, Map<String, graphNode> nodeList) {
        // Split the graph data into lines
        String[] splitData = graphData.split("\n");

        // Process each line of the graph data
        for (String line : splitData) {
            // Clean up each line by adding spaces around symbols and trimming
            line = line.replace("=", " = ")
                    .replace(":", " : ")
                    .replace("%", " % ")
                    .replace("[", " [ ")
                    .replace("]", " ] ")
                    .replace(",", " ")
                    .trim();

            // Skip empty lines or lines starting with '#'
            if (line.isEmpty() || line.charAt(0) == '#') {
                continue;
            }

            // Split the cleaned line into an array of strings
            String[] lineArray = line.split("\\s+");

            // Convert numeric strings to doubles
            for (int i = 0; i < lineArray.length; i++) {
                if (lineArray[i].replace(".", "").matches("\\d+")) {
                    lineArray[i] = String.valueOf(Double.parseDouble(lineArray[i]));
                }
            }

            // Ensure the node exists in the nodeList and get its reference
            checkNewNode(lineArray[0], nodeList);
            graphNode newNode = nodeList.get(lineArray[0]);

            // Process the line based on the second element
            switch (lineArray[1]) {
                case "=" -> newNode.reward = Double.parseDouble(lineArray[2]);
                case "%" -> {
                    // Add probabilities to the node for chance nodes
                    for (int i = 2; i < lineArray.length; i++) {
                        newNode.addProb(Double.parseDouble(lineArray[i]));
                    }
                }
                case ":" -> {
                    // Add edges to the node for decision nodes
                    for (int i = 3; i < lineArray.length - 1; i++) {
                        checkNewNode(lineArray[i], nodeList);
                        newNode.addEdge(lineArray[i]);
                    }
                }
                default -> System.out.println(lineArray[1] + " is in a non [:, %, =] format");
            }
        }
        return nodeList;
    }

    // Ensure a node with the given name exists in the nodeList
    private static Map<String, graphNode> checkNewNode(String nodeName, Map<String, graphNode> nodeList) {
        nodeList.computeIfAbsent(nodeName, k -> new graphNode(nodeName));
        return nodeList;
    }

    // Read the contents of a file and return it as a string
    public static String fileReader(String inputFileName) {
        // String to store the contents of the file
        StringBuilder inputStringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            // Read each line and append it to the string
            while ((line = reader.readLine()) != null) {
                inputStringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            // Terminate the program if file is not found
            System.out.println("File " + inputFileName + " not found. Terminating program");
            System.exit(0);
        }
        return inputStringBuilder.toString();
    }

}
