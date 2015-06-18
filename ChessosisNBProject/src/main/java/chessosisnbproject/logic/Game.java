package chessosisnbproject.logic;

import chessosisnbproject.data.Move;
import chessosisnbproject.gui.ChessosisGUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The class to represent a chess game. For the time being the class is
 * quite minimalistic. It contains a list of Position objects that represent
 * the game history. The last of these Position objects in the history
 * list is the current position of the game. Adding other game status
 * information fields into class Game would be quite straightforward.
 *
 * @author Henrik Lindberg
 */
public class Game {

    private final List<Position> history;
    private static ChessosisGUI debugMsgRef = null;

    /**
     Create a new Game object with the standard starting position.
     */
    public Game() {
        history = new ArrayList<>();
        history.add( new Position() );
    }

    /**
     Used to set the debug message reference. The reference can be used
     to send messages to the GUI. They appear in the text area below
     the board. Example of using a properly set reference:
     <p>
     ref.sendMessage( "Hello, world!" );
    
     @param ref reference to the GUI
     */
    public static void setDebugMsgRef( ChessosisGUI ref ) {
        debugMsgRef = ref;
    }

    /**
     See the Javadoc of setDebugMsgRef().
    
     @return reference to the GUI
     */
    public static ChessosisGUI getDebugMsgRef() {
        return debugMsgRef;
    }

    /**
     Gets the set of moves (Move objects) associated with the current position
     of the game.
    
     @return set of available moves
     @throws Exception 
     */
    public Set<Move> getMoves() throws Exception {
        return MoveGenerator.moveGenerator( getPos() );
    }

    /**
     Gets the current position of the game (the last Position object stored
     in the history list.
    
     @return current position
     */
    public Position getPos() {
        return history.get( history.size() - 1 );
    }

    /**
     Used to make a new move in the game. Making a move results in a new
     position which is stored in the history list.
    
     @param move the move to make
     @return an indication whether the adding to the list was successful
     @throws Exception 
     */
    public boolean newMove( Move move ) throws Exception {
        Position newPos = Position.makeMove( move );

        return history.add( newPos );
    }
}
