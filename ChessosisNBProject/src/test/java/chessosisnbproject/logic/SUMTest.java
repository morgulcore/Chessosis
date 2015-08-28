package chessosisnbproject.logic;

import chessosisnbproject.data.PieceType;
import chessosisnbproject.data.Direction;
import chessosisnbproject.data.Colour;
import chessosisnbproject.data.CSS;
import chessosisnbproject.data.Square;
import java.util.EnumSet;
import java.util.Random;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public class SUMTest {

    /*
     Tests bitboardToSquareSet() with the binary number sequence
     (0, 1, 11, 111, ...) where the last value has all 64 bits set.
     Part one of the test checks the square set sizes.
     */
    @Test
    public void testBitboardToSquareSetWithBitPatternOfIncreasingSetBitsPartOne()
        throws Exception {
        long bitPattern = 0;

        // Testing with a null bitboard to start with
        assertEquals( 0, SUM.bitboardToSqSet( bitPattern ).size() );

        // Indicates the number of set bits
        int bitCounter = 0;
        for ( Square square : Square.values() ) {
            // The bitwise OR'ing "adds" to bitPattern one bit at a time
            bitPattern |= square.bit();
            bitCounter++;
            assertEquals( bitCounter,
                SUM.bitboardToSqSet( bitPattern ).size() );
        }
    }

    /*
     Part 2 of the test with the bit sequence (1, 11, 111, ...). In
     the test the bit sequence is translated into a "sequence of sequences"
     ( (A1), (A1,B1), (A1,B1,C1), ... ). In the final square sequence all
     64 squares are listed in the order defined in enum type Square
     (assuming bitboardToSquareSet() is doing its job). The test works
     by checking the ordinals of the enum constants against a reference
     counter.
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
            EnumSet<Square> squareSet = SUM.bitboardToSqSet( bitPattern );

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

    /*
     Tests squareSetToBitboard() with a few square sets. As there are 2^64
     unique square sets, this is certainly not an ideal approach to testing
     the method.
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

    /*
     Verifies that bitboardToSquareSet() and squareSetToBitboard() are
     inverse functions. In general this sort of a test consists of two
     parts: f(g(x)) == x and g(f(x)) == x
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
            == SUM.squareSetToBitboard( SUM.bitboardToSqSet( randomBitboard ) );
    }

    // Part two of the inverse function test: g(f(x)) == x
    private boolean inverseFunctionTestPartTwo() throws Exception {

        EnumSet<Square> randomSquareSet = generateRandomSquareSet();
        EnumSet<Square> squareSetReturned
            = SUM.bitboardToSqSet( SUM.squareSetToBitboard( randomSquareSet ) );
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

    /*
     Tests squareBitToSquare( long squareBit ) with all 64 valid input values.
     */
    @Test
    public void squareBitToSquareWorksWithValidInput() throws Exception {
        long leftShiftingBit = 1;

        for ( Square square : Square.values() ) {
            assertEquals( square, SUM.squareBitToSquare( leftShiftingBit ) );
            leftShiftingBit <<= 1;
        }
    }

    /*
     Verifies that validSquareBit returns true in all cases it should.
     */
    @Test
    public void validSquareBitReturnsTrueWhenItShould() {
        for ( Square square : Square.values() ) {
            assertTrue( SUM.validSquareBit( square.bit() ) );
        }
    }

    /*
     Tests validSquareBit with random longs. It's extremely unlikely that
     any of the random values would be a square bit so all of the return
     values should be false.
     */
    @Test
    public void validSquareBitReturnsFalseWhenItShould() {
        // Let's get the random number generator started.
        Random random = new Random();

        for ( int i = 1; i <= 10000; i++ ) {
            assertFalse( SUM.validSquareBit( random.nextLong() ) );
        }
    }

    /*
     Verifies that each element in a set returned by method
     splitBitboardIntoSetOfSquareBits( long bitboard ) is indeed a
     square bit. Random values are used here as testing all possible
     long values doesn't seem realistic.
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

    /*
     Tests numberOfSetBits( long bitboard ) with an "increasing" bit
     pattern of 0, 1, 11, 111, ..., the last pattern having 64 set bits.
     During the test the method should return the ascending sequence
     0, 1, 2, ..., 64.
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

    /*
     Checks the set size returned by
     splitBitboardIntoSetOfSquareBits( long bitboard ) for consistency.
     For example, if the bitboard argument is 7 (i.e., 4 + 2 + 1), then
     the set size should be 3.
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

    /*
     Verifies that randomSquare() eventually returns each and every
     constant defined in enum type Square. Note that failure in this
     test means an infinite loop, not explicit failure as in fail().
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

    /*
     Tests methods fileOfSquare() by calling it with random Square values.
     Quick and dirty testing, I admit.
     */
    @Test
    public void fileOfSquareWorks() {
        for ( int i = 1; i <= 10000; i++ ) {
            Square square = SUM.randomSquare();
            final long fileBB = SUM.fileOfSquare( square );

            if ( fileBB == CSS.FILE_A
                && square.toString().charAt( 0 ) == 'A' ) {
                continue;
            } else if ( fileBB == CSS.FILE_B
                && square.toString().charAt( 0 ) == 'B' ) {
                continue;
            } else if ( fileBB == CSS.FILE_C
                && square.toString().charAt( 0 ) == 'C' ) {
                continue;
            } else if ( fileBB == CSS.FILE_D
                && square.toString().charAt( 0 ) == 'D' ) {
                continue;
            } else if ( fileBB == CSS.FILE_E
                && square.toString().charAt( 0 ) == 'E' ) {
                continue;
            } else if ( fileBB == CSS.FILE_F
                && square.toString().charAt( 0 ) == 'F' ) {
                continue;
            } else if ( fileBB == CSS.FILE_G
                && square.toString().charAt( 0 ) == 'G' ) {
                continue;
            } else if ( fileBB == CSS.FILE_H
                && square.toString().charAt( 0 ) == 'H' ) {
                continue;
            }

            fail();
        }
    }

    /*
     Tests method rankOfSquare with random Square constant values.
     */
    @Test
    public void rankOfSquareWorks() {
        for ( int i = 1; i <= 10000; i++ ) {
            Square square = SUM.randomSquare();
            final long rankBB = SUM.rankOfSquare( square );

            if ( rankBB == CSS.RANK_1
                && square.toString().charAt( 1 ) == '1' ) {
                continue;
            } else if ( rankBB == CSS.RANK_2
                && square.toString().charAt( 1 ) == '2' ) {
                continue;
            } else if ( rankBB == CSS.RANK_3
                && square.toString().charAt( 1 ) == '3' ) {
                continue;
            } else if ( rankBB == CSS.RANK_4
                && square.toString().charAt( 1 ) == '4' ) {
                continue;
            } else if ( rankBB == CSS.RANK_5
                && square.toString().charAt( 1 ) == '5' ) {
                continue;
            } else if ( rankBB == CSS.RANK_6
                && square.toString().charAt( 1 ) == '6' ) {
                continue;
            } else if ( rankBB == CSS.RANK_7
                && square.toString().charAt( 1 ) == '7' ) {
                continue;
            } else if ( rankBB == CSS.RANK_8
                && square.toString().charAt( 1 ) == '8' ) {
                continue;
            }

            fail();
        }
    }

    /*
     A comprehensive test for adjacentSquare(). The method is tested with
     each of the 64 squares in all directions. The test works by comparing
     the set of squares generated in the two for loops to that returned
     by MoveGenerator.kingsSquares().

     @throws Exception
     */
    @Test
    public void adjacentSquareWorksWithAllSquaresAndDirections()
        throws Exception {
        // Create empty EnumSet of Squares
        for ( Square square : Square.values() ) {
            EnumSet<Square> squareSet = EnumSet.noneOf( Square.class );
            for ( Direction direction : Direction.values() ) {
                Square squareReturned
                    = SUM.adjacentSquare( square, direction );
                if ( squareReturned != null ) {
                    squareSet.add( squareReturned );
                }
            }
            assertEquals( MoveGenerator.kingsSquares( square ), squareSet );
        }
    }

    /*
     Tests determineTypeOfChessman() with 12 different chessmen, two for
     each type (PAWN, BISHOP, etc.). The test succeeds if each of the
     chessmen returned is of the expected type.

     FEN: 8/pbnrqk2/8/8/8/8/PBNRQK2/8 w - - 0 1
     */
    @Test
    public void testingDetermineTypeOfChessmanWithTwelveChessmen() {
        Position testPosition
            = new Position(
                CSS.A2, CSS.B2, CSS.C2, CSS.D2, CSS.E2, CSS.F2,
                CSS.A7, CSS.B7, CSS.C7, CSS.D7, CSS.E7, CSS.F7,
                Colour.WHITE );

        // Squares to loop over; contains 12 squares
        Square[] squares = { Square.A2, Square.A7, Square.B2, Square.B7,
            Square.C2, Square.C7, Square.D2, Square.D7,
            Square.E2, Square.E7, Square.F2, Square.F7 };

        // Contains the six Chessman constants. They should be in the
        // following order: PAWN, BISHOP, KNIGHT, ROOK, QUEEN, KING
        PieceType[] theSixChessmenTypes = PieceType.availablePieceTypes();

        // During the loop, the following index has all the values from
        // 0 to 11, inclusive
        int chessmenTypesIndex = 0;
        for ( Square square : squares ) {
            // 'chessmenTypesIndex / 2' equals the values from 0 to 5,
            // inclusive, each one repeated twice. This is due to integer
            // division.
            assertEquals( theSixChessmenTypes[ chessmenTypesIndex / 2 ],
                SUM.resolvePieceType( square, testPosition ) );
            chessmenTypesIndex++;
        }
    }

    /*
     Tests determineTypeOfChessman with a particular position to see if
     it really returns null on empty squares.

     FEN: 8/pbnrqk2/8/8/8/8/PBNRQK2/8 w - - 0 1
     */
    @Test
    public void determineTypeOfChessmanReturnsNullForEmptySquares()
        throws Exception {
        Position testPosition
            = new Position(
                CSS.A2, CSS.B2, CSS.C2, CSS.D2, CSS.E2, CSS.F2,
                CSS.A7, CSS.B7, CSS.C7, CSS.D7, CSS.E7, CSS.F7,
                Colour.WHITE );

        // Getting an empty squares BB by flipping zeroes to ones and
        // vica versa
        long emptySquaresBB = ~testPosition.bothArmies();
        EnumSet<Square> emptySquares
            = SUM.bitboardToSqSet( emptySquaresBB );
        // Looping over the 52 empty squares
        for ( Square square : emptySquares ) {
            assertEquals( null, SUM.resolvePieceType(
                square, testPosition ) );
        }
    }

    /*
     validateFENRecord(), valid input test
     */
    @Test
    public void validateFENRecordReturns0() {
        // Should contain each of the positions from Morhpy's
        // famous Opera Game
        String[] validFENRecords = {
            // Standard starting position
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
            // After 1.e4
            "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1",
            // After 1...e5
            "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2",
            // After 2.Nf3
            "rnbqkbnr/pppp1ppp/8/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2"
        };

        for ( String s : validFENRecords ) {
            assertEquals( 0, SUM.validateFENRecord( s ) );
        }
    }

    /*
     validateFENRecord(), invalid characters test
     */
    @Test
    public void validateFENRecordReturns1() {
        String[] invalidFENRecords = {
            // String length less than one
            "",
            // Invalid character 'x'
            "x",
            // 'i'
            "hi",
            // 'W'
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR W KQkq - 0 1",
            // '\n', newline
            "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR\nb KQkq e3 0 1",
            // '\t', tab
            "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq\tc6 0 2",
            // ',', comma
            "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R, b KQkq - 1 2",
            // 'ö'
            "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R ö KQkq - 1 2" };

        for ( String s : invalidFENRecords ) {
            assertEquals( 1, SUM.validateFENRecord( s ) );
        }
    }

    /*
     validateFENRecord(), six space-separated fields test with invalid
     FEN records
     */
    @Test
    public void validateFENRecordReturns2() {
        String[] invalidFENRecords = {
            // Begins with a space
            " rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
            // Ends with a space
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 ",
            // One field
            "abcdef",
            // Five fields
            "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNRb KQkq e3 0 1",
            // Seven fields
            "1 2 3 4 5 6 7",
            // Empty string field (two consecutive spaces)
            "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq  1 2"
        };

        for ( String s : invalidFENRecords ) {
            assertEquals( 2, SUM.validateFENRecord( s ) );
        }
    }

    /*
     validateFENRecord(), malformed first field detection test
     */
    @Test
    public void validateFENRecordReturns3() {
        String[] invalidFENRecords = {
            // Empty subfield in first FEN record field
            "rnbqkbnr/pppppppp/8//8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
            // Extra subfield
            "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR/8 w KQkq c6 0 2",
            // Single subfield
            "RNBQKBNR w KQkq - 0 1",
            // '/' at beginning
            "/rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
            // Trailing '/'
            "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR/ b KQkq e3 0 1",
            // Invalid char '9'
            "rnbqkbnr/pppppppp/8/9/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1",
            // Empty string subfield at end
            "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/ w KQkq c6 0 2"
        };

        for ( String s : invalidFENRecords ) {
            assertEquals( 3, SUM.validateFENRecord( s ) );
        }
    }

    /*
     validateFENRecord(), consecutive digits detection test, applies to the
     first field of a FEN record
     */
    @Test
    public void validateFENRecordReturns4() {
        String[] invalidFENRecords = {
            // Two consecutive digits, exactly
            "rnbqkbnr/pppppppp/71/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
            // Three consecutive digits
            "rnbqkbnr/pppppppp/8/8/4P111/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1",
            // Five consecutive digits
            "rnbqkbnr/pp1ppppp/12345/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2",
            // Two consecutive digits at the beginning of the first field
            "12bqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2",
            // Two consecutive digits at the end of the first field
            "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKB78 w KQkq c6 0 2"
        };

        for ( String s : invalidFENRecords ) {
            assertEquals( 4, SUM.validateFENRecord( s ) );
        }
    }

    /*
     validateFENRecord(): Testing the detection of ranks with an invalid
     number of squares. The valid number of squares is always eight. For
     example, pp1ppppp = 8 and 2p5 = 8.
     */
    @Test
    public void validateFENRecordReturns5() {
        String[] invalidFENRecords = {
            // Extra rook/square on the 8th rank
            "rnbqkbnrr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
            // Missing pawn/square on the 7th rank
            "rnbqkbnr/ppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1",
            // Invalid number of squares on 6th rank
            "rnbqkbnr/pp1ppppp/1/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2",
            // Way too many squares on 1st rank
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R8B7K6N5 w KQkq - 0 1",
            // Extra square on the 7th rank
            "6k1/r1q1b3n/6QP/p3R3/1p3p2/1P6/1PP2P2/2K4R b - - 1 35",
            // Extra pawn on the 2nd rank
            "6k1/r1q1b2n/6QP/p3R3/1p3p2/1P6/1PP2P2P/2K4R b - - 1 35"
        };

        for ( String s : invalidFENRecords ) {
            assertEquals( 5, SUM.validateFENRecord( s ) );
        }
    }
}
