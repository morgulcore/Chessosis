package chessosisnbproject.logic;

import chessosisnbproject.data.Move;
import chessosisnbproject.data.Position;
import chessosisnbproject.data.Color;
import chessosisnbproject.data.CSS;
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

    public Game( /*Position nonStandardStartPos*/) {
        history = new ArrayList<>();
        history.add( testPos1 );
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
        // SB, square bit
        long fromSB = move.from().bit(),
            toSB = move.to().bit();

        Position newPos = newPos( getPos(), fromSB, toSB );
        history.add( newPos );

        return true;
    }

    public static Position newPos( Position pos, long fromSB, long toSB )
        throws Exception {
        long[] chessmen = pos.chessmanBBArray();

        int fromSBPieceIndex = pieceIndex( fromSB, pos );

        if ( fromSBPieceIndex < 0 || fromSBPieceIndex > 11 ) {
            throw new Exception( "Invalid value in fromSBPieceIndex: "
                + fromSBPieceIndex );
        }

        boolean moveIsCapture = false;
        int toSBPieceIndex = pieceIndex( toSB, pos );

        // Non-capture move
        if ( toSBPieceIndex == -1 ) {
        } // White captures black piece
        else if ( pos.turn() == Color.WHITE
            && ( toSBPieceIndex >= 6 && toSBPieceIndex <= 11 ) ) {
            //Game.debugMsgRef.sendMessage( "DB: White captured black\n" );
            moveIsCapture = true;
        } // Black captures white piece
        else if ( pos.turn() == Color.BLACK
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
        long fromToBB = fromSB | toSB;
        chessmen[ fromSBPieceIndex ] ^= fromToBB;
        if ( moveIsCapture ) {
            chessmen[ toSBPieceIndex ] ^= toSB;
        }

        Position newPos = new Position(
            chessmen[ Position.WHITE_PAWNS ],
            chessmen[ Position.WHITE_BISHOPS ],
            chessmen[ Position.WHITE_KNIGHTS ],
            chessmen[ Position.WHITE_ROOKS ],
            chessmen[ Position.WHITE_QUEEN ],
            chessmen[ Position.WHITE_KING ],
            chessmen[ Position.BLACK_PAWNS ],
            chessmen[ Position.BLACK_BISHOPS ],
            chessmen[ Position.BLACK_KNIGHTS ],
            chessmen[ Position.BLACK_ROOKS ],
            chessmen[ Position.BLACK_QUEEN ],
            chessmen[ Position.BLACK_KING ],
            ( pos.turn() == Color.WHITE ) ? Color.BLACK : Color.WHITE,
            false, false, false, false,
            null,
            moveIsCapture ? 0 : ( 1 + pos.halfmoveClock() ),
            1 + pos.fullmoveNumber()
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

    public static final Position testPos1
        = new Position(
            0, 0, CSS.B1 | CSS.G1, CSS.A1 | CSS.H1, 0, CSS.E1,
            0, 0, CSS.B8 | CSS.G8, CSS.A8 | CSS.H8, 0, CSS.E8,
            Color.WHITE,
            false, false, false, false,
            null, 0, 1 );
}
