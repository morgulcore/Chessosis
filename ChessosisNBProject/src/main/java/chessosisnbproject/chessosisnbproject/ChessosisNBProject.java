package chessosisnbproject.chessosisnbproject;

/**
 *
 * @author Henrik Lindberg
 */
public class ChessosisNBProject {

    public static void main( String[] args ) {
        System.out.printf( "%x\n", MoveGenerator.surroundingSquares( Square.E4 ) );
    }
}
