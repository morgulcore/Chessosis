package chessosisnbproject.data;

import chessosisnbproject.logic.Position;
import chessosisnbproject.logic.SUM;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * JUnit tests for class Move.
 *
 * @author Henrik Lindberg
 */
public class MoveTest {

    private Move moveObj;

    @Before
    public void setUp() {
        Square fromSquare = Square.valueOf( "E2" ),
            toSquare = Square.valueOf( "E4" );
        Position standardPos = new Position();
        moveObj = new Move( fromSquare, toSquare, standardPos );
    }

    @After
    public void tearDown() {
        moveObj = null;
    }

    @Test
    public void sourceSquareOK() {
        assertEquals( Square.E2, moveObj.from() );
    }

    @Test
    public void destSquareOK() {
        assertEquals( Square.E4, moveObj.to() );
    }

    @Test
    public void contextPosOK() {
        assertEquals( new Position(), moveObj.context() );
    }

    @Test
    public void toStringOK() {
        assertEquals(
            "1." + moveObj.from() + "-" + moveObj.to(), moveObj.toString() );
    }

    /**
     * First test of overridden equals().
     * "Symmetry: For two references, a and b, a.equals(b) if and only
     * if b.equals(a)."
     * http://www.ibm.com/developerworks/library/j-jtp05273/
     */
    @Test
    public void equalsSymmetryTest() {
        Move rmo = randomMoveObject();
        Move rmoCopy = new Move( rmo.from(), rmo.to(), null );

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
            rmo2 = new Move( rmo1.from(), rmo1.to(), null );
            rmo3 = new Move( rmo2.from(), rmo2.to(), null );
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

        return new Move( fromSquare, toSquare, null );
    }
}
