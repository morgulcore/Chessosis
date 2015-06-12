package chessosisnbproject.logic;

import chessosisnbproject.data.Position;
import chessosisnbproject.data.Chessman;
import chessosisnbproject.data.Direction;
import chessosisnbproject.data.CSS;
import chessosisnbproject.data.Square;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Random;
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
     * @throws Exception in case the parameter receives null
     */
    public static long squareSetToBitboard( EnumSet<Square> squareSet )
        throws Exception {
        // Allowing the method to be called with a null argument would
        // make no sense.
        if ( squareSet == null ) {
            throw new Exception( "squareSet: " + squareSet ); // squareSet: null
        } else if ( squareSet.isEmpty() ) {
            return CSS.EMPTY_BOARD;
        }

        long bitboard = 0;

        for ( Square square : squareSet ) {
            // Doing a sequence of bitwise OR operations
            bitboard |= square.bit();
        }

        return bitboard;
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

    /**
     * Returns a random Square constant.
     *
     * @return random Square constant
     */
    public static Square randomSquare() {
        Random random = new Random();

        char[] randomFileAndRank = {
            // Random file
            (char) ( 65 + random.nextInt( 8 ) ),
            // Random rank
            (char) ( 49 + random.nextInt( 8 ) ) };
        String randomSquareString = new String( randomFileAndRank );

        return Square.valueOf( randomSquareString );
    }

    /**
     * Returns the CSS.FILE_? constant that corresponds to the file the
     * Square parameter is located on.
     *
     * @param square the square to operate on
     * @return one of the CSS.FILE_? constants
     */
    public static long fileOfSquare( Square square ) {
        char file = square.toString().charAt( 0 );

        switch ( file ) {
            case 'A':
                return CSS.FILE_A;
            case 'B':
                return CSS.FILE_B;
            case 'C':
                return CSS.FILE_C;
            case 'D':
                return CSS.FILE_D;
            case 'E':
                return CSS.FILE_E;
            case 'F':
                return CSS.FILE_F;
            case 'G':
                return CSS.FILE_G;
            case 'H':
                return CSS.FILE_H;
            default:
                // Execution should never reach this point
                return 0;
        }
    }

    /**
     * Returns the CSS.RANK_? constant that corresponds to the rank the
     * Square parameter is located on.
     *
     * @param square the square to operate on
     * @return one of the CSS.RANK_? constants
     */
    public static long rankOfSquare( Square square ) {
        // The rank characters '1' to '8' have the integer values of 49 to
        // 56, respectively, being characters in the ASCII set
        char rankChar = square.toString().charAt( 1 );
        // Gets a copy on the RANK_? constants packed in a disposable array
        long[] ranks = CSS.ranks();

        // Much more clever than the switch structure in fileOfSquare(),
        // wouldn't you agree?
        return ranks[ rankChar - 49 ];
    }

    /**
     * Each square on the board has at most eight adjacent squares as
     * illustrated by the following diagram (using E4 as the square for
     * the method's first parameter).
     *
     * <pre>
     * +--------------+       N
     * | D5 | E5 | F5 |       ^
     * |---------------       |
     * | D4 | E4 | F4 |  W {--+--} E
     * ----------------       |
     * | D3 | E3 | F3 |       v
     * +--------------+       S
     * </pre>
     *
     * View the bitboard index diagram at the beginning of CSS.java to
     * get an idea on how the method accomplishes its task.
     *
     * @param square the square to operate on
     * @param direction the direction of the adjacent square
     * @return the adjacent square or null if no such square exists
     * @throws Exception 
     */
    public static Square adjacentSquare( Square square, Direction direction )
        throws Exception {
        long adjacentSquareBB; // BB, bitboard
        switch ( direction ) {
            case NORTH:
                adjacentSquareBB = square.bit() << 8;
                // Moving off the north of the board results in the square
                // bit becoming equal to zero
                if ( adjacentSquareBB == 0 ) {
                    return null;
                }
                return SUM.squareBitToSquare( adjacentSquareBB );
            case EAST:
                adjacentSquareBB = square.bit() << 1;
                // If the square bit has just moved off the rank, the bitwise
                // AND produces zero
                if ( ( adjacentSquareBB & SUM.rankOfSquare( square ) ) == 0 ) {
                    return null;
                }
                return SUM.squareBitToSquare( adjacentSquareBB );
            case SOUTH:
                adjacentSquareBB = square.bit() >>> 8;
                // Moving off the south of the board results in zero
                if ( adjacentSquareBB == 0 ) {
                    return null;
                }
                return SUM.squareBitToSquare( adjacentSquareBB );
            case WEST:
                adjacentSquareBB = square.bit() >>> 1;
                // Square bit moves off rank, bitwise AND produces zero
                if ( ( adjacentSquareBB & SUM.rankOfSquare( square ) ) == 0 ) {
                    return null;
                }
                return SUM.squareBitToSquare( adjacentSquareBB );
            // Same action for the remaining four cases
            case NORTHEAST:
            case NORTHWEST:
            case SOUTHEAST:
            case SOUTHWEST:
                return adjacentSquareOnDiagonal( square, direction );
            // The default case should be impossible
            default:
                throw new Exception(
                    "Executed supposedly impossible default case of switch" );
        }
    }

    /**
     * Determines the type of chessman located on the given square. Possible
     * values include PAWN, BISHOP, KNIGHT, ROOK, QUEEN and KING. If the
     * square parameter is an empty square, the method returns null.
     *
     * @param square the square to examine
     * @param position the context
     * @return the type of the chessman on the square
     */
    public static Chessman typeOfChessman(
        Square square, Position position ) {
        // Pawns
        if ( ( square.bit() & position.whitePawns() ) != 0
            || ( square.bit() & position.blackPawns() ) != 0 ) {
            return Chessman.PAWN;
        } // Bishops
        else if ( ( square.bit() & position.whiteBishops() ) != 0
            || ( square.bit() & position.blackBishops() ) != 0 ) {
            return Chessman.BISHOP;
        } // Knights
        else if ( ( square.bit() & position.whiteKnights() ) != 0
            || ( square.bit() & position.blackKnights() ) != 0 ) {
            return Chessman.KNIGHT;
        } // Rooks
        else if ( ( square.bit() & position.whiteRooks() ) != 0
            || ( square.bit() & position.blackRooks() ) != 0 ) {
            return Chessman.ROOK;
        } // Queens
        else if ( ( square.bit() & position.whiteQueens() ) != 0
            || ( square.bit() & position.blackQueens() ) != 0 ) {
            return Chessman.QUEEN;
        } // Kings
        else if ( ( square.bit() & position.whiteKing() ) != 0
            || ( square.bit() & position.blackKing() ) != 0 ) {
            return Chessman.KING;
        }
        return null;
    }

    public static String[][] unicodeChessSymbolTable( Position pos ) {
        String[][] table = new String[ 8 ][ 8 ];
        for ( int row = 0; row < 8; row++ ) {
            for ( int column = 0; column < 8; column++ ) {
                Square square = tableCellToSquare( row, column );
                table[ row ][ column ]
                    = unicodeChessSymbolOfSquare( pos, square );
            }
        }
        return table;
    }

    private static String unicodeChessSymbolOfSquare(
        Position pos, Square sq ) {

        if ( ( pos.whiteKing() & sq.bit() ) != 0 ) {
            return "\u2654";
        } else if ( ( pos.whiteQueens() & sq.bit() ) != 0 ) {
            return "\u2655";
        } else if ( ( pos.whiteRooks() & sq.bit() ) != 0 ) {
            return "\u2656";
        } else if ( ( pos.whiteBishops() & sq.bit() ) != 0 ) {
            return "\u2657";
        } else if ( ( pos.whiteKnights() & sq.bit() ) != 0 ) {
            return "\u2658";
        } else if ( ( pos.whitePawns() & sq.bit() ) != 0 ) {
            return "\u2659";
        } else if ( ( pos.blackKing() & sq.bit() ) != 0 ) {
            return "\u265a";
        } else if ( ( pos.blackQueens() & sq.bit() ) != 0 ) {
            return "\u265b";
        } else if ( ( pos.blackRooks() & sq.bit() ) != 0 ) {
            return "\u265c";
        } else if ( ( pos.blackBishops() & sq.bit() ) != 0 ) {
            return "\u265d";
        } else if ( ( pos.blackKnights() & sq.bit() ) != 0 ) {
            return "\u265e";
        } else if ( ( pos.blackPawns() & sq.bit() ) != 0 ) {
            return "\u265f";
        } else {
            return "";
        }
    }

    public static Square tableCellToSquare( int row, int col ) {
        char file = (char) ( 'A' + col ),
            rank = (char) ( '8' - row );
        return Square.valueOf(
            // Note the string concatenation. This is not about
            // integer arithmetic.
            (char) file + "" + (char) rank );
    }

    //
    // ============================
    // == Private static methods ==
    // ============================
    //
    // The following method deals with adjacent squares on diagonals.
    private static Square adjacentSquareOnDiagonal(
        Square square, Direction direction ) throws Exception {
        // Call the private method for dealing with getting the single
        // diagonal square of a corner square
        if ( ( square.bit() & CSS.CORNER_SQUARES ) != 0 ) {
            return adjacentSquareOnDiagonalOfCornerSquare( square, direction );
        } // The following two if's deal with non-corner squares on rank 1 and 8
        else if ( ( square.bit() & CSS.RANK_1 ) != 0 ) {
            return adjacentSquareOnDiagonalOfSquareOnRank1( square, direction );
        } else if ( ( square.bit() & CSS.RANK_8 ) != 0 ) {
            return adjacentSquareOnDiagonalOfSquareOnRank8( square, direction );
        } // The following two if's deal with non-corner squares on file A and H
        else if ( ( square.bit() & CSS.FILE_A ) != 0 ) {
            return adjacentSquareOnDiagonalOfSquareOnFileA( square, direction );
        } else if ( ( square.bit() & CSS.FILE_H ) != 0 ) {
            return adjacentSquareOnDiagonalOfSquareOnFileH( square, direction );
        } // The square is not on the edge of the board
        else {
            return adjacentSquareOnDiagonalOfSquareNotOnEdge( square, direction );
        }
    }

    private static Square adjacentSquareOnDiagonalOfSquareNotOnEdge(
        Square square, Direction direction ) throws Exception {
        Square squareToReturn = null;

        switch ( direction ) {
            case NORTHEAST:
                squareToReturn = SUM.squareBitToSquare( square.bit() << 9 );
                break;
            case NORTHWEST:
                squareToReturn = SUM.squareBitToSquare( square.bit() << 7 );
                break;
            case SOUTHEAST:
                squareToReturn = SUM.squareBitToSquare( square.bit() >>> 7 );
                break;
            case SOUTHWEST:
                squareToReturn = SUM.squareBitToSquare( square.bit() >>> 9 );
                break;
            // This method cannot return null as we are dealing with a
            // non-corner square; there are squares in every direction
            default:
                throw new Exception(
                    "Executed forbidden default case of switch with value "
                    + direction );
        }

        return squareToReturn;
    }

    // Deals with non-corner squares on the a-file
    private static Square adjacentSquareOnDiagonalOfSquareOnFileA(
        Square square, Direction direction ) throws Exception {
        Square squareToReturn = null;
        // The assumption is the square argument is always valid (is on
        // file A)
        switch ( direction ) {
            case NORTHEAST:
                squareToReturn = SUM.squareBitToSquare( square.bit() << 9 );
                break;
            case SOUTHEAST:
                squareToReturn = SUM.squareBitToSquare( square.bit() >>> 7 );
                break;
        }

        return squareToReturn;
    }

    // Deals with non-corner squares on the h-file
    private static Square adjacentSquareOnDiagonalOfSquareOnFileH(
        Square square, Direction direction ) throws Exception {
        Square squareToReturn = null;
        // The assumption is that the square argument is always valid
        // (is on file H)
        switch ( direction ) {
            case NORTHWEST:
                squareToReturn = SUM.squareBitToSquare( square.bit() << 7 );
                break;
            case SOUTHWEST:
                squareToReturn = SUM.squareBitToSquare( square.bit() >>> 9 );
                break;
        }

        return squareToReturn;
    }

    // Deals with non-corner squares on the first rank
    private static Square adjacentSquareOnDiagonalOfSquareOnRank1(
        Square square, Direction direction ) throws Exception {
        Square squareToReturn = null;
        // It is assumed the square argument is always valid (is on the
        // first rank)
        switch ( direction ) {
            case NORTHEAST:
                // Moving nine bits towards the most significant bit
                squareToReturn = SUM.squareBitToSquare( square.bit() << 9 );
                break;
            case NORTHWEST:
                // Moving seven bits
                squareToReturn = SUM.squareBitToSquare( square.bit() << 7 );
                break;
        }

        // A null return value is legal
        return squareToReturn;
    }

    // Deals with non-corner squares on the eighth rank
    private static Square adjacentSquareOnDiagonalOfSquareOnRank8(
        Square square, Direction direction ) throws Exception {
        Square squareToReturn = null;
        // It is assumed the square argument is always valid (is on the
        // eighth rank)
        switch ( direction ) {
            case SOUTHEAST:
                squareToReturn = SUM.squareBitToSquare( square.bit() >>> 7 );
                break;
            case SOUTHWEST:
                squareToReturn = SUM.squareBitToSquare( square.bit() >>> 9 );
                break;
        }

        return squareToReturn;
    }

    // Gets the adjacent diagonal squares of the corner squares
    private static Square adjacentSquareOnDiagonalOfCornerSquare(
        Square square, Direction direction ) throws Exception {
        // Let's assume the square argument is always valid (is a
        // corner square)
        Square squareToReturn = null;
        switch ( square ) {
            case A1:
                if ( direction == Direction.NORTHEAST ) {
                    squareToReturn = Square.B2;
                }
                break;
            case A8:
                if ( direction == Direction.SOUTHEAST ) {
                    squareToReturn = Square.B7;
                }
                break;
            case H1:
                if ( direction == Direction.NORTHWEST ) {
                    squareToReturn = Square.G2;
                }
                break;
            case H8:
                if ( direction == Direction.SOUTHWEST ) {
                    squareToReturn = Square.G7;
                }
                break;
        }

        // Returning null is legal for this method
        return squareToReturn;
    }
}
