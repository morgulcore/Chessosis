package chessosisnbproject.logic;

import chessosisnbproject.data.Square;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for class Move.
 *
 * @author Henrik Lindberg
 */
public class MoveTest {

    /**
     * First test of overridden equals().
     * "Symmetry: For two references, a and b, a.equals(b) if and only
     * if b.equals(a)."
     * http://www.ibm.com/developerworks/library/j-jtp05273/
     */
    @Test
    public void equalsSymmetryTest() {
        Move rmo = randomMoveObject();
        Move rmoCopy = new Move( rmo.from(), rmo.to() );

        assertTrue( rmo.equals( rmoCopy ) && rmoCopy.equals( rmo ) );
    }

    /**
     * Second test of overridden equals().
     * "Reflexivity: For all non-null references, a.equals(a)."
     * http://www.ibm.com/developerworks/library/j-jtp05273/
     */
    @Test
    public void equalsReflexivityTest() {
        for ( int i = 1; i <= 1000; i++ ) {
            Move rmo = randomMoveObject();

            assertTrue( rmo.equals( rmo ) );
        }
    }

    /**
     * Third test of overridden equals().
     * "If a.equals(b) and b.equals(c), then a.equals(c)."
     * http://www.ibm.com/developerworks/library/j-jtp05273/
     */
    @Test
    public void equalsTransitivityTest() {
        Move rmo1, rmo2, rmo3;

        for ( int i = 1; i <= 1000; i++ ) {
            rmo1 = randomMoveObject();
            rmo2 = new Move( rmo1.from(), rmo1.to() );
            rmo3 = new Move( rmo2.from(), rmo2.to() );
            if ( rmo1.equals( rmo2 )
                && rmo2.equals( rmo3 ) && rmo1.equals( rmo3 ) ) {
                continue;
            }
            fail();
        }
    }

    /**
     * Method hashCode() consistency check.
     * "Consistency with hashCode(): Two equal objects must have the same
     * hashCode() value."
     * http://www.ibm.com/developerworks/library/j-jtp05273/
     */
    @Test
    public void equalsConsistencyWithHashCodeTest() {
        Move rmo1, rmo2;
        // Counts the number of times the two Move objects had the same
        // 'from' and 'to' field values.
        int equalsCounter = 0;

        do {
            rmo1 = randomMoveObject();
            rmo2 = randomMoveObject();

            if ( rmo1.equals( rmo2 ) ) {
                equalsCounter++;
                if ( rmo1.hashCode() != rmo2.hashCode() ) {
                    fail();
                }
            }

        } while ( equalsCounter < 10 );
    }

    // ============================
    // == Private helper methods ==
    // ============================
    //
    private static Move randomMoveObject() {
        Square fromSquare, toSquare;
        fromSquare = SUM.randomSquare();
        do {
            toSquare = SUM.randomSquare();
        } while ( toSquare == fromSquare );

        return new Move( fromSquare, toSquare );
    }
}
