package chessosisnbproject.chessosisnbproject;

import java.util.EnumSet;

/**
 * This class contains data and methods I suspect I will no longer be needing.
 *
 * @author Henrik Lindberg
 */
public class Junkyard {

    /**
     * Returns the square bit at a particular bit index.
     *
     * @param index The bit index of a 64-bit integer (an int between 0 and 63).
     * @return A square bit, i.e., a bitboard with a single bit set.
     * @throws Exception In case of an invalid index.
     */
    public static long bitIndexToSquareBit( int index ) throws Exception {
        if ( index < 0 || index > 63 ) {
            throw new Exception( "index: " + index );
        }

        long bitboard = 0x0000000000000001L;

        for ( int counter = 0; counter < index; counter++ ) {
            // Shift the set bit left a single position
            bitboard <<= 1;
        }

        return bitboard;
    }

    /**
     * The inverse function (or method) of bitIndexToSquareBit.
     *
     * @param squareBit A bitboard (Java long) with exactly one bit set.
     * @return The bit index, a value between 0 and 63.
     * @throws Exception In case of an invalid square bit.
     */
    public static int squareBitToBitIndex( long squareBit ) throws Exception {
        // A square bit (bitboard) has exactly one bit set. Any other bitboard
        // value will cause an Exception to be thrown.
        if ( !SUM.validSquareBit( squareBit ) ) {
            throw new Exception( "squareBit: " + squareBit );
        }

        long comparisonSquareBit = 1;
        int bitIndex = 0;

        while ( true ) {
            if ( squareBit == comparisonSquareBit ) {
                break;
            }
            comparisonSquareBit <<= 1;
            bitIndex++;
        }

        return bitIndex;
    }

    /**
     * Determine the square name from the bitboard bit index.
     *
     * @param index An index between 0 and 63.
     * @return The square name that corresponds to the bit index.
     */
    public static String bitIndexToSquareName( int index ) {
        // Strings generated with the following bash command:
        // squares="$( for i in {1..8}; do echo {A..H}"$i"; done | tr ' ' '\n' )"
        // for s in $squares; do
        //     echo "$s" | sed 's/^/"/' | sed 's/$/", /' | tr -d '\n'
        // done >output.txt

        String[] squareNames
            = { "A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1",
                "A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2",
                "A3", "B3", "C3", "D3", "E3", "F3", "G3", "H3",
                "A4", "B4", "C4", "D4", "E4", "F4", "G4", "H4",
                "A5", "B5", "C5", "D5", "E5", "F5", "G5", "H5",
                "A6", "B6", "C6", "D6", "E6", "F6", "G6", "H6",
                "A7", "B7", "C7", "D7", "E7", "F7", "G7", "H7",
                "A8", "B8", "C8", "D8", "E8", "F8", "G8", "H8" };

        return squareNames[ index ];
    }

    /**
     * Does the opposite of bitIndexToSquareName.
     *
     * @param squareName The square name.
     * @return The bit index.
     */
    public static int squareNameToBitIndex( String squareName ) {
        final long BITBOARD = Square.valueOf( squareName ).bit();
        long leftShiftingBit = 1;
        int bitIndex;

        for ( bitIndex = 0;; bitIndex++ ) {
            if ( BITBOARD == leftShiftingBit ) {
                break;
            }
            // Shift the set bit left a single position
            leftShiftingBit <<= 1;
        }
        return bitIndex;
    }
    
    private long squareRangeBitwiseORed( Square first, Square last ) {
        long expectedRank = 0;

        // The method EnumSet.range returns the range of enum constants
        // specified as its arguments.
        for ( Square square : EnumSet.range( first, last ) ) {
            expectedRank |= square.bit();
        }

        return expectedRank;
    }    
}
