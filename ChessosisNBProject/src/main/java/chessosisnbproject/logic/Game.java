package chessosisnbproject.logic;

import chessosisnbproject.data.CSS;
import chessosisnbproject.data.Move;
import chessosisnbproject.data.Position;
import chessosisnbproject.data.Colour;
import chessosisnbproject.data.Piece;
import chessosisnbproject.data.PieceType;
import chessosisnbproject.data.Square;
import chessosisnbproject.gui.ChessosisGUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The class to represent a chess game.
 *
 * @author Henrik Lindberg
 */
public class Game {

    private final List<Position> history;
    private static ChessosisGUI debugMsgRef = null;

    public Game() {
        history = new ArrayList<>();
        //history.add( new Position() ); // Standard starting position

        Position testPos = new Position(
            CSS.F2 | CSS.G2 | CSS.H2, 0, 0, CSS.H1, 0, CSS.E1,
            CSS.F7 | CSS.G7 | CSS.H7, 0, 0, CSS.H8, 0, CSS.E8,
            Colour.WHITE, true, true, true, true, null, 0, 1 );
        history.add( testPos );
    }

    public static void setDebugMsgRef( ChessosisGUI ref ) {
        debugMsgRef = ref;
    }

    public static ChessosisGUI getDebugMsgRef() {
        return debugMsgRef;
    }

    public Set<Move> getMoves() throws Exception {
        return MoveGenerator.moveGenerator( getPos() );
    }

    public Position getPos() {
        return history.get( history.size() - 1 );
    }

    public boolean makeMove( Move move ) throws Exception {
        if ( !getMoves().contains( move ) ) {
            return false;
        }

        Position newPos;
        // Kingside castle
        if ( ( move.from() == Square.E1 && move.to() == Square.G1
            && SUM.resolvePiece( Square.E1, move.context() )
            == Piece.WHITE_KING )
            || ( move.from() == Square.E8 && move.to() == Square.G8
            && SUM.resolvePiece( Square.E8, move.context() )
            == Piece.BLACK_KING ) ) {
            newPos = makeMoveKingsideCastle( move.context() );
        } // Non-castling move
        else {
            newPos = newPos( getPos(), move.from(), move.to() );
        }

        history.add( newPos );
        return true;
    }

    private static Position makeMoveKingsideCastle( Position pos )
        throws Exception {
        long whiteKingSB, whiteRooksBB, blackKingSB, blackRooksBB;
        boolean whiteCanCastleKS, whiteCanCastleQS,
            blackCanCastleKS, blackCanCastleQS;

        if ( pos.turn() == Colour.WHITE ) {
            whiteKingSB = CSS.G1;
            whiteRooksBB = ( pos.whiteRooks() ^ CSS.H1 ) | CSS.F1;
            blackKingSB = pos.blackKing();
            blackRooksBB = pos.blackRooks();
            whiteCanCastleKS = whiteCanCastleQS = false;
            blackCanCastleKS = pos.blackCanCastleKingside();
            blackCanCastleQS = pos.blackCanCastleQueenside();
        } else if ( pos.turn() == Colour.BLACK ) {
            whiteKingSB = pos.whiteKing();
            whiteRooksBB = pos.whiteRooks();
            blackKingSB = CSS.G8;
            blackRooksBB = ( pos.blackRooks() ^ CSS.H8 ) | CSS.F8;
            whiteCanCastleKS = pos.whiteCanCastleKingside();
            whiteCanCastleQS = pos.whiteCanCastleQueenside();
            blackCanCastleKS = blackCanCastleQS = false;
        } else {
            throw new Exception( "Invalid turn value: " + pos.turn() );
        }

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

    public static Position newPos( Position pos, Square fromSq, Square toSq )
        throws Exception {
        long[] pieces = pos.chessmanBBArray();

        int fromSBPieceIndex = pieceIndex( fromSq.bit(), pos );

        if ( fromSBPieceIndex < 0 || fromSBPieceIndex > 11 ) {
            throw new Exception( "Invalid value in fromSBPieceIndex: "
                + fromSBPieceIndex );
        }

        boolean moveIsCapture = false;
        int toSBPieceIndex = pieceIndex( toSq.bit(), pos );

        // Non-capture move
        if ( toSBPieceIndex == -1 ) {
        } // White captures black piece
        else if ( pos.turn() == Colour.WHITE
            && ( toSBPieceIndex >= 6 && toSBPieceIndex <= 11 ) ) {
            //Game.debugMsgRef.sendMessage( "DB: White captured black\n" );
            moveIsCapture = true;
        } // Black captures white piece
        else if ( pos.turn() == Colour.BLACK
            && ( toSBPieceIndex >= 0 && toSBPieceIndex <= 5 ) ) {
            //Game.debugMsgRef.sendMessage( "DB: Black captured white\n" );
            moveIsCapture = true;
        } // Invalid index
        else if ( toSBPieceIndex >= 12 ) {
            throw new Exception( "Invalid value in toSBPieceIndex: "
                + toSBPieceIndex );
        } else { // Did a piece attempt to kill its own kind?
            throw new Exception( "Cannibalism? Turn: " + pos.turn()
                + ", toSBPieceIndex: " + toSBPieceIndex );
        }

        // Make the move
        long fromToBB = fromSq.bit() | toSq.bit();
        pieces[ fromSBPieceIndex ] ^= fromToBB;
        if ( moveIsCapture ) {
            pieces[ toSBPieceIndex ] ^= toSq.bit();
        }

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
            false, false, false, false,
            null,
            moveIsCapture ? 0 : ( 1 + pos.halfmoveClock() ),
            ( pos.turn() == Colour.BLACK ) ? ( 1 + pos.fullmoveNumber() )
                : ( pos.fullmoveNumber() )
        );

        return newPos;
    }

    private static int pieceIndex( long squareBit, Position pos )
        throws Exception {
        long[] chessmen = pos.chessmanBBArray();
        int pieceIndex = -1;

        for ( int i = 0; i < 12; i++ ) {
            if ( ( squareBit & chessmen[ i ] ) != 0 ) {
                pieceIndex = i;
                break;
            }
        }

        return pieceIndex;
    }
}
