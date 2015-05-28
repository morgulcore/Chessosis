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
