package chessosisnbproject.logic;

import chessosisnbproject.data.CSS;
import chessosisnbproject.data.Colour;
import chessosisnbproject.data.Square;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PositionTest {

    // Reference for the standard starting position object
    private Position standard;

    // Sets up the test fixture (called before every test case method).
    @Before
    public void setUp() {
        // Should set up the standard starting position
        standard = new Position();
    }

    // Tears down the test fixture (called after every test case method).
    @After
    public void tearDown() {
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
        assertEquals( Colour.WHITE, standard.turn() );
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

    /*
     deepEquals(): Valid data test using a manually specified starting
     position object and the supposedly identical object returned by Position's
     no-argument constructor.
     */
    @Test
    public void deepEqualsBasicReturnsTrueTest() {
        Position pos1 = new Position();
        // Manually specified starting position
        Position pos2 = new Position(
            // White pawns
            CSS.A2 | CSS.B2 | CSS.C2 | CSS.D2 | CSS.E2 | CSS.F2 | CSS.G2 | CSS.H2,
            // White bishops
            CSS.C1 | CSS.F1,
            // White knights
            CSS.B1 | CSS.G1,
            // White rooks
            CSS.A1 | CSS.H1,
            // White queen
            CSS.D1,
            // White king
            CSS.E1,
            // Black pawns
            CSS.A7 | CSS.B7 | CSS.C7 | CSS.D7 | CSS.E7 | CSS.F7 | CSS.G7 | CSS.H7,
            // Black bishops
            CSS.C8 | CSS.F8,
            // Black knights
            CSS.B8 | CSS.G8,
            // Black rooks
            CSS.A8 | CSS.H8,
            // Black queen
            CSS.D8,
            // Black king
            CSS.E8,
            // Active color
            Colour.WHITE,
            // The four castling rights (KQkq)
            true, true, true, true,
            // En passant target square
            null,
            // Halfmove clock and fullmove number
            0, 1
        );

        assertEquals( true, pos1.deepEquals( pos2 ) );
    }

    /*
     deepEquals(): Non-identical objects detection test. The test compares
     objects returned by Position.randomDataPositionObject() which in
     practical terms of probability should never be identical. The test
     succeeds if all objects compared are found to be not identical.
     */
    @Test
    public void deepEqualsReturnsFalse() {
        for ( int i = 1; i <= 100000; i++ ) {
            Position pos1 = Position.randomDataPositionObject();
            //Position pos2 = pos1;
            Position pos2 = Position.randomDataPositionObject();
            //Position pos1 = new Position();
            //Position pos2 = new Position();

            assertEquals( false, pos1.deepEquals( pos2 ) );
        }
    }

    @Test
    public void fENToPositionConversionTest1() throws Exception {
        Position actualPos = Position.fENToPosition(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1" );
        if ( !identicalPositions( standard, actualPos ) ) {
            fail();
        }
    }

    @Test
    public void fENToPositionConversionTest2() throws Exception {
        Position expectedPos = new Position(
            CSS.B2 | CSS.C2 | CSS.F2 | CSS.B3 | CSS.H6,
            0,
            0,
            CSS.H1 | CSS.E5,
            CSS.G6,
            CSS.C1,
            CSS.B4 | CSS.F4 | CSS.A5,
            CSS.E7,
            CSS.H7,
            CSS.A7,
            CSS.C7,
            CSS.G8,
            Colour.BLACK,
            false, false, false, false,
            null,
            1,
            35
        );

        Position actualPos = Position.fENToPosition(
            "6k1/r1q1b2n/6QP/p3R3/1p3p2/1P6/1PP2P2/2K4R b - - 1 35" );
        if ( !identicalPositions( expectedPos, actualPos ) ) {
            fail();
        }
    }

    // Compares each and every field of two Position objects and returns
    // true if they are all equal. Note that the overridden equals() of
    // Position doesn't compare all fields.
    //
    // ___Position.deepEquals() does exactly the same job as this method.___
    private static boolean identicalPositions(
        Position first, Position second ) throws Exception {
        if ( first == second ) { // One and the same object
            throw new Exception(
                "Variables 'first' and 'second' point to the same object" );
        }

        // Compare the bitboards
        if ( first.whitePawns() != second.whitePawns() ) { // white pawns
            System.out.printf( "%x\t%x\n", first.whitePawns(), second.whitePawns() );
            return false;
        } else if ( first.whiteBishops() != second.whiteBishops() ) { // white bishops
            return false;
        } else if ( first.whiteKnights() != second.whiteKnights() ) { // white knights
            return false;
        } else if ( first.whiteRooks() != second.whiteRooks() ) {
            return false;
        } else if ( first.whiteQueens() != second.whiteQueens() ) {
            return false;
        } else if ( first.whiteKing() != second.whiteKing() ) {
            return false;
        } else if ( first.blackPawns() != second.blackPawns() ) {
            return false;
        } else if ( first.blackBishops() != second.blackBishops() ) {
            return false;
        } else if ( first.blackKnights() != second.blackKnights() ) {
            return false;
        } else if ( first.blackRooks() != second.blackRooks() ) {
            return false;
        } else if ( first.blackQueens() != second.blackQueens() ) {
            return false;
        } else if ( first.blackKing() != second.blackKing() ) {
            return false;
        } // Compare the turn indicators
        else if ( first.turn() != second.turn() ) {
            return false;
        } // Compare castling rights
        else if ( first.whiteCanCastleKingside() != second.whiteCanCastleKingside() ) {
            return false;
        } else if ( first.whiteCanCastleQueenside() != second.whiteCanCastleQueenside() ) {
            return false;
        } else if ( first.blackCanCastleKingside() != second.blackCanCastleKingside() ) {
            return false;
        } else if ( first.blackCanCastleQueenside() != second.blackCanCastleQueenside() ) {
            return false;
        } // Compare en passant target square indicators
        else if ( first.enPassantTargetSquare() != second.enPassantTargetSquare() ) {
            return false;
        } // Compare halfmove and fullmove counters
        else if ( first.halfmoveClock() != second.halfmoveClock() ) {
            return false;
        } else if ( first.fullmoveNumber() != second.fullmoveNumber() ) {
            return false;
        }

        return true;
    }

    //
    // =============================
    // == Private utility methods ==
    // =============================
    //
    //
    // There's the similar method Position.randomDataPositionObject() that
    // serves the same basic function as this method and contains much
    // the same code.
    private static Position randomPositionObject(
        boolean makeCopy, int fullmove, Colour turn ) {
        Random rand = new Random();

        int valueForFullmoveNumber
            = makeCopy ? fullmove : ( 1 + rand.nextInt( 1000 ) );
        Colour valueForTurn
            = makeCopy ? turn
                : ( rand.nextBoolean() ? Colour.WHITE : Colour.BLACK );

        Position randPos = new Position(
            // White pawns
            rand.nextLong(),
            // White bishops
            rand.nextLong(),
            // White knights
            rand.nextLong(),
            // White rooks
            rand.nextLong(),
            // White queens
            rand.nextLong(),
            // White king
            rand.nextLong(),
            // Black pawns
            rand.nextLong(),
            // Black bishops
            rand.nextLong(),
            // Black knights
            rand.nextLong(),
            // Black rooks
            rand.nextLong(),
            // Black queens
            rand.nextLong(),
            // Black king
            rand.nextLong(),
            // Active color
            valueForTurn,
            // The four castling rights
            rand.nextBoolean(),
            rand.nextBoolean(),
            rand.nextBoolean(),
            rand.nextBoolean(),
            // En passant target square
            SUM.randomSquare(),
            // Halfmove clock
            rand.nextInt(),
            // Fullmove number
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
