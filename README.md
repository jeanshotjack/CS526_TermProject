# CS526_TermProject
Term project for the course CS526 Data Structures &amp; Algorithms for my CS Masters


## Pseudocode
First declared are class static variables:
numNodes - number of Nodes
nodeArray-Array of the nodes
nodeHash - hashmap, the way that you can look up the int of the node adjMatrix - each col and row represents a node and each intersection of
metric represents weight of edge
ddMap direct distance map - imported file - character and int map look up what
distance node a is to node z
adjList adjacency list - look up what other nodes are attached to the same node
you looked up

Algorithm 1 - choice1
For loop
Iterates through filtered choices (the ones that have no dead ends)
finds the minimum direct distance by checking the filtered choices in the direct distance hash map against minNum
returns the char associated with the minimum direct distance

Algorithm 2 - choice2
Iterates through filtered choices (the ones that have no dead ends)
finds the choice that minimizes the direct distance as well as the edge weight returns char

main function
Scanner to read the graph
Read a single row from top of input file and from that row, I get length and split it up into separate pieces. Each is a character along words.
Set up data structure to define the graph:
Get the length and that corresponds number of nodes and that corresponds to
an array
I take the first row of the graph input file and splitting that row into an array of
strings and trimming white space.
Then I assign the length of array to be the number of nodes.
Then, I use that array to create an array of nodes (same array of characters to
define the node array), use the same row to make a node hash and then use the rest of the file input scanner to create adjacency matrix.
Set up params of this specific search:
Reassign file input scanner and tell scanner to look in the file direct distance Direct distance is specific for this search
Then I make a hashmap of direct distance data and use a helper function that
reads it and builds the hashmap. The helper function collects input from user for which starting node to start from.
 Defining helper objects for the search:
I create 2 objects both of them are array deques - history1 and master1 -
later we have the same thing but with ending 2
Then I have a function which passes an empty history and empty master
and also the starting node which is just a character and also an int that will represent which algorithm method (choice1 or choice2) it chooses for its pathfinder. history and master are objects, so are passed as reference, so any changes to them are persistent
The pathfinding object returns distance, and distance variable is the distance that that pathfinding method took to return
The same again is repeated for a second method and also passes in a second version of the history and master
Finally, print the results

indexofNode takes the node array and searches the static node array, and stops when it finds the node and returns the index of that node.
makeArray sets numNodes length to length of the row.I pass a node array - define the one we will return. We then will go through the array of strings which comes from the file read and we pick the first char in every single index of the array of strings called row and add that first char to the node array. After the for loop, return node array.

** not sure if this is necessary
makeHashmap might be useful for doing it faster. It creates a hash map, then for every string within array of strings it takes the first char and puts it into the map and maps with its index. Then returns entire hash.

makeMatrix creates adjacency matrix. It creates matrix sized by number of nodes. In a while loop, check the file that we're reading that it has something next and then splits the file by spaces. In a for loop, for every single item in the row it adds integer at each position into the matrix. It clips the first column for design purposes. Returns matrix.

makeList defines a new adjacency list equally sized to be number of nodes. Then it loops for number of nodes and creates array list that we will add to the array later. In a for loop - every index in the array list corresponds to the node and iterates across every column in the adjacency matrix. If a column doesn't contain 0, then we add the node to the array list and then, after collecting all of the adjacent nodes we assign the list into the array of lists.

makeDD makes a map of direct distance values. It loops through every line in scanner and put every line into two strings based upon white space then it puts a new key value pair into a hash map. The key is the first character in the first part of the row and the value is the int that is made from the second part of the row.

printAdjMatrix is a printing function to print adjacency matrix. It iterates through a 2D matrix and prints out contents of each position.

printAdjList iterates through each node and concat/prints series of chars that are in the list of adjacent nodes.

getStart creates new Scanner objects using System.in, then defines start node. A boolean determines whether we should prompt for user input (error handling). In a while loop, while user input is empty or invalid, user input is prompted to input a starting node. Close scanner and return the starting node character.

filterDeadEnds filters out dead nodes. It takes two array lists of dead nodes: first array list is initial nodes, second is dead ends. Iterate through initial and remove dead ends, then return the filtered list.

filterHistoryNodes works the same as dead ends but it concerns visited nodes.

function pathFind pushes starting node into the history and master. Thencreates a new array list called deadends. Sets current node character and assigns to same char as start, then defines new char choice and assigns it to start as well. Sets init distance variable to 0. Initial choices and filtered choices are defined as array list of chars. Inits a boolean reverse and sets it to false. In a while loop, while curr doesn't equal Z which is the destination, it makes initial choices to the adjacency list that is at the index of the current node. Then it filters out dead ends from initial choices and equates to filtered choices, then test if it is at a dead end with if else statement: if num choices after filtering is at 1, then it is a dead end except if distance is 0; else: check and see if it is reversing and if it is, it pushes current position back into history and sets reverse to false, then filter choices again for other nodes in history, then it decides if it is using choice1 or choice2, and pushes the choice into history once it makes a choice. Then it updates the distance by defining variables curr and choice, and if it is reversing, it decrements distance by adjMatrix (else it increments by adjMatrix). It pushes choice into master, assigns curr to choice, and returns the distance.
