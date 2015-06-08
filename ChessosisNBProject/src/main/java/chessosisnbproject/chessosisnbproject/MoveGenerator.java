package chessosisnbproject.chessosisnbproject;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class is responsible for calculating the possible (legal) moves
 * for a given position. The means for accomplishing the task is the static
 * method moveGenerator() which takes a Position object as its single parameter
 * and returns a set of Move objects each of which represent a possible move.
 * The class also contains other static methods, many of which are public.
 * All the methods of the class contribute to the move generation process
 * and operations in one way or the other.
 *
 * @author Henrik Lindberg
 */
public class MoveGenerator {

    /**
     * Generates the set of possible (legal) moves for a given position.
     * This is one of the fundamental mechanisms of Chessosis. A lot of
     * effort should and will be invested on testing this method.
     *
     * @param position the Position object to examine
     * @return a set of zero or more Move objects
     * @throws Exception
     */
    public static Set<Move> moveGenerator( Position position )
        throws Exception {
        // The set of Moves is empty to start with
        Set<Move> moves = new LinkedHashSet<>();

        long chessmenOfSideToMoveBB;
        if ( position.turn() == Color.WHITE ) {
            chessmenOfSideToMoveBB = position.whiteArmy();
        } else {
            chessmenOfSideToMoveBB = position.blackArmy();
        }
        EnumSet<Square> chessmenOfSideToMove
            = SUM.bitboardToSquareSet( chessmenOfSideToMoveBB );

        for ( Square chessman : chessmenOfSideToMove ) {
            EnumSet<Square> chessmansDestSquares
                = potentialDestSquares( chessman, position );
            // Chessman is a king
            if ( ( chessman.bit() & position.whiteKing() ) != 0
                || ( chessman.bit() & position.blackKing() ) != 0 ) {
                // Set difference
                chessmansDestSquares.removeAll(
                    squaresUnderAttack( position ) );
            }
            moves.addAll( generateMoveSetForChessman(
                chessman, chessmansDestSquares ) );
        }

        return moves;
    }

    // Discover the potential destination squares of a chessman.
    private static EnumSet<Square> potentialDestSquares(
        Square chessman, Position position ) throws Exception {
        // The chessman is a king
        if ( ( chessman.bit() & position.whiteKing() ) != 0
            || ( chessman.bit() & position.blackKing() ) != 0 ) {
            return kingsSquares( chessman );
        }

        return null;
    }

    // Finds the squares that are "under attack" by the opposing side, i.e.,
    // the side who's turn it is *not* to move. A square is under attack by
    // a chessman if the chessman could move into it.
    private static EnumSet<Square> squaresUnderAttack( Position position )
        throws Exception {
        EnumSet<Square> squaresUnderAttack = EnumSet.noneOf( Square.class );
        long chessmenOfSideNotToMoveBB;
        if ( position.turn() == Color.WHITE ) {
            chessmenOfSideNotToMoveBB = position.blackArmy();
        } else {
            chessmenOfSideNotToMoveBB = position.whiteArmy();
        }
        EnumSet<Square> chessmenOfSideNotToMove
            = SUM.bitboardToSquareSet( chessmenOfSideNotToMoveBB );

        for ( Square chessman : chessmenOfSideNotToMove ) {
            if ( ( chessman.bit() & position.whiteKing() ) != 0
                || ( chessman.bit() & position.blackKing() ) != 0 ) {
                squaresUnderAttack.addAll( kingsSquares( chessman ) );
            }
        }

        return squaresUnderAttack;
    }

    // Generates the set of possible moves for a single chessman
    private static Set<Move> generateMoveSetForChessman(
        Square chessman, EnumSet<Square> chessmansDestSquares ) {
        Set<Move> moves = new LinkedHashSet<>();
        if ( chessmansDestSquares.isEmpty() ) {
            return moves;
        }

        for ( Square destSquare : chessmansDestSquares ) {
            moves.add( new Move( chessman, destSquare ) );
        }

        return moves;
    }

    /**
     * An alias for surroundingSquares(), added for the sake of consistency.
     * Similar name, similar basic idea: kingsSquares(), queensSquares(),
     * rooksSquares(), knightsSquares(), bishopsSquares().
     *
     * @param square any square on the board
     * @return a set of squares
     * @throws Exception
     */
    public static EnumSet<Square> kingsSquares( Square square )
        throws Exception {
        return surroundingSquares( square );
    }

    /**
     * Returns the rook's squares of the square parameter. The rook's squares
     * mean the squares to which a rook could move to on an empty board.
     *
     * @param square
     * @return 
     * @throws java.lang.Exception
     */
    public static EnumSet<Square> rooksSquares( Square square ) throws Exception {
        // XOR'ing the relevant file and rank is all that is needed to produce
        // the set of rook's squares
        long rooksSquaresBB
            = SUM.fileOfSquare( square ) ^ SUM.rankOfSquare( square );
        return SUM.bitboardToSquareSet( rooksSquaresBB );
    }

    /**
     * Returns the surrounding squares of its Square parameter. Surrounding
     * squares are the squares to which a king can move from a particular
     * square. Note that the size of the set returned by the method is always
     * either 3, 5 or 8 depending on where the Square argument is located
     * on the board (corners, edges or elsewhere).
     *
     * @param square any square on the board
     * @return a set of squares
     * @throws Exception
     */
    public static EnumSet<Square> surroundingSquares( Square square )
        throws Exception {
        // The square is a corner square
        if ( ( square.bit() & CSS.CORNER_SQUARES ) != 0 ) {
            return surroundingSquaresOfCorners( square );
        } // The square is on the edge of the board
        else if ( ( square.bit() & CSS.EDGE ) != 0 ) {
            return surroundingSquaresOfSquaresOnEdge( square );
        }
        // Still here, so the square is one of the 36 non-edge squares
        return surroundingSquaresOfSquaresNotOnEdge( square );
    }

    /**
     * Calculates the set of squares to which a rook could move to. As an
     * example, view the following position by pasting this FEN string (sans
     * quotes) into Chess.com's Analysis Board Editor:
     * "4k3/8/8/4K3/8/2r1R3/8/8 w - - 0 1". Using that position as the
     * second argument to accessibleRooksSquares() and CSS.E3 as the first
     * one, the method would return the square set {E4, E2, E1, C3, D3, F3,
     * G3, H3}. That's all the squares the rook can move to including the
     * capture on C3. Note that there doesn't have to be a rook located at
     * the method's Square parameter; the method will calculate the
     * accessible rook's squares of any square whether or not there's an
     * actual rook on it.
     * <p>
     * Consider another position: "4k3/8/8/2b5/3R4/4K3/8/8 w - - 0 1" for
     * which accessibleRooksSquares() -- assuming correct operation --
     * would return { D8, D7, D6, D5, D3, D2, D1, A4, B4, C4, E4,
     * F4, G4, H4 }. Yet the rook cannot move anywhere without exposing
     * the white king to check, i.e., moving the rook is illegal. This
     * illustrates the fact that the "moves" (destination squares) generated
     * by the method are pseudo-legal.
     *
     * @param square the square of a real or imaginary rook
     * @param position the context of the operation
     * @return the set of squares where the rook could move to
     * @throws Exception
     */
    public static EnumSet<Square> accessibleRooksSquares( Square square,
        Position position ) throws Exception {
        long accessibleSquaresBB = 0;
        return SUM.bitboardToSquareSet( accessibleSquaresBB );
    }
    
    // Finds the accessible rook's squares on a particular rank
    /*
    private static long accessibleRooksSquaresOuterLoop(
        Square square, Position position ) throws Exception {
        // A bitboard describing the accessible rook's squares on the
        // rank of the Square parameter
        long accessibleSquaresOnRank = 0;
        // The rank on which the square parameter is located on
        final long RANK = SUM.rankOfSquare( square );

        // Go east on the first iteration of the for loop. "Going east"
        // means left-shifting a square bit while it is still located
        // on the rank we are operating on. For example, going east of
        // square E4 would involve the squares F4, G4 and H4.
        //
        // On the second iteration of the for loop, go west. This involves
        // unsigned right-shifting of the square bit.
        for ( int direction = 1; direction <= 4; direction++ ) {
            // Set the initial value for the shifting bit. It's either
            // one bit left or one bit right of the square parameter's bit.
            // This means it's a square to the right or left of the square
            // parameter. It can also be a square on a different rank if
            // the square parameter is one the a- or h-file. The condition
            // of the inner (while) loop should deal with this situation:
            // while ( ( shiftingBit & RANK ) != 0 ) { ... }
            long shiftingBit
                = ( direction == 1 ) ? ( square.bit() << 1 ) : ( square.bit() >>> 1 );
            switch ( direction ) {
                // North
                case 1:
                    shiftingBit <<= 8;
                    break;
                // East
                case 2:
                    shiftingBit <<= 1;
                    break;
                // South
                case 3:
                    shiftingBit >>> 
                    break;
                // West
                case 4:
                    break;
                default:
                    throw new Exception(
                        "Invalid for counter value: " + direction );
            }
            // Doing bitwise OR'ing which is really about a set union operation
            accessibleSquaresOnRank
                |= accessibleRooksSquaresInnerLoop(
                    position, accessibleSquaresOnRank, RANK, shiftingBit, direction );
        }
        return accessibleSquaresOnRank;
    }*/

    // Trying to hide the long and dirty details of
    // accessibleRooksSquaresEastAndWest()
    /*
    private static long accessibleRooksSquaresInnerLoop(
        Position position, long accessibleSquaresOnRank, final long RANK,
        long shiftingBit, int outerLoopCounter ) {

        while ( ( shiftingBit & RANK ) != 0 ) {
            // Square indicated by the shifting bit is not empty
            if ( ( shiftingBit & position.bothArmies() ) != 0 ) {
                // It's either
                // (1) White's turn with a black chessman blocking the rank
                // OR
                // (2) Black's turn with a white chessman blocking the rank
                if ( ( position.turn() == Color.WHITE
                    && ( shiftingBit & position.blackArmy() ) != 0 )
                    || ( position.turn() == Color.BLACK
                    && ( shiftingBit & position.whiteArmy() ) != 0 ) ) {
                    // A chessman of the opposing color can be captured,
                    // thus the square is accessible
                    accessibleSquaresOnRank |= shiftingBit;
                }
                // Start going west (second iteration of the for loop)
                // or if that has just been done, exit the outer loop.
                // Note that if the chessman blocking the rank was of
                // the same color as the side to move, its square was
                // not added to the set of accessible squares. No
                // chessman can capture a piece of its own color.
                break;
            }

            // Add the empty square indicated by the shifting bit to the
            // set of accessible squares
            accessibleSquaresOnRank |= shiftingBit;

            // After that, move the square bit one square left or right on
            // the rank. The while loop's condition will deal with the
            // situation where the square bit moves off the rank to the rank
            // above or below. Moving above the 8th rank or below the 1st
            // rank will result in shiftingBit becoming equal to zero. The
            // while condition works in this case, too.
            shiftingBit = ( outerLoopCounter == 1 )
                ? ( shiftingBit << 1 ) : ( shiftingBit >>> 1 );
        } // end while

        return accessibleSquaresOnRank;
    } */

    //
    // =====================================
    // == Private (static) helper methods ==
    // =====================================
    //
    //
    private static EnumSet<Square> surroundingSquaresOfCorners( Square square )
        throws Exception {
        EnumSet<Square> ss; // surrounding squares

        switch ( square ) {
            case A1:
                ss = SUM.bitboardToSquareSet( CSS.A2 | CSS.B2 | CSS.B1 );
                break;
            case A8:
                ss = SUM.bitboardToSquareSet( CSS.A7 | CSS.B7 | CSS.B8 );
                break;
            case H1:
                ss = SUM.bitboardToSquareSet( CSS.H2 | CSS.G2 | CSS.G1 );
                break;
            case H8:
                ss = SUM.bitboardToSquareSet( CSS.G8 | CSS.G7 | CSS.H7 );
                break;
            // Should be (made) impossible
            default:
                ss = null;
                break;
        }

        return ss;
    }

    private static EnumSet<Square> surroundingSquaresOfSquaresOnEdge(
        Square square ) throws Exception {
        // Square on 1st rank
        if ( ( square.bit() & CSS.RANK_1 ) != 0 ) {
            return surroundingSquaresOfSquaresOnRank1( square );
        } // Square on 8th rank
        else if ( ( square.bit() & CSS.RANK_8 ) != 0 ) {
            return surroundingSquaresOfSquaresOnRank8( square );
        } // Square on a-file
        else if ( ( square.bit() & CSS.FILE_A ) != 0 ) {
            return surroundingSquaresOfSquaresOnFileA( square );
        }
        // Square must be on h-file
        return surroundingSquaresOfSquaresOnFileH( square );
    }

    // ___Should be called only from surroundingSquaresOfSquaresOnEdge()___
    //
    // The diagram below is meant to illustrate how the method works. The
    // numbers on top are the square indices, and below are the corresponding
    // square names. The asterisk indicates the square itself (the method
    // parameter), the X's mark set bits in the bitboard and the rest of the
    // cells are unset bits (zeroes). It's important to observe that this
    // pattern applies in finding the surrounding squares of any of the
    // squares B1 to G1.
    //
    //  10  09  08  07  06  05  04  03  02  01  00
    // +-------------------------------------------+
    // | X | X | X |   |   |   |   |   | X | * | X |
    // +-------------------------------------------+
    //  C2  B2  A2  H1  G1  F1  E1  D1  C1  B1  A1
    // << direction of left-shifts
    //
    private static EnumSet<Square> surroundingSquaresOfSquaresOnRank1(
        Square square ) throws Exception {
        long minus1, plus1, plus7, plus8, plus9;
        minus1 = plus1 = plus7 = plus8 = plus9 = square.bit();

        // The five surrounding squares (square bits) are shifted to their
        // correct positions
        minus1 >>>= 1;
        plus1 <<= 1; // Must use unsigned version of the right-shift operator
        plus7 <<= 7;
        plus8 <<= 8;
        plus9 <<= 9;

        return SUM.bitboardToSquareSet(
            minus1 | plus1 | plus7 | plus8 | plus9 );
    }

    // ___Should be called only from surroundingSquaresOfSquaresOnEdge()___
    //
    //  58  57  56  55  54  53  52  51  50  49  48
    // +-------------------------------------------+
    // | X | * | X |   |   |   |   |   | X | X | X |
    // +-------------------------------------------+
    //  C8  B8  A8  H7  G7  F7  E7  D7  C7  B7  A7
    // << direction of left-shifts
    //
    private static EnumSet<Square> surroundingSquaresOfSquaresOnRank8(
        Square square ) throws Exception {
        long plus1, minus1, minus7, minus8, minus9;
        plus1 = minus1 = minus7 = minus8 = minus9 = square.bit();

        plus1 <<= 1;
        minus1 >>>= 1;
        minus7 >>>= 7;
        minus8 >>>= 8;
        minus9 >>>= 9;

        return SUM.bitboardToSquareSet(
            plus1 | minus1 | minus7 | minus8 | minus9 );
    }

    // ___Should be called only from surroundingSquaresOfSquaresOnEdge()___
    //
    //  17  16  15  14  13  12  11  10  09  08  07  06  05  04  03  02  01  00
    // +-----------------------------------------------------------------------+
    // | X | X |   |   |   |   |   |   | X | * |   |   |   |   |   |   | X | X |
    // +-----------------------------------------------------------------------+
    //  B3  A3  H2  G2  F2  E2  D2  C2  B2  A2  H1  G1  F1  E1  D1  C1  B1  A1
    // << direction of left-shifts
    //
    private static EnumSet<Square> surroundingSquaresOfSquaresOnFileA(
        Square square ) throws Exception {
        long minus7, minus8, plus1, plus8, plus9;
        minus7 = minus8 = plus1 = plus8 = plus9 = square.bit();

        minus7 >>>= 7;
        minus8 >>>= 8;
        plus1 <<= 1;
        plus8 <<= 8;
        plus9 <<= 9;

        return SUM.bitboardToSquareSet(
            minus7 | minus8 | plus1 | plus8 | plus9 );
    }

    // ___Should be called only from surroundingSquaresOfSquaresOnEdge()___
    //
    //  23  22  21  20  19  18  17  16  15  14  13  12  11  10  09  08  07  06
    // +-----------------------------------------------------------------------+
    // | X | X |   |   |   |   |   |   | * | X |   |   |   |   |   |   | X | X |
    // +-----------------------------------------------------------------------+
    //  H3  G3  F3  E3  D3  C3  B3  A3  H2  G2  F2  E2  D2  C2  B2  A2  H1  G1
    // << direction of left-shifts
    //
    private static EnumSet<Square> surroundingSquaresOfSquaresOnFileH(
        Square square ) throws Exception {
        long minus1, minus8, minus9, plus7, plus8;
        minus1 = minus8 = minus9 = plus7 = plus8 = square.bit();

        minus1 >>>= 1;
        minus8 >>>= 8;
        minus9 >>>= 9;
        plus7 <<= 7;
        plus8 <<= 8;

        return SUM.bitboardToSquareSet(
            minus1 | minus8 | minus9 | plus7 | plus8 );
    }

    // ___Should be called only from surroundingSquaresOfSquaresOnEdge()___
    //
    //  18  17  16  15  14  13  12  11  10  09  08  07  06  05  04  03  02  01  00
    // +---------------------------------------------------------------------------+
    // | X | X | X |   |   |   |   |   | X | * | X |   |   |   |   |   | X | X | X |
    // +---------------------------------------------------------------------------+
    //  C3  B3  A3  H2  G2  F2  E2  D2  C2  B2  A2  H1  G1  F1  E1  D1  C1  B1  A1
    // << direction of left-shifts
    //
    private static EnumSet<Square> surroundingSquaresOfSquaresNotOnEdge(
        Square square ) throws Exception {
        long minus1, minus7, minus8, minus9, plus1, plus7, plus8, plus9;
        minus1 = minus7 = minus8 = minus9 = plus1 = plus7 = plus8 = plus9
            = square.bit();

        minus1 >>>= 1;
        minus7 >>>= 7;
        minus8 >>>= 8;
        minus9 >>>= 9;
        plus1 <<= 1;
        plus7 <<= 7;
        plus8 <<= 8;
        plus9 <<= 9;

        return SUM.bitboardToSquareSet(
            minus1 | minus7 | minus8 | minus9 | plus1 | plus7 | plus8 | plus9 );
    }
}
