package chessosisnbproject.logic;

import chessosisnbproject.data.Move;
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
        history.add( new Position() );
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

    public boolean newMove( Move move ) throws Exception {
        Position newPos = Position.makeMove( move );

        return history.add( newPos );
    }
}
