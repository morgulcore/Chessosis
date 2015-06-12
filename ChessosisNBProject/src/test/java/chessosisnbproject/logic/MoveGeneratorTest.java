package chessosisnbproject.logic;

import chessosisnbproject.data.Position;
import chessosisnbproject.data.Color;
import chessosisnbproject.data.CSS;
import chessosisnbproject.data.Square;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
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
     *
     * @throws Exception
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

    /**
     * Tests the size of the set returned by rooksSquares(). The size of this
     * set should be 14 with all Square constant inputs.
     *
     * @throws Exception 
     */
    @Test
    public void sizeOfRooksSquaresSetEqualTo14() throws Exception {
        for ( Square square : Square.values() ) {
            assertEquals( 14, MoveGenerator.rooksSquares( square ).size() );
        }
    }

    /**
     * The set of squares returned by accessibleRooksSquares() must in all
     * cases be a subset of the squares returned by rooksSquares() (provided
     * that the input for the methods is the same square).
     */
    /*
     @Test
     public void accessibleRooksSquaresReturnsSubsetOfRooksSquares() {
     for ( Square square : Square.values() ) {
     long resultOfUnion = MoveGenerator.rooksSquares( square )
     | MoveGenerator.accessibleRooksSquares( square, null );
     }
     }
     */
    //
    // ======================================
    // == Manual tests for moveGenerator() ==
    // ======================================
    //
    //
    /**
     * The first of the manual moveGenerator() tests. The idea in these tests
     * is to first come up with a chess position and manually calculate all
     * available moves in it. The next step is to specify the position to
     * class Position's constructor and the set of available moves as a String
     * array. The set of expected moves is constructed from the array. The
     * expected moves set is then compared to the move set generated by
     * moveGenerator() when called with the test's Position object. The test
     * is successful if the sets match. A successful test proves that
     * moveGenerator() is able to determine all the available moves in the
     * position used in the test. This gives at least a rough idea of the
     * level of reliability of moveGenerator().
     * <p>
     * In the javadoc before each test I express the position to be tested
     * using the standard Forsyth–Edwards Notation or FEN for short. To view
     * (and perhaps play) the test position, use the following web page
     * (or some other suitable tool). Just copy the FEN string from the
     * test's javadoc, go to the page and click the "Paste FEN" button.
     * <p>
     * http://www.chess.com/analysis-board-editor
     * <p>
     * Here's the FEN for the first manual test: 8/8/4k3/8/4K3/8/8/8 b - - 0 1
     *
     * @throws Exception
     */
    @Test
    public void manualMoveGeneratorMethodTest001() throws Exception {
        Position testPosition
            = new Position(
                // Only the kings left on board; Black's turn
                0, 0, 0, 0, 0, CSS.E4, 0, 0, 0, 0, 0, CSS.E6, Color.BLACK );

        String[] expectedMoves = {
            "E6-D7", "E6-E7", "E6-F7", "E6-D6", "E6-F6" };

        manualMoveGeneratorMethodTestWorkhorse( testPosition, expectedMoves );
    }

    /**
     * FEN: 8/8/8/8/8/3k4/8/3K4 w - - 0 1
     *
     * @throws Exception
     */
    @Test
    public void manualMoveGeneratorMethodTest002() throws Exception {
        Position testPosition
            = new Position(
                // White king on D1, black king on D3, White's turn to move
                0, 0, 0, 0, 0, CSS.D1, 0, 0, 0, 0, 0, CSS.D3, Color.WHITE );

        String[] expectedMoves = {
            "D1-C1", "D1-E1" };

        manualMoveGeneratorMethodTestWorkhorse( testPosition, expectedMoves );
    }

    /**
     * FEN: 8/1k6/8/8/8/8/6K1/8 w - - 0 1
     *
     * @throws Exception
     */
    @Test
    public void manualMoveGeneratorMethodTest003() throws Exception {
        Position testPosition
            = new Position(
                // White king on G2, black king on B7, White's turn to move
                0, 0, 0, 0, 0, CSS.G2, 0, 0, 0, 0, 0, CSS.B7, Color.WHITE );

        String[] expectedMoves = {
            "G2-F3", "G2-G3", "G2-H3", "G2-F2",
            "G2-H2", "G2-F1", "G2-G1", "G2-H1" };

        manualMoveGeneratorMethodTestWorkhorse( testPosition, expectedMoves );
    }

    /**
     * FEN: 8/8/3r1k2/8/8/3R1K2/8/8 w - - 0 1
     *
     * @throws java.lang.Exception
     */
    @Test
    public void manualMoveGeneratorMethodTest004() throws Exception {
        Position testPosition
            = new Position(
                0, 0, 0, CSS.D3, 0, CSS.F3, 0, 0, 0, CSS.D6, 0, CSS.F6, Color.WHITE );

        String[] expectedMoves = {
            // White king's moves
            "F3-F4", "F3-G4", "F3-G3", "F3-G2",
            "F3-F2", "F3-E2", "F3-E3", "F3-E4",
            // White rook's moves
            "D3-D4", "D3-D5", "D3-D6",
            "D3-E3", "D3-C3", "D3-B3",
            "D3-A3", "D3-D2", "D3-D1" };

        manualMoveGeneratorMethodTestWorkhorse( testPosition, expectedMoves );
    }

    /**
     * FEN: 1k1K4/7R/8/8/8/8/8/8 b - - 0 1
     *
     * @throws Exception 
     */
    @Test
    public void manualMoveGeneratorMethodTest005() throws Exception {
        Position testPosition
            = new Position(
                0, 0, 0, CSS.H7, 0, CSS.D8, 0, 0, 0, 0, 0, CSS.B8, Color.BLACK );

        String[] expectedMoves = { "B8-A8" }; // Black's only move

        manualMoveGeneratorMethodTestWorkhorse( testPosition, expectedMoves );
    }

    /**
     * FEN: 3r4/8/8/k6q/4K3/8/8/5r2 w - - 0 1
     *
     * @throws Exception 
     */
    @Test
    public void manualMoveGeneratorMethodTest006() throws Exception {
        Position testPosition
            = new Position(
                // 0x800000000000020L: bitboard with square bits
                // CSS.F1 and CSS.D8 set
                0, 0, 0, 0, 0, CSS.E4,
                0, 0, 0, 0x800000000000020L, CSS.H5, CSS.A5,
                Color.WHITE );

        String[] expectedMoves = { "E4-E3" }; // White's only move

        manualMoveGeneratorMethodTestWorkhorse( testPosition, expectedMoves );
    }
    
    //
    // ============================
    // == Private helper methods ==
    // ============================
    //
    //
    // Does the actual work of the manual moveGenerator() tests
    private void manualMoveGeneratorMethodTestWorkhorse(
        Position position, String[] expectedMovesSA ) // SA, string array
        throws Exception {
        Set<Move> actualMoves = MoveGenerator.moveGenerator( position );
        Set<Move> expectedMoves = new HashSet<>();

        // Construct the set of expected moves from the String array
        for ( String expectedMoveString : expectedMovesSA ) {
            // The move strings are of the form "A1-B1" so they are always
            // five characters long. The following tries to avoid (detect)
            // problems caused by a typo.
            if ( expectedMoveString.length() != 5 ) {
                throw new Exception(
                    "expectedMoveString.length(): "
                    + expectedMoveString.length() );
            }
            String fromSquareString = expectedMoveString.substring( 0, 2 );
            String toSquareString = expectedMoveString.substring( 3 );
            expectedMoves.add(
                new Move(
                    Square.valueOf( fromSquareString ),
                    Square.valueOf( toSquareString ) ) );
        }

        assertEquals( expectedMoves, actualMoves );
    }

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