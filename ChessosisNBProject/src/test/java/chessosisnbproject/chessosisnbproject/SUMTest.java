package chessosisnbproject.chessosisnbproject;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for class SUM.
 *
 * @author Henrik
 */
public class SUMTest {

    /**
     * Tests bitboardToSquareSet() with the binary number sequence
     * (0, 1, 11, 111, ...) where the last value has all 64 bits set.
     * Part one of the test checks the square set sizes.
     *
     * @throws Exception might be thrown in squareBitToSquare()
     */
    @Test
    public void testBitboardToSquareSetWithBitPatternOfIncreasingSetBitsPartOne()
        throws Exception {
        long bitPattern = 0;

        // Testing with a null bitboard to start with
        assertEquals( 0, SUM.bitboardToSquareSet( bitPattern ).size() );

        // Indicates the number of set bits
        int bitCounter = 0;
        for ( Square square : Square.values() ) {
            // The bitwise OR'ing "adds" to bitPattern one bit at a time
            bitPattern |= square.bit();
            bitCounter++;
            assertEquals( bitCounter,
                SUM.bitboardToSquareSet( bitPattern ).size() );
        }
    }

    /**
     * Part 2 of the test with the bit sequence (1, 11, 111, ...). In
     * the test the bit sequence is translated into a "sequence of sequences"
     * ( (A1), (A1,B1), (A1,B1,C1), ... ). In the final square sequence all
     * 64 squares are listed in the order defined in enum type Square
     * (assuming bitboardToSquareSet() is doing its job). The test works
     * by checking the ordinals of the enum constants against a reference
     * counter.
     *
     * @throws Exception might be thrown in squareBitToSquare()
     */
    @Test
    public void testBitboardToSquareSetWithBitPatternOfIncreasingSetBitsPartTwo()
        throws Exception {
        long bitPattern = 0;

        // Number of iterations: 64
        for ( Square squareOfBoard : Square.values() ) {
            // The bitwise OR'ing "adds" to bitPattern one bit at a time
            bitPattern |= squareOfBoard.bit();

            int expectedOrdinal = 0;
            // Note that in this test bitboardToSquareSet() does not get called
            // with bitPattern equal to zero. There seems to be no point in
            // doing that.
            EnumSet<Square> squareSet = SUM.bitboardToSquareSet( bitPattern );

            // Number of iterations: between 1 and 64
            for ( Square squareOfSet : squareSet ) {
                // On EnumSet's method public final int ordinal():
                // "Returns the ordinal of this enumeration constant (its
                // position in its enum declaration, where the initial constant
                // is assigned an ordinal of zero)."
                assertEquals( expectedOrdinal, squareOfSet.ordinal() );
                expectedOrdinal++;
            }
        }
    }

    /**
     * Tests squareSetToBitboard() with a few square sets. As there are 2^64
     * unique square sets, this is certainly not an ideal approach to testing
     * the method.
     *
     * @throws Exception might be thrown in squareSetToBitboard()
     */
    @Test
    public void quickAndDirtyTestingOnSquareSetToBitboard() throws Exception {
        // This creates an empty EnumSet
        EnumSet<Square> emptySet = EnumSet.noneOf( Square.class );
        assertEquals( CSS.EMPTY_BOARD, SUM.squareSetToBitboard( emptySet ) );
        // The set that contains all squares
        EnumSet<Square> fullSet = EnumSet.allOf( Square.class );
        assertEquals( CSS.OMNIBOARD, SUM.squareSetToBitboard( fullSet ) );
        // The single square E4
        EnumSet<Square> squareE4 = EnumSet.of( Square.E4 );
        assertEquals( CSS.E4, SUM.squareSetToBitboard( squareE4 ) );
        // The edge(s) of the board (including the corner squares)
        EnumSet<Square> edge = EnumSet.of(
            Square.A1, Square.A2, Square.A3, Square.A4, Square.A5, Square.A6,
            Square.A7, Square.A8, Square.H1, Square.H2, Square.H3, Square.H4,
            Square.H5, Square.H6, Square.H7, Square.H8, Square.B1, Square.C1,
            Square.D1, Square.E1, Square.F1, Square.G1, Square.B8, Square.C8,
            Square.D8, Square.E8, Square.F8, Square.G8
        );
        assertEquals( CSS.EDGE, SUM.squareSetToBitboard( edge ) );
    }

    /**
     * Verifies that bitboardToSquareSet() and squareSetToBitboard() are
     * inverse functions. In general this sort of a test consists of two
     * parts: f(g(x)) == x and g(f(x)) == x
     *
     * @throws Exception
     */
    @Test
    public void inverseFunctionTestOnBitboardToSquareSetAndSquareSetToBitboard()
        throws Exception {
        assertTrue( inverseFunctionTestPartOne() && inverseFunctionTestPartTwo() );
    }

    // Part one of the inverse function test: f(g(x)) == x
    private boolean inverseFunctionTestPartOne() throws Exception {
        Random random = new Random();
        long randomBitboard = random.nextLong();

        return randomBitboard
            == SUM.squareSetToBitboard( SUM.bitboardToSquareSet( randomBitboard ) );
    }

    // Part two of the inverse function test: g(f(x)) == x
    private boolean inverseFunctionTestPartTwo() throws Exception {

        EnumSet<Square> randomSquareSet = generateRandomSquareSet();
        EnumSet<Square> squareSetReturned
            = SUM.bitboardToSquareSet( SUM.squareSetToBitboard( randomSquareSet ) );
        return randomSquareSet.equals( squareSetReturned );
    }

    // Each of the 64 Square constants has a 25 % chance of being included
    // in the set returned by this method. Therefore the size of the returned
    // set is on average 16.
    private static EnumSet<Square> generateRandomSquareSet() {
        // This creates an empty EnumSet
        EnumSet<Square> squareSet = EnumSet.noneOf( Square.class );
        Random random = new Random();

        // Generate the random square set. It has 16 elements *on average*.
        for ( Square square : Square.values() ) {
            // There's a 25 % chance that the square will be added to the set as
            // nextInt() will return 0, 1, 2 or 3, all with equal probability.
            if ( random.nextInt( 4 ) == 0 ) {
                squareSet.add( square );
            }
        } // Done generating the random square set

        return squareSet;
    }

    /**
     * Tests squareBitToSquare( long squareBit ) with all 64 valid input values.
     *
     * @throws Exception might be thrown in squareBitToSquare
     */
    @Test
    public void squareBitToSquareWorksWithValidInput() throws Exception {
        long leftShiftingBit = 1;

        for ( Square square : Square.values() ) {
            assertEquals( square, SUM.squareBitToSquare( leftShiftingBit ) );
            leftShiftingBit <<= 1;
        }
    }

    /**
     * Verifies that validSquareBit returns true in all cases it should.
     */
    @Test
    public void validSquareBitReturnsTrueWhenItShould() {
        for ( Square square : Square.values() ) {
            assertTrue( SUM.validSquareBit( square.bit() ) );
        }
    }

    /**
     * Tests validSquareBit with random longs. It's extremely unlikely that
     * any of the random values would be a square bit so all of the return
     * values should be false.
     */
    @Test
    public void validSquareBitReturnsFalseWhenItShould() {
        // Let's get the random number generator started.
        Random random = new Random();

        for ( int i = 1; i <= 10000; i++ ) {
            assertFalse( SUM.validSquareBit( random.nextLong() ) );
        }
    }

    /**
     * Verifies that each element in a set returned by method
     * splitBitboardIntoSetOfSquareBits( long bitboard ) is indeed a
     * square bit. Random values are used here as testing all possible
     * long values doesn't seem realistic.
     */
    @Test
    public void splitBitboardIntoSetOfSquareBitsRandomInputTestOne() {
        Random random = new Random();
        Set<Long> setOfBitboards;

        for ( int i = 1; i <= 10000; i++ ) {
            setOfBitboards = SUM.splitBitboardIntoSetOfSquareBits(
                random.nextLong() );
            for ( Long bitboard : setOfBitboards ) {
                assertTrue( SUM.validSquareBit( bitboard ) );
            }
        }
    }

    /**
     * Tests numberOfSetBits( long bitboard ) with an "increasing" bit
     * pattern of 0, 1, 11, 111, ..., the last pattern having 64 set bits.
     * During the test the method should return the ascending sequence
     * 0, 1, 2, ..., 64.
     */
    @Test
    public void numberOfSetBitsBitwiseORTest() {
        long bitPattern = 0;

        // Testing with a null bitboard to start with
        assertEquals( bitPattern, SUM.numberOfSetBits( bitPattern ) );

        int bitCounter = 0;
        for ( Square square : Square.values() ) {
            // The bitwise OR'ing "adds" to bitPattern one bit at a time
            bitPattern |= square.bit();
            bitCounter++;
            assertEquals( bitCounter, SUM.numberOfSetBits( bitPattern ) );
        }
    }

    /**
     * Checks the set size returned by
     * splitBitboardIntoSetOfSquareBits( long bitboard ) for consistency.
     * For example, if the bitboard argument is 7 (i.e., 4 + 2 + 1), then
     * the set size should be 3.
     */
    @Test
    public void splitBitboardIntoSetOfSquareBitsRandomInputTestTwo() {
        Random random = new Random();

        for ( int i = 1; i <= 10000; i++ ) {
            long randomBitboard = random.nextLong();
            Set<Long> setOfBitboards
                = SUM.splitBitboardIntoSetOfSquareBits( randomBitboard );
            assertEquals( SUM.numberOfSetBits( randomBitboard ),
                setOfBitboards.size() );
        }
    }

    /**
     * Verifies that randomSquare() eventually returns each and every
     * constant defined in enum type Square. Note that failure in this
     * test means an infinite loop, not explicit failure as in fail().
     */
    @Test
    public void randomSquareCanReturnAllSquareConstants() {
        for ( Square square : Square.values() ) {
            Square randomSquare;
            do {
                randomSquare = SUM.randomSquare();
            } while ( randomSquare != square );
        }

        assertTrue( true );
    }
}
