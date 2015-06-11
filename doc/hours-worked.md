# Hours worked on Chessosis, Henrik Lindberg's JavaLabra project

## 2015-05-13 | Hours worked: 7

Getting started with things can be difficult and confusing. This course seems to be no exception. The first deadline is tomorrow, and the initial tasks involved have to do with preparation. I created a GitHub account for myself. I have no previous experience with GitHub (or Git for that matter) so I spent some time just familiarizing myself with the service.

The JavaLabra pages (https://github.com/javaLabra/Javalabra2015-5) have instructions on how to get things up and running for the actual work on the course. The instructions aren't very well written but are understandable, given some effort. I got my first hands-on experience with using Git through these instructions. Git seems confusing at first but I'm sure I'll grow comfortable with it over time.

Creating the public-private SSH key pair for my computer and GitHub account went fairly well. I haven't used SSH client programs or SSH-based authentication that much before but I suppose I already understand the basic principles involved.

I tried setting up a NetBeans project for my repository using Maven and the PIT testing features. I don't think it went that well although I did get some idea what I'm supposed to accomplish by it. I'll try again tomorrow.

Much of the time I've used today on JavaLabra has involved me just reading the course pages. I want to get a good general idea on what this course is all about, how it is structured and what I am to accomplish during it.

This document is in English as is everything else I intend to write for this project. I believe English is the language of choice for computer science and ICT engineering whereas Finnish is more suitable for expressing great thoughts and deep feelings. Also, my project might turn out to be more useful for others (or for a greater audience) if it is written in English.

## 2015-05-14 | Hours worked: 6

I started the day by getting more practice and a general knowledge on Git. I now have a degree of routine involved what comes to the few Git commands needed to work on my GitHub repository. I read Wikipedia's article on Git which obviously enhanced my knowledge and understanding of the program. While typing at the Linux command line I made myself a small reference of the commands I'm likely to need in future Git sessions. Like so:

* `ssh-agent /bin/bash   # Gets rid of the need to type...`
* `ssh-add ~/.ssh/id_rsa # ...the passphrase more than once`
* `cd uf/yo1415/javalabra/`
* `git clone 'git@github.com:morgulcore/Chessosis.git'`
* `cd Chessosis/`
* `git status`
* `git add <dir>`
* `git commit -am 'Commit message'`
* `git push`

I suppose my NetBeans project is now set up correctly. At least the were no more error messages when running the PIT thing. I haven't written any actual code for my project yet, although I did create a HELLO WORLD main method. For better or for worse, I think the stage is now set.

I registered for labtool. I suppose my work on Chessosis can and will be monitored by someone from the computer science department from now on. That is the general idea, anyway. Not that I don't realize that the repository is public; in principle, anyone on the Internet can inspect my work.

After completing this entry I'm going to write the topic specification, or whatever "aihemäärittely" is in English.

## 2015-05-17 | Hours worked: 8

I've done a lot of reading about chess programming today. The first thing I have really had to consider about my chess program is how to represent the chess board along with its pieces as data. Bitboards (64-bit integers treated as a sort of a bit array) seem like a good choice. Each bit in a bitboard corresponds to a certain square on the chessboard (e.g., E4), and the bitboard is used to deliver information about the game. Ultimately it's all about using bitboards as a way to express a set of squares, such as {E4,E5,D4,D5}. A lot of very useful information about the status of a chess game can be expressed with a few bitboards. The standard starting position serves as a basic example of this. Here's a representation of it using both standard set notation and bitboards.

* White pawns: {A2,B2,C2,D2,E2,F2,G2,H2} 0x000000000000ff00L
* White knights: {B1,G1}                 0x0000000000000042L
* White bishops: {C1,F1}                 0x0000000000000024L
* White rooks: {A1,H1}                   0x0000000000000081L
* White queen: {D1}                      0x0000000000000008L
* White king: {E1}                       0x0000000000000010L
* Black pawns: {A7,B7,C7,D7,E7,F7,G7,H7} 0x00ff000000000000L
* Black knights: {B8,G8}                 0x4200000000000000L
* Black bishops: {C8,F8}                 0x2400000000000000L
* Black rooks: {A8,H8}                   0x8100000000000000L
* Black queen: {D8}                      0x0800000000000000L
* Black king: {E8}                       0x1000000000000000L

A tool for working on bitboards: http://cinnamonchess.altervista.org/bitboard_calculator/Calc.html

I created a Java enum type called Square.java for Chessosis. It defines the most basic building blocks for my bitboard-based approach: the mappings between square names and bit indices. For example, square F8 is mapped to bit 0x2000000000000000L in the layout I'm using.

I also created a few JUnit tests for Square.java. I think I'm more or less done with working on that particular enum.

## 2015-05-18 | Hours worked: 8

I created the class ConstantSquareSet along with JUnit tests for it. The javadoc and other comments in the class describe what it's all about. At some point I have to work on the JUnit tests of the class as they violate the rule of non-copy-paste-coding. Even so, I consider the tests rather thorough.

I have done many other things today on the project. It would be inconvenient and counter-productive to document all of them in detail here. Let's just say that I've added a few more classes and am trying to figure out how I should get started in making them work together. My near-future goal is making the program smart enough to determine the legal moves in simple chess game positions. I'm going to start with a position with just the kings left on the board with the white king on E1 and the black on E8, and with White's turn to move. There are five legal moves in this position.

The king can only ever move to one of its surrounding squares (apart from castling). This made me think that it would be good to have some method that would return the surrounding squares of any particular square. For example `findSurroundingSquares(E4)` would return the set {D5,E5,F5,F4,F3,E3,D3,D4} or the equivalent bitboard 0x0000001c141c0000L. I'm in the middle of working on the details of implementing the method.

No doubt there is need for methods with a similar idea as what findSurroundingSquares has. I mean a situation where I place, let's say, a queen or a knight on some particular square of an empty chessboard. The methods I'm talking about would compute the squares where the piece can move to. No doubt such methods would be useful in generating the legal move list in real-game situations, too.

I need to define more constant square sets or bitboards in ConstantSquareSet.java such as diagonals, the four corner squares and the edges of the chessboard.

## 2015-05-27 | Hours worked: 8

I created the file doc/notes.md and added the first entry to it on the mapping of square names to bit indices.

I created the static method `Square.bitIndexToBitboard( int index )`. I'm not sure how useful it will be. The test I wrote for the method turned out to be quite complicated, and unnecessarily so. Work smart, not hard :(

Created...
* static method `Square.squareNameToBitIndex` along with JUnit test `validSquareNameToBitIndexMappings`
* static method `MoveGenerator.surroundingSquares( Square square )` along with the first JUnit test for it
* ...and a lot of other stuff

My JUnit test `surroundingSquaresWorksOnNonEdgeSquares` is interesting because it tests `surroundingSquares` by computing the same set of results but in a different manner. I think the test method does the job in a more elegant manner. Well, maybe I'll swap the program logic of the test method with the tested at some point.

There's so much to do.

## 2015-05-28 | Hours worked: 10

I've been doing various things with Chessosis today only to discover that I have some issues with its basic design. Therefore I'm going to make a few changes during the evening as documented below.

* Renaming class ConstantSquareSet to CSS and removing static imports of the class from other files. From now on I'll refer to the constants with the prefix, I mean like CSS.E4 or CSS.EDGE rather than E4 or EDGE.
* The square bits of CSS (constants A1, A2, etc.) are now initialized explicitly rather than with a call to enum Square's method getSquareBit.
* I updated CSS's javadoc. The diagram of the square name to bit index mappings is now at the beginning of CSS.
* In Square.java, the enum constants are no longer initialized with the explicit long values. Rather, the CSS bit square constants are used for the job.
* Renamed Square's method getSquareBit to bit.
* Renamed bitIndexToBitboard to bitIndexToSquareBit.
* Created an inverse function method for bitIndexToSquareBit: squareBitToBitIndex
* Worked on the tests in SquareTest.java.

I no longer consider the square name to bit index mappings something that should be hidden from most parts of the program. Many parts of Chessosis will depend on the mappings being like in the diagram in CSS's javadoc.

## 2015-05-29 | Hours worked: 8

I added a class called SUM which is Static Utility Methods for short. It's a sort of a general purpose toolbox and contains public static methods that are likely to be used by many other classes. The first two methods I added to the class are `bitboardToSquareSet` and `squareSetToBitboard` (so far they don't contain any actual program logic).

SUM does not and will not contain any non-static members.

I moved method validSquareBit from Square to SUM along with its tests (SquareTest to SUMTest). I intend to move more static methods into class SUM.

[A few hours later.] I created many static utility methods for class SUM. I also created JUnit tests for them. They seem to work. However, I am still to create the methods I mentioned before, `bitboardToSquareSet` and `squareSetToBitboard`. Now having the helper methods I need, I think I'll code their program logic and JUnit tests next. After that I might actually get some work done on MoveGenerator.surroundingSquares().

## 2015-05-30 | Hours worked: 10

I have completed my work on `SUM.bitboardToSquareSet()` and `SUM.squareSetToBitboard()` both what comes to the methods themselves and their JUnit tests. I now have the tools to fluently and reliably convert between the two ways a set of squares can be expressed in Chessosis. The names of the methods are of course a bit misleading as a bitboard IS a set of squares or a means to represent such a set, anyway. "Square set" in the method names refers to a set of Square enum constants. I also updated Square's javadoc.

I moved the remaining static methods in enum Square into a class called Junkyard (I'm assuming its name falls into the category of "self-documenting"). I also made the tests for CSS more concise. I don't think constant data requires a lot of code for its testing. Even so, it's good to have some tests for CSS. Nothing can be trusted to work correctly in Chessosis if CSS contains inconsistent data.

Having now a set of useful and well-tested tools in class SUM, I returned back to my work on `MoveGenerator.surroundingSquares()`. I simply deleted most of the code I had written a few days ago, made a fresh start. So far the method seems to work on the corner squares and also non-corner squares on the 1st and 8th rank. I think my new implementation of the method is turning out to be rather elegant.

## 2015-05-31 | Hours worked: 8

`MoveGenerator.surroundingSquares()` is now fully implemented and fairly well tested. This is a major milestone.

I spent the rest of the day doing a code review on https://github.com/mikkoka/keskustelujarjestaja

## 2015-06-01 | Hours worked: 10

I intend to create methods similar to `surroundingSquares()` for each chess piece (apart from pawns, I think). These methods will be named as follows:

* `rooksSquares()`
* `bishopsSquares()`
* `queensSquares()`
* `knightsSquares()`

It is because of this intended naming scheme that I decided to add an alias for surroundingSquares(): `kingsSquares()`. Simply renaming the method didn't seem appealing, as the documentation I've written constantly and consistently uses the term of surrounding squares. Anyway, from now on calling kingsSquares() is preferable to calling surroundingSquares() directly.

I added a simply enum type `Color` which consists of two constants, BLACK and WHITE. I will be using the type at least in indicating who's turn it is. I'll likely also express piece color and perhaps also square color with the enum type.

I added a class called Move. It's purpose is to represent a legal move in a give position. Initially it will contain only the enum Square fields `from` and `to`. Maybe I'll also override the `toString()` method so that it's to generate a String representation of the move. It seems I'll need to override Object's `equals()` and `hashCode()` methods in class Move. That's because I'm placing Move objects in Sets. This implies testing for equility as sets can't hold duplicate elements.

I have begun my work on `MoveGenerator.moveGenerator( Position position )`, the method that's supposed to discover all the legal moves in the given position.

Too... much... coding...

## 2015-06-02 | Hours worked: 8

Most of my work today on Chessosis has involved the static `moveGenerator()` method. I've also written and updated the javadoc and familiarized myself with FEN (Forsyth–Edwards Notation), the means to record chess positions as strings that span less than a line.

I'm considering writing two new methods: `public static boolean squareAttacked( Square s )` and `routeClear( Square s1, Square s2 )` (the names might change). Method squareAttacked() has to do with move generation; a king can never move to an attacked square, be it an empty square or an enemy chessman protected by another chessman. Method routeClear() determines whether or not there's an "obstacle" (chessman) between the Square parameters which have to be on the same file, rank or diagonal.

## 2015-06-09 | Hours worked: 10

Due to an occupational hazard and a twist of fate I had to depart from my work on Chessosis for almost a week. I realize I am now behind schedule and the weekly deadlines. I do however still have faith in being able to complete the course.

Today I've worked heavily on the MoveGenerator class. I has been quite tricky and frustrating. Even so, I seem to be over the worst of it. Tomorrow I think I can come up with a quick and dirty command line UI. Soon after that I'll start my work on the GUI. I'll also invest as much time as I can in coming up with the diagrams required in the deadlines (class diagram, sequence diagram, and what not).

Now I'll just do some tidying of code and wrapping things up for today.

## 2015-06-10 | Hours worked: 9

I have worked very busily on MoveGenerator and later on on GUI research. I'm writing this on the next day. Yesterday things were too hectic for me to really be able to make a report of my doings (what comes to this course). In retrospect I can say that I was feeling frustrated. I was trying to solve my problems with Chessosis by writing methods. I can't seem to be able to think in classes but rather in methods (perhaps I should switch to programming in C). Anyway, for every method I wrote some new problem came up. Well, I could think of no other solution than to write more and more methods -- methods that would solve the problems I had introduced with my previous methods. It's an interesting recursive pattern. Anyway, I'm happy as for now my cardhouse of methods shows no signs of collapsing.

## 2015-06-10 | Hours worked: 10

I managed to come up with a GUI for Chessosis. At some point I felt it was an impossible task for me but then somehow a solution just started to appear in my mind. My solution, the GUI, is a combination of many ideas and sources mostly from the Internet, all of which I've examined, considered and made variations upon enough to consider them a part of my own knowledge, also. I'll document my studies of creating GUIs in Java at some later point.

Oh, one more thing -- I finally managed to come up with a class diagram, luokkakaavio. I'll update it as needed so that it will remain an accurate graphical representation of the basic structure of Chessosis.

In extreme hurry I tried coming up with a sequence diagram. I'll work on it some more in the near future.
