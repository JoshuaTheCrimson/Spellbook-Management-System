
package student;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class BuildSpellbook {

   public final Integer                  MAXCOMS = 1000;
   private ArrayList<LinkedList<String>> prerequisiteSpells;
   private Map<String, Boolean>          spellsLearned;
   private LinkedList<String>            longestCycle;
   private LinkedList<String>            shortestCycle;

   public BuildSpellbook() {
      prerequisiteSpells = new ArrayList<LinkedList<String>>();
      spellsLearned = new LinkedHashMap<>();
      longestCycle = new LinkedList<String>();
      shortestCycle = new LinkedList<String>();

   }

   public Vector<String> execNSpecs(Vector<String> specs, Integer N) {
	    // Initialize the output vector to store results and a counter for processed commands
	    Vector<String> output = new Vector<String>();
	    int processedCount = 0;

	    // Iterate through each specification provided in the specs vector
	    for (String spec : specs) {
	        // Check if the number of processed specifications has reached the limit N
	        if (processedCount >= N) {
	            // If the limit is reached, return the current output vector
	            return output;
	        }

	        // Add the current specification to the output vector
	        output.add(spec);
	        // Increment the count of processed specifications
	        processedCount++;

	        // Split the current specification string into arguments using space as the delimiter
	        String[] arguments = spec.split(" ");
	        // The first argument is the command (e.g., PREREQ, LEARN, FORGET, ENUM)
	        String cmd = arguments[0];

	        // Process the command using a switch statement
	        switch (cmd) {
	            case "PREREQ":
	                // If the command is PREREQ, call the preReqadd method to add the prerequisite spells
	                preReqadd(arguments);
	                break;
	            case "LEARN":
	                // If the command is LEARN, call the learnExp method to learn the specified spell
	                learnExp(arguments[1], output);
	                break;
	            case "FORGET":
	                // If the command is FORGET, call the forget method to forget the specified spell
	                forget(arguments[1], output);
	                break;
	            case "ENUM":
	                // If the command is ENUM, enumerate all learned spells
	                // Iterate through the spellsLearned map and add each spell to the output vector
	                for (Entry<String, Boolean> entry : spellsLearned.entrySet()) {
	                    output.add("   " + entry.getKey());
	                }
	                break;
	            default:
	                // If the command is unrecognized, return the current output vector
	                return output;
	        }
	    }

	    // Return the output vector after processing all specifications or reaching the limit N
	    return output;
	}



   public void preReqadd(String[] args) {
	    // The first argument after the command is the main spell which requires prerequisites
	    String mainSpell = args[1];

	    // Initialize a new linked list to store the main spell and its prerequisites
	    LinkedList<String> prerequisites = new LinkedList<>();
	    
	    // Add the main spell to the prerequisites list
	    prerequisites.add(mainSpell);
	    
	    // Iterate through the remaining arguments, starting from the second index (index 2)
	    // These arguments represent the prerequisite spells for the main spell
	    for (int index = 2; index < args.length; index++) {
	        // Add each prerequisite spell to the prerequisites list
	        prerequisites.add(args[index]);
	    }
	    
	    // Add the complete prerequisites list to the prerequisiteSpells list
	    prerequisiteSpells.add(prerequisites);
	}

   
   

   public void learnExp(String spell, Vector<String> output) {
	    // Check if the spell is already learned by looking it up in the spellsLearned map
	    if (spellsLearned.containsKey(spell)) {
	        // If the spell is already learned, add a message to the output and return
	        output.add("   " + spell + " is already learned");
	        return;
	    }
	    
	    // Check if the spell has any prerequisites by calling the hasPrereq method
	    int prereqIndex = hasPrereq(spell);
	    if (prereqIndex != -1) {
	        // If the spell has prerequisites, learn those prerequisites first
	        learnImp(prereqIndex, output);
	        // Get the main spell from the prerequisites list
	        String prereqSpell = prerequisiteSpells.get(prereqIndex).get(0);
	        // Mark the main spell as learned but not explicitly by adding it to spellsLearned with a value of false
	        spellsLearned.put(prereqSpell, false);
	        // Add a message to the output indicating that the main spell is being learned
	        output.add("   Learning " + prereqSpell);
	        return;
	    }
	    
	    // If the spell has no prerequisites, mark it as learned explicitly by adding it to spellsLearned with a value of false
	    spellsLearned.put(spell, false);
	    // Add a message to the output indicating that the spell is being learned
	    output.add("   Learning " + spell);
	}



   public int hasPrereq(String spell) {
	    // Iterate through the prerequisiteSpells list
	    for (int index = 0; index < prerequisiteSpells.size(); index++) {
	        // Check if the main spell in the current prerequisites list matches the provided spell
	        if (prerequisiteSpells.get(index).get(0).contains(spell)) {
	            // If a match is found, return the index of the prerequisites list
	            return index;
	        }
	    }
	    // If no match is found after checking all prerequisites lists, return -1
	    return -1;
	}



   public void learnImp(int position, Vector<String> output) {
	    // Retrieve the list of prerequisites for the given position in prerequisiteSpells
	    LinkedList<String> prerequisites = prerequisiteSpells.get(position);

	    // Iterate through the prerequisites list, starting from index 1 (skipping the main spell at index 0)
	    for (int index = 1; index < prerequisites.size(); index++) {
	        // Get the current spell from the prerequisites list
	        String currentSpell = prerequisites.get(index);
	        
	        // Check if the current spell has its own prerequisites by calling hasPrereq
	        if (hasPrereq(currentSpell) != -1) {
	            // Recursively learn the prerequisites of the current spell
	            learnImp(hasPrereq(currentSpell), output);
	        }
	        
	        // Check if the current spell is not already learned
	        if (!spellsLearned.containsKey(currentSpell)) {
	            // Mark the current spell as learned and explicitly by adding it to spellsLearned with a value of true
	            spellsLearned.put(currentSpell, true);
	            // Add a message to the output indicating that the current spell is being learned
	            output.add("   Learning " + currentSpell);
	        }
	    }
	}


    	 
   		//CHECK THIS ONE !! !!!  ! ! ! ! ! ! ! ! ! ! ! ! ! !  ! ! ! ! ! !  ! ! !  ! ! ! !  ! ! ! ! ! 1 ! 1 ! 1 
   public boolean learnedSpell(String spell) {
	    return spellsLearned.containsKey(spell);
	}
   

   public boolean isPre(String targetSpell) {
	    // Iterate through each list of prerequisite spells in prerequisiteSpells
	    for (int i = 0; i < prerequisiteSpells.size(); i++) {
	        // Retrieve the current list of spells, where the first element is the main spell and the rest are its prerequisites
	        LinkedList<String> spellList = prerequisiteSpells.get(i);
	        // The main spell is the first element in the list
	        String mainSpell = spellList.get(0);
	        
	        // Iterate through the prerequisite spells, starting from index 1 (skipping the main spell)
	        for (int j = 1; j < spellList.size(); j++) {
	            // Get the current prerequisite spell
	            String currentPrereq = spellList.get(j);
	            
	            // Check if the current prerequisite spell contains the target spell
	            // and if the main spell has already been learned
	            if (currentPrereq.contains(targetSpell) && learnedSpell(mainSpell)) {
	                // If both conditions are true, return true indicating that the target spell is a prerequisite
	                return true;
	            }
	        }
	    }
	    // If no matching prerequisite is found, return false
	    return false;
	}


   public void forget(String spell, Vector<String> output) {
	    // Check if the spell has been learned
	    if (learnedSpell(spell)) {
	        // If the spell is still needed as a prerequisite for other spells, add a message and return
	        if (isPre(spell)) {
	            output.add("   " + spell + " is still needed");
	            return;
	        }

	        // Check if the spell itself has prerequisites
	        int prereqPosition = hasPrereq(spell);
	        if (prereqPosition != -1) {
	            // If the spell has prerequisites, retrieve the main spell from the prerequisites list
	            String mainSpell = prerequisiteSpells.get(prereqPosition).get(0);
	            // Add a message indicating that the main spell is being forgotten
	            output.add("   Forgetting " + mainSpell);
	            // Remove the main spell from the spellsLearned map
	            spellsLearned.remove(mainSpell);
	            // Call DropHelp to recursively forget all prerequisite spells
	            DropHelp(prereqPosition, output);
	        } else {
	            // If the spell has no prerequisites, add a message indicating that the spell is being forgotten
	            output.add("   Forgetting " + spell);
	            // Remove the spell from the spellsLearned map
	            spellsLearned.remove(spell);
	        }
	    } else {
	        // If the spell has not been learned, add a message indicating that the spell is not learned
	        output.add("   " + spell + " is not learned");
	    }
	}



   public void DropHelp(int position, Vector<String> output) {
	    // Retrieve the list of spells for the given position in prerequisiteSpells
	    LinkedList<String> spells = prerequisiteSpells.get(position);

	    // Iterate through the spells list in reverse order, starting from the last spell
	    for (int index = spells.size() - 1; index > 0; index--) {
	        // Get the current spell from the spells list
	        String currentSpell = spells.get(index);

	        // Check if the current spell is not a prerequisite for other learned spells
	        // and if it is currently marked as learned in spellsLearned
	        if (!isPre(currentSpell) && spellsLearned.getOrDefault(currentSpell, false)) {
	            // Add a message to the output indicating that the current spell is being forgotten
	            output.add("   Forgetting " + currentSpell);
	            // Remove the current spell from the spellsLearned map
	            spellsLearned.remove(currentSpell);
	        }

	        // Check if the current spell itself has prerequisites
	        int prereqIndex = hasPrereq(currentSpell);
	        if (prereqIndex != -1) {
	            // If the current spell has prerequisites, recursively call DropHelp to forget them
	            DropHelp(prereqIndex, output);
	        }
	    }
	}



   public Vector<String> execNSpecswCheck(Vector<String> specs, Integer N) {
	    // Initialize the output vector to store results and a counter for processed commands
	    Vector<String> output = new Vector<>();
	    int processedCount = 0;
	    boolean cycleDetected = false;

	    // Iterate through each specification provided in the specs vector
	    for (String spec : specs) {
	        // Check if the number of processed specifications has reached the limit N
	        if (processedCount >= N) {
	            // If the limit is reached, return the current output vector
	            return output;
	        }

	        // Add the current specification to the output vector
	        output.add(spec);
	        // Increment the count of processed specifications
	        processedCount++;

	        // Split the current specification string into arguments using space as the delimiter
	        String[] arguments = spec.split(" ");
	        // The first argument is the command (e.g., PREREQ, LEARN, FORGET, ENUM)
	        String command = arguments[0];

	        // Process the command using a switch statement
	        switch (command) {
	            case "PREREQ":
	                // If the command is PREREQ, call the preReqadd method to add the prerequisite spells
	                preReqadd(arguments);
	                // Check for cycles if there are at least two prerequisite lists
	                if (prerequisiteSpells.size() >= 2 && checkForCycle()) {
	                    // If a cycle is detected, add a message to the output and set cycleDetected to true
	                    output.add("   Found cycle in prereqs");
	                    cycleDetected = true;
	                }
	                break;

	            case "LEARN":
	                // If no cycle is detected, call the learnExp method to learn the specified spell
	                if (!cycleDetected) {
	                    learnExp(arguments[1], output);
	                }
	                break;

	            case "FORGET":
	                // If no cycle is detected, call the forget method to forget the specified spell
	                if (!cycleDetected) {
	                    forget(arguments[1], output);
	                }
	                break;

	            case "ENUM":
	                // If no cycle is detected, enumerate all learned spells
	                if (!cycleDetected) {
	                    for (Entry<String, Boolean> entry : spellsLearned.entrySet()) {
	                        output.add("   " + entry.getKey());
	                    }
	                }
	                break;

	            default:
	                // Handle unexpected commands or end of specifications
	                break;
	        }
	    }

	    // Return the output vector after processing all specifications or reaching the limit N
	    return output;
	}



   public boolean checkForCycle() {
	    // Get the number of spells in the prerequisiteSpells list
	    int numSpells = prerequisiteSpells.size();
	    // Initialize visited and recStack arrays to keep track of visited nodes and the recursion stack
	    boolean[] visited = new boolean[numSpells];
	    boolean[] recStack = new boolean[numSpells];

	    // Iterate through each spell in reverse order to check for cycles
	    for (int index = numSpells - 1; index > 0; index--) {
	        // If a cycle is detected during the check, return true
	        if (UtilCycleChecker(index, visited, recStack)) {
	            return true;
	        }
	    }
	    // If no cycle is detected, return false
	    return false;
	}

	private boolean UtilCycleChecker(int spellIndex, boolean[] visited, boolean[] recStack) {
	    // Mark the current node as visited and add it to the recursion stack
	    visited[spellIndex] = true;
	    recStack[spellIndex] = true;

	    // Retrieve the list of prerequisites for the current spell
	    LinkedList<String> prerequisites = prerequisiteSpells.get(spellIndex);
	    // Iterate through the prerequisites in reverse order
	    for (int i = prerequisites.size() - 1; i > 0; i--) {
	        // Get the current prerequisite spell and its index in the prerequisiteSpells list
	        String prereqSpell = prerequisites.get(i);
	        int prereqIndex = hasPrereq(prereqSpell);

	        // If the prerequisite spell is not visited and a cycle is detected, return true
	        if (prereqIndex != -1 && !visited[prereqIndex] && UtilCycleChecker(prereqIndex, visited, recStack)) {
	            return true;
	        // If the prerequisite spell is already in the recursion stack, return true indicating a cycle
	        } else if (prereqIndex != -1 && recStack[prereqIndex]) {
	            return true;
	        }
	    }

	    // Remove the current node from the recursion stack
	    recStack[spellIndex] = false;
	    // Return false if no cycle is detected
	    return false;
	}

   

	public Vector<String> execNSpecswCheckRecLarge(Vector<String> specs, Integer N) {
	    // Initialize the output vector to store results and a counter for processed commands
	    Vector<String> output = new Vector<>();
	    int processedCount = 0;
	    boolean cycleDetected = false;

	    // Iterate through each specification provided in the specs vector
	    for (String spec : specs) {
	        // Check if the number of processed specifications has reached the limit N
	        if (processedCount >= N) {
	            // If the limit is reached, return the current output vector
	            return output;
	        }

	        // Add the current specification to the output vector
	        output.add(spec);
	        // Increment the count of processed specifications
	        processedCount++;

	        // Split the current specification string into arguments using space as the delimiter
	        String[] arguments = spec.split(" ");
	        // The first argument is the command (e.g., PREREQ, LEARN, FORGET, ENUM)
	        String command = arguments[0];

	        // Process the command using a switch statement
	        switch (command) {
	            case "PREREQ":
	                // If the command is PREREQ, call the preReqadd method to add the prerequisite spells
	                preReqadd(arguments);
	                // Check for cycles if there are at least two prerequisite lists
	                if (prerequisiteSpells.size() >= 2) {
	                    // Find the longest cycle in the prerequisite spells
	                    longestCycle = LongestCSearcher();
	                    if (longestCycle != null) {
	                        // If a cycle is detected, identify the long cycle and suggest forgetting it
	                        String preReqWithShortCycle = identifyLong();
	                        output.add("   Found cycle in prereqs");
	                        if (!preReqWithShortCycle.isEmpty()) {
	                            output.add("   Suggest forgetting PREREQ" + preReqWithShortCycle);
	                        }
	                        // Set cycleDetected to true to stop further processing
	                        cycleDetected = true;
	                    }
	                }
	                break;

	            case "LEARN":
	                // If no cycle is detected, call the learnExp method to learn the specified spell
	                if (!cycleDetected) {
	                    learnExp(arguments[1], output);
	                }
	                break;

	            case "FORGET":
	                // If no cycle is detected, call the forget method to forget the specified spell
	                if (!cycleDetected) {
	                    forget(arguments[1], output);
	                }
	                break;

	            case "ENUM":
	                // If no cycle is detected, enumerate all learned spells
	                if (!cycleDetected) {
	                    for (Entry<String, Boolean> entry : spellsLearned.entrySet()) {
	                        output.add("   " + entry.getKey());
	                    }
	                }
	                break;

	            default:
	                // Handle unexpected command or end of specifications
	                break;
	        }
	    }

	    // Return the output vector after processing all specifications or reaching the limit N
	    return output;
	}



public String identifyLong() {
    StringBuilder result = new StringBuilder();

    if (longestCycle != null) {
        for (LinkedList<String> prereqList : prerequisiteSpells) {
            if (prereqList.get(0).equals(longestCycle.get(0))) {
                for (String spell : prereqList) {
                    result.append(" ").append(spell);
                }
            }
        }
    }

    return result.toString();
}


public LinkedList<String> LongestCSearcher() {
    // Initialize the longest cycle as null
    longestCycle = null;

    // Array to keep track of visited nodes
    boolean[] visitedNodes = new boolean[prerequisiteSpells.size()];
    // List to keep track of the current path during traversal
    LinkedList<String> currentTraversalPath = new LinkedList<>();
    // Variable to store the longest cycle found
    LinkedList<String> detectedLongestCycle = null;

    // Iterate over each spell in reverse order
    for (int spellIndex = prerequisiteSpells.size() - 1; spellIndex > 0; spellIndex--) {
        // If the current spell has not been visited
        if (!visitedNodes[spellIndex]) {
            // Call the helper function to find the longest cycle starting from this spell
            LongestCycle(spellIndex, visitedNodes, currentTraversalPath);
            // Update the detectedLongestCycle if a longer cycle is found
            if (detectedLongestCycle == null || (longestCycle != null && longestCycle.size() > detectedLongestCycle.size())) {
                detectedLongestCycle = longestCycle;
            }
        }
    }

    // Return the longest cycle found
    return detectedLongestCycle;
}


private void LongestCycle(int v, boolean[] visited, LinkedList<String> currentPath) {
    // Mark the current node as visited and add it to the current path
    visited[v] = true;
    currentPath.addLast(prerequisiteSpells.get(v).get(0));
    
    // Iterate through the prerequisites of the current spell in reverse order
    for (int i = prerequisiteSpells.get(v).size() - 1; i > 0; i--) {
        int index = hasPrereq(prerequisiteSpells.get(v).get(i));
        
        // If the prerequisite has not been visited, recursively call LongestCycle
        if (index != -1 && !visited[index]) {
            LongestCycle(index, visited, currentPath);
        }
        // Check if the current path forms a cycle
        else if (currentPath.contains(prerequisiteSpells.get(v).get(i)) 
                 && prerequisiteSpells.get(v).get(i).equals(currentPath.getFirst())) {
            int cycleStartIndex = currentPath.indexOf(prerequisiteSpells.get(v).get(i));
            LinkedList<String> cycle = new LinkedList<>(currentPath.subList(cycleStartIndex, currentPath.size()));
            
            // Update the longest cycle if the found cycle is longer
            if (longestCycle == null || (cycle.size() > longestCycle.size() && cycle.size() > 2)) {
                longestCycle = cycle;
            }
        }
    }
    
    // Backtrack: remove the last spell from the current path and mark the node as unvisited
    currentPath.removeLast();
    visited[v] = false;
}

   
   
public String TheShort() {
    // Initialize a StringBuilder to build the result string
    StringBuilder result = new StringBuilder();

    // Check if the shortestCycle is not null
    if (shortestCycle != null) {
        // Iterate through each list of prerequisite spells in prerequisiteSpells
        for (int spellIndex = 0; spellIndex < prerequisiteSpells.size(); spellIndex++) {
            // Check if the first spell in the current prereqList matches the first spell in the shortestCycle
            if (prerequisiteSpells.get(spellIndex).get(0).equals(shortestCycle.get(0))) {
                // If a match is found, iterate through all spells in the prereqList
                for (String spell : prerequisiteSpells.get(spellIndex)) {
                    // Append each spell to the result string, prefixed with a space
                    result.append(" ").append(spell);
                }
            }
        }
    }

    // Convert the StringBuilder to a string and return it
    return result.toString();
}


public Vector<String> execNSpecswCheckRecSmall(Vector<String> specs, Integer N) {
    // Initialize the output vector to store results and a counter for processed commands
    Vector<String> output = new Vector<>();
    int size = 0;
    boolean cycleDetected = false;

    // Iterate through each specification provided in the specs vector
    for (String line : specs) {
        // Check if the number of processed specifications has reached the limit N
        if (size >= N) {
            break;
        }

        // Add the current specification to the output vector
        output.add(line);
        // Increment the count of processed specifications
        size++;

        // Split the current specification string into arguments using space as the delimiter
        String[] args = line.split(" ");
        // The first argument is the command (e.g., PREREQ, LEARN, FORGET, ENUM)
        String command = args[0];

        // Process the command using a switch statement
        if (command.equals("PREREQ")) {
            // If the command is PREREQ, call the preReqadd method to add the prerequisite spells
            preReqadd(args);
            // Check for cycles if there are at least two prerequisite lists
            if (prerequisiteSpells.size() >= 2) {
                // Find the shortest cycle in the prerequisite spells
                shortestCycle = ShortesCycleSearcher();
                if (shortestCycle != null) {
                    // If a cycle is detected, identify the short cycle and suggest forgetting it
                    String preReqWithSmallCycle = TheShort();
                    output.add("   Found cycle in prereqs");
                    if (!preReqWithSmallCycle.isEmpty()) {
                        output.add("   Suggest forgetting PREREQ" + preReqWithSmallCycle);
                    }
                    // Set cycleDetected to true to stop further processing
                    cycleDetected = true;
                }
            }
        } else if (!cycleDetected) {
            switch (command) {
                case "LEARN":
                    // If no cycle is detected, call the learnExp method to learn the specified spell
                    learnExp(args[1], output);
                    break;
                case "FORGET":
                    // If no cycle is detected, call the forget method to forget the specified spell
                    forget(args[1], output);
                    break;
                case "ENUM":
                    // If no cycle is detected, enumerate all learned spells
                    for (Entry<String, Boolean> entry : spellsLearned.entrySet()) {
                        output.add("   " + entry.getKey());
                    }
                    break;
                default:
                    // Handle unexpected command or end of specifications
                    break;
            }
        }
    }

    // Return the output vector after processing all specifications or reaching the limit N
    return output;
}




public LinkedList<String> ShortesCycleSearcher() {
    // Initialize the variable to store the shortest cycle found
    LinkedList<String> shortest = null;
    // Initialize the shortestCycle variable to null
    shortestCycle = null;
    // Array to keep track of visited nodes
    boolean[] visitedNodes = new boolean[prerequisiteSpells.size()];
    // List to keep track of the current path during traversal
    LinkedList<String> currentPath = new LinkedList<>();

    // Iterate over each spell in reverse order
    for (int spellIndex = prerequisiteSpells.size() - 1; spellIndex > 0; spellIndex--) {
        // If the current spell has not been visited
        if (!visitedNodes[spellIndex]) {
            // Call the helper function to find the shortest cycle starting from this spell
            _ShortestCycle_Utility(spellIndex, visitedNodes, currentPath);
            // Update the shortest cycle if a shorter cycle is found
            switch (spellIndex) {
                default:
                    if (shortest == null || (shortestCycle != null && shortestCycle.size() < shortest.size())) {
                        shortest = shortestCycle;
                    }
                    break;
            }
        }
    }

    // Return the shortest cycle found
    return shortest;
}



private void _ShortestCycle_Utility(int vertex, boolean[] visitedNodes, LinkedList<String> path) {
    // Mark the current vertex as visited
    visitedNodes[vertex] = true;
    // Add the main spell at the current vertex to the current path
    path.addLast(prerequisiteSpells.get(vertex).get(0));

    // Iterate through the prerequisites of the current spell in reverse order
    for (int i = prerequisiteSpells.get(vertex).size() - 1; i > 0; i--) {
        // Get the index of the prerequisite spell
        int prereqIndex = hasPrereq(prerequisiteSpells.get(vertex).get(i));

        // If the prerequisite spell is valid and not yet visited, recurse
        if (prereqIndex != -1 && !visitedNodes[prereqIndex]) {
            _ShortestCycle_Utility(prereqIndex, visitedNodes, path);
        } 
        // Check if the current path forms a cycle with the starting spell
        else if (path.contains(prerequisiteSpells.get(vertex).get(i)) && 
                 prerequisiteSpells.get(vertex).get(i).equals(path.getFirst())) {
            // Identify the starting index of the cycle in the current path
            int cycleStart = path.indexOf(prerequisiteSpells.get(vertex).get(i));
            // Extract the sublist that forms the current cycle
            LinkedList<String> currentCycle = new LinkedList<>(path.subList(cycleStart, path.size()));

            // Update the shortest cycle if the current cycle is shorter
            if (shortestCycle == null || currentCycle.size() < shortestCycle.size()) {
                shortestCycle = currentCycle;
            }
        }
    }

    // Backtrack: remove the last spell from the current path and mark the vertex as unvisited
    path.removeLast();
    visitedNodes[vertex] = false;
}


   

   public Vector<String> readSpecsFromFile(String filePath) throws IOException {
	    Vector<String> lines = new Vector<>();
	    
	    try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
	        String lineContent;
	        while ((lineContent = fileReader.readLine()) != null) {
	            lines.add(lineContent);
	        }
	    }
	    
	    return lines;
	}


   public Vector<String> readSolnFromFile(String fileName, Integer limit) throws IOException {
	    // Create a BufferedReader to read from the specified file
	    BufferedReader reader = new BufferedReader(new FileReader(fileName));
	    String line;
	    // Initialize a Vector to store the lines read from the file
	    Vector<String> result = new Vector<>();
	    int counter = 0;

	    // Read lines from the file until the end of the file or the limit is reached
	    while (((line = reader.readLine()) != null) && (counter <= limit)) {
	        // Add the line to the result if it is within the limit or it starts with spaces
	        if ((counter != limit) || line.startsWith("   ")) {
	            result.add(line);
	        }
	        // Increment the counter if the line does not start with spaces
	        if (!line.startsWith("   ")) {
	            counter++;
	        }
	    }
	    // Close the BufferedReader to release resources
	    reader.close();
	    // Return the Vector containing the lines read from the file
	    return result;
	}



   public Boolean compareExecWSoln(Vector<String> executionList, Vector<String> solutionList) {
	    // Get the sizes of the execution and solution lists
	    int execSize = executionList.size();
	    int solnSize = solutionList.size();

	    // If the sizes do not match, return FALSE
	    if (execSize != solnSize) {
	        return Boolean.FALSE;
	    }

	    // Compare each item in the execution list with the corresponding item in the solution list
	    for (int position = 0; position < execSize; position++) {
	        String execItem = executionList.get(position);
	        String solnItem = solutionList.get(position);

	        // If any item does not match, return FALSE
	        if (!execItem.equals(solnItem)) {
	            return Boolean.FALSE;
	        }
	    }

	    // If all items match, return TRUE
	    return Boolean.TRUE;
	}

}
