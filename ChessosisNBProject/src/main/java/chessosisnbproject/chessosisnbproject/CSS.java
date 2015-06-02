package chessosisnbproject.chessosisnbproject;

/**
 * Class for globally available Constant Square Set data.
 * <p>
 * The class contains a number of public static final long declarations
 * that should be used in all parts of Chessosis that require them. Constant
 * square sets refer to things such as the center of the board (square set
 * {E4,E5,D4,D5}) or the ranks and files. In class CSS all of the sets
 * are represented using bitboards.
 * <p>
 * The most fundamental square sets of the class are the square bits (bitboards
 * with a single bit set). Their purpose is to create a mapping between
 * a square name and a bit index (bit position) on any bitboard. The square
 * name to bit index mappings used in Chessosis are as follows (index 00
 * corresponds to the least significant bit of a bitboard, index 63 to the
 * most significant bit).
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
 *
 * @author Henrik Lindberg
 */
public class CSS {

    // Square bit, used to map a square name to a bit index.
    public static final long A1 = 0x0000000000000001L,
        B1 = 0x0000000000000002L,
        C1 = 0x0000000000000004L,
        D1 = 0x0000000000000008L,
        E1 = 0x0000000000000010L,
        F1 = 0x0000000000000020L,
        G1 = 0x0000000000000040L,
        H1 = 0x0000000000000080L,
        A2 = 0x0000000000000100L,
        B2 = 0x0000000000000200L,
        C2 = 0x0000000000000400L,
        D2 = 0x0000000000000800L,
        E2 = 0x0000000000001000L,
        F2 = 0x0000000000002000L,
        G2 = 0x0000000000004000L,
        H2 = 0x0000000000008000L,
        A3 = 0x0000000000010000L,
        B3 = 0x0000000000020000L,
        C3 = 0x0000000000040000L,
        D3 = 0x0000000000080000L,
        E3 = 0x0000000000100000L,
        F3 = 0x0000000000200000L,
        G3 = 0x0000000000400000L,
        H3 = 0x0000000000800000L,
        A4 = 0x0000000001000000L,
        B4 = 0x0000000002000000L,
        C4 = 0x0000000004000000L,
        D4 = 0x0000000008000000L,
        E4 = 0x0000000010000000L,
        F4 = 0x0000000020000000L,
        G4 = 0x0000000040000000L,
        H4 = 0x0000000080000000L,
        A5 = 0x0000000100000000L,
        B5 = 0x0000000200000000L,
        C5 = 0x0000000400000000L,
        D5 = 0x0000000800000000L,
        E5 = 0x0000001000000000L,
        F5 = 0x0000002000000000L,
        G5 = 0x0000004000000000L,
        H5 = 0x0000008000000000L,
        A6 = 0x0000010000000000L,
        B6 = 0x0000020000000000L,
        C6 = 0x0000040000000000L,
        D6 = 0x0000080000000000L,
        E6 = 0x0000100000000000L,
        F6 = 0x0000200000000000L,
        G6 = 0x0000400000000000L,
        H6 = 0x0000800000000000L,
        A7 = 0x0001000000000000L,
        B7 = 0x0002000000000000L,
        C7 = 0x0004000000000000L,
        D7 = 0x0008000000000000L,
        E7 = 0x0010000000000000L,
        F7 = 0x0020000000000000L,
        G7 = 0x0040000000000000L,
        H7 = 0x0080000000000000L,
        A8 = 0x0100000000000000L,
        B8 = 0x0200000000000000L,
        C8 = 0x0400000000000000L,
        D8 = 0x0800000000000000L,
        E8 = 0x1000000000000000L,
        F8 = 0x2000000000000000L,
        G8 = 0x4000000000000000L,
        H8 = 0x8000000000000000L; // Long.MIN_VALUE

    /**
     * Files are the columns of the chessboard. For example, file F
     * consists of the squares F1-F8.
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
     * Ranks are the rows of the chessboard. For example, rank 1 (the first
     * rank) equals the set {A1,B1,C1,D1,E1,F1,G1,H1}.
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
     * The ranks packed conveniently as an array. The array returned is just
     * a temporary copy which can be modified as well as discarded.
     *
     * @return array of the eight ranks of the board
     */
    public static long[] ranks() {
        return new long[]{
            RANK_1, RANK_2, RANK_3, RANK_4, RANK_5, RANK_6, RANK_7, RANK_8 };
    }

    /**
     * The edge of the board. It includes all squares from FILE_A and FILE_H
     * as well as RANK_1 and RANK_8.
     */
    public static final long EDGE = FILE_A | FILE_H | RANK_1 | RANK_8;

    /**
     * The empty board, i.e., a board with no bits set (all 64 bits
     * equal to zero).
     */
    public static final long EMPTY_BOARD = 0;

    /**
     * Omniboard, the opposite of the empty board.
     */
    public static final long OMNIBOARD = ~EMPTY_BOARD;

    /**
     * The chessboard has four corner squares.
     */
    public static final long CORNER_SQUARES = A1 | A8 | H1 | H8;
}
