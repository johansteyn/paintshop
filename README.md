# Paint Shop
## The Paint Shop Code Challenge

### Contents

1. The Challenge
2. Design
3. Algorithm
4. Implementation
4. Build
6. Run

--------------------------------------------------------------------------------
### 1. The Challenge

You run a paint shop, and there are a few different colors of paint you can prepare. Each color can be either "gloss" or "matte".

You have a number of customers, and each have some colors they like, either gloss or matte. No customer will like more than one color in matte.

You want to mix the colors, so that:
  * There is just one batch for each color, and it's either gloss or matte.
  * For each customer, there is at least one color they like.
  * You make as few mattes as possible (because they are more expensive). 

Your program should accept an input file as a command line argument, and print a result to standard out. An example input file is: 
```
5 
1 M 3 G 5 G 
2 G 3 M 4 G 
5 M
```
The first line specifies how many colors there are. Each subsequent line describes a customer. For example, the first customer likes color 1 in matte, color 3 in gloss and color 5 in gloss.

Your program should read an input file like this, and print out either that it is impossible to satisfy all the customers, or describe, for each of the colors, whether it should be made gloss or matte.

The output for the above file should be: 
```
G G G G M 
```
...because all customers can be made happy by every paint being prepared as gloss except number 5. 

An example of a file with no solution is: 
```
1
1 G
1 M 
```
Your program should print:
```
No solution exists 
```
A slightly richer example is: 
```
5
2 M
5 G
1 G
5 G 1 G 4 M
3 G
5 G
3 G 5 G 1 G
3 G
2 M
5 G 1 G
2 M
5 G 
4 M
5 G 4 M
```
...which should print:
```
G M G M G 
```
One more example. The input: 
```
2
1 G 2 M
1 M 
```
...should produce:
```
M M 
```
There is no time limit, just show us your best ;-) 

--------------------------------------------------------------------------------
### 2. Design

The design centers around a class called `Paintshop` with a public constructor, a public `solve` method, 
and a static `main` method.

The constructor parses the specified input file to load the problem into a simple data representation:

  * The width, which represents the number of colours in the pallette.
  * A list of requirement strings, one for each customer

Individual requirements and solutions are represented internally by strings.

For example, input file:
```
5
1 M 3 G 5 G
2 G 3 M 4 G
5 M
```
Is represented internally by:
```
Numeric width: 
  5
List of requirement strings: 
  M_G_G
  _GMG_
  ____M
```
An underscore (_) in a requirement string means that the colour is not required (ie. it can be either Gloss or Matte - it doesn't matter).

A single `solve` public method finds a solution to the problem (if any).

Solutions, like requirements, are represented by strings, except that an underscore (_) in a solution string means that the solution is *partial* (ie. it is not a *full* solution - it is an *interim* proposal - we still need to figure out some values).


For example, a proposed partial solution for the above requirements could be:
```
_G__M
```
While the full, final solution would be:
```
GGGGM
```

--------------------------------------------------------------------------------
### 3. Algorithm

The core functionality resides in the private, recursive `solve` method,
which takes 2 parameters:

  * A list of requirement strings
  * A proposed solution string

The proposed solution can be either empty (all underscores), partial (some underscores) or full (no underscores).

The first call passes the original requirements along with an empty proposed solution.
Each subsequent (recursive) call will be made with a reduced list of requirements and a more complete proposed solution.
So, as we go deeper down the rabbit hole, the list of requirements will become smaller, while the proposed solution will become "fuller" (fewer blanks).

A proposed solution either:
  * Cannot be a better solution that what we already found (return null), or
  * Satisfies all the requirements (return it), or
  * Is a full solution that does not satisfy all the requirements (return null)
  * Is a partial solution that can be sent down the rabbit hole with a more complete proposal with:
      * A column set for a single colour requirement, or
      * Any other column not yet set in the current proposed solution.

Every time a full solution is found it is compared against the "best" solution found so far (ie. fewest M's), and if it is better  then it will be returned (as the new "best" solution), otherwise it will return null.

#### Walk-through Example:
##### _First Call_
The first call to the recursive "solve" method is made with requirements:
```
M_G_M
_GMG_
____M
```
And proposes an empty solution:
```
_____
```
Since positions 2 and 5 do not have any M's for any of the requirements, we know that the solution must contain a G in those positions, so we modify the solution accordingly:
```
_G_G_
```
The (modified) proposed solution is applied to the requirements, yielding a new (hopefully, reduced) list of requirements:
```
M_G_M
____M
```
Now we are ready to "go down the rabbit hole" with the new list of requirements and a new proposed solution, based on the current proposal but with one chosen column set to G or M. To choose a column, we first go for some "low hanging fruit" by looking for any requirement that has only one colour (ie. the second one above), resulting in modified porposal:
```
_G_GM
```
#### Second call
The second call is made with the reduced list of requirements:
```
M_G_M
____M
```
And the modified proposed solution:
```
_G_GM
```
Noticing that position 3 of the requirements contains no M's, we modify the proposed solution further:
```
_GGGM
```
And we apply it to the requirements, yielding an empty list of requirements (since it matches both requirements), so we return that proposed solution, with underscores replaced by G's:
```
GGGGM
```

This solution is then also returned from the first call up to the public "solve" method, to be printed to stdout as the final solution in the main method.

--------------------------------------------------------------------------------
### 4. Implementation

The initial implementaion has been done in Java (JDK 1.8).

It requires JUnit as build time, but does not require any 3rd party libraries at runtime.

I intend to port it to Scala if time allows...

--------------------------------------------------------------------------------
### 5. Build

To compile, run unit tests and build the JAR artifact:
```
$ mvn clean package
```
To generate Javadoc:
```
$ mvn clean site
```

--------------------------------------------------------------------------------
### 6. Run
For these examples, the built artifact was renamed to **paintshop.jar**.

To show help/usage:
```
$ java -jar paintshop.jar -h
Paint Shop Code Challenge - Johan Steyn
  
Usage: java -jar paintshop.jar [-h] [-v[v]] <file>

Args:
  -h     Help (shows this usage text)
  -v[v]  Verbosity level (-v = show time taken, -vv = show loads more)
  <file> Input file
```
To solve a puzzle:
```
java -jar paintshop.jar test-5a.txt 
G G G G M
```
To show time taken:
```
$ java -jar paintshop.jar test-5b.txt -v
G M G M G
Time: 2 milliseconds
```
To show loads more information (handy for debugging):
```
$ java -jar paintshop.jar test-5b.txt -vv
_M___
____G
G____
G__MG
__G__
G_G_G
G___G
___M_
___MG
|||||
G_G_G
GMG_G
GMGMG
2 milliseconds
G M G M G 
```




