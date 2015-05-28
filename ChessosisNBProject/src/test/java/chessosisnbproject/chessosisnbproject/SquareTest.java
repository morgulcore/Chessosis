package chessosisnbproject.chessosisnbproject;

import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for enum type Square.
 *
 * @author Henrik Lindberg
 */
public class SquareTest {

    /**
     * Verifies that the number of enum constants in Square equals 64.
     */
    @Test
    public void correctNumberOfSquareConstants() {
        assertEquals( 64, Square.values().length );
    }

    /**
     * Checks that the constant names are correct and are in the right order.
     * <p>
     * The first constant name should be A1, the last H8. The test first
     * concatenates the constant names as a single string and then does a string
     * comparison to a reference string.
     */
    @Test
    public void correctConstantNames() {
        String concatenatedConstantNames = "";

        for ( Square square : Square.values() ) {
            // Implicit call to method toString which returns the
            // constants name, such as "E4".
            concatenatedConstantNames += square;
        }

        // The comparison ("expected") string was created with the
        // following bash command:
        // for i in {1..8}; do echo {A..H}"$i"; done | tr -cd '[:alnum:]'
        assertEquals(
            "A1B1C1D1E1F1G1H1A2B2C2D2E2F2G2H2A3B3C3D3E3F3G3H3A4B4C4D4E4F4G4H4"
            + "A5B5C5D5E5F5G5H5A6B6C6D6E6F6G6H6A7B7C7D7E7F7G7H7A8B8C8D8E8F8G8H8",
            concatenatedConstantNames );
    }

    /**
     * Checks the squareBit property of each enum constant.
     * <p>
     * Each squareBit should be a power of two (2^n) and as a sequence they
     * should be in ascending order starting from 2^0 and ending in 2^63.
     */
    @Test
    public void squareBitsArePowersOfTwo() {
        int exponent = -1;
        for ( Square square : Square.values() ) {
            // exponent will have values between 0 and 63, inclusive,
            // when used in pow()
            exponent++;
            if ( exponent == 63 ) {
                // There's an interesting problem in testing the bitboard
                // value of H8: longs in Java are (typically) treated as
                // signed integer values. The maximum value for long is
                // defined in Long.MAX_VALUE as 0x7fffffffffffffff whereas
                // Long.MIN_VALUE is 0x8000000000000000 which is equal
                // to Long.MAX_VALUE + 1 when interpreted as an unsigned
                // value. 2^63 is also one greater than a signed long's
                // maximum value, so the expression
                // (long) Math.pow( 2, 63 ) would give a misleading
                // result.
                assertEquals( Long.MIN_VALUE, square.bit() );
            } else {
                assertEquals(
                    (long) Math.pow( 2, exponent ), square.bit() );
            }
        }
    }

    /**
     * Doing a bitwise OR between all the squareBits of the enum constants
     * should produce a 64-bit integer with all its bits set.
     */
    @Test
    public void allSquaresBitwiseORed() {
        long result
            // Produced with the command
            // for i in {1..8}; do echo Square.{A..H}$i.getSquareBit; done | \
            // tr ' ' '\n' | sed 's/$/() |/'
            = Square.A1.bit()
            | Square.B1.bit()
            | Square.C1.bit()
            | Square.D1.bit()
            | Square.E1.bit()
            | Square.F1.bit()
            | Square.G1.bit()
            | Square.H1.bit()
            | Square.A2.bit()
            | Square.B2.bit()
            | Square.C2.bit()
            | Square.D2.bit()
            | Square.E2.bit()
            | Square.F2.bit()
            | Square.G2.bit()
            | Square.H2.bit()
            | Square.A3.bit()
            | Square.B3.bit()
            | Square.C3.bit()
            | Square.D3.bit()
            | Square.E3.bit()
            | Square.F3.bit()
            | Square.G3.bit()
            | Square.H3.bit()
            | Square.A4.bit()
            | Square.B4.bit()
            | Square.C4.bit()
            | Square.D4.bit()
            | Square.E4.bit()
            | Square.F4.bit()
            | Square.G4.bit()
            | Square.H4.bit()
            | Square.A5.bit()
            | Square.B5.bit()
            | Square.C5.bit()
            | Square.D5.bit()
            | Square.E5.bit()
            | Square.F5.bit()
            | Square.G5.bit()
            | Square.H5.bit()
            | Square.A6.bit()
            | Square.B6.bit()
            | Square.C6.bit()
            | Square.D6.bit()
            | Square.E6.bit()
            | Square.F6.bit()
            | Square.G6.bit()
            | Square.H6.bit()
            | Square.A7.bit()
            | Square.B7.bit()
            | Square.C7.bit()
            | Square.D7.bit()
            | Square.E7.bit()
            | Square.F7.bit()
            | Square.G7.bit()
            | Square.H7.bit()
            | Square.A8.bit()
            | Square.B8.bit()
            | Square.C8.bit()
            | Square.D8.bit()
            | Square.E8.bit()
            | Square.F8.bit()
            | Square.G8.bit()
            | Square.H8.bit();

        // At this point all bits should be set in the 64-bit
        // result variable.
        assertEquals( 0xffffffffffffffffL, result );
    }

    /**
     * Inverse function test on SquareNameToBitIndex and BitIndexToSquareName.
     * <p>
     * The methods do opposite things so they should cancel each other's
     * effect. Therefore, something like
     * Square.bitIndexToSquareName( Square.squareNameToBitIndex( "A1" ) )
     * should evaluate to "A1".
     */
    @Test
    public void bitIndexSquareNameInverseFunctionTest() {
        // First test: f( g( x ) ) == x
        for ( Square square : Square.values() ) {
            assertEquals(
                square.toString(),
                Square.bitIndexToSquareName(
                    Square.squareNameToBitIndex( square.toString() ) ) );
        }
        // Second test: g( f( x ) ) == x
        for ( int i = 0; i < 64; i++ ) {
            assertEquals( i, Square.squareNameToBitIndex(
                Square.bitIndexToSquareName( i ) ) );
        }
    }

    /**
     * Tests method Square.bitIndexToSquareName.
     * <p>
     * The correct order of the enum constants was or will also be verified,
     * so they are helpful in this test.
     */
    @Test
    public void validBitIndexToSquareNameMappings() {
        int index = -1;
        for ( Square square : Square.values() ) {
            index++;
            assertEquals( square.toString(),
                Square.bitIndexToSquareName( index ) );
        }
        assertEquals( 63, index );
    }

    /**
     * Tests method bitIndexToSquareBit.
     * <p>
     * The test works by first generating the one-bit-set bitboards (2^n,
     * where n between 0 and 63, inclusive) as an array of strings. They
     * are then converted into their actual long values and compared to
     * the actual return value of bitIndexToSquareBit.
     * <p>
     * The test method should be divided into smaller functions. I will do
     * that if and when time permits.
     *
     * @throws Exception In case of an invalid bit index value.
     */
    @Test
    public void validBitIndexToBitboardMappings() throws Exception {
        String[] bitboardStrings = new String[ 64 ];
        char[] eightBytesAsHex = "0000000000000000".toCharArray();
        // Should be 16 to start with. Note that this is not an off-by-one
        // error.
        int charArrayIndex = eightBytesAsHex.length;

        // Iterate over the 64 elements of the string array
        for ( int i = 0; i < bitboardStrings.length; i++ ) {
            int modulo = i % 4; // Gives a value between 0 and 3
            switch ( modulo ) {
                case 0:
                    if ( charArrayIndex < eightBytesAsHex.length ) {
                        eightBytesAsHex[ charArrayIndex ] = '0';
                    }
                    charArrayIndex--;
                    eightBytesAsHex[ charArrayIndex ] = '1';
                    break;
                case 1:
                    eightBytesAsHex[ charArrayIndex ] = '2';
                    break;
                case 2:
                    eightBytesAsHex[ charArrayIndex ] = '4';
                    break;
                case 3:
                    eightBytesAsHex[ charArrayIndex ] = '8';
                    break;
                default:
                    System.out.println( "validBitIndexToBitboardMappings: "
                        + "invalid modulo value: " + modulo );
                    return;
            }

            // "Convert" the char array to a String and then store it
            // in the string array.
            bitboardStrings[ i ] = new String( eightBytesAsHex );
        }
        for ( int i = 0; i <= 62; i++ ) {
            // Convert the hexadecimal String to a long
            long expectedValue = Long.valueOf( bitboardStrings[ i ], 16 );
            assertEquals( expectedValue, Square.bitIndexToSquareBit( i ) );
        }
        // When the most significant bit is the one that is set, the
        // comparison must be done manually. Note that Long.MIN_VALUE
        // is 0x8000000000000000 in hex.
        assertEquals( Long.MIN_VALUE, Square.bitIndexToSquareBit( 63 ) );
    }

    /**
     * Verifies that validSquareBit returns true in all cases it should.
     */
    @Test
    public void validSquareBitReturnsTrueWhenItShould() {
        for ( Square square : Square.values() ) {
            assertTrue( Square.validSquareBit( square.bit() ) );
        }
    }

    /**
     * Tests validSquareBit with random longs.
     * <p>
     * It's extremely unlikely that any of the random values would be a
     * square bit so all of the return values should be false.
     */
    @Test
    public void validSquareBitReturnsFalseWhenItShould() {
        // Let's get the random number generator started.
        Random random = new Random();

        for ( int i = 1; i <= 10000; i++ ) {
            assertFalse( Square.validSquareBit( random.nextLong() ) );
        }
    }

    /**
     * Inverse function test on bitIndexToSquareBit and squareBitToBitIndex.
     *
     * @throws Exception Thrown in squareBitToBitIndex.
     */
    @Test
    public void bitIndexSquareBitInverseFunctionTest() throws Exception {
        for ( Square square : Square.values() ) {
            assertEquals( square.bit(), Square.bitIndexToSquareBit(
                Square.squareBitToBitIndex( square.bit() ) ) );

        }
    }
}
