package chessosisnbproject.data;

import java.util.Objects;

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
 * <p>
 * Class Position should be considered "dumb by design". It has no error
 * detection or consistency checking logic in it so it can be initialized
 * with completely meaningless data. This simply means the assumption that
 * the classes (or people) who use Position take responsibility for the
 * consistency of the data they initialize it with.
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
    private final Colour turn;

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

    /**
     The constructor whose parameters describe any chess position completely
     (apart from the rule of draw by threefold repetition). This is the
     recommended constructor to use when setting up a game to start from
     something else than the standard starting position. For the standard
     starting position, use the constructor with the empty parameter list.
    
     @param whitePawnBB six bitboards to express the placement of White's chessmen
     @param whiteBishopBB
     @param whiteKnightBB
     @param whiteRookBB
     @param whiteQueenBB
     @param whiteKingBB this bitboard has always exactly one bit set
     @param blackPawnBB six bitboards to express the placement of Black's chessmen
     @param blackBishopBB
     @param blackKnightBB
     @param blackRookBB
     @param blackQueenBB
     @param blackKingBB this bitboard has always exactly one bit set
     @param turn indicates the active color
     @param whiteCanCastleKingside castling right indicators
     @param whiteCanCastleQueenside
     @param blackCanCastleKingside
     @param blackCanCastleQueenside
     @param enPassantTargetSquare non-null if a pawn has just advanced two squares
     @param halfmoveClock number of halfmoves since last capture or pawn advance
     @param fullmoveNumber starts at 1, is incremented after Black's move
     */
    public Position(
        long whitePawnBB, long whiteBishopBB, long whiteKnightBB,
        long whiteRookBB, long whiteQueenBB, long whiteKingBB,
        long blackPawnBB, long blackBishopBB, long blackKnightBB,
        long blackRookBB, long blackQueenBB, long blackKingBB,
        Colour turn,
        boolean whiteCanCastleKingside, boolean whiteCanCastleQueenside,
        boolean blackCanCastleKingside, boolean blackCanCastleQueenside,
        Square enPassantTargetSquare,
        int halfmoveClock, int fullmoveNumber
    ) {
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
        // Set the active color
        this.turn = turn;
        // Set castling rights
        this.whiteCanCastleKingside = whiteCanCastleKingside;
        this.whiteCanCastleQueenside = whiteCanCastleQueenside;
        this.blackCanCastleKingside = blackCanCastleKingside;
        this.blackCanCastleQueenside = blackCanCastleQueenside;
        // Initialize the three remaining fields of the Position object
        this.enPassantTargetSquare = enPassantTargetSquare;
        this.halfmoveClock = halfmoveClock;
        this.fullmoveNumber = fullmoveNumber;
    }

    /**
     Constructor for creating the standard starting position.
     */
    public Position() {
        this(
            // The 12 bitboards for representing piece placement
            CSS.A2 | CSS.B2 | CSS.C2 | CSS.D2
            | CSS.E2 | CSS.F2 | CSS.G2 | CSS.H2,
            CSS.C1 | CSS.F1,
            CSS.B1 | CSS.G1,
            CSS.A1 | CSS.H1,
            CSS.D1,
            CSS.E1,
            CSS.A7 | CSS.B7 | CSS.C7 | CSS.D7
            | CSS.E7 | CSS.F7 | CSS.G7 | CSS.H7,
            CSS.C8 | CSS.F8,
            CSS.B8 | CSS.G8,
            CSS.A8 | CSS.H8,
            CSS.D8,
            CSS.E8,
            // The rest of the position information
            Colour.WHITE, true, true, true, true, null, 0, 1
        );
    }

    /**
     Use of this constructor is discouraged because it initializes some of
     the position data with "default" values. Only piece placement and active
     color need to be explicitly specified to this constructor. While in some
     situations this is convenient, it will also lead to loss of game
     information if used in serious gameplay.
    
     @param whitePawnBB
     @param whiteBishopBB
     @param whiteKnightBB
     @param whiteRookBB
     @param whiteQueenBB
     @param whiteKingBB
     @param blackPawnBB
     @param blackBishopBB
     @param blackKnightBB
     @param blackRookBB
     @param blackQueenBB
     @param blackKingBB
     @param turn 
     */
    public Position( long whitePawnBB, long whiteBishopBB, long whiteKnightBB,
        long whiteRookBB, long whiteQueenBB, long whiteKingBB,
        long blackPawnBB, long blackBishopBB, long blackKnightBB,
        long blackRookBB, long blackQueenBB, long blackKingBB,
        Colour turn ) {
        // The actual workhorse is the constructor with the complete
        // parameter list. No point in repeating code.
        this(
            whitePawnBB, whiteBishopBB, whiteKnightBB,
            whiteRookBB, whiteQueenBB, whiteKingBB,
            blackPawnBB, blackBishopBB, blackKnightBB,
            blackRookBB, blackQueenBB, blackKingBB,
            turn,
            // The default field values used by this constructor
            false, false, false, false,
            null,
            0, 1
        );
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

    public long whiteQueens() {
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

    public long blackQueens() {
        return this.blackQueenBB;
    }

    public long blackKing() {
        return this.blackKingBB;
    }

    /**
     * Used to determine the active color, i.e., who's turn it is.
     *
     * @return a Color constant, either WHITE or BLACK
     */
    public Colour turn() {
        return this.turn;
    }

    public boolean whiteCanCastleKingside() {
        return this.whiteCanCastleKingside;
    }

    public boolean whiteCanCastleQueenside() {
        return this.whiteCanCastleQueenside;
    }

    public boolean blackCanCastleKingside() {
        return this.blackCanCastleKingside;
    }

    public boolean blackCanCastleQueenside() {
        return this.blackCanCastleQueenside;
    }

    /**
     Accessor method that returns non-null only when an enemy pawn has just
     advanced two squares. Whether or not the pawn can actually be captured
     en passant makes no difference.
    
     @return Square constant or null
     */
    public Square enPassantTargetSquare() {
        return this.enPassantTargetSquare;
    }

    public int halfmoveClock() {
        return this.halfmoveClock;
    }

    public int fullmoveNumber() {
        return this.fullmoveNumber;
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

    /**
     Packs the 12 fundamental bitboards into an array and returns it to
     the caller. Note the 12 int constants for specifying the array indices.
    
     @return a long array containing all 12 BB's that express piece placement
     */
    public long[] chessmanBBArray() {

        long copyOfWhitePawnBB = this.whitePawnBB,
            copyOfWhiteBishopBB = this.whiteBishopBB,
            copyOfWhiteKnightBB = this.whiteKnightBB,
            copyOfWhiteRookBB = this.whiteRookBB,
            copyOfWhiteQueenBB = this.whiteQueenBB,
            copyOfWhiteKingBB = this.whiteKingBB,
            copyOfBlackPawnBB = this.blackPawnBB,
            copyOfBlackBishopBB = this.blackBishopBB,
            copyOfBlackKnightBB = this.blackKnightBB,
            copyOfBlackRookBB = this.blackRookBB,
            copyOfBlackQueenBB = this.blackQueenBB,
            copyOfBlackKingBB = this.blackKingBB;

        return new long[]{ copyOfWhitePawnBB, copyOfWhiteBishopBB,
            copyOfWhiteKnightBB, copyOfWhiteRookBB,
            copyOfWhiteQueenBB, copyOfWhiteKingBB,
            copyOfBlackPawnBB, copyOfBlackBishopBB,
            copyOfBlackKnightBB, copyOfBlackRookBB,
            copyOfBlackQueenBB, copyOfBlackKingBB };
    }

    /**
     Mainly to be used with the array returned by chessmenBBArray().
     */
    public static final int WHITE_PAWNS = 0, WHITE_BISHOPS = 1, WHITE_KNIGHTS = 2,
        WHITE_ROOKS = 3, WHITE_QUEEN = 4, WHITE_KING = 5,
        BLACK_PAWNS = 6, BLACK_BISHOPS = 7, BLACK_KNIGHTS = 8,
        BLACK_ROOKS = 9, BLACK_QUEEN = 10, BLACK_KING = 11;

    /**
     I had this code generated automatically by NetBeans. Overriding hashCode()
     seems to be necessary whenever overriding equals(). This is because it is
     assumed (required) that whenever two objects are equal (according to the
     objects' equals() method), they also have the same hashCode() value.
    
     @return a stash of hash?
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode( this.turn );
        hash = 17 * hash + this.fullmoveNumber;
        return hash;
    }

    /**
     The means for defining when two Position objects are equal. The equality
     I'm interested in is both the fullmoveNumber and turn fields having the
     same value. This is because I'll be adding Position objects into a Set
     during the course of a single chess game. Every time a piece is moved,
     a new Position object is added into the set. Also, every time a piece is
     moved, this.turn gets toggled; every other time a piece gets moved,
     this.fullmoveNumber gets incremented.
    
     @param obj
     @return 
     */
    @Override
    public boolean equals( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Position other = (Position) obj;
        if ( this.turn != other.turn ) {
            return false;
        }
        return this.fullmoveNumber == other.fullmoveNumber;
    }
}
