package chessosisnbproject.chessosisnbproject;

import static chessosisnbproject.chessosisnbproject.ConstantSquareSet.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for class MoveGenerator.
 *
 * @author Henrik Lindberg
 */
public class MoveGeneratorTest {

    /**
     * Tests method surroundingSquares with the 36 squares that are not on
     * the edge of the board.
     * <p>
     * The test assumes the bit index layout described in the javadoc at
     * the beginning of Square.java. Let's have a look at the bit indices
     * of the surrounding squares of B2 (bit index 09):
     * <p>
     *     00 01 02 08 10 16 17 18
     * <p>
     * Then we move to C2 (bit index 10):
     * <p>
     *     01 02 03 09 11 17 18 19
     * <p>
     * A simple pattern of adding one to each bit index. The pattern
     * indicates the surrounding squares of a particular square as long
     * as the square in not on the edge of the board. Adding one to each
     * bit index can be achieved conviniently and effectively by left-shifting
     * a bit pattern on a bitboard.
     */
    @Test
    public void surroundingSquaresWorksOnNonEdgeSquares() {
        long bitPattern = A1 | B1 | C1 | A2 | C2 | A3 | B3 | C3;
        // Used in the call to Square.bitIndexToSquareName.
        int bitIndex = 9;

        // Moving on the six ranks (2nd rank to 7th rank)
        for ( int i = 1; i <= 6; i++ ) {
            // Moving on the six files (b-file to g-file)
            for ( int j = 1; j <= 6; j++ ) {
                Square square = bitIndexToSquareEnum( bitIndex );
                assertEquals(
                    bitPattern, MoveGenerator.surroundingSquares( square ) );
                bitPattern <<= 1;
                bitIndex += 1;
            }
            // Moving to the next rank requires two extra left-shifts.
            // For example, moving from G2 to B3 there are the edge
            // squares H2 and A3 in between.
            bitPattern <<= 2;
            bitIndex += 2;
        }
    }

    /**
     * Tests method surroundingSquares with the four corner squares.
     */
    @Test
    public void surroundingSquaresWorksOnCornerSquares() {
        long ssA1 = A2 | B2 | B1, // Surrounding squares of A1, etc.
            ssA8 = A7 | B7 | B8,
            ssH1 = H2 | G2 | G1,
            ssH8 = H7 | G7 | G8;
        // Squares A1, A8, H1, H8, respectively
        int[] bitIndices = { 0, 56, 7, 63 };

        for ( int i = 0; i < bitIndices.length; i++ ) {
            assertEquals( ssA1, MoveGenerator.surroundingSquares(
                bitIndexToSquareEnum( i ) ) ); // A1
        }
    }

    // ============================
    // == Private helper methods ==
    // ============================
    private static Square bitIndexToSquareEnum( int bitIndex ) {
        String squareName = Square.bitIndexToSquareName( bitIndex );
        return Square.valueOf( squareName );
    }
}
