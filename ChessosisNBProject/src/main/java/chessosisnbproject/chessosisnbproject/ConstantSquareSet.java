package chessosisnbproject.chessosisnbproject;

/**
 * Class for globally available constant square set data.
 * <p>
 * The class contains a number of public static final long bitboard declarations
 * that should be used in all parts of Chessosis that require them. Constant
 * square sets refer to things such as the center of the board (square set
 * {E4,E5,D4,D5}) or the ranks and files.
 * <p>
 * The most fundamental square sets of the class are the square constants. They
 * are initialized with the enum Square constants and are used in the
 * initialization of all other constant square sets.
 *
 * @author Henrik Lindberg
 */
public class ConstantSquareSet {

    // The bash command used to generate most of the squares declaration:
    // squares="$( for i in {1..8}; do echo {A..H}"$i"; done | tr ' ' '\n' )"
    // for s in $squares; do echo "${s} = Square.${s}.getSquareBit(),"; done
    //
    /**
     * ___SOME JAVADOC HERE, PLEASE___
     */
    public static final long A1 = Square.A1.getSquareBit(),
        B1 = Square.B1.getSquareBit(),
        C1 = Square.C1.getSquareBit(),
        D1 = Square.D1.getSquareBit(),
        E1 = Square.E1.getSquareBit(),
        F1 = Square.F1.getSquareBit(),
        G1 = Square.G1.getSquareBit(),
        H1 = Square.H1.getSquareBit(),
        A2 = Square.A2.getSquareBit(),
        B2 = Square.B2.getSquareBit(),
        C2 = Square.C2.getSquareBit(),
        D2 = Square.D2.getSquareBit(),
        E2 = Square.E2.getSquareBit(),
        F2 = Square.F2.getSquareBit(),
        G2 = Square.G2.getSquareBit(),
        H2 = Square.H2.getSquareBit(),
        A3 = Square.A3.getSquareBit(),
        B3 = Square.B3.getSquareBit(),
        C3 = Square.C3.getSquareBit(),
        D3 = Square.D3.getSquareBit(),
        E3 = Square.E3.getSquareBit(),
        F3 = Square.F3.getSquareBit(),
        G3 = Square.G3.getSquareBit(),
        H3 = Square.H3.getSquareBit(),
        A4 = Square.A4.getSquareBit(),
        B4 = Square.B4.getSquareBit(),
        C4 = Square.C4.getSquareBit(),
        D4 = Square.D4.getSquareBit(),
        E4 = Square.E4.getSquareBit(),
        F4 = Square.F4.getSquareBit(),
        G4 = Square.G4.getSquareBit(),
        H4 = Square.H4.getSquareBit(),
        A5 = Square.A5.getSquareBit(),
        B5 = Square.B5.getSquareBit(),
        C5 = Square.C5.getSquareBit(),
        D5 = Square.D5.getSquareBit(),
        E5 = Square.E5.getSquareBit(),
        F5 = Square.F5.getSquareBit(),
        G5 = Square.G5.getSquareBit(),
        H5 = Square.H5.getSquareBit(),
        A6 = Square.A6.getSquareBit(),
        B6 = Square.B6.getSquareBit(),
        C6 = Square.C6.getSquareBit(),
        D6 = Square.D6.getSquareBit(),
        E6 = Square.E6.getSquareBit(),
        F6 = Square.F6.getSquareBit(),
        G6 = Square.G6.getSquareBit(),
        H6 = Square.H6.getSquareBit(),
        A7 = Square.A7.getSquareBit(),
        B7 = Square.B7.getSquareBit(),
        C7 = Square.C7.getSquareBit(),
        D7 = Square.D7.getSquareBit(),
        E7 = Square.E7.getSquareBit(),
        F7 = Square.F7.getSquareBit(),
        G7 = Square.G7.getSquareBit(),
        H7 = Square.H7.getSquareBit(),
        A8 = Square.A8.getSquareBit(),
        B8 = Square.B8.getSquareBit(),
        C8 = Square.C8.getSquareBit(),
        D8 = Square.D8.getSquareBit(),
        E8 = Square.E8.getSquareBit(),
        F8 = Square.F8.getSquareBit(),
        G8 = Square.G8.getSquareBit(),
        H8 = Square.H8.getSquareBit();

    /**
     * Files are the columns of the chessboard.
     * <p>
     * For example, file F consists of the squares F1-F8.
     */
    public static final long FILE_A = A1 | A2 | A3 | A4 | A5 | A6 | A7 | A8,
        FILE_B = B1 | B2 | B3 | B4 | B5 | B6 | B7 | B8,
        FILE_C = C1 | C2 | C3 | C4 | C5 | C6 | C7 | C8,
        FILE_D = D1 | D2 | D3 | D4 | D5 | D6 | D7 | D8,
        FILE_E = E1 | E2 | E3 | E4 | E5 | E6 | E7 | E8,
        FILE_F = F1 | F2 | F3 | F4 | F5 | F6 | F7 | F8,
        FILE_G = G1 | G2 | G3 | G4 | G5 | G6 | G7 | G8,
        FILE_H = H1 | H2 | H3 | H4 | H5 | H6 | H7 | H8;

    /**
     * Ranks are the rows of the chessboard.
     * <p>
     * For example, rank 1 (the first rank) equals the set
     * {A1,B1,C1,D1,E1,F1,G1,H1}.
     */
    public static final long RANK_1 = A1 | B1 | C1 | D1 | E1 | F1 | G1 | H1,
        RANK_2 = A2 | B2 | C2 | D2 | E2 | F2 | G2 | H2,
        RANK_3 = A3 | B3 | C3 | D3 | E3 | F3 | G3 | H3,
        RANK_4 = A4 | B4 | C4 | D4 | E4 | F4 | G4 | H4,
        RANK_5 = A5 | B5 | C5 | D5 | E5 | F5 | G5 | H5,
        RANK_6 = A6 | B6 | C6 | D6 | E6 | F6 | G6 | H6,
        RANK_7 = A7 | B7 | C7 | D7 | E7 | F7 | G7 | H7,
        RANK_8 = A8 | B8 | C8 | D8 | E8 | F8 | G8 | H8;

    /**
     * The edge of the board.
     * <p>
     * The edge of the board includes all squares from FILE_A and FILE_H
     * as well as RANK_1 and RANK_8.
     */
    public static final long EDGE = FILE_A | FILE_H | RANK_1 | RANK_8;

    /**
     * The empty board.
     * <p>
     * A board with no bits set (all 64 bits equal to zero).
     */
    public static final long EMPTY_BOARD = 0;

    /**
     * Omniboard, the opposite of the empty board.
     */
    public static final long OMNIBOARD = ~EMPTY_BOARD;
}
