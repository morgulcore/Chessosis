package chessosisnbproject.chessosisnbproject;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * All about generating legal moves. Will add more javadoc later.
 *
 * @author Henrik Lindberg
 */
public class MoveGenerator {

    public static Set<Move> moveGenerator( Position position )
        throws Exception {
        // The set of Moves is empty to start with
        Set<Move> moves = new LinkedHashSet<>();

        long chessmenOfSideToMoveBB;
        if ( position.turn() == Color.WHITE ) {
            chessmenOfSideToMoveBB = position.whiteChessmen();
        } else {
            chessmenOfSideToMoveBB = position.blackChessmen();
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
            moves.addAll( generateMoveSetForChessman( chessman, chessmansDestSquares ) );
        }

        return moves;
    }

    private static EnumSet<Square> potentialDestSquares(
        Square chessman, Position position ) throws Exception {
        // The chessman is a king
        if ( ( chessman.bit() & position.whiteKing() ) != 0
            || ( chessman.bit() & position.blackKing() ) != 0 ) {
            return kingsSquares( chessman );
        }

        return null;
    }

    private static EnumSet<Square> squaresUnderAttack( Position position )
        throws Exception {
        EnumSet<Square> squaresUnderAttack = EnumSet.noneOf( Square.class );
        long chessmenOfSideNotToMoveBB;
        if ( position.turn() == Color.WHITE ) {
            chessmenOfSideNotToMoveBB = position.blackChessmen();
        } else {
            chessmenOfSideNotToMoveBB = position.whiteChessmen();
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
