package chessosisnbproject.data;

import org.junit.Test;
import static org.junit.Assert.*;

public class CSSTest {

    /*
     * Testing the CSS.FILE_* constants by bitwise OR'ing them together.
     * The operation should produce a bitboard with all of its bits set.
     */
    @Test
    public void bitwiseORDoneOnFiles() {
        assertEquals( 0xffffffffffffffffL,
            CSS.FILE_A | CSS.FILE_B | CSS.FILE_C | CSS.FILE_D
            | CSS.FILE_E | CSS.FILE_F | CSS.FILE_G | CSS.FILE_H );
    }

    /*
     * Testing the CSS.RANK_* constants by bitwise OR'ing them together.
     * The operation should produce a bitboard with all of its bits set.
     */
    @Test
    public void bitwiseORDoneOnRanks() {
        assertEquals( 0xffffffffffffffffL,
            CSS.RANK_1 | CSS.RANK_2 | CSS.RANK_3 | CSS.RANK_4
            | CSS.RANK_5 | CSS.RANK_6 | CSS.RANK_7 | CSS.RANK_8 );
    }

    /*
     * Testing OMNIBOARD, the bitboard with all of its 64 bits set.
     */
    @Test
    public void allBitsSetInOmniboard() {
        assertEquals( 0xffffffffffffffffL, CSS.OMNIBOARD );
    }

    /*
     * A quick check on ranks().
     */
    @Test
    public void ranksReturnsConsistentArray() {
        long[] ranksArray = CSS.ranks();
        long result = 0;
        // Each of the ranks in the array is bitwise OR'd to result
        for ( int i = 0; i < ranksArray.length; i++ ) {
            result |= ranksArray[ i ];
        }
        // All of the bits should now be set in result
        assertEquals( CSS.OMNIBOARD, result );
    }
}
