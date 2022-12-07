# UCB-CS61B-sp18
[source: https://sp18.datastructur.es/](https://sp18.datastructur.es/)  
[run your code on: https://www.gradescope.com/](https://www.gradescope.com/)  


## 1.1 Intro, Hello World Java

### lab0: Setting Up Your Computer  
- download the JDK, git, and run HelloWorld in command line.
## 1.2 Defining and using classes
- classes in java  
### lab1: javac, java, git
- basically how to use **git** and **github**  
- submit HelloWorld.java, HelloNumbers.java and LeapYear.java  
### HW0: Basic Java Programs (optional)
- basic syntax of java

## 2.1 References, Recursion, and Lists
- difference between reference type and primitives  
- a simple example of IntList  
### lab2 setup
- set up the IntelliJ
### lab2 
- learn to debug  
- polish the IntList (about destructive and non-destructive)  
## 2.2 SLLists, Nested Classes, Sentinel Nodes
- build SLList instead of the naked recursive IntList
- nested classes
- cathing
- use sentinel Nodes to show the invariants
## 2.3 DLLists
- To make *addLast()* faster, we could add a *last* variable.  
  But the *removeLast()* would be very slow.
- What about adding a *secondToLast* pointer?  
  In fact, it's not helpful, because then we'll need to find the third to  
  last item to obey the appropriate invariants after removing the last item.
- Sol: add a previous pointer to each *IntNode*  
  namely **Doubly Linked List**  
- Improvement: Sentinel Upgrade  
  add a second sentinel node to the back of the list  
  Or the front and back pointer share the same sentinel node, making the list circular
- Generic DLList  
  public class DLList<Type> {}  
  DLList<String> d = new DLList<>("hello")  
## 2.4 Arrays
- basic array and 2D array
### proj0 NBody  
- class In StdDraw
## 2.5 ALists, Resizing, vs. SLists  
- To fasten *get()*, try the ALists.
- resize **geometric**  
- memory performance "usage radio" R  
## 3.1 Testing  
- Unit Test
- TDD (*Test-Driven Development*)
- Integration Testing
## 4.1 Inheritance, Implements
- Dog is a **hypernym(superclass)** of poodle, and poodle is **hyponyms(subclass)** of dog.  
- interface implements
- @Override  
- GRoE(Golden Rules of Equals)  
- Implement Inheritance *default*  
### lab3 Testing, Debugging
- Unit Testing(JUnit)  
### proj1a proj1b proj1gold  
- Deque

## 4.2 Extends, Casting, Higher Order Functions
- extends  
- super.methods() super() in the front of the constructor  
- encapsulation  
- static type / dynamic type  
- casting: (poodle) maxDog(a,b)  
- higher order functions  
## 4.3 Subtype Polymorphism vs. HoFs  
- HoF: a common way; Subtype Polymorphism: the object itself makes the choices  
- Comparable<T> -> compare to others  
- java.util.Comparator -> compare two things  
## 4.4 Libraries, Abstract Classes, Packages  
- Abstract Data Type  
- Abstract classes  
- Package  

## 8.1 Disjoint Sets  
- the Dynamic Connectivity Problem  
- Put connected objects in an equivalence class  
- Quick find O(MN)  
- Quick Union make id[i] the parent of object i  
- Weighed Quick Union make the root of the smaller tree point to the root of the larger one->O(lgN)  
- Weighed quick union with path compression O(MlogN)  
## 8.2 Trees, BSTs  
- When deleting the root, choose the biggest object in the left BST or the smallest in the right BST
## 8.3 Balenced BSTs
- 2-3 and 2-3-4 trees: search and insert  
- 2-3 trees <=> LLRBs  
## 9.1 Hashing  
- Instead of the brute force approach, hash the object -> returns an Integer (32 bits)  
- hashCode() to index conversion: hashCode & 0x7FFFFFFF  
- good hash function: use all the bits in the key  
- Collision resolution: Separate chaining & "open addressing"  
- resizing may lead to objects moving to another linked list
## 9.2 Heaps and Priority Queues  
- A max (min) heap is an array representation of a binary tree such that every node is larger than all of its children
- tree representation  
## 9.3 Advanced Trees, incl. Geometric  
### Traversals  
- Depth First Traversals: pre-odered, in-ordered and post-ordered  
- Level Order Traversals  
- Visitor Pattern  
- Range Finding and Pruning  
- 2D Quadtrees  
## 10 Graph  
### Graph Traversals
- DFS Preorder, DFS Postorder, BFS  
- Topological Sorting (reverse of the DFS postorder)  
## 11.1 Shortest Paths and Dijstra's Algorithm  
## 11.2 Minimum Spanning Trees, Kruskal's, Prim's  
- The minimum spanning tree is the spanning tree whose edge weights have the smallest sum  
- Cut Property: The minimum crossing edge for ANY cut is part of the MST  
- Prim's Algorithm: Starting from any arbitary source, repeatedly add the shortest edge that connects some vertex in the tree to some vertex outside the tree  
- Kruskal's Algorithm: add the shortest path that doesn't result in a cycle  
## 11.3 Dynamic Programming  
- DAGs: directed acyclc graph  
- DP: solve subproblems  
- LLIS (length of longest increasing subsequence)   
