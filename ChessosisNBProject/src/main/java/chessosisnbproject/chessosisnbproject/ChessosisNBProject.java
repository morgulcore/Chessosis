package chessosisnbproject.chessosisnbproject;

/**
 *
 * @author Henrik Lindberg
 */
public class ChessosisNBProject {

    public static void main( String[] args ) {
        System.out.println(
            MoveGenerator.findSurroundingSquares( ConstantSquareSet.E4 ) );
    }
}
