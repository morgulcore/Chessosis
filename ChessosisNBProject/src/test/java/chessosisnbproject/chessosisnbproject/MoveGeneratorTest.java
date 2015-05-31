package chessosisnbproject.chessosisnbproject;

import java.util.EnumSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for class MoveGenerator.
 *
 * @author Henrik Lindberg
 */
public class MoveGeneratorTest {

    /**
     * Testing surroundingSquares() with each of its 64 possible Square
     * parameter values. The test works by using an alternate way to
     * generate the surrounding squares (SS's) of a particular square.
     * So basically, for each square two SS EnumSets are created. They
     * are then compared for equility with assertEquals().
     * <p>
     * Although this test is comprehensive (all possible inputs are used)
     * it doesn't really prove that surroundingSquares() produces correct
     * output. As unlikely as it may be, it's conceivable that both
     * surroundingSquares() and the alternate implementation produce
     * exactly the same INVALID results. Well, no one's life depends
     * on surroundingSquares()...
     *
     * @throws Exception
     */
    @Test
    public void surroundingSquaresComprehensiveInputTest() throws Exception {
        EnumSet<Square> actualSquareSet, expectedSquareSet;

        for ( Square square : Square.values() ) {
            // Get the square set proper
            actualSquareSet = MoveGenerator.surroundingSquares( square );
            // Get the comparison square set created by different means
            // altogether
            expectedSquareSet = generateComparisonEnumSetForSSTest(
                generateSurroundingSquareMatrix( square.toString() ) );
            assertEquals( expectedSquareSet, actualSquareSet );
        }
    }

    /**
     * surroundingSquares() should not return null with any of its
     * 64 possible Square parameter values.
     * @throws java.lang.Exception
     */
    @Test
    public void surroundingSquaresNeverReturnsNull() throws Exception {
        for ( Square square : Square.values() ) {
            if ( MoveGenerator.surroundingSquares( square ) == null ) {
                fail();
            }
        }
    }

    /**
     * Verifies that the number of surrounding squares is correct. Corner
     * squares have three SS's, non-corner squares on the edge of the board
     * have five SS's and all other squares have eight SS's. Each of the
     * 64 Square enum constants are used in this test as the argument to
     * surroundingSquares().
     *
     * @throws Exception
     */
    @Test
    public void sizeOfEnumSetsReturnedBySurroundingSquaresCorrect()
        throws Exception {
        EnumSet<Square> squareSet;
        for ( Square square : Square.values() ) {
            squareSet = MoveGenerator.surroundingSquares( square );
            // Corner square case
            if ( ( square.bit() & CSS.CORNER_SQUARES ) != 0 ) {
                // A corner square has three surrounding squares
                assertEquals( 3, squareSet.size() );
            } // Square is not a corner, but still is on the edge of the board
            else if ( ( square.bit() & CSS.EDGE ) != 0 ) {
                // Non-corner edge squares have five surrounding squares
                assertEquals( 5, squareSet.size() );
            } // Square is a non-edge square so it has eight surrounding squares
            else {
                assertEquals( 8, squareSet.size() );
            }
        }
    }

    //
    // ============================
    // == Private helper methods ==
    // ============================
    //
    //
    // Square name validity check. A square name is a two-character string
    // such as "A1" or "E4".
    private static boolean validSquareName( String string ) {
        if ( string == null ) {
            return false;
        } else if ( string.length() != 2 ) {
            return false;
        } else if ( string.charAt( 0 ) < 'A' || string.charAt( 0 ) > 'H' ) {
            return false;
        } else if ( string.charAt( 1 ) < '1' || string.charAt( 1 ) > '8' ) {
            return false;
        }
        return true;
    }

    // Creates the surrounding square name matrix of its square name
    // argument. Below are two examples of SSN matrices. As can be observed
    // the matrix can also contain invalid square names. This is ok and happens
    // when the square name parameter is a square on the edge of the board.
    // The invalid square names will later on be weeded out by
    // validSquareName().
    //
    // +--------------+
    // | D5 | E5 | F5 |
    // ---------------+
    // | D4 | E4 | F4 |  Square name argument the one in the center, E4
    // ---------------+
    // | D3 | E3 | F3 |
    // +--------------+
    //
    // +--------------+
    // | G9 | H9 | I9 |
    // ---------------+
    // | G8 | H8 | I8 |  Corner square -- note the five invalid square names
    // ---------------+
    // | G7 | H7 | I7 |
    // +--------------+
    //
    private static String[][] generateSurroundingSquareMatrix(
        String squareName ) {
        // Surrounding square name matrix, a 3 by 3 String array
        String[][] ssnm = new String[ 3 ][ 3 ];
        char file = squareName.charAt( 0 ); // value between 'A' to 'H'
        char rank = squareName.charAt( 1 ); // value between '1' to '8'

        // Rows/ranks
        for ( int i = 0; i < 3; i++ ) {
            // Columns/files
            for ( int j = 0; j < 3; j++ ) {
                // Note the String concatenation
                ssnm[ i ][ j ]
                    = (char) ( file - 1 + j ) + "" + (char) ( rank + 1 - i );
            }
        }

        return ssnm;
    }

    // Creates the comparison EnumSet needed in
    // surroundingSquaresComprehensiveInputTest()
    private static EnumSet<Square> generateComparisonEnumSetForSSTest(
        String[][] ssnm ) {
        // This creates an empty EnumSet
        EnumSet<Square> comparisonSquareSet = EnumSet.noneOf( Square.class );

        // The center square of the matrix shouldn't be included in the
        // set of its surrounding squares
        ssnm[ 1 ][ 1 ] = "removed";

        for ( int i = 0; i < 3; i++ ) {
            for ( int j = 0; j < 3; j++ ) {
                if ( validSquareName( ssnm[ i ][ j ] ) ) {
                    comparisonSquareSet.add( Square.valueOf( ssnm[ i ][ j ] ) );
                }
            }
        }

        return comparisonSquareSet;
    }
}
