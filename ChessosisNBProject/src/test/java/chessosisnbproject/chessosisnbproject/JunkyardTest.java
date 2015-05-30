package chessosisnbproject.chessosisnbproject;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testing the Junkyard.
 *
 * @author Henrik Lindberg
 */
public class JunkyardTest {

    /**
     * Inverse function test on bitIndexToSquareBit and squareBitToBitIndex.
     *
     * @throws Exception Thrown in squareBitToBitIndex.
     */
    @Test
    public void bitIndexSquareBitInverseFunctionTest() throws Exception {
        for ( Square square : Square.values() ) {
            assertEquals( square.bit(), Junkyard.bitIndexToSquareBit(
                Junkyard.squareBitToBitIndex( square.bit() ) ) );

        }
        for ( int i = 0; i < 64; i++ ) {
            assertEquals( i, Junkyard.squareBitToBitIndex(
                Junkyard.bitIndexToSquareBit( i ) ) );
        }
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
            assertEquals( expectedValue, Junkyard.bitIndexToSquareBit( i ) );
        }
        // When the most significant bit is the one that is set, the
        // comparison must be done manually. Note that Long.MIN_VALUE
        // is 0x8000000000000000 in hex.
        assertEquals( Long.MIN_VALUE, Junkyard.bitIndexToSquareBit( 63 ) );
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
                Junkyard.bitIndexToSquareName(
                    Junkyard.squareNameToBitIndex( square.toString() ) ) );
        }
        // Second test: g( f( x ) ) == x
        for ( int i = 0; i < 64; i++ ) {
            assertEquals( i, Junkyard.squareNameToBitIndex(
                Junkyard.bitIndexToSquareName( i ) ) );
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
                Junkyard.bitIndexToSquareName( index ) );
        }
        assertEquals( 63, index );
    }
}
