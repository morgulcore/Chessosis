package chessosisnbproject.logic;

import chessosisnbproject.data.CSS;
import chessosisnbproject.data.Colour;
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
        history.add( new Position() ); // Standard starting position

        /*Position testPos = new Position(
         CSS.F2 | CSS.G2 | CSS.H2, 0, CSS.F3, CSS.H1, 0, CSS.E1,
         CSS.F7 | CSS.G7 | CSS.H7, CSS.B5 | CSS.B4, 0, CSS.H8, 0, CSS.E8,
         Colour.WHITE, true, true, true, true, null, 0, 1 );
         history.add( testPos );*/
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
