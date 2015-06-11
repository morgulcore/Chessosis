package chessosisnbproject.data;

/**
 * Enum type Square makes it possible to treat the 64 squares of the chessboard
 * as a distinct data type. The enum type consists of 64 constants that each
 * have a chessboard square as their name. The naming starts from A1 and ends
 * in H8. Each of the constants has a single square bit property which is
 * initialized using the appropriate value from class CSS. The enum type serves
 * mostly as an alternative to bitboards in representing squares and sets of
 * squares. The choice of which one to use -- bitboards or Squares -- for a
 * particular task is mostly a matter of taste and convenience. Ultimately
 * bitboards and Squares are just two different ways to express the same thing.
 * Also, converting one to another is easy thanks to the static methods in
 * class SUM.
 *
 * @author Henrik Lindberg
 */
public enum Square {

    // Each enum constant has a single square bit property which is
    // initialized with the appropriate constant from class CSS.
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
}
