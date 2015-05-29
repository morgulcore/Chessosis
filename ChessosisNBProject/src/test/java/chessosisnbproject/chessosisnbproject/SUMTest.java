package chessosisnbproject.chessosisnbproject;

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
     * Test of squareSetToBitboard method, of class SUM.
     */
    @Test
    public void testSquareSetToBitboard() {
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
}
