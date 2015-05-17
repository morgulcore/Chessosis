package chessosisnbproject.chessosisnbproject;

/**
 * Holds the bitboard representations of the 64 squares of the chessboard.
 * <p>
 * Type Square consists of 64 enum constants that each hold a unique bitboard
 * value with a single bit set. This bit and the constant name
 * create a mapping between a chessboard square and a bit index.
 * The mappings I've used are as follows (index 00 corresponds to the
 * least significant bit, index 63 to the most significant):
 * <p>
 * <pre>
 *   +---------------------------------------+
 * 8 | 56 | 57 | 58 | 59 | 60 | 61 | 62 | 63 |
 *   |---------------------------------------|
 * 7 | 48 | 49 | 50 | 51 | 52 | 53 | 54 | 55 |
 *   |---------------------------------------|
 * 6 | 40 | 41 | 42 | 43 | 44 | 45 | 46 | 47 |
 *   |---------------------------------------|
 * 5 | 32 | 33 | 34 | 35 | 36 | 37 | 38 | 39 |
 *   |---------------------------------------|
 * 4 | 24 | 25 | 26 | 27 | 28 | 29 | 30 | 31 |
 *   |---------------------------------------|
 * 3 | 16 | 17 | 18 | 19 | 20 | 21 | 22 | 23 |
 *   |---------------------------------------|
 * 2 | 08 | 09 | 10 | 11 | 12 | 13 | 14 | 15 |
 *   |---------------------------------------|
 * 1 | 00 | 01 | 02 | 03 | 04 | 05 | 06 | 07 |
 *   +---------------------------------------+
 *     A    B    C    D    E    F    G    H
 * </pre>
 * <p>
 * Note that the mappings described in the diagram should be considered
 * an implementation detail of the Square type. No other class or part
 * of Chessosis (apart from the JUnit tests) should rely on these particular
 * mappings being in effect. As long as this requirement is adhered to,
 * it should be possible to change the mappings into something else without
 * having to modify any other part of Chessosis.
 * 
 * @author Henrik Lindberg
 */
public enum Square {

    // Each enum constant has as its single property a type long value.
    // The "L" at the end of the constants indicates that it is a type
    // long constant and the prefix "0x" that it is expressed in hexadecimal.
    A1( 0x0000000000000001L ),
    B1( 0x0000000000000002L ),
    C1( 0x0000000000000004L ),
    D1( 0x0000000000000008L ),
    E1( 0x0000000000000010L ),
    F1( 0x0000000000000020L ),
    G1( 0x0000000000000040L ),
    H1( 0x0000000000000080L ),
    A2( 0x0000000000000100L ),
    B2( 0x0000000000000200L ),
    C2( 0x0000000000000400L ),
    D2( 0x0000000000000800L ),
    E2( 0x0000000000001000L ),
    F2( 0x0000000000002000L ),
    G2( 0x0000000000004000L ),
    H2( 0x0000000000008000L ),
    A3( 0x0000000000010000L ),
    B3( 0x0000000000020000L ),
    C3( 0x0000000000040000L ),
    D3( 0x0000000000080000L ),
    E3( 0x0000000000100000L ),
    F3( 0x0000000000200000L ),
    G3( 0x0000000000400000L ),
    H3( 0x0000000000800000L ),
    A4( 0x0000000001000000L ),
    B4( 0x0000000002000000L ),
    C4( 0x0000000004000000L ),
    D4( 0x0000000008000000L ),
    E4( 0x0000000010000000L ),
    F4( 0x0000000020000000L ),
    G4( 0x0000000040000000L ),
    H4( 0x0000000080000000L ),
    A5( 0x0000000100000000L ),
    B5( 0x0000000200000000L ),
    C5( 0x0000000400000000L ),
    D5( 0x0000000800000000L ),
    E5( 0x0000001000000000L ),
    F5( 0x0000002000000000L ),
    G5( 0x0000004000000000L ),
    H5( 0x0000008000000000L ),
    A6( 0x0000010000000000L ),
    B6( 0x0000020000000000L ),
    C6( 0x0000040000000000L ),
    D6( 0x0000080000000000L ),
    E6( 0x0000100000000000L ),
    F6( 0x0000200000000000L ),
    G6( 0x0000400000000000L ),
    H6( 0x0000800000000000L ),
    A7( 0x0001000000000000L ),
    B7( 0x0002000000000000L ),
    C7( 0x0004000000000000L ),
    D7( 0x0008000000000000L ),
    E7( 0x0010000000000000L ),
    F7( 0x0020000000000000L ),
    G7( 0x0040000000000000L ),
    H7( 0x0080000000000000L ),
    A8( 0x0100000000000000L ),
    B8( 0x0200000000000000L ),
    C8( 0x0400000000000000L ),
    D8( 0x0800000000000000L ),
    E8( 0x1000000000000000L ),
    F8( 0x2000000000000000L ),
    G8( 0x4000000000000000L ),
    H8( 0x8000000000000000L );

    private final long squareBit;

    // The constructor for an enum type must be package access or private,
    // i.e., it cannot be public or protected. Therefore it falls outside
    // of javadoc.
    private Square( long squareBit ) {
        this.squareBit = squareBit;
    }

    /**
     * Accessor method for the enum constants.
     * 
     * @return The enum constant's bitboard property.
     */
    public long getSquareBit() {
        return this.squareBit;
    }
}