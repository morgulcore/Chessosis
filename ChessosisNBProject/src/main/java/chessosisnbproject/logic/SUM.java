package chessosisnbproject.logic;

import chessosisnbproject.data.PieceType;
import chessosisnbproject.data.Direction;
import chessosisnbproject.data.CSS;
import chessosisnbproject.data.Colour;
import chessosisnbproject.data.Piece;
import chessosisnbproject.data.Square;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

/**
 SUM (Static Utility Methods) is a sort of a lightweight library or collection
 of static methods needed by other classes. No objects of type SUM should be
 instantiated as the class contains only static members.

 @author Henrik Lindberg
 */
public class SUM {

    // Private constructor -- no instances, no Javadoc
    private SUM() {
    }

    /**
     Used to determine the pieces of the active color that have "pseudo-legal"
     access to the square specified by the first parameter. The main use of
     the method is checking the legality of a newly generated position. If any
     piece of the active color has pseudo-legal access to the square of the
     enemy king (i.e., can capture it), then the position is illegal and the
     move leading to it should be discarded from the list of legal move
     candidates.

     @param sq the square to which access will be determined
     @param pos the context
     @return the set of pieces that have pseudo-legal access to the given
     square
     @throws Exception
     */
    public static EnumSet<Square> pseudoLegalAccess( Square sq, Position pos )
            throws Exception {
        EnumSet<Square> activePiecesWithAccess
                = EnumSet.noneOf( Square.class );
        Colour activeColor
                = (pos.turn() == Colour.WHITE) ? Colour.WHITE : Colour.BLACK;

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
     Converts a bitboard-based square set representation to a Square
     EnumSet-based one. This method is the inverse function of
     squareSetToBitboard.

     @param bitboard the Java long to convert
     @return a Square EnumSet
     @throws Exception might be thrown in squareBitToSquare()
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
     Converts a Square EnumSet-based square set representation to a
     bitboard-based one. This method is the inverse function of
     bitboardToSquareSet.

     @param squareSet the Square EnumSet to convert
     @return a bitboard
     @throws Exception in case the parameter receives null
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
     Square bit to enum type Square conversion method.

     @param squareBit Java long to convert
     @return a Square constant; should never return null
     @throws Exception in case of an invalid square bit
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
     Determines whether a bitboard is a square bit. A square bit (bitboard)
     has exactly one bit set. In other words, its a value 2^n where n is
     between 0 and 63.

     @param bitboard the bitboard to examine
     @return true when bitboard is a square bit
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
     Splits a bitboard into its square bit "elements". For example, a bitboard
     with the decimal value 7 (first three bits set) would result in a set of
     three square bits with the values 1, 2 and 4. The size of the set is in
     all cases equal to the number of set bits in the bitboard.

     @param bitboard the bitboard to split
     @return a set of square bits
     */
    public static Set<Long> splitBitboardIntoSetOfSquareBits( long bitboard ) {
        // LinkedHashSet orders its elements based on the order in which
        // they were inserted into the set. The set is empty to begin with.
        Set<Long> setOfSquareBits = new LinkedHashSet<>();
        long leftShiftingValue = 1;

        do {
            // If the condition is true, bitboard has a bit set at the
            // position indicated by leftShiftingValue
            if ( (leftShiftingValue & bitboard) != 0 ) {
                setOfSquareBits.add( leftShiftingValue );
            }
            leftShiftingValue <<= 1;
        } while ( leftShiftingValue != 0 );

        return setOfSquareBits;
    }

    /**
     Counts the number of set bits in a bitboard.

     @param bitboard a bitboard with 0 to 64 set bits
     @return a value between 0 and 64
     */
    public static int numberOfSetBits( long bitboard ) {
        int setBitCount = 0;
        long leftShiftingValue = 1;

        do {
            // A set bit has been found if the bitwise AND produces
            // a non-zero result
            if ( (leftShiftingValue & bitboard) != 0 ) {
                setBitCount++;
            }
            leftShiftingValue <<= 1;
        } while ( leftShiftingValue != 0 );

        return setBitCount;
    }

    /**
     Returns a random Square constant.

     @return random Square constant
     */
    public static Square randomSquare() {
        Random random = new Random();

        char[] randomFileAndRank = {
            // Random file
            (char) (65 + random.nextInt( 8 )),
            // Random rank
            (char) (49 + random.nextInt( 8 )) };
        String randomSquareString = new String( randomFileAndRank );

        return Square.valueOf( randomSquareString );
    }

    /**
     Returns the CSS.FILE_? constant that corresponds to the file the Square
     parameter is located on.

     @param square the square to operate on
     @return one of the CSS.FILE_? constants
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
     Returns the CSS.RANK_? constant that corresponds to the rank the Square
     parameter is located on.

     @param square the square to operate on
     @return one of the CSS.RANK_? constants
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
     Each square on the board has at most eight adjacent squares as
     illustrated by the following diagram (using E4 as the square for the
     method's first parameter).

     <pre>
     +--------------+       N
     | D5 | E5 | F5 |       ^
     |---------------       |
     | D4 | E4 | F4 |  W {--+--} E
     ----------------       |
     | D3 | E3 | F3 |       v
     +--------------+       S
     </pre>

     View the bitboard index diagram at the beginning of CSS.java to get an
     idea on how the method accomplishes its task.

     @param square the square to operate on
     @param direction the direction of the adjacent square
     @return the adjacent square or null if no such square exists
     @throws Exception
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
                if ( (adjacentSquareBB & SUM.rankOfSquare( square )) == 0 ) {
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
                if ( (adjacentSquareBB & SUM.rankOfSquare( square )) == 0 ) {
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
     Returns the piece located on the square argument or null if the square is
     empty. Strictly speaking the method returns an enum constant that
     identifies the type and color of the piece.

     @param sq the square to examine
     @param pos the context
     @return the piece found on the square
     */
    public static Piece resolvePiece( Square sq, Position pos ) {
        PieceType pieceType = resolvePieceType( sq, pos );
        if ( pieceType == null ) {
            return null;
        }
        Colour pieceColor = ((pos.whiteArmy() & sq.bit()) != 0)
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
     Resolves the color of the piece found on the square parameter or null if
     it is empty.

     @param sq the square to examine
     @param pos the context
     @return the color of the piece or null
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
     Determines the type of chessman located on the given square. Possible
     values include PAWN, BISHOP, KNIGHT, ROOK, QUEEN and KING. If the square
     parameter is an empty square, the method returns null.

     @param square the square to examine
     @param position the context
     @return the type of the chessman on the square
     */
    public static PieceType resolvePieceType(
            Square square, Position position ) {
        // Pawns
        if ( (square.bit() & position.whitePawns()) != 0
                || (square.bit() & position.blackPawns()) != 0 ) {
            return PieceType.PAWN;
        } // Bishops
        else if ( (square.bit() & position.whiteBishops()) != 0
                || (square.bit() & position.blackBishops()) != 0 ) {
            return PieceType.BISHOP;
        } // Knights
        else if ( (square.bit() & position.whiteKnights()) != 0
                || (square.bit() & position.blackKnights()) != 0 ) {
            return PieceType.KNIGHT;
        } // Rooks
        else if ( (square.bit() & position.whiteRooks()) != 0
                || (square.bit() & position.blackRooks()) != 0 ) {
            return PieceType.ROOK;
        } // Queens
        else if ( (square.bit() & position.whiteQueens()) != 0
                || (square.bit() & position.blackQueens()) != 0 ) {
            return PieceType.QUEEN;
        } // Kings
        else if ( (square.bit() & position.whiteKing()) != 0
                || (square.bit() & position.blackKing()) != 0 ) {
            return PieceType.KING;
        }
        return null;
    }

    /**
     Returns a two-dimensional String array that is used in drawing the chess
     pieces (which are actually characters) seen on the GUI.

     @param pos the position to operate on
     @return a table of characters representing the input position
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
     Returns the square name that corresponds to a given cell in an 8-by-8
     table. The cell is identified by the row and col parameters. For example,
     tableCellToSquare( 0, 0 ) would return Square.A8 and tableCellToSquare(
     7, 7 ) Square.H1

     @param row the row (or chessboard rank)
     @param col the column (or chessboard file)
     @return
     */
    public static Square tableCellToSquare( int row, int col ) {
        char file = (char) ('A' + col),
                rank = (char) ('8' - row);
        return Square.valueOf(
                // Note the string concatenation. This is not about
                // integer arithmetic.
                (char) file + "" + (char) rank );
    }

    /**
     Used to split a FEN string into its six constituent fields.

     @param fEN the FEN string to split
     @return a String array of six elements
     @throws Exception
     */
    public static String[] splitFENIntoFields( String fEN ) throws Exception {
        // Field separator: a sequence of one of more tabs or spaces
        String[] fENFields = fEN.split( "\\p{Blank}++" );
        if ( fENFields.length != 6 ) {
            throw new Exception(
                    "Invalid number of FEN fields: " + fENFields.length );
        }
        return fENFields;
    }

    /**
     Splits the first field of a FEN string into eight parts. Each of these
     parts represents a rank on the chessboard along with the pieces placed on
     it. As an example consider the standard starting position:
     <p>
     rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR

     @param firstFENField the first field of a FEN string
     @return a String array representing the eight ranks of the board
     @throws Exception
     */
    public static String[] splitFirstFENField( String firstFENField )
            throws Exception {
        String[] fENRanks = firstFENField.split( "/" );
        if ( fENRanks.length != 8 ) {
            throw new Exception(
                    "Invalid number of rank fields: " + fENRanks.length );
        }
        return fENRanks;
    }

    /**
     Converts the eight ranks of the FEN strings' first field into an array of
     12 bitboards. Both the eight ranks and the 12 bitboards are a means to
     express the same thing -- the piece placement of the board.

     @param fENRanks the first field of a FEN string
     @return an array of 12 bitboards that expresses piece placement on the
     board
     @throws Exception
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

     The ordinal number of the tests correspond to the integer value
     returned by the method should the test fail. If all of the tests
     succeed, the method returns 0.

     JUNIT TESTS:
     --validateFENRecordReturns0()
     --validateFENRecordReturns1()
     --validateFENRecordReturns2()
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
                '^' +
                charClassPlus + '/' + charClassPlus + '/' +
                charClassPlus + '/' + charClassPlus + '/' +
                charClassPlus + '/' + charClassPlus + '/' +
                charClassPlus + '/' + charClassPlus + " [a-h 0-9kKqQw-]+$",
                fENRecord ) ) {
            return 3;
        }

        return 0;
    }

    /**
     Returns the square bit at a particular bit index.

     @param index The bit index of a 64-bit integer (an int between 0 and 63).
     @return A square bit, i.e., a bitboard with a single bit set.
     @throws Exception In case of an invalid index.
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
        if ( (square.bit() & CSS.CORNER_SQUARES) != 0 ) {
            return adjacentSquareOnDiagonalOfCornerSquare( square, direction );
        } // The following two if's deal with non-corner squares on rank 1 and 8
        else if ( (square.bit() & CSS.RANK_1) != 0 ) {
            return adjacentSquareOnDiagonalOfSquareOnRank1( square, direction );
        } else if ( (square.bit() & CSS.RANK_8) != 0 ) {
            return adjacentSquareOnDiagonalOfSquareOnRank8( square, direction );
        } // The following two if's deal with non-corner squares on file A and H
        else if ( (square.bit() & CSS.FILE_A) != 0 ) {
            return adjacentSquareOnDiagonalOfSquareOnFileA( square, direction );
        } else if ( (square.bit() & CSS.FILE_H) != 0 ) {
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

        if ( (pos.whiteKing() & sq.bit()) != 0 ) {
            return "\u2654";
        } else if ( (pos.whiteQueens() & sq.bit()) != 0 ) {
            return "\u2655";
        } else if ( (pos.whiteRooks() & sq.bit()) != 0 ) {
            return "\u2656";
        } else if ( (pos.whiteBishops() & sq.bit()) != 0 ) {
            return "\u2657";
        } else if ( (pos.whiteKnights() & sq.bit()) != 0 ) {
            return "\u2658";
        } else if ( (pos.whitePawns() & sq.bit()) != 0 ) {
            return "\u2659";
        } else if ( (pos.blackKing() & sq.bit()) != 0 ) {
            return "\u265a";
        } else if ( (pos.blackQueens() & sq.bit()) != 0 ) {
            return "\u265b";
        } else if ( (pos.blackRooks() & sq.bit()) != 0 ) {
            return "\u265c";
        } else if ( (pos.blackBishops() & sq.bit()) != 0 ) {
            return "\u265d";
        } else if ( (pos.blackKnights() & sq.bit()) != 0 ) {
            return "\u265e";
        } else if ( (pos.blackPawns() & sq.bit()) != 0 ) {
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
