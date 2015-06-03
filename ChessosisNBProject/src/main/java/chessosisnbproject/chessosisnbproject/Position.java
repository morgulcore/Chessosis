package chessosisnbproject.chessosisnbproject;

/**
 * The instances of class Position contain information about positions.
 * The instances contain enough such information that they can be used
 * to produce a FEN string representation of the position. The following
 * is Wikipedia's description of the six fields of a FEN record:
 * <p>
 * http://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation#Definition
 * <p>
 * Instances of class Position should be considered immutable objects.
 * That is, there's no way to modify the instance variables after the creation
 * of an Position object.
 *
 * @author Henrik Lindberg
 */
public class Position {

    // The 12 fundamental bitboards that correspond to the different
    // types of chessmen (chess pieces) on the board.
    private final long whitePawnBB, whiteBishopBB, whiteKnightBB,
        whiteRookBB, whiteQueenBB, whiteKingBB,
        blackPawnBB, blackBishopBB, blackKnightBB,
        blackRookBB, blackQueenBB, blackKingBB;

    // Indicates who's turn it is to move
    private final Color turn;

    // Castling rights
    private final boolean whiteCanCastleKingside, whiteCanCastleQueenside,
        blackCanCastleKingside, blackCanCastleQueenside;

    // "En passant target square in algebraic notation. If there's no
    // en passant target square, this is "-". If a pawn has just made a
    // two-square move, this is the position "behind" the pawn. This is
    // recorded regardless of whether there is a pawn in position to make
    // an en passant capture." --Wikipedia
    private final Square enPassantTargetSquare;

    // "Halfmove clock: This is the number of halfmoves since the last capture
    // or pawn advance. This is used to determine if a draw can be claimed
    // under the fifty-move rule." --Wikipedia
    private final int halfmoveClock;

    // "Fullmove number: The number of the full move. It starts at 1, and is
    // incremented after Black's move." --Wikipedia
    private final int fullmoveNumber;

    public Position( long whitePawnBB, long whiteBishopBB, long whiteKnightBB,
        long whiteRookBB, long whiteQueenBB, long whiteKingBB,
        long blackPawnBB, long blackBishopBB, long blackKnightBB,
        long blackRookBB, long blackQueenBB, long blackKingBB,
        Color turn ) {
        // Initialize white chessmen
        this.whitePawnBB = whitePawnBB;
        this.whiteBishopBB = whiteBishopBB;
        this.whiteKnightBB = whiteKnightBB;
        this.whiteRookBB = whiteRookBB;
        this.whiteQueenBB = whiteQueenBB;
        this.whiteKingBB = whiteKingBB;
        // Initialize black chessmen
        this.blackPawnBB = blackPawnBB;
        this.blackBishopBB = blackBishopBB;
        this.blackKnightBB = blackKnightBB;
        this.blackRookBB = blackRookBB;
        this.blackQueenBB = blackQueenBB;
        this.blackKingBB = blackKingBB;
        // Initialize turn
        this.turn = turn;
        //
        // Initialize the rest of the fields with default values
        //
        this.whiteCanCastleKingside = this.whiteCanCastleQueenside
            = this.blackCanCastleKingside = this.blackCanCastleQueenside
            = false;
        this.enPassantTargetSquare = null;
        this.halfmoveClock = this.fullmoveNumber = 0;
    }

    public long whitePawns() {
        return this.whitePawnBB;
    }

    public long whiteBishops() {
        return this.whiteBishopBB;
    }

    public long whiteKnights() {
        return this.whiteKnightBB;
    }

    public long whiteRooks() {
        return this.whiteRookBB;
    }

    public long whiteQueen() {
        return this.whiteQueenBB;
    }

    public long whiteKing() {
        return this.whiteKingBB;
    }

    public long blackPawns() {
        return this.blackPawnBB;
    }

    public long blackBishops() {
        return this.blackBishopBB;
    }

    public long blackKnights() {
        return this.blackKnightBB;
    }

    public long blackRooks() {
        return this.blackRookBB;
    }

    public long blackQueen() {
        return this.blackQueenBB;
    }

    public long blackKing() {
        return this.blackKingBB;
    }

    /**
     * Used to determine who's turn it is.
     *
     * @return a Color constant, either WHITE or BLACK
     */
    public Color turn() {
        return this.turn;
    }

    /**
     * Returns the white army, i.e., all of White's chessmen.
     *
     * @return a bitboard of all white chessmen
     */
    public long whiteArmy() {
        return this.whitePawnBB | this.whiteBishopBB | this.whiteKnightBB
            | this.whiteRookBB | this.whiteQueenBB | this.whiteKingBB;
    }

    /**
     * Returns the black army, i.e., all of Black's chessmen.
     *
     * @return a bitboard of all black chessmen
     */
    public long blackArmy() {
        return this.blackPawnBB | this.blackBishopBB | this.blackKnightBB
            | this.blackRookBB | this.blackQueenBB | this.blackKingBB;
    }

    /**
     * Returns the union of the white and black armies, i.e., all chessmen
     * on the board.
     *
     * @return all chessmen on the board
     */
    public long bothArmies() {
        return whiteArmy() | blackArmy();
    }
}
