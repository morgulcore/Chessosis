package chessosisnbproject.data;

import chessosisnbproject.logic.SUM;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Henrik Lindberg
 */
public class PositionTest {

    // Reference for the standard starting position object
    Position standard;

    /**
     * Sets up the test fixture. 
     * (Called before every test case method.)
     */
    @Before
    public void setUp() {
        //System.out.println( "Setting up" );
        // Should set up the standard starting position
        standard = new Position();
    }

    /**
     * Tears down the test fixture. 
     * (Called after every test case method.)
     */
    @After
    public void tearDown() {
        //System.out.println( "Tearing down" );
        standard = null;
    }

    @Test
    public void pawnsOK() {
        assertEquals( 0xff00000000ff00L,
            standard.whitePawns() | standard.blackPawns() );
    }

    @Test
    public void rooksOK() {
        assertEquals( 0x8100000000000081L,
            standard.whiteRooks() | standard.blackRooks() );
    }

    @Test
    public void minorPiecesOK() {
        assertEquals( 0x6600000000000066L,
            standard.whiteKnights() | standard.whiteBishops()
            | standard.blackKnights() | standard.blackBishops()
        );
    }

    @Test
    public void queensOK() {
        assertEquals( 0x800000000000008L,
            standard.whiteQueens() | standard.blackQueens() );
    }

    @Test
    public void kingsOK() {
        assertEquals( 0x1000000000000010L,
            standard.whiteKing() | standard.blackKing() );
    }

    @Test
    public void activeColorOK() {
        assertEquals( Color.WHITE, standard.turn() );
    }

    @Test
    public void castlingRightsOK() {
        assertEquals( false,
            !standard.whiteCanCastleKingside()
            | !standard.whiteCanCastleQueenside()
            | !standard.blackCanCastleKingside()
            | !standard.blackCanCastleQueenside() );
    }

    @Test
    public void bothArmiesMightBeOK() {
        assertEquals( 0xffff00000000ffffL, standard.bothArmies() );
    }

    /*
     * First test of overridden equals().
     * "Symmetry: For two references, a and b, a.equals(b) if and only
     * if b.equals(a)."
     * http://www.ibm.com/developerworks/library/j-jtp05273/
     */
    @Test
    public void equalsSymmetryTest() {
        Position rpo = randomPositionObject( false, -1, null );

        Position rpoCopy = randomPositionObject(
            true, rpo.fullmoveNumber(), rpo.turn() );

        assertTrue( rpo.equals( rpoCopy ) && rpoCopy.equals( rpo ) );
    }

    /*
     * Second test of overridden equals().
     * "Reflexivity: For all non-null references, a.equals(a)."
     * http://www.ibm.com/developerworks/library/j-jtp05273/
     */
    @Test
    public void equalsReflexivityTest() {
        for ( int i = 1; i <= 1000; i++ ) {
            Position rpo = randomPositionObject( false, -1, null );

            assertTrue( rpo.equals( rpo ) );
        }
    }

    /*
     * Third test of overridden equals().
     * "If a.equals(b) and b.equals(c), then a.equals(c)."
     * http://www.ibm.com/developerworks/library/j-jtp05273/
     */
    @Test
    public void equalsTransitivityTest() {
        Position rpo1, rpo2, rpo3;

        for ( int i = 1; i <= 1000; i++ ) {
            rpo1 = randomPositionObject( false, -1, null );
            //rpo2 = rpo1;
            //rpo3 = rpo2;
            rpo2 = randomPositionObject(
                true, rpo1.fullmoveNumber(), rpo1.turn() );
            rpo3 = randomPositionObject(
                true, rpo2.fullmoveNumber(), rpo2.turn() );
            if ( rpo1.equals( rpo2 )
                && rpo2.equals( rpo3 )
                && rpo1.equals( rpo3 ) ) {
                continue;
            }
            fail();
        }
    }

    /*
     * Method hashCode() consistency check.
     * "Consistency with hashCode(): Two equal objects must have the same
     * hashCode() value."
     * http://www.ibm.com/developerworks/library/j-jtp05273/
     */
    @Test
    public void equalsConsistencyWithHashCodeTest() {
        Position rpo1, rpo2;
        // Counts the number of times the two Move objects had the same
        // 'from' and 'to' field values.
        int equalsCounter = 0;

        do {
            rpo1 = randomPositionObject( false, -1, null );
            rpo2 = randomPositionObject( false, -1, null );

            if ( rpo1.equals( rpo2 ) ) {
                equalsCounter++;
                if ( rpo1.hashCode() != rpo2.hashCode() ) {
                    fail();
                }
            }

        } while ( equalsCounter < 10 );
    }

    private static Position randomPositionObject(
        boolean makeCopy, int fullmove, Color turn ) {
        Random rand = new Random();

        int valueForFullmoveNumber
            = makeCopy ? fullmove : ( 1 + rand.nextInt( 1000 ) );
        Color valueForTurn
            = makeCopy ? turn
                : ( rand.nextBoolean() ? Color.WHITE : Color.BLACK );

        Position randPos = new Position(
            rand.nextLong(),
            rand.nextLong(),
            rand.nextLong(),
            rand.nextLong(),
            rand.nextLong(),
            rand.nextLong(),
            rand.nextLong(),
            rand.nextLong(),
            rand.nextLong(),
            rand.nextLong(),
            rand.nextLong(),
            rand.nextLong(),
            valueForTurn,
            rand.nextBoolean(),
            rand.nextBoolean(),
            rand.nextBoolean(),
            rand.nextBoolean(),
            SUM.randomSquare(),
            rand.nextInt(),
            valueForFullmoveNumber );

        return randPos;
    }

    /*
     @Test(expected=IndexOutOfBoundsException.class)
     public void testForException() {
     Object o = emptyList.get(0);
     }
     */
}
