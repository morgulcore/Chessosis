package chessosisnbproject.logic;

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
     * effort should and will be invested in testing this method.
     *
     * @param position the Position object to examine
     * @return a set of zero or more Move objects
     * @throws Exception
     */
    public static Set<Move> moveGenerator( Position position )
        throws Exception {
        EnumSet<Square> chessmenOfSideToMove;

        if ( position.turn() == Color.WHITE ) {
            chessmenOfSideToMove = SUM.bitboardToSquareSet( position.whiteArmy() );
        } else {
            chessmenOfSideToMove = SUM.bitboardToSquareSet( position.blackArmy() );
        }

        // Returns a set of zero or more Move objects
        return moveGeneratorMainLoop( position, chessmenOfSideToMove );
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
        EnumSet<Square> squareSet = EnumSet.noneOf( Square.class );
        for ( Direction direction : Direction.cardinalDirections() ) {
            Square nextSquare = square;
            while ( true ) {
                nextSquare = SUM.adjacentSquare( nextSquare, direction );
                // There's no next square in a particular direction
                if ( nextSquare == null ) {
                    break;
                } // White chessman cannot capture another white one
                else if ( ( position.turn() == Color.WHITE )
                    && ( position.whiteArmy() & nextSquare.bit() ) != 0 ) {
                    break;
                } // Black chessman cannot capture another black one
                else if ( ( position.turn() == Color.BLACK )
                    && ( position.blackArmy() & nextSquare.bit() ) != 0 ) {
                    break;
                }

                squareSet.add( nextSquare );

                // The square just added to the set contained a chessman
                // of opposing color
                if ( ( nextSquare.bit() & position.bothArmies() ) != 0 ) {
                    break;
                }
            }
        }
        return squareSet;
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

    private static Set<Move> moveGeneratorMainLoop( Position position,
        EnumSet<Square> chessmenOfSideToMove ) throws Exception {
        // The set of legal moves in the position is empty to start with
        Set<Move> moves = new LinkedHashSet<>();

        // Each chessman's set of possible moves is calculated individually
        // to begin with. The first step in getting the set of moves for an
        // individual chessman is getting the destination squares. These
        // include any square where the chessman can move to.
        for ( Square chessmansSquare : chessmenOfSideToMove ) {
            EnumSet<Square> destinationSquares = EnumSet.noneOf( Square.class );

            // Chessman is a king
            if ( SUM.typeOfChessman( chessmansSquare, position )
                == Chessman.KING ) {
                destinationSquares = accessibleKingsSquares(
                    chessmansSquare, position );
            } // Chessman is a rook
            else if ( SUM.typeOfChessman( chessmansSquare, position )
                == Chessman.ROOK ) {
                destinationSquares = accessibleRooksSquares(
                    chessmansSquare, position );
            }

            moves.addAll( generateMoveSetForChessman( chessmansSquare, destinationSquares ) );

        } // end for

        return moves;
    } // end moveGeneratorMainLoop()
    
    private static EnumSet<Square> accessibleKingsSquares(
        Square kingsSquare, Position position ) throws Exception {
        // The initial assumption: all of the king's squares are accessible
        EnumSet<Square> accessibleKingsSquares = kingsSquares( kingsSquare );

        // The chessmen of the king's own color
        long kingsOwnMenBB
            = ( position.turn() == Color.WHITE )
                ? position.whiteArmy() : position.blackArmy();
        // Remove the king himself from the list of his servants ("men")
        kingsOwnMenBB ^= kingsSquare.bit();

        // Loop over the 3, 5 or 8 king's squares
        for ( Square square : accessibleKingsSquares ) {
            // The king can't capture one of his own men or move into check
            if ( ( square.bit() & kingsOwnMenBB ) != 0
                || squareInCheck( square, position ) ) {
                // Remove the square from the set of accessible ones
                accessibleKingsSquares.remove( square );
            }
        }

        return accessibleKingsSquares;
    } // end accessibleKingsSquares()
    
    private static boolean squareInCheck(
        Square square, Position position ) throws Exception {
        if ( ( overlappingKingsSquares( position ) & square.bit() ) != 0 ) {
            return true;
        } else if ( squareInCheckByRooksOrQueens( square, position ) ) {
            return true;
        }

        return false;
    } // end squareInCheck()
    
    // The rook or queen "checking" the square must be on the same rank
    // or file as the square itself. Note the distinction between
    // checking a square and attacking it -- the square-checking chessman
    // might be under something like an absolute pin. In that case it
    // couldn't capture an enemy chessman that moved to the checked
    // square. Even so, it would be illegal for the king to move onto
    // the checked square. Indeed, the whole point of this method is
    // to check the legality of a king's move.
    private static boolean squareInCheckByRooksOrQueens(
        Square square, Position position ) throws Exception {
        // Collect the enemy rooks and queens into a single bitboard
        long enemyRooksAndQueensBB
            = ( position.turn() == Color.BLACK )
                ? ( position.whiteRooks() | position.whiteQueens() )
                : ( position.blackRooks() | position.blackQueens() );
        // Conversion to a Square-based EnumSet
        EnumSet<Square> enemyRooksAndQueens
            = SUM.bitboardToSquareSet( enemyRooksAndQueensBB );

        for ( Square rookOrQueen : enemyRooksAndQueens ) {
            EnumSet<Square> squaresCheckedByRookOrQueen
                = accessibleRooksSquares( rookOrQueen, position );
            // Bitwise AND'ing the square bit with the bitboard to see
            // if there are any overlapping squares
            if ( ( square.bit()
                & SUM.squareSetToBitboard( squaresCheckedByRookOrQueen ) )
                != 0 ) {
                // Square in check; that's all we need to know
                return true;
            }
        }

        return false;
    } // end squareInCheckByRooksOrQueens()
    
    // Returns a bitboard representing the overlapping king's squares
    // of the two kings
    private static long overlappingKingsSquares( Position position )
        throws Exception {
        // First we get the two squares on which the kings of both
        // sides reside
        Square whiteKing = SUM.squareBitToSquare( position.whiteKing() ),
            blackKing = SUM.squareBitToSquare( position.blackKing() );
        // Then we get the king's squares of both kings
        EnumSet<Square> whiteKingsSquares = kingsSquares( whiteKing ),
            blackKingsSquares = kingsSquares( blackKing );
        // We continue by converting the two square sets into bitboards
        long whiteKingsSquaresBB = SUM.squareSetToBitboard( whiteKingsSquares ),
            blackKingsSquaresBB = SUM.squareSetToBitboard( blackKingsSquares );
        // Now we can examine whether any of the squares overlap

        long overlappingSquaresOfTheTwoKingsBB
            = ( whiteKingsSquaresBB & blackKingsSquaresBB );

        return overlappingSquaresOfTheTwoKingsBB;
    } // end overlappingKingsSquares()
    
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
    } // end generateMoveSetForChessman()
}
