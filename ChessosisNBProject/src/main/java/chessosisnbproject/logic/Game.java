package chessosisnbproject.logic;

import chessosisnbproject.data.Position;
import chessosisnbproject.data.Color;
import chessosisnbproject.data.CSS;
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

    public Game( /*Position nonStandardStartPos*/) {
        history = new ArrayList<>();
        history.add( testPos1 );
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

    private static Position newPos( Position pos, long fromSB, long toSB )
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
            moveIsCapture = true;
        } // Black captures white piece
        else if ( pos.turn() == Color.BLACK
            && ( toSBPieceIndex >= 0 && toSBPieceIndex <= 5 ) ) {
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

    /*public Chessman chessmanOnSquare( Square square ) {
     return SUM.typeOfChessman( square, testPos1 );
     }*/
    public static final Position testPos1
        = new Position(
            0, 0, 0, CSS.D3, 0, CSS.F3,
            0, 0, 0, CSS.D6, 0, CSS.F6,
            Color.WHITE );
}
