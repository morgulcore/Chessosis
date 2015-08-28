package chessosisnbproject.logic;

import chessosisnbproject.data.CSS;
import chessosisnbproject.data.Colour;
import chessosisnbproject.data.Move;
import chessosisnbproject.data.Piece;
import chessosisnbproject.data.Square;
import java.util.Objects;

/**
 The instances of class Position contain information about positions.
 The instances contain enough such information that they can be used
 to produce a FEN string representation of the position. The following
 is Wikipedia's description of the six fields of a FEN record:
 <p>
 http://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation#Definition
 <p>
 Instances of class Position should be considered immutable objects.
 That is, there's no way to modify the instance variables after the creation
 of an Position object.
 <p>
 Class Position should be considered "dumb by design". It has no error
 detection or consistency checking logic in it so it can be initialized
 with completely meaningless data. This simply means the assumption that
 the classes (or people) who use Position take responsibility for the
 consistency of the data they initialize it with.

 @author Henrik Lindberg
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
     Used to determine the active color, i.e., who's turn it is.

     @return a Color constant, either WHITE or BLACK
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
     Returns the white army, i.e., all of White's chessmen.

     @return a bitboard of all white chessmen
     */
    public long whiteArmy() {
        return this.whitePawnBB | this.whiteBishopBB | this.whiteKnightBB
            | this.whiteRookBB | this.whiteQueenBB | this.whiteKingBB;
    }

    /**
     Returns the black army, i.e., all of Black's chessmen.

     @return a bitboard of all black chessmen
     */
    public long blackArmy() {
        return this.blackPawnBB | this.blackBishopBB | this.blackKnightBB
            | this.blackRookBB | this.blackQueenBB | this.blackKingBB;
    }

    /**
     Returns the union of the white and black armies, i.e., all chessmen
     on the board.

     @return all chessmen on the board
     */
    public long bothArmies() {
        return whiteArmy() | blackArmy();
    }

    /**
     Packs the 12 fundamental bitboards into an array and returns it to
     the caller. Note the 12 int constants for specifying the array indices.

     @return a long array containing all 12 BB's that express piece placement
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
     Mainly to be used with the array returned by pieceBBArray().
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

    /**
     The means to make or execute a move. The result of the operation is a
     new Position object.

     @param move the move to make
     @return the position that resulted from making the move in the previous position
     @throws Exception
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

    /**
     Converts a FEN string into a Position object. FEN strings and Position
     objects are interchangable in the sense that they both contain the same
     information. Initializing new Position objects from FEN strings can be
     convenient. The reason why I created the FEN string to Position object
     mechanism was to facilitate testing of class MoveGenerator.
     <p>
     For now the conversion mechanism takes the form of this static method.
     However, a more natural approach would probably be a Position constructor
     that would accept as its parameter a single FEN string.
     <p>
     More on FEN strings:
     <p>
     https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation

     @param fEN a FEN string
     @return the Position object created
     @throws Exception
     */
    public static Position fENToPosition( String fEN ) throws Exception {
        String[] fENFields = fEN.split( " " );
        if ( fENFields.length != 6 ) { // Serious error
            System.out.println(
                "ERROR: fENToPosition(): fENFields.length == "
                + fENFields.length );
            System.exit( 1 );
        }
        
        String[] fENRanks = fENFields[ 0 ].split( "/" );
        if ( fENRanks.length != 8 ) { // Serious error
            System.out.println(
                "ERROR: fENToPosition(): fENRanks.length == "
                + fENRanks.length );
            System.exit( 1 );
        }

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

    //
    // =============================
    // == Private utility methods ==
    // =============================
    //
    //
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
        return (move.from() == Square.E1 && move.to() == Square.G1
            && SUM.resolvePiece( Square.E1, move.context() )
            == Piece.WHITE_KING)
            || (move.from() == Square.E8 && move.to() == Square.G8
            && SUM.resolvePiece( Square.E8, move.context() )
            == Piece.BLACK_KING);
    }

    private static boolean isQueensideCastlingMove( Move move ) {
        return false;
    }

    private static Position makeKingsideCastlingMove( Position pos )
        throws Exception {
        long whiteKingSB = CSS.G1, // Making a guess: it's White's turn
            whiteRooksBB = (pos.whiteRooks() ^ CSS.H1) | CSS.F1,
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
            blackRooksBB = (pos.blackRooks() ^ CSS.H8) | CSS.F8;
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
            (pos.turn() == Colour.WHITE) ? Colour.BLACK : Colour.WHITE,
            whiteCanCastleKS, whiteCanCastleQS,
            blackCanCastleKS, blackCanCastleQS,
            null,
            1 + pos.halfmoveClock(),
            (pos.turn() == Colour.BLACK) ? (1 + pos.fullmoveNumber())
                : (pos.fullmoveNumber())
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
            && (toSBPieceIndex >= Position.BLACK_PAWNS
            && toSBPieceIndex <= Position.BLACK_KING) ) {
            //Game.debugMsgRef.sendMessage( "DB: White captured black\n" );
            moveIsCapture = true;
        } // Black captures white piece
        else if ( turn == Colour.BLACK
            && (toSBPieceIndex >= Position.WHITE_PAWNS
            && toSBPieceIndex <= Position.WHITE_KING) ) {
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
            (pos.turn() == Colour.WHITE) ? Colour.BLACK : Colour.WHITE,
            pos.whiteCanCastleKingside(), pos.whiteCanCastleQueenside(),
            pos.blackCanCastleKingside(), pos.blackCanCastleQueenside(),
            null,
            moveIsCapture ? 0 : (1 + pos.halfmoveClock()),
            (pos.turn() == Colour.BLACK) ? (1 + pos.fullmoveNumber())
                : (pos.fullmoveNumber())
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
            if ( (squareBit & pieces[ i ]) != 0 ) {
                pieceIndex = i;
                break;
            }
        }

        return pieceIndex;
    }
}
