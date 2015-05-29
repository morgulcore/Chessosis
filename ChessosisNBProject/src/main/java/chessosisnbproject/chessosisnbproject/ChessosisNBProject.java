package chessosisnbproject.chessosisnbproject;

import java.util.EnumSet;

/**
 *
 * @author Henrik Lindberg
 */
public class ChessosisNBProject {

    public static void main( String[] args ) throws Exception {
        EnumSet<Square> squareSet = SUM.bitboardToSquareSet( 72624976668147840L );
        System.out.println( "Square set size: " + squareSet.size() );
        for(Square square: squareSet) {
            System.out.print( square + " " );
        }
        System.out.println( "" );
    }
}
