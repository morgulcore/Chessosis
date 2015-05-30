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
     * Tests method surroundingSquares with the four corner squares.
     *
     * @throws Exception
     */
    @Test
    public void surroundingSquaresWorksOnCornerSquares() throws Exception {
        assertEquals( CSS.A2 | CSS.B2 | CSS.B1,
            SUM.squareSetToBitboard( MoveGenerator.surroundingSquares( Square.A1 ) ) );
        assertEquals( CSS.A7 | CSS.B7 | CSS.B8,
            SUM.squareSetToBitboard( MoveGenerator.surroundingSquares( Square.A8 ) ) );
        assertEquals( CSS.H2 | CSS.G2 | CSS.G1,
            SUM.squareSetToBitboard( MoveGenerator.surroundingSquares( Square.H1 ) ) );
        assertEquals( CSS.H7 | CSS.G7 | CSS.G8,
            SUM.squareSetToBitboard( MoveGenerator.surroundingSquares( Square.H8 ) ) );
    }

    /**
     * Tests method surroundingSquares with the 36 squares that are not on
     * the edge of the board.
     */
    @Test
    public void surroundingSquaresWorksOnNonEdgeSquares() {
        fail();
    }

    /**
     * surroundingSquares() should not return null with any of its
     * 64 possible Square parameter values.
     */
    @Test
    public void surroundingSquaresNeverReturnsNull() {
        fail();
    }
}
