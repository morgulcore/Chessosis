package chessosisnbproject.chessosisnbproject;

import java.util.EnumSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for class CSS.
 *
 * @author Henrik Lindberg
 */
public class CSSTest {

    /**
     * Checks that the file (chessboard column) is valid.
     */
    @Test
    public void validFileA() {
        assertEquals(0x0101010101010101L, CSS.FILE_A );
    }

    /**
     * Checks that the file (chessboard column) is valid.
     */
    @Test
    public void validFileB() {
        assertEquals(0x0202020202020202L, CSS.FILE_B );
    }

    /**
     * Checks that the file (chessboard column) is valid.
     */
    @Test
    public void validFileC() {
        assertEquals(0x0404040404040404L, CSS.FILE_C );
    }

    /**
     * Checks that the file (chessboard column) is valid.
     */
    @Test
    public void validFileD() {
        assertEquals(0x0808080808080808L, CSS.FILE_D );
    }

    /**
     * Checks that the file (chessboard column) is valid.
     */
    @Test
    public void validFileE() {
        assertEquals(0x1010101010101010L, CSS.FILE_E );
    }

    /**
     * Checks that the file (chessboard column) is valid.
     */
    @Test
    public void validFileF() {
        assertEquals(0x2020202020202020L, CSS.FILE_F );
    }

    /**
     * Checks that the file (chessboard column) is valid.
     */
    @Test
    public void validFileG() {
        assertEquals(0x4040404040404040L, CSS.FILE_G );
    }

    /**
     * Checks that the file (chessboard column) is valid.
     */
    @Test
    public void validFileH() {
        assertEquals(0x8080808080808080L, CSS.FILE_H );
    }

    /**
     * Verifies that the rank is valid.
     */
    @Test
    public void validRank1() {
        long expectedRank = squareRangeBitwiseORed( Square.A1, Square.H1 );

        // In the first assertEquals the OR'ed values of the squareBit
        // properties of Square.A1 to Square.H1 (inclusive) are compared
        // to the constant. The second test uses a long constant as the
        // expected value.
        assertEquals(expectedRank, CSS.RANK_1 );
        assertEquals(0x00000000000000ffL, CSS.RANK_1 );
    }

    /**
     * Verifies that the rank is valid.
     */
    @Test
    public void validRank2() {
        long expectedRank = squareRangeBitwiseORed( Square.A2, Square.H2 );

        assertEquals(expectedRank, CSS.RANK_2 );
        assertEquals(0x000000000000ff00L, CSS.RANK_2 );
    }

    /**
     * Verifies that the rank is valid.
     */
    @Test
    public void validRank3() {
        long expectedRank = squareRangeBitwiseORed( Square.A3, Square.H3 );

        assertEquals(expectedRank, CSS.RANK_3 );
        assertEquals(0x0000000000ff0000L, CSS.RANK_3 );
    }

    /**
     * Verifies that the rank is valid.
     */
    @Test
    public void validRank4() {
        long expectedRank = squareRangeBitwiseORed( Square.A4, Square.H4 );

        assertEquals(expectedRank, CSS.RANK_4 );
        assertEquals(0x00000000ff000000L, CSS.RANK_4 );
    }

    /**
     * Verifies that the rank is valid.
     */
    @Test
    public void validRank5() {
        long expectedRank = squareRangeBitwiseORed( Square.A5, Square.H5 );

        assertEquals(expectedRank, CSS.RANK_5 );
        assertEquals(0x000000ff00000000L, CSS.RANK_5 );
    }

    /**
     * Verifies that the rank is valid.
     */
    @Test
    public void validRank6() {
        long expectedRank = squareRangeBitwiseORed( Square.A6, Square.H6 );

        assertEquals(expectedRank, CSS.RANK_6 );
        assertEquals(0x0000ff0000000000L, CSS.RANK_6 );
    }

    /**
     * Verifies that the rank is valid.
     */
    @Test
    public void validRank7() {
        long expectedRank = squareRangeBitwiseORed( Square.A7, Square.H7 );

        assertEquals(expectedRank, CSS.RANK_7 );
        assertEquals(0x00ff000000000000L, CSS.RANK_7 );
    }

    /**
     * Verifies that the rank is valid.
     */
    @Test
    public void validRank8() {
        long expectedRank = squareRangeBitwiseORed( Square.A8, Square.H8 );

        assertEquals(expectedRank, CSS.RANK_8 );
        assertEquals(0xff00000000000000L, CSS.RANK_8 );
    }

    // Private helper method used in the validRank* tests.
    private long squareRangeBitwiseORed( Square first, Square last ) {
        long expectedRank = 0;

        // The method EnumSet.range returns the range of enum constants
        // specified as its arguments.
        for ( Square square : EnumSet.range( first, last ) ) {
            expectedRank |= square.bit();
        }

        return expectedRank;
    }
}
