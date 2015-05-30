package chessosisnbproject.chessosisnbproject;

/**
 *
 * @author Henrik Lindberg
 */
public class ChessosisNBProject {

    public static void main( String[] args ) throws Exception {
        System.out.println( MoveGenerator.surroundingSquares( Square.G8 ) );
    }
}
