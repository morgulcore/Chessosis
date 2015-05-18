package chessosisnbproject.chessosisnbproject;

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
        Square[] values = Square.values();
        assertEquals( 64, values.length );
    }

    /**
     * Checks that the constant names are correct and in the right order.
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
        System.out.println( "getSquareBit" );

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
                assertEquals( Long.MIN_VALUE, square.getSquareBit() );
            } else {
                assertEquals(
                    (long) Math.pow( 2, exponent ), square.getSquareBit() );
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
            = Square.A1.getSquareBit()
            | Square.B1.getSquareBit()
            | Square.C1.getSquareBit()
            | Square.D1.getSquareBit()
            | Square.E1.getSquareBit()
            | Square.F1.getSquareBit()
            | Square.G1.getSquareBit()
            | Square.H1.getSquareBit()
            | Square.A2.getSquareBit()
            | Square.B2.getSquareBit()
            | Square.C2.getSquareBit()
            | Square.D2.getSquareBit()
            | Square.E2.getSquareBit()
            | Square.F2.getSquareBit()
            | Square.G2.getSquareBit()
            | Square.H2.getSquareBit()
            | Square.A3.getSquareBit()
            | Square.B3.getSquareBit()
            | Square.C3.getSquareBit()
            | Square.D3.getSquareBit()
            | Square.E3.getSquareBit()
            | Square.F3.getSquareBit()
            | Square.G3.getSquareBit()
            | Square.H3.getSquareBit()
            | Square.A4.getSquareBit()
            | Square.B4.getSquareBit()
            | Square.C4.getSquareBit()
            | Square.D4.getSquareBit()
            | Square.E4.getSquareBit()
            | Square.F4.getSquareBit()
            | Square.G4.getSquareBit()
            | Square.H4.getSquareBit()
            | Square.A5.getSquareBit()
            | Square.B5.getSquareBit()
            | Square.C5.getSquareBit()
            | Square.D5.getSquareBit()
            | Square.E5.getSquareBit()
            | Square.F5.getSquareBit()
            | Square.G5.getSquareBit()
            | Square.H5.getSquareBit()
            | Square.A6.getSquareBit()
            | Square.B6.getSquareBit()
            | Square.C6.getSquareBit()
            | Square.D6.getSquareBit()
            | Square.E6.getSquareBit()
            | Square.F6.getSquareBit()
            | Square.G6.getSquareBit()
            | Square.H6.getSquareBit()
            | Square.A7.getSquareBit()
            | Square.B7.getSquareBit()
            | Square.C7.getSquareBit()
            | Square.D7.getSquareBit()
            | Square.E7.getSquareBit()
            | Square.F7.getSquareBit()
            | Square.G7.getSquareBit()
            | Square.H7.getSquareBit()
            | Square.A8.getSquareBit()
            | Square.B8.getSquareBit()
            | Square.C8.getSquareBit()
            | Square.D8.getSquareBit()
            | Square.E8.getSquareBit()
            | Square.F8.getSquareBit()
            | Square.G8.getSquareBit()
            | Square.H8.getSquareBit();

        // At this point all bits should be set in the 64-bit
        // result variable.
        assertEquals( 0xffffffffffffffffL, result );
    }

    /**
     * Tests method Square.bitIndexToSquareName.
     * <p>
     * The correct order of the enum constants is also verified, so
     * they are helpful in this test.
     */
    @Test
    // validBitIndexToSquareNameMappings
    public void validBitIndexToSquareNameMappings() {
        int index = -1;
        for ( Square square : Square.values() ) {
            index++;
            assertEquals( square.toString(), Square.bitIndexToSquareName( index ) );
        }
        assertEquals( 63, index );

        /*
         int expectedEnumConstantOrdinal = -1;
         for ( Square square : Square.values() ) {
         ++expectedEnumConstantOrdinal;
         assertEquals( expectedEnumConstantOrdinal, square.ordinal() );
         }
         */
    }
}
