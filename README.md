# Minimax Algorithm Implementation

### Author: 
Rahi Krishna / rk4748

## Running the Program

The `.java` file can be executed on any operating system with JDK 8.0 or higher. Follow the steps below:

###Compile the Java file:

```shell
javac mdp.java
java mdp {arg1 arg2 arg3 ...}
```
#### The following arguments are accepted (if appended after "java Minimax"):
IMPORTANT: Order of these arguments does not matter

#### Required command line arguments:
1. input - A graph file must be passed as input to calculate the optimal policy
   
File must be of the format "$input$.txt"

#### Optional command line arguments:
1. `-df $val$` - Takes a custom discount factor for the Markov process as an argument
2. `-min` - Minimizes values as costs, defaults to false and maximizes values as rewards
3. `-tol $val$` - Input a custom float tolerance value for the process, defaults to `0.01`
4. `-iter $val$` - Takes a custom integer value that indicates a cutoff for value iteration, defaults to `100`

#### Example commands:
```
javac mdp.java
java mdp input.txt
java mdp -tol 0.001 -iter 100 input.txt
java mdp input.txt -min -df .9
java mdp -tol 0.003 -iter 75 input.txt -df .7
```

#### IMPORTANT:
Please make sure `mdp.java`, `markov.java`, `readFile.java` and `graphNode.java` are in the same folder
