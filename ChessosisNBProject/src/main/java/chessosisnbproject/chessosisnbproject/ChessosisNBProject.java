package chessosisnbproject.chessosisnbproject;

import java.util.Set;

/**
 *
 * @author Henrik Lindberg
 */
public class ChessosisNBProject {

    public static void main( String[] args ) throws Exception {
        Position testPosition
            = new Position(
                0, 0, 0, CSS.D3, 0, CSS.F3, 0, 0, 0, CSS.D6, 0, CSS.F6, Color.WHITE );
        Set<Move> moves = MoveGenerator.moveGenerator( testPosition );
        for(Move move: moves) {
            System.out.println( move );
        }
    }
}
