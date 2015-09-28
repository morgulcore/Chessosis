package chessosisnbproject.logic;

import chessosisnbproject.data.CSS;
import chessosisnbproject.data.Colour;
import java.util.Random;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class PositionTest {

    // Reference for the standard starting position object
    private Position stdStartPos;

    // Sets up the test fixture (called before every test case method).
    @Before
    public void setUp() {
        // Should set up the standard starting position
        stdStartPos = new Position();
    }

    // Tears down the test fixture (called after every test case method).
    @After
    public void tearDown() {
        stdStartPos = null;
    }

    /*
     Position( Position pos, boolean randomWhitePawnBB, ... ): Testing
     Position's randomizing constructor. The test calls the constructor
     with the boolean arguments all being false. Thus the constructor
     should return an object identical to the first parameter.
     */
    @Test
    public void randomizingPositionConstructorTest() {
        Position identical
            = new Position(
                stdStartPos,
                false, false, false, false, false,
                false, false, false, false, false,
                false, false, false, false, false,
                false, false, false, false, false );

        assertEquals( true, stdStartPos.deepEquals( identical ) );
    }

    @Test
    public void pawnsOK() {
        assertEquals( 0xff00000000ff00L,
            stdStartPos.whitePawns() | stdStartPos.blackPawns() );
    }

    @Test
    public void rooksOK() {
        assertEquals( 0x8100000000000081L,
            stdStartPos.whiteRooks() | stdStartPos.blackRooks() );
    }

    @Test
    public void minorPiecesOK() {
        assertEquals( 0x6600000000000066L,
            stdStartPos.whiteKnights() | stdStartPos.whiteBishops()
            | stdStartPos.blackKnights() | stdStartPos.blackBishops()
        );
    }

    @Test
    public void queensOK() {
        assertEquals( 0x800000000000008L,
            stdStartPos.whiteQueens() | stdStartPos.blackQueens() );
    }

    @Test
    public void kingsOK() {
        assertEquals( 0x1000000000000010L,
            stdStartPos.whiteKing() | stdStartPos.blackKing() );
    }

    @Test
    public void activeColorOK() {
        assertEquals( Colour.WHITE, stdStartPos.turn() );
    }

    @Test
    public void castlingRightsOK() {
        assertEquals( false,
            !stdStartPos.whiteCanCastleKingside()
            | !stdStartPos.whiteCanCastleQueenside()
            | !stdStartPos.blackCanCastleKingside()
            | !stdStartPos.blackCanCastleQueenside() );
    }

    @Test
    public void bothArmiesMightBeOK() {
        assertEquals( 0xffff00000000ffffL, stdStartPos.bothArmies() );
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
     position object and an identical object returned by Position's
     no-argument constructor. Note that this also indirectly tests the
     no-argument constructor.
     */
    @Test
    public void deepEqualsReturnsTrue() {
        // Manually specified starting position
        Position manuallySpecifiedPos = new Position(
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

        assertEquals( true, stdStartPos.deepEquals( manuallySpecifiedPos ) );
    }

    /*
     deepEquals(): Checks that Position's whitePawnBB field gets randomized
     */
    @Test
    public void deepEqualsReturnsFalse1() {
        Position pos
            = new Position(
                stdStartPos,
                true, false, false, false, false,
                false, false, false, false, false,
                false, false, false, false, false,
                false, false, false, false, false );

        assertEquals( false, stdStartPos.whitePawns() == pos.whitePawns() );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    /*
     deepEquals(): Checks that Position's whiteBishopBB field gets randomized
     */
    @Test
    public void deepEqualsReturnsFalse2() {
        Position pos
            = new Position(
                stdStartPos,
                false, true, false, false, false,
                false, false, false, false, false,
                false, false, false, false, false,
                false, false, false, false, false );

        assertEquals( false, stdStartPos.whiteBishops() == pos.whiteBishops() );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    /*
     deepEquals(): Checks that Position's whiteKnightBB field gets randomized
     */
    @Test
    public void deepEqualsReturnsFalse3() {
        Position pos
            = new Position(
                stdStartPos,
                false, false, true, false, false,
                false, false, false, false, false,
                false, false, false, false, false,
                false, false, false, false, false );

        assertEquals( false, stdStartPos.whiteKnights() == pos.whiteKnights() );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    /*
     deepEquals(): Checks that Position's whiteRookBB field gets randomized
     */
    @Test
    public void deepEqualsReturnsFalse4() {
        Position pos
            = new Position(
                stdStartPos,
                false, false, false, true, false,
                false, false, false, false, false,
                false, false, false, false, false,
                false, false, false, false, false );

        assertEquals( false, stdStartPos.whiteRooks() == pos.whiteRooks() );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    /*
     deepEquals(): Checks that Position's whiteQueenBB field gets randomized
     */
    @Test
    public void deepEqualsReturnsFalse5() {
        Position pos
            = new Position(
                stdStartPos,
                false, false, false, false, true,
                false, false, false, false, false,
                false, false, false, false, false,
                false, false, false, false, false );

        assertEquals( false, stdStartPos.whiteQueens() == pos.whiteQueens() );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    /*
     deepEquals(): Checks that Position's whiteKingBB field gets randomized
     */
    @Test
    public void deepEqualsReturnsFalse6() {
        Position pos
            = new Position(
                stdStartPos,
                false, false, false, false, false,
                true, false, false, false, false,
                false, false, false, false, false,
                false, false, false, false, false );

        assertEquals( false, stdStartPos.whiteKing() == pos.whiteKing() );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    /*
     deepEquals(): Checks that Position's blackPawnBB field gets randomized
     */
    @Test
    public void deepEqualsReturnsFalse7() {
        Position pos
            = new Position(
                stdStartPos,
                false, false, false, false, false,
                false, true, false, false, false,
                false, false, false, false, false,
                false, false, false, false, false );

        assertEquals( false, stdStartPos.blackPawns() == pos.blackPawns() );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    /*
     deepEquals(): Checks that Position's blackBishopBB field gets randomized
     */
    @Test
    public void deepEqualsReturnsFalse8() {
        Position pos
            = new Position(
                stdStartPos,
                false, false, false, false, false,
                false, false, true, false, false,
                false, false, false, false, false,
                false, false, false, false, false );

        assertEquals( false, stdStartPos.blackBishops() == pos.blackBishops() );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    /*
     deepEquals(): Checks that Position's blackKnightBB field gets randomized
     */
    @Test
    public void deepEqualsReturnsFalse9() {
        Position pos
            = new Position(
                stdStartPos,
                false, false, false, false, false,
                false, false, false, true, false,
                false, false, false, false, false,
                false, false, false, false, false );

        assertEquals( false, stdStartPos.blackKnights() == pos.blackKnights() );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    /*
     deepEquals(): Checks that Position's blackRookBB field gets randomized
     */
    @Test
    public void deepEqualsReturnsFalse10() {
        Position pos
            = new Position(
                stdStartPos,
                false, false, false, false, false,
                false, false, false, false, true,
                false, false, false, false, false,
                false, false, false, false, false );

        assertEquals( false, stdStartPos.blackRooks() == pos.blackRooks() );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    /*
     deepEquals(): Checks that Position's blackQueenBB field gets randomized
     */
    @Test
    public void deepEqualsReturnsFalse11() {
        Position pos
            = new Position(
                stdStartPos,
                false, false, false, false, false,
                false, false, false, false, false,
                true, false, false, false, false,
                false, false, false, false, false );

        assertEquals( false, stdStartPos.blackQueens() == pos.blackQueens() );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    /*
     deepEquals(): Checks that Position's blackKingBB field gets randomized
     */
    @Test
    public void deepEqualsReturnsFalse12() {
        Position pos
            = new Position(
                stdStartPos,
                false, false, false, false, false,
                false, false, false, false, false,
                false, true, false, false, false,
                false, false, false, false, false );

        assertEquals( false, stdStartPos.blackKing() == pos.blackKing() );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    /*
     deepEquals(): Checks that Position's turn field gets randomized
     */
    @Test
    public void deepEqualsReturnsFalse13() {

        boolean unequalTurnFieldsDetected = false;
        Position pos = null;

        // As the turn field can get only two values (excluding null),
        // there's a 50 % chance that the randomized field will equal the
        // original. That's why we'll try 20 times in a row to obtain
        // differing field values.
        for ( int i = 1; i <= 20; i++ ) {
            pos = new Position(
                stdStartPos,
                false, false, false, false, false,
                false, false, false, false, false,
                false, false, true, false, false,
                false, false, false, false, false );
            if ( stdStartPos.turn() != pos.turn() ) {
                unequalTurnFieldsDetected = true;
                break;
            }
        }

        assertEquals( true, unequalTurnFieldsDetected );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    /*
     deepEquals(): Checks that Position's castling rights fields get
     randomized
     */
    @Test
    public void deepEqualsReturnsFalse14to17() {
        boolean unequalCastlingRightsFieldsDetected = false;
        Position pos = null;

        // With only a single iteration there's a 1/16 chance that the
        // test will fail. This is the case where the four randomized
        // boolean castling rights fields happen to all equal their
        // counterparts in the other Position object.
        for ( int i = 1; i <= 10; i++ ) {
            pos = new Position(
                stdStartPos,
                false, false, false, false, false,
                false, false, false, false, false,
                false, false, false, true, true,
                true, true, false, false, false );
            if ( stdStartPos.whiteCanCastleKingside() != pos.whiteCanCastleKingside()
                || stdStartPos.whiteCanCastleQueenside() != pos.whiteCanCastleQueenside()
                || stdStartPos.blackCanCastleKingside() != pos.blackCanCastleKingside()
                || stdStartPos.blackCanCastleQueenside() != pos.blackCanCastleQueenside() ) {
                unequalCastlingRightsFieldsDetected = true;
                break;
            }
        }

        assertEquals( true, unequalCastlingRightsFieldsDetected );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    /*
     deepEquals(): Checks that Position's en passant target square field
     gets randomized
     */
    @Test
    public void deepEqualsReturnsFalse18() {
        boolean unequalEnPassantTargetSquareFieldsDetected = false;
        Position pos = null;

        for ( int i = 1; i <= 20; i++ ) {
            pos = new Position(
                stdStartPos,
                false, false, false, false, false,
                false, false, false, false, false,
                false, false, false, false, false,
                false, false, true, false, false );
            if ( stdStartPos.enPassantTargetSquare() != pos.enPassantTargetSquare() ) {
                unequalEnPassantTargetSquareFieldsDetected = true;
                break;
            }
        }

        assertEquals(
            true, unequalEnPassantTargetSquareFieldsDetected );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    /*
     deepEquals(): Checks that Position's halfmove clock field gets randomized
     */
    @Test
    public void deepEqualsReturnsFalse19() {
        Position pos
            = new Position(
                stdStartPos,
                false, false, false, false, false,
                false, false, false, false, false,
                false, false, false, false, false,
                false, false, false, true, false );

        assertEquals(
            false, stdStartPos.halfmoveClock() == pos.halfmoveClock() );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    /*
     deepEquals(): Checks that Position's fullmove number field gets randomized
     */
    @Test
    public void deepEqualsReturnsFalse20() {
        Position pos
            = new Position(
                stdStartPos,
                false, false, false, false, false,
                false, false, false, false, false,
                false, false, false, false, false,
                false, false, false, false, true );

        assertEquals(
            false, stdStartPos.fullmoveNumber() == pos.fullmoveNumber() );
        assertEquals( false, stdStartPos.deepEquals( pos ) );
    }

    @Test
    public void fENToPositionConversionTest1() throws Exception {
        Position actualPos = Position.fENToPosition(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1" );
        if ( !identicalPositions( stdStartPos, actualPos ) ) {
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

    /*
     Position( String fENRecord ): Tests the constructor with the standard
     starting position
     */
    @Test
    public void fENRecordConstructorStdStartPos() {
        stdStartPos = new Position(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1" );
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
