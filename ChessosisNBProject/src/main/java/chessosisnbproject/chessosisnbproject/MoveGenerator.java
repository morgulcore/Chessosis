package chessosisnbproject.chessosisnbproject;

import static chessosisnbproject.chessosisnbproject.ConstantSquareSet.*;

/**
 *
 * @author Henrik Lindberg
 */
public class MoveGenerator {

    public static long surroundingSquares( Square square ) {
        // After processing this string array will always contain
        // eight square strings (e.g., "A1"). If parameter square
        // is on the edge of the board, part of the strings will
        // refer to invalid squares such as "H9".
        String[] fakeAndRealSquareStrings;
        
        Square[] theEightSurroundingSquares
            = { null, null, null, null, null, null, null, null };
        int index = 0; // Used and incremented within the for loops
        char fileOfSquare = square.toString().substring( 0, 1 ).charAt( 0 );
        char rankOfSquare = square.toString().substring( 1 ).charAt( 0 );

        // Example situation with Square.B2:
        // | A3 | B3 | C3 |
        // |--------------|
        // | A2 | B2 | C2 |
        // |--------------|
        // | A1 | B1 | C1 |
        // Iterate through three files, for example files A, B and C
        for ( int file = fileOfSquare - 1; file <= fileOfSquare + 1; file++ ) {
            // The two conditions: Square parameter is on the A or H files
            if ( (char) file < 'A' || (char) file > 'H' ) {
                continue;
            }
            // Iterate through three ranks, for example ranks 1, 2 and 3
            for ( int rank = rankOfSquare - 1; rank <= rankOfSquare + 1; rank++ ) {
                // The two conditions: Square parameter is on the ranks 1 or 8
                if ( (char) rank < '1' || (char) rank < '1' ) {
                    continue;
                }
                // Don't add the square itself to the list of
                // surrounding squares
                if ( file == fileOfSquare && rank == rankOfSquare ) {
                    continue;
                }
                String squareString = Character.toString( (char) file )
                    + Character.toString( (char) rank );
                theEightSurroundingSquares[ index ]
                    = Square.valueOf( squareString );
                index++;
            }
        }

        return squareArrayToBitboard( theEightSurroundingSquares );
    }

    private static long squareArrayToBitboard( Square[] squares ) {
        long bitboard = EMPTY_BOARD;
        // Bitwise OR the individual square bitboard together
        for ( int i = 0; i < squares.length; i++ ) {
            if ( squares[ i ] != null ) {
                bitboard |= squares[ i ].getSquareBit();
            }
        }
        return bitboard;
    }
}
