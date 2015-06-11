package chessosisnbproject.logic;

import chessosisnbproject.data.Position;
import chessosisnbproject.data.Chessman;
import chessosisnbproject.data.Color;
import chessosisnbproject.data.CSS;
import chessosisnbproject.data.Square;

/**
 * The class to represent a chess game.
 *
 * @author Henrik Lindberg
 */
public class Game {

    private Position currentPosition
        = new Position(
            0, 0, 0, CSS.D3, 0, CSS.F3, 0, 0, 0, CSS.D6, 0, CSS.F6, Color.WHITE );

    public Position currentPosition() {
        return currentPosition;
    }

    public Chessman chessmanOnSquare( Square square ) {
        return SUM.typeOfChessman( square, currentPosition );
    }
}
