package chessosisnbproject.chessosisnbproject;

/**
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

    public Color turn() {
        return this.turn;
    }

    public long whiteChessmen() {
        return this.whitePawnBB | this.whiteBishopBB | this.whiteKnightBB
            | this.whiteRookBB | this.whiteQueenBB | this.whiteKingBB;
    }

    public long blackChessmen() {
        return this.blackPawnBB | this.blackBishopBB | this.blackKnightBB
            | this.blackRookBB | this.blackQueenBB | this.blackKingBB;
    }
}
