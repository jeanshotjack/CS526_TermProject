package CS526_TermProject;

import java.io.*;
import java.util.*;

public class project {
    
    public static int numNodes;
    public static char[] nodeArray;
    public static HashMap<Character, Integer> nodeHash;
    public static int[][] adjMatrix;
    public static HashMap<Character, Integer> ddMap;
    public static ArrayList<Character>[] adjList;


    // finds index  of node and returns the index
    public static int indexofNode(char node) {
        int index = 0;
        for (int i = 0; i < nodeArray.length; i++) {
            if (nodeArray[i] == node) {
                index = i;
                break;
            }
        }
        return index;
    }

    // make an array of nodes and return the array
    public static char[] makeArray(String[] row) {
        int numNodes = row.length;
        char[] nodeArray = new char[numNodes];
        for (int j = 0; j < numNodes; j++) {
            nodeArray[j] = row[j].charAt(0);
        }
        return nodeArray;
    }

    // make a hashmap of nodes and return it
    public static HashMap<Character, Integer> makeHashmap(String[] row) {
        int numNodes = row.length;
        HashMap<Character, Integer> nodeHash = new HashMap<>();
        for (int j = 0; j < numNodes; j++) {
            nodeHash.put(row[j].charAt(0), j);
        }
        return nodeHash;
    }

    // make adjacency matrix of ints 
    // scanner reads adj matrix  and returns
    public static int[][] makeMatrix(Scanner file) {
        int[][] adjMatrix = new int[numNodes][numNodes];
        int i = 0;
        String[] row;
        while (file.hasNext()) {
            row = file.nextLine().split("\\s+");
            for (int j = 1;  j < row.length; j++) {
                int p = Integer.parseInt(row[j]);
                adjMatrix[i][j-1] = p;
            }
            i++;
        }
        return adjMatrix;
    }

    // make adjacency list with scanner, each array entry is a node
    public static ArrayList<Character>[] makeList() {
        ArrayList<Character>[] adjList = new ArrayList[numNodes];
        for (int i = 0; i < numNodes; i++) {
            ArrayList<Character> list = new ArrayList<>();
            for (int j = 0;  j < numNodes; j++) {
                if (adjMatrix[i][j] !=0) {
                    if (adjMatrix[i][j] != 0) {
                        list.add(nodeArray[j]);
                    }
                }
            }
            adjList[i] = list;
        }
        return adjList;
    }

    // make hashmap of node to direct distance values
    // scanner reads direct distance table 
    // returns mapped node names to direct distance from destination node
    public static HashMap<Character, Integer> makeDD(Scanner ddFile) {
        HashMap<Character, Integer> ddHM = new HashMap<>();
        for (int i = 0; i < numNodes; i++) {
            String[] row = ddFile.nextLine().trim().split("\\s+");
            ddHM.put(row[0].charAt(0), Integer.parseInt(row[1]));
        }
        return ddHM;
    }

    //print adj matrix
    public static void printAdjMatrix() {
        System.out.println("\nPrint adj matrix");
        for (int i = 0; i < numNodes; i++) {
            for (int j =  0; j < numNodes; j++) {
                System.out.printf("%3d", adjMatrix[i][j]);
            }
            System.out.println();
        }
    }

    // print adjacency list
    public static void printAdjList() {
        System.out.println("\nPrint adj list");
        for (int i = 0; i < numNodes; i++) {
            ArrayList<Character> list = adjList[i];
            System.out.print(nodeArray[i] + ": ");
            for (int j = 0; j < list.size(); j++) {
                System.out.print(list.get(j));
                if (j < list.size()-1) {
                    System.out.print(" --> ");
                }
            }
            System.out.println();
        }
    }

    //ask user to supply starting node 
    public static char getStart() {
        Scanner in = new Scanner(System.in);
        char start = 'Z';
        boolean progress = true;
        while (progress) {
            System.out.print("Please input a starting node: ");
            start = in.next().charAt(0);
            for (char vertex:nodeArray) {
                if (vertex == start) {
                    progress = false;
                }
            }
            if (progress == true) {
                System.out.println("\nCharacter isn't a valid node");
            }
        }
        in.close();
        return start;
    }

    // takes arraylist of choices and filters out the ones that are in the second one
    // and then returns a filtered arraylist
    public static ArrayList<Character> filterDeadEnds(ArrayList<Character>  initial, ArrayList<Character> deadends) {
        ArrayList<Character> filter = (ArrayList<Character>) initial.clone();
        for (int i = 0; i < filter.size(); i++) {
            if (deadends.contains(filter.get(i))) {
                filter.remove(filter.get(i));
            }
        }
        return filter;
    }

    // filter out initial choices that are also in array deque which is the history
    public static ArrayList<Character> filterHistoryNodes(ArrayList<Character> initial, ArrayDeque<Character> history) {
        ArrayList<Character> filter = (ArrayList<Character>) initial.clone();
        for (int i = 0; i < filter.size(); i++) {
            if (history.contains(filter.get(i))) {
                filter.remove(filter.get(i));
            }
        }
        return filter;
    }

    //  algorithm 1 to make node direction choice
    public static char choice1(ArrayList<Character> filtered) {
        char minChar = filtered.get(0);
        int minNum = ddMap.get(minChar);
        for (int i = 1; i < filtered.size(); i++) {
            if (ddMap.get(filtered.get(i)) < minNum) {
                minChar = filtered.get(i);
                minNum = ddMap.get(minChar);
            }
        }
        return minChar;
    }

    // algorithm 2 to make node direction choice
    public static char choice2(ArrayList<Character> filtered, char curr) {
        char minChar = filtered.get(0);
        int minNum = adjMatrix[indexofNode(curr)][indexofNode(minChar)] + ddMap.get(minChar);
        char testchar = minChar;
        int testnum = minNum;
        for (int i = 1; i < filtered.size(); i++) {
            testchar = filtered.get(i);
            testnum = adjMatrix[indexofNode(curr)][indexofNode(testchar)] + ddMap.get(testchar);
            if (testnum < minNum) {
                minChar = testchar;
                minNum = testnum;
            }
        }
        return minChar;
    }

    // loop to find destination node 
    // arraylist passed and updated with path search 
    // arraylist passed and updated, including backtracking 
    // returns min distance between start and destination

    public static int pathFind(ArrayDeque<Character> history, ArrayDeque<Character> master, char start, int how) {
        history.push(start);
        master.push(start);
        ArrayList<Character> deadends = new ArrayList<Character>();
        char curr = start;
        char choice = start;
        int distance = 0;
        ArrayList<Character> initial = new ArrayList<Character>();
        ArrayList<Character> filtered = new ArrayList<Character>();
        boolean reverse = false;
        while (curr != 'Z') {
            initial = adjList[indexofNode(curr)];
            filtered = filterDeadEnds(initial, deadends);

            // tests if its a dead end and ignores the case that this is starting move
            if (filtered.size() == 1 && distance != 0) {
                // back up nodes to deadends list
                choice = history.pop();
                deadends.add(curr);
                if (reverse == false) {
                    choice = history.pop();
                }
                reverse = true;
            }
            else {
                if (reverse == true) {
                    history.push(curr);
                }
                reverse = true;
                filtered = filterHistoryNodes(filtered, history);
                if (how == 1) {
                    choice = choice1(filtered);
                }
                else if (how == 2) {
                    choice = choice2(filtered, curr);
                }
                history.push(choice);
            }
            // update distance
            int i = indexofNode(curr), j = indexofNode(choice);
            if (reverse == true) {
                distance -= adjMatrix[i][j];
            }
            else {
                distance += adjMatrix[i][j];
            }
            master.push(choice);

            //update current node
            curr = choice;
        }
        return distance;
    }

    // print results of pathfind
    public static void printPathFind(ArrayDeque<Character> history, ArrayDeque<Character> master, int distance) {
        System.out.println();
        System.out.print("Sequence of all nodes: ");
        boolean first = true;
        while (!master.isEmpty()) {
            if (first) {
                System.out.print(" ");
                first = false;
            }
            else {
                System.out.print(" --> ");
            }
            System.out.print(master.pollLast());
        }
        System.out.println();
        System.out.print("Shortest: ");
        first = true;
        while (!history.isEmpty()) {
            if (first) {
                System.out.print(" ");
                first = false;
            }
            else {
                System.out.print(" --> ");
            }
            System.out.print(history.pollLast());
        }
        System.out.println();
        System.out.println("Shortest path length: " + distance);
    } 

    // main method
    public static void main(String[] args) throws IOException {
        Scanner file = new Scanner (new File("graph_input.txt"));
        String[] row = file.nextLine().trim().split("\\s+");

        // set up structures to define graph
        numNodes = row.length;
        nodeArray = makeArray(row);
        nodeHash = makeHashmap(row);
        adjMatrix = makeMatrix(file);
        file.close();
        adjList = makeList();

        // set up params of this search
        file = new Scanner (new File("direct_distance.txt"));
        ddMap = makeDD(file);
        file.close();
        char start = getStart();

        // define helper objects
        ArrayDeque<Character> history1 = new ArrayDeque<Character>();
        ArrayDeque<Character> master1 = new ArrayDeque<Character>();
        int distance1 = pathFind(history1, master1, start, 1);

        ArrayDeque<Character> history2 = new ArrayDeque<Character>();
        ArrayDeque<Character> master2 = new ArrayDeque<Character>();
        int distance2 = pathFind(history2, master2, start, 2);
        printPathFind(history1, master1, distance1);
        printPathFind(history2, master2, distance2);

        printAdjMatrix();
        printAdjList();
    }
}
