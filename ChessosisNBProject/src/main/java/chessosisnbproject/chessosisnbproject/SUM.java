package chessosisnbproject.chessosisnbproject;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * SUM (Static Utility Methods) is a sort of a lightweight library or
 * collection of static methods needed by other classes. No objects of
 * type SUM should be instantiated as the class contains only static members.
 *
 * @author Henrik Lindberg
 */
public class SUM {

    // Private constructor -- no instances, no javadoc
    private SUM() {
    }

    /**
     * Converts a bitboard-based square set representation to a Square
     * EnumSet-based one. This method is the inverse function of
     * squareSetToBitboard.
     *
     * @param bitboard the Java long to convert
     * @return a Square EnumSet
     * @throws Exception might be thrown in squareBitToSquare()
     */
    public static EnumSet<Square> bitboardToSquareSet( long bitboard )
        throws Exception {
        // Extract the individual square bits from the bitboard
        Set<Long> setOfSquareBits
            = splitBitboardIntoSetOfSquareBits( bitboard );

        // This creates an empty EnumSet
        EnumSet<Square> squareSet = EnumSet.noneOf( Square.class );

        // Convert the square bits to Square constants and add them
        // to the EnumSet
        for ( Long squareBit : setOfSquareBits ) {
            Square square = squareBitToSquare( squareBit );
            squareSet.add( square );
        }

        return squareSet;
    }

    /**
     * Converts a Square EnumSet-based square set representation to a
     * bitboard-based one. This method is the inverse function of
     * bitboardToSquareSet.
     *
     * @param squareSet the Square EnumSet to convert
     * @return a bitboard
     */
    public static long squareSetToBitboard( EnumSet<Square> squareSet ) {
        return 0;
    }

    /**
     * Square bit to enum type Square conversion method.
     *
     * @param squareBit Java long to convert
     * @return a Square constant; should never return null
     * @throws Exception in case of an invalid square bit
     */
    public static Square squareBitToSquare( long squareBit ) throws Exception {
        // Invalid input won't be tolerated
        if ( !SUM.validSquareBit( squareBit ) ) {
            throw new Exception( "squareBit: " + squareBit );
        }

        for ( Square square : Square.values() ) {
            if ( square.bit() == squareBit ) {
                return square;
            }
        }

        return null;
    }

    /**
     * Determines whether a bitboard is a square bit. A square bit (bitboard)
     * has exactly one bit set. In other words, its a value 2^n where n is
     * between 0 and 63.
     *
     * @param bitboard the bitboard to examine
     * @return true when bitboard is a square bit
     */
    public static boolean validSquareBit( long bitboard ) {
        for ( Square square : Square.values() ) {
            if ( square.bit() == bitboard ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Splits a bitboard into its square bit "elements". For example, a
     * bitboard with the decimal value 7 (first three bits set) would result
     * in a set of three square bits with the values 1, 2 and 4. The size
     * of the set is in all cases equal to the number of set bits in the
     * bitboard.
     *
     * @param bitboard the bitboard to split
     * @return a set of square bits
     */
    public static Set<Long> splitBitboardIntoSetOfSquareBits( long bitboard ) {
        // LinkedHashSet orders its elements based on the order in which
        // they were inserted into the set. The set is empty to begin with.
        Set<Long> setOfSquareBits = new LinkedHashSet<>();
        long leftShiftingValue = 1;

        do {
            // If the condition is true, bitboard has a bit set at the
            // position indicated by leftShiftingValue
            if ( ( leftShiftingValue & bitboard ) != 0 ) {
                setOfSquareBits.add( leftShiftingValue );
            }
            leftShiftingValue <<= 1;
        } while ( leftShiftingValue != 0 );

        return setOfSquareBits;
    }

    /**
     * Counts the number of set bits in a bitboard.
     *
     * @param bitboard a bitboard with 0 to 64 set bits
     * @return a value between 0 and 64
     */
    public static int numberOfSetBits( long bitboard ) {
        int setBitCount = 0;
        long leftShiftingValue = 1;

        do {
            // A set bit has been found if the bitwise AND produces
            // a non-zero result
            if ( ( leftShiftingValue & bitboard ) != 0 ) {
                setBitCount++;
            }
            leftShiftingValue <<= 1;
        } while ( leftShiftingValue != 0 );

        return setBitCount;
    }
}
