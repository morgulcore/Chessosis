package chessosisnbproject.chessosisnbproject;

/**
 *
 * @author Henrik Lindberg
 */
public class ChessosisNBProject {

    public static void main( String[] args ) throws Exception {
        Position testPosition
            = new Position(
                // White king on G2, black king on B7, White's turn to move
                0, 0, 0, CSS.E4, 0, CSS.G2, 0, 0, 0, 0, 0, CSS.B7, Color.WHITE );
        
        System.out.println( MoveGenerator.accessibleRooksSquares( Square.E4, testPosition) );
        
    }
}
