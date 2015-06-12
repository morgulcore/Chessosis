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

    /*public Chessman chessmanOnSquare( Square square ) {
     return SUM.typeOfChessman( square, testPos1 );
     }*/
    public static final Position testPos1
        = new Position(
            0, 0, 0, CSS.D3, 0, CSS.F3,
            0, 0, 0, CSS.D6, 0, CSS.F6,
            Color.WHITE );
}
