# Notes

## On the 64! different mappings of square names to bit indices

A bitboard is simply a 64-bit integer with zero or more of its bits set. When used to represent a particular square (such as A1), exactly one bit is set in the bitboard. In principle, a square can be mapped to any single bit index (bit position) of the bitboard. The only requirement is that each square is mapped to exactly one bit index. This means that there are 64! possible ways of mapping squares to the one-bit-set-bitboards that represent them in memory.

The javadoc at the beginning of Square.java contains a mapping of squares to bit indices: A1 is mapped to index 0 (the least significant bit of the bitboard), A2 to 1 and, finally, H8 to 63. I find this the most "intuitive" mapping. Yet I think the principles of good software engineering would demand that I don't make the mapping something carved in stone. That is, Chessosis as a whole should not depend on any particular mapping of square names to bit indices; rather, the mapping used should be treated as an implementation detail hidden as private data in some class.

Whether I'll actually go by this ideal I don't know -- sticking to ideals is at times overkill. But even if I don't do it now, I might do it in the future if and when I create a more sophisticated version of a chess program.

## Hex representations of three long values

* `ffffffffffffffff` is -1 in decimal
* `8000000000000000` is Long.MIN_VALUE
* `7fffffffffffffff` is Long.MAX_VALUE

## Something about EnumSet...

https://weblogs.java.net/blog/mkarg/archive/2010/01/03/fun-enumset
http://javarevisited.blogspot.com/2014/03/how-to-use-enumset-in-java-with-example.html
