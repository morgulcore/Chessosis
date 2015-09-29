package chessosisnbproject.logic;

import chessosisnbproject.data.CSS;
import chessosisnbproject.data.Colour;
import chessosisnbproject.data.Direction;
import chessosisnbproject.data.Piece;
import chessosisnbproject.data.PieceType;
import chessosisnbproject.data.Square;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * SUM (Static Utility Methods) is a sort of a lightweight library or collection
 * of static methods needed by other classes. No objects of type SUM should be
 * instantiated as the class contains only static members.
 *
 * @author Henrik Lindberg
 */
public class SUM {

    // Private constructor -- no instances, no Javadoc
    private SUM() {
    }

    /**
     * Used to determine the pieces of the active color that have "pseudo-legal"
     * access to the square specified by the first parameter. The main use of
     * the method is checking the legality of a newly generated position. If any
     * piece of the active color has pseudo-legal access to the square of the
     * enemy king (i.e., can capture it), then the position is illegal and the
     * move leading to it should be discarded from the list of legal move
     * candidates.
     *
     * @param sq the square to which access will be determined
     * @param pos the context
     * @return the set of pieces that have pseudo-legal access to the given
     * square
     * @throws Exception
     */
    public static EnumSet<Square> pseudoLegalAccess( Square sq, Position pos )
        throws Exception {
        EnumSet<Square> activePiecesWithAccess
            = EnumSet.noneOf( Square.class );
        Colour activeColor
            = ( pos.turn() == Colour.WHITE ) ? Colour.WHITE : Colour.BLACK;

        // Pawns
        activePiecesWithAccess.addAll(
            pLegalAccessPawns( sq, pos, activeColor ) );
        // Bishops
        activePiecesWithAccess.addAll(
            pLegalAccessBishops( sq, pos, activeColor, false ) );
        // Knights
        activePiecesWithAccess.addAll(
            pLegalAccessKnights( sq, pos, activeColor ) );
        // Rooks
        activePiecesWithAccess.addAll(
            pLegalAccessRooks( sq, pos, activeColor, false ) );
        // Queens
        activePiecesWithAccess.addAll(
            pLegalAccessQueens( sq, pos, activeColor ) );
        // Kings
        activePiecesWithAccess.addAll(
            pLegalAccessKing( sq, pos, activeColor ) );

        return activePiecesWithAccess;
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
    public static EnumSet<Square> bitboardToSqSet( long bitboard )
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
     * Splits a bitboard into its square bit "elements". For example, a bitboard
     * with the decimal value 7 (first three bits set) would result in a set of
     * three square bits with the values 1, 2 and 4. The size of the set is in
     * all cases equal to the number of set bits in the bitboard.
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
     * Returns the CSS.FILE_? constant that corresponds to the file the Square
     * parameter is located on.
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
     * Returns the CSS.RANK_? constant that corresponds to the rank the Square
     * parameter is located on.
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
     * illustrated by the following diagram (using E4 as the square for the
     * method's first parameter).
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
     * View the bitboard index diagram at the beginning of CSS.java to get an
     * idea on how the method accomplishes its task.
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
     * Returns the piece located on the square argument or null if the square is
     * empty. Strictly speaking the method returns an enum constant that
     * identifies the type and color of the piece.
     *
     * @param sq the square to examine
     * @param pos the context
     * @return the piece found on the square
     */
    public static Piece resolvePiece( Square sq, Position pos ) {
        PieceType pieceType = resolvePieceType( sq, pos );
        if ( pieceType == null ) {
            return null;
        }
        Colour pieceColor = ( ( pos.whiteArmy() & sq.bit() ) != 0 )
            ? Colour.WHITE : Colour.BLACK;
        if ( pieceColor == Colour.WHITE && pieceType == PieceType.PAWN ) {
            return Piece.WHITE_PAWN;
        } else if ( pieceColor == Colour.WHITE && pieceType == PieceType.BISHOP ) {
            return Piece.WHITE_BISHOP;
        } else if ( pieceColor == Colour.WHITE && pieceType == PieceType.KNIGHT ) {
            return Piece.WHITE_KNIGHT;
        } else if ( pieceColor == Colour.WHITE && pieceType == PieceType.ROOK ) {
            return Piece.WHITE_ROOK;
        } else if ( pieceColor == Colour.WHITE && pieceType == PieceType.QUEEN ) {
            return Piece.WHITE_QUEEN;
        } else if ( pieceColor == Colour.WHITE && pieceType == PieceType.KING ) {
            return Piece.WHITE_KING;
        } else if ( pieceType == PieceType.PAWN ) {
            return Piece.BLACK_PAWN;
        } else if ( pieceType == PieceType.BISHOP ) {
            return Piece.BLACK_BISHOP;
        } else if ( pieceType == PieceType.KNIGHT ) {
            return Piece.BLACK_KNIGHT;
        } else if ( pieceType == PieceType.ROOK ) {
            return Piece.BLACK_ROOK;
        } else if ( pieceType == PieceType.QUEEN ) {
            return Piece.BLACK_QUEEN;
        } else { // Black king
            return Piece.BLACK_KING;
        }
    }

    /**
     * Resolves the color of the piece found on the square parameter or null if
     * it is empty.
     *
     * @param sq the square to examine
     * @param pos the context
     * @return the color of the piece or null
     */
    public static Colour resolvePieceColor( Square sq, Position pos ) {
        Piece piece = resolvePiece( sq, pos );
        if ( piece == null ) { // Empty square
            return null;
        } else if ( piece == Piece.WHITE_PAWN || piece == Piece.WHITE_BISHOP
            || piece == Piece.WHITE_KNIGHT || piece == Piece.WHITE_ROOK
            || piece == Piece.WHITE_QUEEN || piece == Piece.WHITE_KING ) {
            return Colour.WHITE;
        } else {
            return Colour.BLACK;
        }
    }

    /**
     * Determines the type of chessman located on the given square. Possible
     * values include PAWN, BISHOP, KNIGHT, ROOK, QUEEN and KING. If the square
     * parameter is an empty square, the method returns null.
     *
     * @param square the square to examine
     * @param position the context
     * @return the type of the chessman on the square
     */
    public static PieceType resolvePieceType(
        Square square, Position position ) {
        // Pawns
        if ( ( square.bit() & position.whitePawns() ) != 0
            || ( square.bit() & position.blackPawns() ) != 0 ) {
            return PieceType.PAWN;
        } // Bishops
        else if ( ( square.bit() & position.whiteBishops() ) != 0
            || ( square.bit() & position.blackBishops() ) != 0 ) {
            return PieceType.BISHOP;
        } // Knights
        else if ( ( square.bit() & position.whiteKnights() ) != 0
            || ( square.bit() & position.blackKnights() ) != 0 ) {
            return PieceType.KNIGHT;
        } // Rooks
        else if ( ( square.bit() & position.whiteRooks() ) != 0
            || ( square.bit() & position.blackRooks() ) != 0 ) {
            return PieceType.ROOK;
        } // Queens
        else if ( ( square.bit() & position.whiteQueens() ) != 0
            || ( square.bit() & position.blackQueens() ) != 0 ) {
            return PieceType.QUEEN;
        } // Kings
        else if ( ( square.bit() & position.whiteKing() ) != 0
            || ( square.bit() & position.blackKing() ) != 0 ) {
            return PieceType.KING;
        }
        return null;
    }

    /**
     * Returns a two-dimensional String array that is used in drawing the chess
     * pieces (which are actually characters) seen on the GUI.
     *
     * @param pos the position to operate on
     * @return a table of characters representing the input position
     */
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

    /**
     * Returns the square name that corresponds to a given cell in an 8-by-8
     * table. The cell is identified by the row and col parameters. For example,
     * tableCellToSquare( 0, 0 ) would return Square.A8 and tableCellToSquare(
     * 7, 7 ) Square.H1
     *
     * @param row the row (or chessboard rank)
     * @param col the column (or chessboard file)
     * @return
     */
    public static Square tableCellToSquare( int row, int col ) {
        char file = (char) ( 'A' + col ),
            rank = (char) ( '8' - row );
        return Square.valueOf(
            // Note the string concatenation. This is not about
            // integer arithmetic.
            (char) file + "" + (char) rank );
    }

    /**
     * Converts the eight ranks of the FEN strings' first field into an array of
     * 12 bitboards. Both the eight ranks and the 12 bitboards are a means to
     * express the same thing -- the piece placement of the board.
     *
     * @param fENRanks the first field of a FEN string
     * @return an array of 12 bitboards that expresses piece placement on the
     * board
     * @throws Exception
     */
    public static long[] fENRanksToBBArray( String[] fENRanks )
        throws Exception {
        long[] pieces = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        int bitIndexOfFirstColumn = 64;
        for ( String fENRank : fENRanks ) {
            char[] fENRankCA = fENRank.toCharArray();
            bitIndexOfFirstColumn -= 8;
            long cursorSB = bitIndexToSquareBit( bitIndexOfFirstColumn );
            fENRanksToBBArrayHelper( fENRankCA, pieces, cursorSB );
        }

        return pieces;
    }

    /*
     Divides the first field of a FEN record (AKA the FEN ranks) into 12
     strings with the same syntactic structure as the first field. The
     division is done based on piece type and color. As an example, let us
     consider the first field of the FEN record that represents the standard
     starting position:
     "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"

     With that as input, the method would return a 12-element string array
     consisting of the following strings, in this order:
     "8/8/8/8/8/8/PPPPPPPP/8"   [ 0] White pawns
     "8/8/8/8/8/8/8/2B2B2"      [ 1] White bishops
     "8/8/8/8/8/8/8/1N4N1"      [ 2] White knights
     "8/8/8/8/8/8/8/R6R"        [ 3] White rooks
     "8/8/8/8/8/8/8/3Q4"        [ 4] White queens
     "8/8/8/8/8/8/8/4K3"        [ 5] White king
     "8/pppppppp/8/8/8/8/8/8"   [ 6] Black pawns
     "2b2b2/8/8/8/8/8/8/8"      [ 7] Black bishops
     "1n4n1/8/8/8/8/8/8/8"      [ 8] Black knights
     "r6r/8/8/8/8/8/8/8"        [ 9] Black rooks
     "3q4/8/8/8/8/8/8/8"        [10] Black queens
     "4k3/8/8/8/8/8/8/8"        [11] Black king

     The string array returned by the method contains exactly the same
     information as the first field of the FEN record parameter but divided
     into 12 "layers".

     Calling this method can be a useful first step in converting FEN ranks
     into bitboards representing the placement of the 12 types of chess pieces
     in a given position.

     JUNIT TESTS:
     --layeredFENRanksWorksWithStdStartPos()

     Most of the JUnit testing of the method is done indirectly while testing
     the Position( String fENRecord ) constructor.
     */
    public static String[] layeredFENRanks( String fENRecord ) {
        // An invalid FEN record will stop execution of Chessosis
        SUM.bugtrap( SUM.validateFENRecord( fENRecord ) != 0,
            "layeredFENRanks()",
            "Invalid FEN record: " + fENRecord );

        // The method call verifies that the size of the array
        // returned is eight
        String[] fENRanks = SUM.splitFirstFENField( fENRecord );

        String[] layers = {
            null, null, null, null, null, null,
            null, null, null, null, null, null
        };

        // A piece identity means the color and type of a piece, for
        // example, a black bishop
        char[] pieceIdentities = {
            'P', 'B', 'N', 'R', 'Q', 'K',
            'p', 'b', 'n', 'r', 'q', 'k'
        };

        // 12 iterations, one for each piece identity
        for ( int index = 0; index < pieceIdentities.length; index++ ) {
            layers[ index ]
                = layeredFENRanksSecondInnermostLoop(
                    fENRanks, pieceIdentities[ index ] );
        }

        return layers;
    }

    /*
     Used to stop program execution when unacceptable flaws in program logic
     are detected. The point is to alert the developer of programming errors
     rather than letting them go undetected.

     The first parameter is the error condition. It's important to note that
     program execution is stopped only if the condition evaluates to true.
     The second parameter specifies where the error occured. This should
     usually be a method name. The third parameter specifies the error
     message to display.

     JUNIT TESTS: (none)
     */
    public static void bugtrap(
        boolean errorCondition, String source, String errorMsg ) {
        if ( !errorCondition ) {
            return;
        }

        System.out.println( "CRITICAL ERROR: " + source + ": " + errorMsg );
        System.exit( 1 );
    }

    /*
     Validates a FEN record by performing the following tests in sequence:
     1. The record matches the regex '^[abBcdefghkKnNpPrRqQw 0-9/-]+$'.
     This defines the set of acceptable characters in any FEN record.
     2. Matches '^[^ ]+ [^ ]+ [^ ]+ [^ ]+ [^ ]+ [^ ]+$', i.e., a string
     with six fields separated by single spaces.
     3. The structure (but not necessarily contents) of the first field
     is valid. I should consist of eight sub-fields containing one or more
     characters in the set [pPnNbBrRqQkK1-8] with the field separator being
     '/'. A sort of a side-effect test is making sure that everything after
     the first field and the single space up to the end of the string is
     in the set [a-h0-9kKqQw-].
     4. The first field doesn't contain two (or more) consecutive decimal
     digits. Something like .../2p311/... doesn't make much sense.
     5. The "sum" of each of the subfields of the first field is 8. For
     example, 2p5 = 2 + 1 + 5, rnbqkbnr = 8 * 1 and 4P3 = 4 + 1 + 3.
     6. There's exactly one king per color.
     7. Validate the second field. It is always either 'w' or 'b'.
     8. Validate the third field which is a string in the 16-element set
     { "-", "K", "Q", "k", "q", "KQ", "Kk", "Kq", "Qk", "Qq", "kq",
     "Qkq", "Kkq", "KQq", "KQk", "KQkq" }
     9. Validate the fourth field (en passant target square). It is either
     the one-character string "-" or a two-character string with the first
     character being in the set [a-h] and the second [36].
     10. Check that the fourth field is logically correct. If the rank
     number of the en passant target square is 3 then the second field
     must be "b"; if the rank number is 6 then the second field must be "w".
     11. Validate the fifth field (the halfmove clock) as a non-negative
     decimal integer with a maximum value of 9999.
     12. Validate the sixth field (the fullmove number) as a positive
     decimal integer with a maximum value of 9999.
     13. Consistency check between the halfmove clock (fifth field) and the
     fullmove number (sixth field): the halfmove clock must be less or equal
     than the total ply count. The total ply count is computed with the
     formula 2m - c where m is the fullmove number. The value of c depends
     on the active color. If the active color is "w" then c = 2; otherwise
     c = 1.
     14. Check that there are no pawns of either color on the first and
     eighth ranks (a more suitable place for this test would be right after
     test 6).

     The ordinal number of the tests correspond to the integer value
     returned by the method should the test fail. If all of the tests
     succeed, the method returns 0.

     JUNIT TESTS:
     --validateFENRecordReturns0()
     --validateFENRecordReturns1()
     --validateFENRecordReturns2()
     --validateFENRecordReturns3()
     --validateFENRecordReturns4()
     --validateFENRecordReturns5()
     --validateFENRecordReturns6()
     --validateFENRecordReturns7()
     --validateFENRecordReturns8()
     --validateFENRecordReturns9()
     --validateFENRecordReturns10()
     --validateFENRecordReturns11()
     --validateFENRecordReturns12()
     --validateFENRecordReturns13()
     --validateFENRecordReturns14()
     */
    public static int validateFENRecord( String fENRecord ) {
        String charClassPlus = "[pPnNbBrRqQkK1-8]+"; // Needed in test 3

        // Test 1
        if ( !Pattern.matches(
            "^[abBcdefghkKnNpPrRqQw 0-9/-]+$", fENRecord ) ) {
            return 1;
        } // Test 2
        else if ( !Pattern.matches(
            "^[^ ]+ [^ ]+ [^ ]+ [^ ]+ [^ ]+ [^ ]+$", fENRecord ) ) {
            return 2;
        } // Test 3
        else if ( !Pattern.matches(
            '^'
            + charClassPlus + '/' + charClassPlus + '/'
            + charClassPlus + '/' + charClassPlus + '/'
            + charClassPlus + '/' + charClassPlus + '/'
            + charClassPlus + '/' + charClassPlus + " [a-h 0-9kKqQw-]+$",
            fENRecord ) ) {
            return 3;
        } // Test 4. Note that the result of the match is not negated, i.e.,
        // a FEN record that matches the regex is invalid.
        else if ( Pattern.matches( "^[^ ]*[1-8]{2}[^ ]* .*$", fENRecord ) ) {
            return 4;
        } // Test 5
        else if ( !validateFENRecordRankSumsAreValid( fENRecord ) ) {
            return 5;
        } // Test 6
        else if ( !validateFENRecordExactlyOneKingPerColor( fENRecord ) ) {
            return 6;
        } // Test 7
        else if ( !validateFENRecordValidActiveColor( fENRecord ) ) {
            return 7;
        } // Test 8
        else if ( !validateFENRecordValidCastlingAvailabilityString( fENRecord ) ) {
            return 8;
        } // Test 9
        else if ( !Pattern.matches( "^-$|^[a-h]{1}[36]{1}$",
            splitFENRecord( fENRecord )[ 3 ] ) ) {
            return 9;
        } // Test 10
        else if ( !validateFENRecord4thFieldConsistentWith2ndField( fENRecord ) ) {
            return 10;
        } // Test 11. The halfmove clock field should match either a single
        // character string of the set [0-9] or a string between two to four
        // characters with the first character in the set [1-9] and the remaining
        // characters [0-9].
        else if ( !Pattern.matches( "^[0-9]$|^[1-9][0-9]{1,3}$",
            splitFENRecord( fENRecord )[ 4 ] ) ) {
            return 11;
        } // Test 12. The fullmove number field has the same requirements
        // as the halfmove clock field, except that the smallest allowed
        // integer is one, not zero.
        else if ( !Pattern.matches( "^[1-9][0-9]{0,3}$",
            splitFENRecord( fENRecord )[ 5 ] ) ) {
            return 12;
        } // Test 13
        else if ( !validateFENRecord5thFieldConsistentWith6thField( fENRecord ) ) {
            return 13;
        } // Test 14
        else if ( !validateFENRecordNoPawnsOn1stAnd8thRank( fENRecord ) ) {
            return 14;
        }

        return 0;
    }

    /*
     Splits a FEN record into its six constituent fields. The parameter
     is the FEN record and the return type a String array of the fields.

     It is assumed the FEN record has been validated before passing it
     to this method.

     JUNIT TESTS:
     --splitFENRecordTest()
     */
    public static String[] splitFENRecord( String fENRecord ) {
        String[] fENFields = fENRecord.split( " " );

        SUM.bugtrap( fENFields.length != 6, // Abort if true
            "splitFENRecord()", "Invalid fENFields.length: "
            + fENFields.length );

        return fENFields;
    }

    /*
     Splits the first field of a FEN record into eight parts. Each of these
     parts represents a rank on the chessboard along with the pieces placed
     on it. As an example consider the standard starting position:
     rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR
     With such input the method would return
     { "rnbqkbnr", "pppppppp", "8", "8", "8", "8", "PPPPPPPP", "RNBQKBNR" }

     The method parameter is a FEN record which is assumed to be already
     validated. Even so, the method performs a couple of checks on String
     array sizes.

     JUNIT TESTS:
     --splitFirstFENFieldTest()
     */
    public static String[] splitFirstFENField( String fENRecord ) {
        String[] fENFields = fENRecord.split( " " );

        // Abort execution if the first argument is true
        SUM.bugtrap( fENFields.length != 6, "splitFirstFENField()",
            "Invalid fENFields.length: " + fENFields.length );

        String[] fENRanks = fENFields[ 0 ].split( "/" );

        // Abort execution if the first argument is true
        SUM.bugtrap( fENRanks.length != 8,
            "splitFirstFENField()",
            "Invalid fENRanks.length: " + fENRanks.length );

        return fENRanks;
    }

    /**
     * Returns the square bit at a particular bit index.
     *
     * @param index The bit index of a 64-bit integer (an int between 0 and 63).
     * @return A square bit, i.e., a bitboard with a single bit set.
     * @throws Exception In case of an invalid index.
     */
    public static long bitIndexToSquareBit( int index ) throws Exception {
        if ( index < 0 || index > 63 ) {
            throw new Exception( "index: " + index );
        }

        long bitboard = 0x0000000000000001L;

        for ( int counter = 0; counter < index; counter++ ) {
            // Shift the set bit left a single position
            bitboard <<= 1;
        }

        return bitboard;
    }

    //
    // ============================
    // == Private static methods ==
    // ============================
    //
    //
    /*
     Helper method for layeredFENRanks(). The method makes eight
     calls to extractPiecesFromFENRanksInnermostLoop() per invocation,
     resulting in a string similar to the first field of a FEN record but
     containing only a single type of piece. For example, if the first
     parameter corresponds to the standard starting position and the second
     to the character 'K', the method would return the string
     "8/8/8/8/8/8/8/4K3".
     */
    private static String layeredFENRanksSecondInnermostLoop(
        String[] fENRanks, char pieceIdentity ) {
        // Piece identity means the color and type of a piece, for example,
        // black bishop. A piece identity board is similar in idea to a
        // bitboard describing the placement of a particular type of piece
        // (or a particular piece identity).
        String pieceIdentityBoard = "";

        // Eight iterations, one for each rank
        for ( int index = 0; index < fENRanks.length; index++ ) {
            pieceIdentityBoard
                += layeredFENRanksInnermostLoop( fENRanks[ index ], pieceIdentity );

            // The '/' character separates the ranks but is not found
            // after the final rank
            if ( index != 7 ) {
                pieceIdentityBoard += "/";
            }
        }

        return pieceIdentityBoard;
    }

    /*
     Helper method for layeredFENRanks(). The method takes as
     its input a single rank from the first field of a FEN record and a
     character specifying the identity (type and color) of a piece. The
     rank is then scanned for pieces with the specified identity. The
     return string is a rank similar to the rank parameter but contains
     only empty squares and pieces with the specified identity.

     Example: the method call
     layeredFENRanksInnermostLoop( "RNBQKBNR", 'K' )
     would return "4K3".
     */
    private static String layeredFENRanksInnermostLoop(
        String rank, char pieceIdentity ) {
        String extractedPiecesRank = ""; // The string var to be returned
        int inBetweenSquareCount = 0;

        // There will be one to eight iterations, depending on the length
        // of the string representing an individual rank
        for ( int i = 0; i < rank.length(); ++i ) {
            char currentChar = rank.charAt( i );

            if ( currentChar == pieceIdentity ) {
                // Append the in between ("empty") square count digit
                // (which is between 1 and 8, inclusive) to the rank
                // being constructed
                extractedPiecesRank
                    += ( inBetweenSquareCount > 0 ) ? inBetweenSquareCount : "";
                inBetweenSquareCount = 0; // Reset the counter
                // Append the piece identity character to the rank
                // being constructed
                extractedPiecesRank += pieceIdentity;
            } // Tests if the current character is a digit between 1 to 8
            else if ( currentChar >= '1' && currentChar <= '8' ) {
                // The expression on the right produces an integer between
                // 1 and 8, inclusive. The value produced corresponds to
                // the digit in currentChar.
                inBetweenSquareCount += (int) ( currentChar - '0' );
            } // currentChar contains a piece with an identity different
            // to the one specified in the method call
            else {
                ++inBetweenSquareCount;
            }
        }

        // Append any trailing "in between" squares to the rank before
        // returning it
        extractedPiecesRank
            += ( inBetweenSquareCount > 0 ) ? inBetweenSquareCount : "";

        // Program execution will be terminated if the square count of
        // 'extractedPiecesRank' doesn't add up to eight or if it contains
        // other characters than digits and the one specified by the
        // second parameter.
        extractPiecesFromFENRanksInnermostLoopRankSumCheck(
            extractedPiecesRank, pieceIdentity );

        return extractedPiecesRank;
    }

    /*
     Verifies that the square count of a rank adds up to eight. As an example,
     consider the rank "1p2p2p". The three black pawns in it add up to three
     and the three sequences of empty squares add up to five. Therefore it
     is a valid rank (square count of eight).

     The method should only be called from
     extractPiecesFromFENRanksInnermostLoop(). The pieceIdentity parameter
     specifies the allowed type and color of piece(s) on the rank. If
     problems are detected, program execution is terminated.
     */
    private static void extractPiecesFromFENRanksInnermostLoopRankSumCheck(
        String rank, char pieceIdentity ) {
        int squareCount = 0;

        for ( int i = 0; i < rank.length(); i++ ) {
            char currentChar = rank.charAt( i );
            if ( currentChar == pieceIdentity ) {
                ++squareCount;
            } else if ( currentChar >= '1' && currentChar <= '8' ) {
                // Produces an integer value between 1 to 8
                squareCount += (int) ( currentChar - '0' );
            } else {
                SUM.bugtrap( true,
                    "extractPiecesFromFENRanksInnermostLoopRankSumCheck()",
                    "Invalid piece identity: " + currentChar );
            }
        }

        SUM.bugtrap( squareCount != 8,
            "extractPiecesFromFENRanksInnermostLoopRankSumCheck()",
            "Rank sum check produced invalid value instead of 8: "
            + squareCount );
    }

    // validateFENRecord() helper method
    private static boolean validateFENRecordRankSumsAreValid( String fENRecord ) {
        String[] fENRanks = splitFirstFENField( fENRecord );

        for ( String s : fENRanks ) {
            int sANPieceLetterCount = 0, sumOfDigits = 0;
            for ( int i = 0; i < s.length(); i++ ) {
                if ( Character.isLetter( s.charAt( i ) ) ) {
                    ++sANPieceLetterCount;
                } else { // The character is a digit
                    // Declaration for java.lang.Character.digit() method
                    // public static int digit( char ch, int radix );
                    sumOfDigits += Character.digit( s.charAt( i ), 10 );
                }
            }
            if ( sANPieceLetterCount + sumOfDigits != 8 ) {
                // Invalid rank string detected
                return false;
            }
        }

        return true;
    }

    // validateFENRecord() helper method
    private static boolean validateFENRecordExactlyOneKingPerColor(
        String fENRecord ) {
        int whiteKingCount = 0, blackKingCount = 0;

        String[] fENRanks = splitFirstFENField( fENRecord );

        for ( String s : fENRanks ) {
            for ( int i = 0; i < s.length(); i++ ) {
                if ( s.charAt( i ) == 'K' ) {
                    ++whiteKingCount;
                } else if ( s.charAt( i ) == 'k' ) {
                    ++blackKingCount;
                }
            }
        }

        return whiteKingCount == 1 && blackKingCount == 1;
    }

    // validateFENRecord() helper method
    private static boolean validateFENRecordValidActiveColor( String fENRecord ) {
        String[] fENFields = splitFENRecord( fENRecord );
        if ( fENFields[ 1 ].equals( "b" ) || fENFields[ 1 ].equals( "w" ) ) {
            return true;
        }

        return false;
    }

    // validateFENRecord() helper method
    private static boolean validateFENRecordValidCastlingAvailabilityString(
        String fENRecord ) {
        String[] validCastlingAvailabilityStrings = {
            // Binomial Coefficient nCk
            // Altogether there are 16 castling availability combinations
            // 4C0 = 1
            "-",
            // 4C1 = 4
            "K", "Q", "k", "q",
            // 4C2 = 6
            "KQ", "Kk", "Kq", "Qk", "Qq", "kq",
            // 4C3 = 4
            "Qkq", "Kkq", "KQq", "KQk",
            // 4C4 = 1
            "KQkq"
        };

        for ( String s : validCastlingAvailabilityStrings ) {
            // A maximum of 16 string comparisons will be performed
            if ( splitFENRecord( fENRecord )[ 2 ].equals( s ) ) {
                return true;
            }
        }

        return false;
    }

    private static boolean validateFENRecord4thFieldConsistentWith2ndField(
        String fENRecord ) {
        String[] fENFields = splitFENRecord( fENRecord );

        // If the en passant target square field equals "-", then it
        // is always consistent with the active color field.
        if ( fENFields[ 3 ].equals( "-" ) ) {
            return true;
        } // If the en passant target square is on the 6th rank then it can
        // only be White's turn.
        else if ( fENFields[ 3 ].charAt( 1 ) == '6'
            && fENFields[ 1 ].equals( "w" ) ) {
            return true;
        } // If the en passant target square is on the 3rd rank then it can
        // only be Black's turn.
        else if ( fENFields[ 3 ].charAt( 1 ) == '3'
            && fENFields[ 1 ].equals( "b" ) ) {
            return true;
        }

        return false;
    }

    private static boolean validateFENRecord5thFieldConsistentWith6thField(
        String fENRecord ) {
        String[] fENFields = splitFENRecord( fENRecord );

        // When the active color is "b", the total ply count is calculated
        // with the formula 2m - 1 where m is the fullmove number.
        int totalPlyCount = 2 * Integer.parseInt( fENFields[ 5 ] ) - 1;
        // If the active color is "w", the formula is 2m - 2
        if ( fENFields[ 1 ].equals( "w" ) ) {
            --totalPlyCount;
        }

        int halfmoveClock = Integer.parseInt( fENFields[ 4 ] );

        if ( totalPlyCount >= halfmoveClock ) {
            // The fullmove number is consistent with the halfmove clock
            return true;
        }

        // Inconsistency detected
        return false;
    }

    private static boolean validateFENRecordNoPawnsOn1stAnd8thRank(
        String fENRecord ) {
        String[] fENRanks = splitFirstFENField( fENRecord );

        int arrayIndex = -7;
        // Repeat twice: once for 8th rank, once for 1st rank
        for ( int i = 1; i <= 2; i++ ) {
            arrayIndex += 7; // Will first equal 0, then 7
            for ( int stringPosIndex = 0;
                stringPosIndex < fENRanks[ arrayIndex ].length();
                ++stringPosIndex ) { // Pos, position
                if ( fENRanks[ arrayIndex ].charAt( stringPosIndex ) == 'P'
                    || fENRanks[ arrayIndex ].charAt( stringPosIndex ) == 'p' ) {
                    // A pawn was found on the first or eighth rank
                    return false;
                }
            }
        }

        return true;
    }

    private static void fENRanksToBBArrayHelper(
        char[] fENRankCA, long[] pieces, long cursorSB ) {
        for ( int i = 0; i < fENRankCA.length; i++ ) {
            char currentChar = fENRankCA[ i ];
            // A digit between 1 and 8 indicating the number of consecutive
            // empty squares
            if ( currentChar >= '1' && currentChar <= '8' ) {
                int numberOfEmptySquares = currentChar - 48;
                cursorSB <<= numberOfEmptySquares;
                // Without the continue there will be one more left-shift
                // after the last else-if
                continue;
            } else if ( currentChar == 'P' ) {
                pieces[ 0 ] |= cursorSB;
            } else if ( currentChar == 'B' ) {
                pieces[ 1 ] |= cursorSB;
            } else if ( currentChar == 'N' ) {
                pieces[ 2 ] |= cursorSB;
            } else if ( currentChar == 'R' ) {
                pieces[ 3 ] |= cursorSB;
            } else if ( currentChar == 'Q' ) {
                pieces[ 4 ] |= cursorSB;
            } else if ( currentChar == 'K' ) {
                pieces[ 5 ] |= cursorSB;
            } else if ( currentChar == 'p' ) {
                pieces[ 6 ] |= cursorSB;
            } else if ( currentChar == 'b' ) {
                pieces[ 7 ] |= cursorSB;
            } else if ( currentChar == 'n' ) {
                pieces[ 8 ] |= cursorSB;
            } else if ( currentChar == 'r' ) {
                pieces[ 9 ] |= cursorSB;
            } else if ( currentChar == 'q' ) {
                pieces[ 10 ] |= cursorSB;
            } else if ( currentChar == 'k' ) {
                pieces[ 11 ] |= cursorSB;
            }
            cursorSB <<= 1;
        }
    }

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

    private static EnumSet<Square> pLegalAccessPawns( // p for pseudo
        Square sq, Position pos, Colour activeColor ) throws Exception {
        EnumSet<Square> pawnsWithAccess = EnumSet.noneOf( Square.class );

        EnumSet<Square> pawnsSquares
            = MoveGenerator.pawnsSquares( sq, resolvePieceColor( sq, pos ) );
        for ( Square pSq : pawnsSquares ) {
            if ( resolvePieceType( pSq, pos ) == PieceType.PAWN
                && resolvePieceColor( pSq, pos ) == activeColor ) {
                pawnsWithAccess.add( pSq );
            }
        }

        return pawnsWithAccess;
    }

    private static EnumSet<Square> pLegalAccessBishops( // p for pseudo
        Square sq, Position pos, Colour activeColor, boolean queensInstead )
        throws Exception {
        // Search for queens instead of bishops?
        PieceType bishopOrQueen
            = queensInstead ? PieceType.QUEEN : PieceType.BISHOP;

        EnumSet<Square> bishopsWithAccess = EnumSet.noneOf( Square.class );

        for ( Direction dir : Direction.intermediateDirections() ) {
            Square nextSq = sq;
            while ( true ) {
                nextSq = adjacentSquare( nextSq, dir );
                if ( nextSq == null ) {
                    break;
                } else if ( resolvePieceType( nextSq, pos ) != null ) {
                    if ( resolvePieceType( nextSq, pos ) == bishopOrQueen
                        && resolvePieceColor( nextSq, pos ) == activeColor ) {
                        bishopsWithAccess.add( nextSq );
                    }
                    break;
                }
            }
        }

        return bishopsWithAccess;
    }

    private static EnumSet<Square> pLegalAccessKnights( // p for pseudo
        Square sq, Position pos, Colour activeColor ) throws Exception {
        EnumSet<Square> knightsWithAccess = EnumSet.noneOf( Square.class );

        EnumSet<Square> knightsSquares = MoveGenerator.knightsSquares( sq );
        for ( Square kSq : knightsSquares ) {
            if ( resolvePieceType( kSq, pos ) == PieceType.KNIGHT
                && resolvePieceColor( kSq, pos ) == activeColor ) {
                knightsWithAccess.add( kSq );
            }
        }

        return knightsWithAccess;
    }

    private static EnumSet<Square> pLegalAccessRooks( // p for pseudo
        Square sq, Position pos, Colour activeColor, boolean queensInstead )
        throws Exception {
        // Search for queens instead of rooks?
        PieceType rookOrQueen
            = queensInstead ? PieceType.QUEEN : PieceType.ROOK;

        EnumSet<Square> rooksWithAccess = EnumSet.noneOf( Square.class );

        for ( Direction dir : Direction.cardinalDirections() ) {
            Square nextSq = sq;
            while ( true ) {
                nextSq = adjacentSquare( nextSq, dir );
                if ( nextSq == null ) {
                    break;
                } else if ( resolvePieceType( nextSq, pos ) != null ) {
                    if ( resolvePieceType( nextSq, pos ) == rookOrQueen
                        && resolvePieceColor( nextSq, pos ) == activeColor ) {
                        rooksWithAccess.add( nextSq );
                    }
                    break;
                }
            }
        }

        return rooksWithAccess;
    }

    private static EnumSet<Square> pLegalAccessQueens( // p for pseudo
        Square sq, Position pos, Colour activeColor ) throws Exception {
        EnumSet<Square> queensWithAccess = EnumSet.noneOf( Square.class );

        queensWithAccess.addAll(
            pLegalAccessBishops( sq, pos, activeColor, true ) );
        queensWithAccess.addAll(
            pLegalAccessRooks( sq, pos, activeColor, true ) );

        return queensWithAccess;
    }

    private static EnumSet<Square> pLegalAccessKing( // p for pseudo
        Square sq, Position pos, Colour activeColor ) throws Exception {
        EnumSet<Square> kingWithAccess = EnumSet.noneOf( Square.class );

        EnumSet<Square> kingsSquares = MoveGenerator.kingsSquares( sq );
        for ( Square kSq : kingsSquares ) {
            if ( resolvePieceType( kSq, pos ) == PieceType.KING
                && resolvePieceColor( kSq, pos ) == activeColor ) {
                kingWithAccess.add( kSq );
                break; // There's only one king
            }
        }

        return kingWithAccess;
    }
}
