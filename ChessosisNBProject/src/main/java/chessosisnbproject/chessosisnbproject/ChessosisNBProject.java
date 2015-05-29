package chessosisnbproject.chessosisnbproject;

import java.util.Set;

/**
 *
 * @author Henrik Lindberg
 */
public class ChessosisNBProject {

    public static void main( String[] args ) throws Exception {
        /*
        Set<Long> result = SUM.splitBitboardIntoSetOfSquareBits( CSS.FILE_A );
        System.out.println( "Set size: " + result.size() );
        for( Long bitboard : result ) {
            System.out.print( SUM.squareBitToSquare( bitboard ) + " " );
        }
        System.out.println( "" );
        */
        System.out.println( "Set bits: " + SUM.numberOfSetBits( 255 ) );
    }
}
