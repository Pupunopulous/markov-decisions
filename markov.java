import java.util.*;

public class markov {

    // Process each node in the given nodeList
    public static void processNodes(Map<String, graphNode> nodeList) {
        for (Map.Entry<String, graphNode> entry : nodeList.entrySet()) {
            graphNode targetNode = entry.getValue();
            // Assign nodeType for each node
            targetNode.nodeType();
        }
    }

    // Perform Bellman equation calculation for the given targetNode
    public static double bellman(graphNode targetNode, Map<String, graphNode> nodeMap, double df) {
        // Calculate the Bellman equation value
        double score = calScore(targetNode, nodeMap);
        return targetNode.reward + df * score;
    }

    // Calculate the score for the given node based on its type (chance or decision)
    public static double calScore(graphNode targetNode, Map<String, graphNode> nodeMap) {
        if (targetNode.isChance) {
            // Calculate the score for a chance node
            double totalSum = 0;
            for (int count = 0; count < targetNode.edges.size(); count++) {
                totalSum += targetNode.probability.get(count) * nodeMap.get(targetNode.edges.get(count)).value;
            }
            return totalSum;
        }
        if (targetNode.isDecision) {
            // Calculate the score for a decision node
            double totalSum = 0;
            double mainProb = targetNode.probability.get(0);
            double remainingProb = 0;
            if (targetNode.edges.size() != 1) {
                remainingProb = (1 - mainProb) / (targetNode.edges.size() - 1);
            }
            String policy = targetNode.curPolicy;
            for (int i = 0; i < targetNode.edges.size(); i++) {
                String edge = targetNode.edges.get(i);
                if (edge.equals(policy)) {
                    totalSum += mainProb * nodeMap.get(edge).value;
                } else {
                    totalSum += remainingProb * nodeMap.get(edge).value;
                }
            }
            return totalSum;
        }
        // Default
        return 0;
    }

    // Calculate the policy for the given node based on specified criteria
    public static String calculatePolicy(graphNode targetNode, Map<String, graphNode> nl, boolean argMin) {
        double mainProb = targetNode.probability.get(0);
        double remainingProb = 0;
        if (targetNode.edges.size() != 1) {
            remainingProb = (1 - mainProb) / (targetNode.edges.size() - 1);
        }
        double curValue = targetNode.value;
        for (int i = 0; i < targetNode.edges.size(); i++) {
            String mainEdge = targetNode.edges.get(i);
            double totalSum = 0;
            for (String sideEdge : targetNode.edges) {
                if (mainEdge.equals(sideEdge)) {
                    totalSum += mainProb * nl.get(sideEdge).value;
                } else {
                    totalSum += remainingProb * nl.get(sideEdge).value;
                }
            }
            if (argMin) {
                if (totalSum < curValue) {
                    targetNode.curPolicy = mainEdge;
                    curValue = totalSum;
                }
            } else if (totalSum > curValue) {
                targetNode.curPolicy = mainEdge;
                curValue = totalSum;
            }
        }
        // Return the calculated policy
        return targetNode.curPolicy;
    }

    // Solve the Markov decision process using the given parameters
    public static void markovSolver(Map<String, graphNode> nodeMap, double df, boolean argMin, double tol, int argIter) {
        // Initialize the initial policy for non-terminal nodes
        for (Map.Entry<String, graphNode> entry : nodeMap.entrySet()) {
            graphNode node = entry.getValue();
            if (!node.isTerminal) {
                node.curPolicy = node.edges.get(0);
            }
        }

        // Iterate until convergence
        while (true) {
            // Perform value iteration
            for (int i = 0; i < argIter; i++) {
                boolean valueFlag = valueIteration(nodeMap, tol, df);
                if (!valueFlag) {
                    break;
                }
            }
            // Perform greedy policy iteration
            boolean policyFlag = greedyPolicyComputation(nodeMap, argMin);
            if (!policyFlag) {
                break;
            }
        }
    }

    // Perform value iteration for the given nodeMap with specified tolerance and discount factor
    public static boolean valueIteration(Map<String, graphNode> nodeMap, double tol, double df) {
        boolean valueFlag = false;
        // Iterate over each node in the map
        for (Map.Entry<String, graphNode> entry : nodeMap.entrySet()) {
            graphNode targetNode = entry.getValue();
            // Calculate the new value using the Bellman equation
            double newValue = bellman(targetNode, nodeMap, df);
            // Check for convergence based on tolerance
            if (Math.abs(newValue - targetNode.value) > tol) {
                valueFlag = true;
            }
            // Update the node's value with the new calculated value
            targetNode.value = newValue;
        }
        // Return whether any node's value has changed significantly
        return valueFlag;
    }

    // Perform greedy policy computation for the given nodeMap and specified optimization criteria
    public static boolean greedyPolicyComputation(Map<String, graphNode> nodeMap, boolean argMin) {
        boolean policyFlag = false;
        // Iterate over each node in the map
        for (Map.Entry<String, graphNode> entry : nodeMap.entrySet()) {
            graphNode targetNode = entry.getValue();
            // Check if the node is a Decision node
            if (targetNode.isDecision) {
                // Store the current policy, calculate a new policy, and check for a change
                String oldPolicy = targetNode.curPolicy;
                String newPolicy = calculatePolicy(targetNode, nodeMap, argMin);
                if (!oldPolicy.equals(newPolicy)) {
                    policyFlag = true;
                }
                // Update the node's current policy with the new calculated policy
                targetNode.curPolicy = newPolicy;
            }
        }
        // Return whether any decision node's policy has changed
        return policyFlag;
    }

}
