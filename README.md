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

## WorkTime
- 10.27 1.1 lab0  
        1.2  
        lab1  
        HW0  
- 10.28 2.1  
        lab2_setup  
        lab2  
        2.2  
        2.3 & 2.4
