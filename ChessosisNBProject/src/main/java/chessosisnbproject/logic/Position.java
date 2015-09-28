package chessosisnbproject.logic;

import chessosisnbproject.data.CSS;
import chessosisnbproject.data.Colour;
import chessosisnbproject.data.Move;
import chessosisnbproject.data.Piece;
import chessosisnbproject.data.Square;
import java.util.Objects;
import java.util.Random;

/**
 * The instances of class Position contain information about positions. The
 * instances contain enough such information that they can be used to produce a
 * FEN string representation of the position. The following is Wikipedia's
 * description of the six fields of a FEN record:
 * <p>
 * http://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation#Definition
 * <p>
 * Instances of class Position should be considered immutable objects. That is,
 * there's no way to modify the instance variables after the creation of an
 * Position object.
 * <p>
 * Class Position should be considered "dumb by design". It has no error
 * detection or consistency checking logic in it so it can be initialized with
 * completely meaningless data. This simply means the assumption that the
 * classes (or people) who use Position take responsibility for the consistency
 * of the data they initialize it with.
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
     * The constructor whose parameters describe any chess position completely
     * (apart from the rule of draw by threefold repetition). This is the
     * recommended constructor to use when setting up a game to start from
     * something else than the standard starting position. For the standard
     * starting position, use the constructor with the empty parameter list.
     *
     * @param whitePawnBB six bitboards to express the placement of White's
     * chessmen
     * @param whiteBishopBB
     * @param whiteKnightBB
     * @param whiteRookBB
     * @param whiteQueenBB
     * @param whiteKingBB this bitboard has always exactly one bit set
     * @param blackPawnBB six bitboards to express the placement of Black's
     * chessmen
     * @param blackBishopBB
     * @param blackKnightBB
     * @param blackRookBB
     * @param blackQueenBB
     * @param blackKingBB this bitboard has always exactly one bit set
     * @param turn indicates the active color
     * @param whiteCanCastleKingside castling right indicators
     * @param whiteCanCastleQueenside
     * @param blackCanCastleKingside
     * @param blackCanCastleQueenside
     * @param enPassantTargetSquare non-null if a pawn has just advanced two
     * squares
     * @param halfmoveClock number of halfmoves since last capture or pawn
     * advance
     * @param fullmoveNumber starts at 1, is incremented after Black's move
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
     * Constructor for creating the standard starting position.
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
     * Use of this constructor is discouraged because it initializes some of the
     * position data with "default" values. Only piece placement and active
     * color need to be explicitly specified to this constructor. While in some
     * situations this is convenient, it will also lead to loss of game
     * information if used in serious gameplay.
     *
     * @param whitePawnBB
     * @param whiteBishopBB
     * @param whiteKnightBB
     * @param whiteRookBB
     * @param whiteQueenBB
     * @param whiteKingBB
     * @param blackPawnBB
     * @param blackBishopBB
     * @param blackKnightBB
     * @param blackRookBB
     * @param blackQueenBB
     * @param blackKingBB
     * @param turn
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

    /*
     A randomizing constructor needed for testing purposes. The first parameter
     is an existing Position object. The object created by the constructor
     will be the same as the first parameter except for the randomized fields.
     The 20 boolean parameters are used to specify which fields are to be
     randomized. Note that in most cases randomizing a field of a Position
     object is likely to produce inconsistent data. In other words, this
     constructor doesn't produce a random chess position but rather an object
     with junk contents.

     JUNIT TEST: randomizingPositionConstructorTest()
     */
    public Position(
        Position pos,
        boolean randomWhitePawnBB,
        boolean randomWhiteBishopBB,
        boolean randomWhiteKnightBB,
        boolean randomWhiteRookBB,
        boolean randomWhiteQueenBB,
        boolean randomWhiteKingBB,
        boolean randomBlackPawnBB,
        boolean randomBlackBishopBB,
        boolean randomBlackKnightBB,
        boolean randomBlackRookBB,
        boolean randomBlackQueenBB,
        boolean randomBlackKingBB,
        boolean randomTurn,
        boolean randomWhiteCanCastleKingside,
        boolean randomWhiteCanCastleQueenside,
        boolean randomBlackCanCastleKingside,
        boolean randomBlackCanCastleQueenside,
        boolean randomEnPassantTargetSquare,
        boolean randomHalfmoveClock,
        boolean randomFullmoveNumber
    ) {
        Random rand = new Random();

        // Initialize white chessmen
        this.whitePawnBB
            = randomWhitePawnBB ? rand.nextLong() : pos.whitePawns();
        this.whiteBishopBB
            = randomWhiteBishopBB ? rand.nextLong() : pos.whiteBishops();
        this.whiteKnightBB
            = randomWhiteKnightBB ? rand.nextLong() : pos.whiteKnights();
        this.whiteRookBB
            = randomWhiteRookBB ? rand.nextLong() : pos.whiteRooks();
        this.whiteQueenBB
            = randomWhiteQueenBB ? rand.nextLong() : pos.whiteQueens();
        this.whiteKingBB
            = randomWhiteKingBB ? rand.nextLong() : pos.whiteKing();
        // Initialize black chessmen
        this.blackPawnBB
            = randomBlackPawnBB ? rand.nextLong() : pos.blackPawns();
        this.blackBishopBB
            = randomBlackBishopBB ? rand.nextLong() : pos.blackBishops();
        this.blackKnightBB
            = randomBlackKnightBB ? rand.nextLong() : pos.blackKnights();
        this.blackRookBB
            = randomBlackRookBB ? rand.nextLong() : pos.blackRooks();
        this.blackQueenBB
            = randomBlackQueenBB ? rand.nextLong() : pos.blackQueens();
        this.blackKingBB
            = randomBlackKingBB ? rand.nextLong() : pos.blackKing();
        // Set the active color
        this.turn = randomTurn
            ? ( rand.nextBoolean() ? Colour.WHITE : Colour.BLACK )
            : pos.turn();
        // Set castling rights
        this.whiteCanCastleKingside
            = randomWhiteCanCastleKingside
                ? rand.nextBoolean() : pos.whiteCanCastleKingside();
        this.whiteCanCastleQueenside
            = randomWhiteCanCastleQueenside
                ? rand.nextBoolean() : pos.whiteCanCastleQueenside();
        this.blackCanCastleKingside
            = randomBlackCanCastleKingside
                ? rand.nextBoolean() : pos.blackCanCastleKingside();
        this.blackCanCastleQueenside
            = randomBlackCanCastleQueenside
                ? rand.nextBoolean() : pos.blackCanCastleQueenside();
        // Initialize the three remaining fields of the Position object.
        // Note that there's a 50 % chance of the en passant target square
        // being null.
        this.enPassantTargetSquare = randomEnPassantTargetSquare
            ? ( rand.nextBoolean() ? null : SUM.randomSquare() )
            : pos.enPassantTargetSquare();
        this.halfmoveClock = randomHalfmoveClock
            ? rand.nextInt() : pos.halfmoveClock();
        this.fullmoveNumber = randomFullmoveNumber
            ? rand.nextInt() : pos.fullmoveNumber();
    }

    /*
     JUNIT TESTS:
     --fENRecordConstructorStdStartPos()
     */
    public Position( String fENRecord ) {
        this();

        int exitStatus = SUM.validateFENRecord( fENRecord );

        SUM.bugtrap( // Abort program in case of invalid FEN record
            exitStatus != 0,
            "Position( String fENRecord )",
            "FEN record validation failed with error code "
            + exitStatus );

        String[] layeredFENRanks = SUM.extractPiecesFromFENRanks( fENRecord );
        String[] stringBasedBBs
            = Position.layeredFENRanksToStringBasedBBs( layeredFENRanks );

        for ( String s : stringBasedBBs ) {
            System.out.println( s );
        }
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
     * Accessor method that returns non-null only when an enemy pawn has just
     * advanced two squares. Whether or not the pawn can actually be captured en
     * passant makes no difference.
     *
     * @return Square constant or null
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
     * Returns the union of the white and black armies, i.e., all chessmen on
     * the board.
     *
     * @return all chessmen on the board
     */
    public long bothArmies() {
        return whiteArmy() | blackArmy();
    }

    /**
     * Packs the 12 fundamental bitboards into an array and returns it to the
     * caller. Note the 12 int constants for specifying the array indices.
     *
     * @return a long array containing all 12 BB's that express piece placement
     */
    public long[] pieceBBArray() {

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
     * Mainly to be used with the array returned by pieceBBArray().
     */
    public static final int WHITE_PAWNS = 0, WHITE_BISHOPS = 1, WHITE_KNIGHTS = 2,
        WHITE_ROOKS = 3, WHITE_QUEEN = 4, WHITE_KING = 5,
        BLACK_PAWNS = 6, BLACK_BISHOPS = 7, BLACK_KNIGHTS = 8,
        BLACK_ROOKS = 9, BLACK_QUEEN = 10, BLACK_KING = 11;

    /**
     * I had this code generated automatically by NetBeans. Overriding
     * hashCode() seems to be necessary whenever overriding equals(). This is
     * because it is assumed (required) that whenever two objects are equal
     * (according to the objects' equals() method), they also have the same
     * hashCode() value.
     *
     * @return a stash of hash?
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode( this.turn );
        hash = 17 * hash + this.fullmoveNumber;
        return hash;
    }

    /*
     The means for defining when two Position objects are equal. The equality
     I'm interested in is both the fullmoveNumber and turn fields having the
     same value. This is because I'll be adding Position objects into a Set
     during the course of a single chess game. Every time a piece is moved,
     a new Position object is added into the set. Also, every time a piece is
     moved, this.turn gets toggled; every other time a piece gets moved,
     this.fullmoveNumber gets incremented.

     JUNIT TESTS:
     --equalsSymmetryTest()
     --equalsReflexivityTest()
     --equalsTransitivityTest()
     --equalsConsistencyWithHashCodeTest()

     On the first four JUnit tests:
     http://www.ibm.com/developerworks/library/j-jtp05273/
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

    /*
     Method deepEquals() could also be called identicalPositionObjectData().
     As the name suggests, it checks each and every field of two Position
     objects (the other being 'this') and returns true if and only if
     they are all identical. Note that the check performed by the overridden
     equals() is much more shallow.

     JUNIT TESTS:
     --deepEqualsReturnsTrue()
     --deepEqualsReturnsFalse1()
     --deepEqualsReturnsFalse2()
     --deepEqualsReturnsFalse3()
     --deepEqualsReturnsFalse4()
     --deepEqualsReturnsFalse5()
     --deepEqualsReturnsFalse6()
     --deepEqualsReturnsFalse7()
     --deepEqualsReturnsFalse8()
     --deepEqualsReturnsFalse9()
     --deepEqualsReturnsFalse10()
     --deepEqualsReturnsFalse11()
     --deepEqualsReturnsFalse12()
     --deepEqualsReturnsFalse13()
     --deepEqualsReturnsFalse14to17()
     --deepEqualsReturnsFalse18()
     --deepEqualsReturnsFalse19()
     --deepEqualsReturnsFalse20()
     */
    public boolean deepEquals( Object obj ) {
        // First the overridden equals() method is called
        if ( !this.equals( obj ) ) {
            // The 'this' object cannot be identical to 'obj' if
            // equals returns false
            return false;
        }
        // It's now established that obj is a Position object
        final Position otherPos = (Position) obj;

        // Compare the bitboards of the white pieces
        if ( this.whitePawns() != otherPos.whitePawns()
            || this.whiteBishops() != otherPos.whiteBishops()
            || this.whiteKnights() != otherPos.whiteKnights()
            || this.whiteRooks() != otherPos.whiteRooks()
            || this.whiteQueens() != otherPos.whiteQueens()
            || this.whiteKing() != otherPos.whiteKing() ) {
            return false;
        } // Compare the bitboards of the black pieces
        else if ( this.blackPawns() != otherPos.blackPawns()
            || this.blackBishops() != otherPos.blackBishops()
            || this.blackKnights() != otherPos.blackKnights()
            || this.blackRooks() != otherPos.blackRooks()
            || this.blackQueens() != otherPos.blackQueens()
            || this.blackKing() != otherPos.blackKing() ) {
            return false;
        } // ACTIVE COLOR has already been compared in equals()
        // Compare the castling rights
        else if ( this.whiteCanCastleKingside() != otherPos.whiteCanCastleKingside()
            || this.whiteCanCastleQueenside() != otherPos.whiteCanCastleQueenside()
            || this.blackCanCastleKingside() != otherPos.blackCanCastleKingside()
            || this.blackCanCastleQueenside() != otherPos.blackCanCastleQueenside() ) {
            return false;
        } // Compare the en passant target squares
        else if ( this.enPassantTargetSquare() != otherPos.enPassantTargetSquare() ) {
            return false;
        } // Compare the halfmove clocks
        else if ( this.halfmoveClock() != otherPos.halfmoveClock() ) {
            return false;
        }
        // FULLMOVE NUMBER already compared in equals()

        // As no unequal fields were found and all fields were compared,
        // all the fields of the two objects must be equal
        return true;
    }

    //
    // =============================
    // == Private utility methods ==
    // =============================
    //
    //
    /*
     Converts the layered FEN record ranks returned by
     extractPiecesFromFENRanks() into 12 string-based bitboards. This is
     an intermediate stage in converting a FEN record's piece placement data
     (its first field) into a set of 12 64-bit integers. A string-based
     bitboard indicates empty square sequences with positive integers and
     squares occupied by a piece with a lowercase 'x'. If every square
     were occupied by the type of piece indicated by the 'x', the
     string-based BB would be 64 characters long, its character at index 0
     would correspond to the least significant bit of a Java long-based BB
     and its character at index 63 to the most significant bit (consult
     the board diagram at the beginning of CSS.java to see the bit index
     to square mappings used in Chessosis).

     Here's an example of a string-based BB: There are eight pieces of the
     piece identity (type) in question and they are all to be found on the
     second rank. Here's what the string-based BB would look like:
     "8xxxxxxxx48".

     The method returns an array of 12 strings. The index of a string in the
     array determines the identity of the pieces recorded in it. Here are
     the array index to piece identity mappings:
     [00] white pawns               [01] white bishops
     [02] white knights             [03] white rooks
     [04] white queens              [05] white king
     [06] black pawns               [07] black bishops
     [08] black knights             [09] black rooks
     [10] black queens              [11] black king
     */
    private static String[] layeredFENRanksToStringBasedBBs(
        String[] layeredFENRanks ) {
        // The string array to return
        String[] stringBasedBBs = {
            null, null, null, null, null, null,
            null, null, null, null, null, null
        };

        // A piece identity means the color and type of a piece, for
        // example, a black bishop
        char[] pieceIdentities = {
            'P', 'B', 'N', 'R', 'Q', 'K',
            'p', 'b', 'n', 'r', 'q', 'k'
        };

        for ( int index = 0; index < pieceIdentities.length; ++index ) {
            // Splits a string such as "8/8/8/8/8/8/8/1N4N1" into eight
            // strings: "8", "8", ..., "1N4N1"
            String[] ranksOfCurrentLayer
                = layeredFENRanks[ index ].split( "/" );
            // Combine the ranks back into a single string without the
            // '/' separator and with the first rank at the beginning
            // of the string
            String firstRankFirst
                = ranksOfCurrentLayer[ 7 ] + ranksOfCurrentLayer[ 6 ]
                + ranksOfCurrentLayer[ 5 ] + ranksOfCurrentLayer[ 4 ]
                + ranksOfCurrentLayer[ 3 ] + ranksOfCurrentLayer[ 2 ]
                + ranksOfCurrentLayer[ 1 ] + ranksOfCurrentLayer[ 0 ];

            // Replace the piece identity char with 'x'
            firstRankFirst
                = layeredFENRanksToStringBasedBBsReplacePieceIdentityWithX(
                    firstRankFirst );

            // Add up the individual digits
            firstRankFirst = layeredFENRanksToStringBasedBBsAddUpDigits(
                firstRankFirst );

            int squareCountCheck
                = layeredFENRanksToStringBasedBBsSquareCount( firstRankFirst );

            SUM.bugtrap( // Abort execution is the square count is not 64
                squareCountCheck != 64,
                "layeredFENRanksToStringBasedBBs()",
                "Invalid square count: " + firstRankFirst + ": "
                + squareCountCheck );

            stringBasedBBs[ index ] = firstRankFirst;
        }

        return stringBasedBBs;
    }

    /*
     Helper method for layeredFENRanksToStringBasedBBs(). Replaces the piece
     identity character with a lowercase 'x' so that something like
     "8PPPPPPPP888888" becomes "8xxxxxxxx888888".
     */
    private static String layeredFENRanksToStringBasedBBsReplacePieceIdentityWithX(
        String firstRankFirst ) {
        String modifiedFirstRankFirst = "";

        for ( int index = 0; index < firstRankFirst.length(); ++index ) {
            char currentChar = firstRankFirst.charAt( index );
            // In case the current character is a valid piece identity...
            if ( currentChar == 'P'
                || currentChar == 'B'
                || currentChar == 'N'
                || currentChar == 'R'
                || currentChar == 'Q'
                || currentChar == 'K'
                || currentChar == 'p'
                || currentChar == 'b'
                || currentChar == 'n'
                || currentChar == 'r'
                || currentChar == 'q'
                || currentChar == 'k' ) {
                // ...then append an 'x' to the string to be returned
                modifiedFirstRankFirst += 'x';
            } // Otherwise...
            else {
                // ...append the current character as it is
                modifiedFirstRankFirst += currentChar;
            }
        }

        return modifiedFirstRankFirst;
    }

    /*
     Helper method for layeredFENRanksToStringBasedBBs(). Adds up the digits
     in the string parameter so that something like "1N4N18888888"
     becomes "1N4N57".
     */
    private static String layeredFENRanksToStringBasedBBsAddUpDigits(
        String firstRankfirst ) {
        String updatedFirstRankFirst = "";
        int emptySquareSequenceCounter = 0;

        for ( int index = 0; index < firstRankfirst.length(); ++index ) {
            char currentChar = firstRankfirst.charAt( index );
            if ( currentChar >= '1' && currentChar <= '8' ) {
                // The expression on the right yields the integer value
                // indicated by the digit, e.g., 5 for '5'
                emptySquareSequenceCounter += (int) ( currentChar - '0' );
            } else if ( currentChar == 'x' ) {
                // No point in appending the value of
                // 'emptySquareSequenceCounter' if it equals zero
                updatedFirstRankFirst
                    += ( emptySquareSequenceCounter > 0 )
                        ? emptySquareSequenceCounter : "";
                emptySquareSequenceCounter = 0;
                updatedFirstRankFirst += 'x';
            } else {
                SUM.bugtrap( // An invalid character was detected
                    true,
                    "layeredFENRanksToStringBasedBBsAddUpDigits()",
                    "Invalid character detected: " + currentChar );
            }
        }

        // Append the remaining count of empty squares to the string,
        // if greater than zero
        updatedFirstRankFirst += ( emptySquareSequenceCounter > 0 )
            ? emptySquareSequenceCounter : "";

        return updatedFirstRankFirst;
    }

    /*
     Helper method for layeredFENRanksToStringBasedBBs(). Counts the squares
     of a string-based BB. The count should always be 64. Any other value
     means the string is invalid.
     */
    private static int layeredFENRanksToStringBasedBBsSquareCount(
        String firstRankFirst ) {
        int squareCount = 0;

        // First we count the number of the 'x' characters in the string
        for ( int index = 0; index < firstRankFirst.length(); ++index ) {
            char currentChar = firstRankFirst.charAt( index );
            if ( currentChar == 'x' ) {
                ++squareCount;
            }
        }

        // Then we split up the string with the 'x' characters as
        // separators. This should result in a string array containing
        // empty strings and strings that can be parsed into positive
        // integers.
        String[] intsAndEmptyStrings = firstRankFirst.split( "x" );

        for ( String s : intsAndEmptyStrings ) {
            if ( !s.equals( "" ) ) {
                // Add the integer to 'squareCount'
                squareCount += Integer.parseInt( s );
            }
        }

        return squareCount;
    }

    private static boolean fENCastlingRights(
        String rights, char colorAndSide ) {
        if ( rights.equals( "-" ) ) {
            return false;
        }
        char[] rightsCA = rights.toCharArray();
        for ( int i = 0; i < rightsCA.length; i++ ) {
            if ( rightsCA[ i ] == colorAndSide ) {
                return true;
            }
        }
        return false;
    }

    private static Colour fENActiveColor( String activeColor )
        throws Exception {
        switch ( activeColor ) {
            case "w":
                return Colour.WHITE;
            case "b":
                return Colour.BLACK;
            default:
                throw new Exception(
                    "Invalid activeColor string: " + activeColor );
        }
    }

    private static Position fENToPositionInit( long[] pieces, Colour turn,
        boolean whiteCanCastleKS, boolean whiteCanCastleQS,
        boolean blackCanCastleKS, boolean blackCanCastleQS,
        Square enPassantTSq, int halfmoveCounter, int fullmoveNumber ) {
        return new Position(
            pieces[ Position.WHITE_PAWNS ],
            pieces[ Position.WHITE_BISHOPS ],
            pieces[ Position.WHITE_KNIGHTS ],
            pieces[ Position.WHITE_ROOKS ],
            pieces[ Position.WHITE_QUEEN ],
            pieces[ Position.WHITE_KING ],
            pieces[ Position.BLACK_PAWNS ],
            pieces[ Position.BLACK_BISHOPS ],
            pieces[ Position.BLACK_KNIGHTS ],
            pieces[ Position.BLACK_ROOKS ],
            pieces[ Position.BLACK_QUEEN ],
            pieces[ Position.BLACK_KING ],
            turn,
            whiteCanCastleKS, whiteCanCastleQS,
            blackCanCastleKS, blackCanCastleQS,
            enPassantTSq,
            halfmoveCounter, fullmoveNumber
        );
    }

    private static boolean isKingsideCastlingMove( Move move ) {
        return ( move.from() == Square.E1 && move.to() == Square.G1
            && SUM.resolvePiece( Square.E1, move.context() )
            == Piece.WHITE_KING )
            || ( move.from() == Square.E8 && move.to() == Square.G8
            && SUM.resolvePiece( Square.E8, move.context() )
            == Piece.BLACK_KING );
    }

    private static boolean isQueensideCastlingMove( Move move ) {
        return false;
    }

    private static Position makeKingsideCastlingMove( Position pos )
        throws Exception {
        long whiteKingSB = CSS.G1, // Making a guess: it's White's turn
            whiteRooksBB = ( pos.whiteRooks() ^ CSS.H1 ) | CSS.F1,
            blackKingSB = pos.blackKing(),
            blackRooksBB = pos.blackRooks();
        boolean whiteCanCastleKS = false, // Making the same guess as above
            whiteCanCastleQS = false,
            blackCanCastleKS = pos.blackCanCastleKingside(),
            blackCanCastleQS = pos.blackCanCastleQueenside();

        if ( pos.turn() == Colour.BLACK ) { // If the guess went wrong, redo
            whiteKingSB = pos.whiteKing();  // the initializations
            whiteRooksBB = pos.whiteRooks();
            blackKingSB = CSS.G8;
            blackRooksBB = ( pos.blackRooks() ^ CSS.H8 ) | CSS.F8;
            whiteCanCastleKS = pos.whiteCanCastleKingside();
            whiteCanCastleQS = pos.whiteCanCastleQueenside();
            blackCanCastleKS = blackCanCastleQS = false;
        }

        return createPositionAfterKingsideCastling(
            pos, whiteKingSB, whiteRooksBB, blackKingSB, blackRooksBB,
            whiteCanCastleKS, whiteCanCastleQS,
            blackCanCastleKS, blackCanCastleQS );
    }

    private static Position makeQueensideCastlingMove( Position pos ) {
        return null;
    }

    private static Position createPositionAfterKingsideCastling(
        Position pos,
        long whiteKingSB, long whiteRooksBB, long blackKingSB, long blackRooksBB,
        boolean whiteCanCastleKS, boolean whiteCanCastleQS,
        boolean blackCanCastleKS, boolean blackCanCastleQS ) {
        Position newPos = new Position(
            pos.whitePawns(),
            pos.whiteBishops(),
            pos.whiteKnights(),
            whiteRooksBB,
            pos.whiteQueens(),
            whiteKingSB,
            pos.blackPawns(),
            pos.blackBishops(),
            pos.blackKnights(),
            blackRooksBB,
            pos.blackQueens(),
            blackKingSB,
            ( pos.turn() == Colour.WHITE ) ? Colour.BLACK : Colour.WHITE,
            whiteCanCastleKS, whiteCanCastleQS,
            blackCanCastleKS, blackCanCastleQS,
            null,
            1 + pos.halfmoveClock(),
            ( pos.turn() == Colour.BLACK ) ? ( 1 + pos.fullmoveNumber() )
                : ( pos.fullmoveNumber() )
        );

        return newPos;
    }

    private static Position makeRegularMove( Move move ) throws Exception {
        Position pos = move.context();
        Square from = move.from(), to = move.to();
        long[] pieces = pos.pieceBBArray(); // The 12 piece placement BB's

        // The piece being moved corresponds to one of the 12 bitboards
        // in pieces[]
        int fromSBPieceIndex = resolvePieceIndexOfFROM( move.context(), from );
        // If the move is a capture, then to.bit() corresponds to one out of
        // ten bitboards in pieces[] (any non-king piece). If moving to an
        // empty square, the pieceIndex() call returns -1.
        int toSBPieceIndex = resolvePieceIndexOfTO( move.context(), to );

        // Create a bitboard with exactly two bits set: the 'from' and 'to'
        // square bits
        long fromToBB = from.bit() | to.bit();
        // Unset the 'from' bit and set the 'to' bit using a clever XOR
        // operation. This means moving the piece from one square to another.
        pieces[ fromSBPieceIndex ] ^= fromToBB;
        if ( moveIsCapture( pos.turn(), toSBPieceIndex ) ) {
            // Unset the bit of the captured piece, i.e., remove it from
            // its bitboard
            pieces[ toSBPieceIndex ] ^= to.bit(); // 1 XOR 1 equals 0
        }

        return createPositionAfterRegularMove(
            pos, pieces, moveIsCapture( pos.turn(), toSBPieceIndex ) );
    }

    private static int resolvePieceIndexOfFROM( Position pos, Square from )
        throws Exception {
        int fromSBPieceIndex = pieceIndex( from.bit(), pos );
        if ( fromSBPieceIndex < 0 || fromSBPieceIndex > 11 ) { // Serious error
            throw new Exception( "Invalid value in fromSBPieceIndex: "
                + fromSBPieceIndex );
        }
        return fromSBPieceIndex;
    }

    private static int resolvePieceIndexOfTO( Position pos, Square to )
        throws Exception {
        int toSBPieceIndex = pieceIndex( to.bit(), pos );
        if ( toSBPieceIndex == Position.WHITE_KING
            || toSBPieceIndex == Position.BLACK_KING ) { // Serious error
            throw new Exception( "King about to be captured: toSBPieceIndex: "
                + toSBPieceIndex );
        }
        return toSBPieceIndex;
    }

    private static boolean moveIsCapture( Colour turn, int toSBPieceIndex )
        throws Exception {
        boolean moveIsCapture = false; // Guess: toSBPieceIndex == -1

        // Non-capture move
        if ( toSBPieceIndex == -1 ) {
            // Do nothing
        } // White captures black piece
        else if ( turn == Colour.WHITE
            && ( toSBPieceIndex >= Position.BLACK_PAWNS
            && toSBPieceIndex <= Position.BLACK_KING ) ) {
            //Game.debugMsgRef.sendMessage( "DB: White captured black\n" );
            moveIsCapture = true;
        } // Black captures white piece
        else if ( turn == Colour.BLACK
            && ( toSBPieceIndex >= Position.WHITE_PAWNS
            && toSBPieceIndex <= Position.WHITE_KING ) ) {
            //Game.debugMsgRef.sendMessage( "DB: Black captured white\n" );
            moveIsCapture = true;
        } // Invalid index
        else if ( toSBPieceIndex > Position.BLACK_KING ) {
            throw new Exception( "Invalid value in toSBPieceIndex: "
                + toSBPieceIndex );
        } else { // Did a piece attempt to kill its own kind?
            throw new Exception( "Cannibalism? Turn: " + turn
                + ", toSBPieceIndex: " + toSBPieceIndex );
        }

        return moveIsCapture;
    }

    private static Position createPositionAfterRegularMove(
        Position pos, long[] pieces, boolean moveIsCapture ) {
        Position newPos = new Position(
            pieces[ Position.WHITE_PAWNS ],
            pieces[ Position.WHITE_BISHOPS ],
            pieces[ Position.WHITE_KNIGHTS ],
            pieces[ Position.WHITE_ROOKS ],
            pieces[ Position.WHITE_QUEEN ],
            pieces[ Position.WHITE_KING ],
            pieces[ Position.BLACK_PAWNS ],
            pieces[ Position.BLACK_BISHOPS ],
            pieces[ Position.BLACK_KNIGHTS ],
            pieces[ Position.BLACK_ROOKS ],
            pieces[ Position.BLACK_QUEEN ],
            pieces[ Position.BLACK_KING ],
            ( pos.turn() == Colour.WHITE ) ? Colour.BLACK : Colour.WHITE,
            pos.whiteCanCastleKingside(), pos.whiteCanCastleQueenside(),
            pos.blackCanCastleKingside(), pos.blackCanCastleQueenside(),
            null,
            moveIsCapture ? 0 : ( 1 + pos.halfmoveClock() ),
            ( pos.turn() == Colour.BLACK ) ? ( 1 + pos.fullmoveNumber() )
                : ( pos.fullmoveNumber() )
        );
        return newPos;
    }

    // Each of the 12 distinct pieces correspond to an index in the array
    // returned by pieceBBArray(). The pieceIndex() method is used to look up
    // the index of the bitboard that matches the squareBit parameter. For
    // example, if in Position somePos there's a white rook on a1, then the
    // call pieceIndex( CSS.A1, somePos ) would return 3.
    //
    // The value returned is between -1 to 11, inclusive. The value -1
    // indicates that the squareBit parameter corresponds to an empty square.
    private static int pieceIndex( long squareBit, Position pos )
        throws Exception {
        long[] pieces = pos.pieceBBArray();
        int pieceIndex = -1;

        for ( int i = 0; i < 12; i++ ) {
            if ( ( squareBit & pieces[ i ] ) != 0 ) {
                pieceIndex = i;
                break;
            }
        }

        return pieceIndex;
    }

    //
    // ===========================
    // == Public static methods ==
    // ===========================
    //
    //
    /**
     * Converts a FEN string into a Position object. FEN strings and Position
     * objects are interchangable in the sense that they both contain the same
     * information. Initializing new Position objects from FEN strings can be
     * convenient. The reason why I created the FEN string to Position object
     * mechanism was to facilitate testing of class MoveGenerator.
     * <p>
     * For now the conversion mechanism takes the form of this static method.
     * However, a more natural approach would probably be a Position constructor
     * that would accept as its parameter a single FEN string.
     * <p>
     * More on FEN strings:
     * <p>
     * https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
     *
     * @param fENRecord a FEN string
     * @return the Position object created
     * @throws Exception
     */
    public static Position fENToPosition( String fENRecord ) throws Exception {
        String[] fENFields = SUM.splitFENRecord( fENRecord );
        String[] fENRanks = SUM.splitFirstFENField( fENRecord );

        long[] pieces = SUM.fENRanksToBBArray( fENRanks );

        Colour turn = fENActiveColor( fENFields[ 1 ] );

        boolean whiteCanCastleKS = fENCastlingRights( fENFields[ 2 ], 'K' ),
            whiteCanCastleQS = fENCastlingRights( fENFields[ 2 ], 'Q' ),
            blackCanCastleKS = fENCastlingRights( fENFields[ 2 ], 'k' ),
            blackCanCastleQS = fENCastlingRights( fENFields[ 2 ], 'q' );

        Square enPassantTSq;
        if ( fENFields[ 3 ].equals( "-" ) ) {
            enPassantTSq = null;
        } else {
            enPassantTSq = Square.valueOf( fENFields[ 3 ].toUpperCase() );
        }

        return fENToPositionInit(
            pieces,
            turn,
            whiteCanCastleKS, whiteCanCastleQS,
            blackCanCastleKS, blackCanCastleQS,
            enPassantTSq, // En passant target square
            Integer.parseInt( fENFields[ 4 ] ),
            Integer.parseInt( fENFields[ 5 ] ) );
    }

    /**
     * The means to make or execute a move. The result of the operation is a new
     * Position object.
     *
     * @param move the move to make
     * @return the position that resulted from making the move in the previous
     * position
     * @throws Exception
     */
    public static Position makeMove( Move move ) throws Exception {
        if ( move.context() == null ) {
            throw new Exception( "Received Move object with null context" );
        }

        // Kingside castling
        if ( isKingsideCastlingMove( move ) ) {
            return makeKingsideCastlingMove( move.context() );
        } // Queenside castling
        else if ( isQueensideCastlingMove( move ) ) {
            return makeQueensideCastlingMove( move.context() );
        } // Non-castling move

        return makeRegularMove( move );
    }
}
