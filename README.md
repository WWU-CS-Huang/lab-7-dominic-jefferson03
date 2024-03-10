% CSCI 241 - Lab 7: Huffman Coding
% Fuqun Huang
% Winter 2024

## Overview

In this lab, you will implement Huffman Coding. As in Lab 6, no skeleton is provided. In addition, you are expected to make good choices of data structures to implement the encoding, decoding, and tree construction efficiently.

You will complete this lab **in pairs**. 

## Git and submission for Lab 7

The Github Classroom link for Lab 7 is available in the Lab 7 assignment on Canvas. The workflow for group assignments is a little different:

0. First, find out who your partner is on Canvas by checking the Lab 7 Groups in the People tab; the groups are named `{section}_{num}` where `{section}` is your section number and `{num}` is your group number within your section.

1. The first member of your pair to accept the Github Classroom invite should **create a team with the following name:** the WWU usernames of the two teammates, ordered lexicographically, and separated by an underscore. For example, if Daniel (one of our TAs) and I were working on this lab together, our  team would be named `korontd_huangf2`.
2. The second member of the pair to accept the Github Classroom invite  should find the team created by the first member and join it.

There is no skeleton code, so your repository will start out empty, and as in Lab 6, you'll start by creating a fresh Gradle project to work in. Please see the Lab 6 handout for a refresher on how to create a gradle project. Name your project `lab7`, use `lab7` as the package name, and put your main program in `Huffman.java`. 

## Tasks: Overview

Building a working implementation of Huffman Coding involves implementing the following operations:

1. **Count frequencies**. Given an input `String`, calculate the frequency (i.e., number of occurrences) of each character in the string.
2. **Build the tree.** Given the frequencies from part (1), build a Huffman Coding Tree.
3. **Decode**. Given a coding tree and an encoded bitstring, decode it into the original input string.
4. **Encode**. Given your coding tree and an input string, encode the string into its compressed binary representation.

5. **Main Program**. Finally, you'll need to write a main program that demonstrates the above steps in action.

## Data Structure Selection and Efficiency Targets

Your task is to implement each of the above steps as efficiently as possible. This requires thinking carefully about which data structures to use and/or design for each task. Start by thinking through each algorithm (you may find it helpful to write pseudocode) and determining what data structures will allow you to complete them most efficiently.

Your approach should meet the following asymptotic efficiency targets:

1. **Count frequencies:** O($n$) average case, where $n$ is the length of the input string.
2. **Build the tree:** O($a \log a$) average case, where $a$ is the number of unique symbols in the input string.
3. **Decode:** O($b$) average case, where $b$ is the length of (number of bits in) the encoded representation.
4. **Encode:** O($b$) average case, where $b$ is the length of (number of bits in) the encoded representation.

If your data structure choices do not meet the above targets, don't proceed to implementation; continue thinking about how to hit these efficiency targets and ask me or the TA for help if you get stuck.

## Main Program Spec

`Huffman.java` should have a `main` method that runs when you type `gradle run` from the project root directory. The behavior of the main program is as follows:

* The program takes one command-line argument that specifies a filename.

* The program reads the contents of that file as the input string.

* The program builds a Huffman Coding Tree for that input, and encodes the string using the constructed tree.

* If the length of the input string is less than 100 characters, print the following three things, one per line:

  * The input string
  * The encoded bitstring
  * The result of decoding the encoded bitstring

* Regardless of the length of the input string, print the following two things, one per line:

  * A boolean that confirms programmatically (i.e., using the `equals` method) that the input and the decoded output are the same.

  * The compression ratio, calculated as `length(encoded bitstring) / length(input) / 8.0`\*

\* We divide by 8 to show what the compression ratio would be if we stored the encoded string as a true bitstring (1 bit per 0 or 1), rather than a String (1 byte = 8 bits per 0 or 1); see Representing Inputs and Bitstrings, below.

Here are a few sample invocations:

```
$ cat example0.txt
feed
$ gradle run -q --args "example0.txt"
Input string: feed
Encoded string: 110010
Decoded string: feed
Decoded equals input: true
Compression ratio: 0.1875
$ 
$ cat example1.txt
beef feed fed calf
$ gradle run -q --args "example1.txt"
Input string: beef feed fed calf
Encoded string: 0011111101101011111000101011100010110011000001001
Decoded string: beef feed fed calf
Decoded equals input: true
Compression ratio: 0.3402777777777778
$ 
$ gradle run -q --args "GreatExpectations.txt"
Decoded equals input: true
Compression ratio: 0.5672382983174206
$
```

Your bitstrings won't necessarily be identical based on tie-breaking choices in your code. As far as I know, since these are optimal codes, the compression ratios should match. Your output does not need to be formatted identically to mine, but your output should follow the guidelines above, including printing one thing per line and in the correct order.

You can find the `GreatExpectations.txt` file used in the last run in the Canvas files section under the folder Lab7.

## Implementation Notes and Guidelines

#### Approach

Ideally you'd implement the above tasks in the order 1, 3, 4, 2, 5. However, since I haven't written extensive test suites for you this time, you can't test encoding or decoding until you have a tree. For this reason, I recommend implementing the tasks in the order listed, except that you should (at a minimum) start building up your Main Program code method as you go to test your code.

You don't need to write rigorous unit tests, but you should convince yourself that each step works before moving onto the next. You don't want to write code for all four steps and then find out that "it doesn't work"; this will leave you with a *lot* of code where the bug(s) might be. This probably looks like printing out the results of a given step and comparing them to what you expect to see on a few different inputs (ideally not all trivially small). Inventing a few well-crafted test inputs is probably worth your while.

#### Design

It's up to you to come up with a sensible structure for your project; the only requirement is that your main program lives in `Huffman.java`.

#### Data Structure Use

You can (and should) make use of **any** data structures from the [Java Collections framework](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/doc-files/coll-overview.html), and/or **any** data structures that we have implemented so far in this class.

To use data structures from prior projects, we can create a `.jar` file that packages up the classes from the project and include it as a dependency in our `lab7` project. Here are the steps for doing this; I'll explain the process using A3, but it works similarly for A2.

1. In the project you want to use (in this case, A3), edit `lib/build.gradle` and add the line

   ```groovy
   id 'java-library'
   ```

   inside the `plugins` block.

2. Run `gradle build`. This will generate a `.jar` file in `lib/build/libs`.

3. Copy this file into your `lab7` project into `app/libs `(if the `libs` directory doesn't exist, create it). The name of the jar file doesn't really matter; I renamed mine `heap.jar`, and I'll assume that's your jar file's name in the next step.

4. Edit `app/build.gradle` in your `lab7` project; inside the `dependencies` block, add the following line:

   ```groovy
       implementation files('libs/heap.jar')
   ```

5. In your code, you should now be able to put `import heap.Heap` among any other imports needed, and make use of the A3 Heap in your code.

If you want to be sure that your dependencies are correct, you are welcome to use either the built-in Java data structures, or download my solution `.jar` files from the files section on Canvas in the Lab7 folder and follow only steps #3 and #4 above.

`avl.jar`

`heap.jar`

The corresponding Java collections are `java.util.PriorityQueue` (this has a somewhat different interface from ours, in that the priority is determined by the `compareTo` method instead of by a separate priority value), `java.util.HashMap`, and `java.util.TreeSet` or `java.util.TreeMap`.

#### (Not) Storing the Tree Alongside the Encoding

Usually we use techniques like this to compress data before storing it in a file or sending it over a network. To properly be able to reconstruct the original input, you'd need to store not just the encoded bitstring but also a representation of the coding tree you used to construct it.

To keep things a little simpler, we will not worry about doing this. This means that when we calculate the compression ratio, we are being generous to ourselves and actually giving a lower bound on the compression ratio that would be achieved if the tree is stored. For long inputs, the size of the tree becomes small compared to the length of the encoded string.

#### Representing Inputs and Bitstrings

In this lab, we'll use characters (or length-1 strings) as the symbols that make up our input string. As mentioned in the lecture video, we could make other choices here if we wanted our algorithm to work on non-String inputs.

In a real-world implementation, we would want to represent our bitstrings as efficiently as possible. The right way to do this in Java is probably to use a [`BitSet`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/BitSet.html). However, to keep things simple, I recommend simply storing the bitstring as a `String` containing only zeros and ones. This incurs a storage cost of 8x, because a String will store each 0 or 1 as a byte (8 bits) where an efficient representation would store each 0 or 1 in a single bit.

#### Building Strings Incrementally

One efficiency gotcha with `Strings` comes from the fact that they are immutable; this means concatenation involves allocating a new string and copying both strings' contents into the new memory. If you're building a string by repeately adding small pieces to it, this is quite inefficient. Fortunately, there's a [`StringBuilder`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/StringBuilder.html) class that allows you to collect all the pieces first, then perform just one O(length) operation at the end to concatenate them all.

#### Printing Trees

Convincing yourself that you've built a correct tree is probably the trickiest testing task. Feel free to look at the `AVL.printTree()` method from the A2 codebase for ideas on how to print out a readable representation of a tree.

#### Node Ordering and Tie Breaking

You do **not** need to follow any specific rules to decide which node should be the left vs. right child when merging two nodes. Similarly, you do **not** need to follow any specific rules to break ties among equal-frequency trees. You will end up with an optimal tree either way.

#### Edge Cases: Empty or Single-Character Inputs

 You do not need to handle inputs that are empty or have only a single unique character.

## Submission

Submit your project using Github. You should include in your repository any input files you created in the course of testing your program. 

## Rubric

This lab is worth 10 points, 2 points for each of the 5 tasks completed. If you are not able to get working code for all 5 tasks, your main method should include - to the extent possible - code that demonstrates the parts that are working correctly. For example, if you completed Step 1 but you don't have a working encoding/decoding pipeline, you could print a table of character frequencies to help convince me that the frequency counting code works. If your program does not demonstrate code for a task, you cannot earn more than 1/2 for that task.

As usual, deductions will be made for style issues. In particular, since there is no skeleton code, make sure that you have included proper specifications for all of your methods.
