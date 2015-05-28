package chessosisnbproject.chessosisnbproject;

/**
 * Enum constants, static methods.
 * <p>
 * I'll add up-to-date javadoc later.
 *
 * @author Henrik Lindberg
 */
public enum Square {

    // Each enum constant has a single square bit property.
    A1( CSS.A1 ),
    B1( CSS.B1 ),
    C1( CSS.C1 ),
    D1( CSS.D1 ),
    E1( CSS.E1 ),
    F1( CSS.F1 ),
    G1( CSS.G1 ),
    H1( CSS.H1 ),
    A2( CSS.A2 ),
    B2( CSS.B2 ),
    C2( CSS.C2 ),
    D2( CSS.D2 ),
    E2( CSS.E2 ),
    F2( CSS.F2 ),
    G2( CSS.G2 ),
    H2( CSS.H2 ),
    A3( CSS.A3 ),
    B3( CSS.B3 ),
    C3( CSS.C3 ),
    D3( CSS.D3 ),
    E3( CSS.E3 ),
    F3( CSS.F3 ),
    G3( CSS.G3 ),
    H3( CSS.H3 ),
    A4( CSS.A4 ),
    B4( CSS.B4 ),
    C4( CSS.C4 ),
    D4( CSS.D4 ),
    E4( CSS.E4 ),
    F4( CSS.F4 ),
    G4( CSS.G4 ),
    H4( CSS.H4 ),
    A5( CSS.A5 ),
    B5( CSS.B5 ),
    C5( CSS.C5 ),
    D5( CSS.D5 ),
    E5( CSS.E5 ),
    F5( CSS.F5 ),
    G5( CSS.G5 ),
    H5( CSS.H5 ),
    A6( CSS.A6 ),
    B6( CSS.B6 ),
    C6( CSS.C6 ),
    D6( CSS.D6 ),
    E6( CSS.E6 ),
    F6( CSS.F6 ),
    G6( CSS.G6 ),
    H6( CSS.H6 ),
    A7( CSS.A7 ),
    B7( CSS.B7 ),
    C7( CSS.C7 ),
    D7( CSS.D7 ),
    E7( CSS.E7 ),
    F7( CSS.F7 ),
    G7( CSS.G7 ),
    H7( CSS.H7 ),
    A8( CSS.A8 ),
    B8( CSS.B8 ),
    C8( CSS.C8 ),
    D8( CSS.D8 ),
    E8( CSS.E8 ),
    F8( CSS.F8 ),
    G8( CSS.G8 ),
    H8( CSS.H8 );

    private final long squareBit;

    // The constructor for an enum type must be package access or private,
    // i.e., it cannot be public or protected. Therefore it falls outside
    // of javadoc.
    private Square( long squareBit ) {
        this.squareBit = squareBit;
    }

    /**
     * Accessor method for the enum constants' square bit property.
     *
     * @return The square bit property.
     */
    public long bit() {
        return this.squareBit;
    }

    //
    // ====================
    // == Static methods ==
    // ====================
    //
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
        if ( !validSquareBit( squareBit ) ) {
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
     * Determines whether a bitboard is a square bit.
     * <p>
     * A square bit (bitboard) has exactly one bit set. In other words,
     * its a value 2^n where n is between 0 and 63.
     *
     *@param bitboard The bitboard to examine.
     *@return True when bitboard is a square bit.
     */
    public static boolean validSquareBit( long bitboard ) {
        for ( Square square : Square.values() ) {
            if ( square.bit() == bitboard ) {
                return true;
            }
        }

        return false;
    }
}
