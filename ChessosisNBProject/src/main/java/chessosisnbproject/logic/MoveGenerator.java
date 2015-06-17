package chessosisnbproject.logic;

import chessosisnbproject.data.Move;
import chessosisnbproject.data.Position;
import chessosisnbproject.data.PieceType;
import chessosisnbproject.data.Direction;
import chessosisnbproject.data.Colour;
import chessosisnbproject.data.CSS;
import chessosisnbproject.data.Square;
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
     * @param pos the Position object to examine
     * @return a set of zero or more Move objects
     * @throws Exception
     */
    public static Set<Move> moveGenerator( Position pos )
        throws Exception {
        // Generate the pseudo-legal moves for the given position
        Set<Move> pseudoLegalMoves
            = pseudoLegalMoveGenerator( pos );

        // Returns a set of zero or more Move objects
        return legalMovesOnly( pseudoLegalMoves );
    }

    private static Set<Move> pseudoLegalMoveGenerator( Position pos )
        throws Exception {
        Set<Move> moves = new LinkedHashSet<>();

        EnumSet<Square> piecesOfSideToMove;
        if ( pos.turn() == Colour.WHITE ) {
            piecesOfSideToMove = SUM.bitboardToSqSet( pos.whiteArmy() );
        } else if ( pos.turn() == Colour.BLACK ) {
            piecesOfSideToMove = SUM.bitboardToSqSet( pos.blackArmy() );
        } else { // In case of null
            throw new Exception( "Enum type Colour: " + pos.turn() );
        }

        // Each piece's set of possible moves is calculated individually
        // to begin with. The first step in getting the set of moves for an
        // individual piece is getting the destination squares (destSq's).
        // These include any square where the chessman can (pseudo-legally)
        // move to.
        for ( Square sqOfPiece : piecesOfSideToMove ) {
            EnumSet<Square> destSquares
                = pseudoLegalMGPieceTypeSel( sqOfPiece, pos );
            moves.addAll(
                generateMoveSetForPiece( sqOfPiece, destSquares, pos ) );
        }

        return moves;
    } // end pseudoLegalMoveGenerator()

    public static Set<Move> legalMovesOnly( Set<Move> moves )
        throws Exception {
        // The placement of both kings on the board
        for ( Move move : moves ) {
            long placementOfKingsBB
                = move.context().whiteKing() | move.context().blackKing();
            // A king is in check if the condition fails
            if ( ( move.to().bit() & placementOfKingsBB ) == 0 ) {
                Position posAfterMove = Game.newPos(
                    move.context(), move.from().bit(), move.to().bit() );
                long kingSB = ( posAfterMove.turn() == Colour.WHITE )
                    ? posAfterMove.whiteKing() : posAfterMove.blackKing();
                Square king = SUM.squareBitToSquare( kingSB );
            }
        }

        return moves;
    }

    private static EnumSet<Square> pseudoLegalMGPieceTypeSel( // SELection
        Square sqOfPiece, Position pos ) throws Exception {
        if ( PieceType.PAWN == SUM.resolvePieceType( sqOfPiece, pos ) ) {
            return pawnMoveGenerator( sqOfPiece, pos );
        } else if ( PieceType.BISHOP == SUM.resolvePieceType( sqOfPiece, pos ) ) {
            return bishopMoveGenerator( sqOfPiece, pos );
        } else if ( PieceType.KNIGHT == SUM.resolvePieceType( sqOfPiece, pos ) ) {
            return knightMoveGenerator( sqOfPiece, pos );
        } else if ( PieceType.ROOK == SUM.resolvePieceType( sqOfPiece, pos ) ) {
            return accessibleRooksSquares( sqOfPiece, pos );
        } else if ( PieceType.QUEEN == SUM.resolvePieceType( sqOfPiece, pos ) ) {
            return queenMoveGenerator( sqOfPiece, pos );
        } else if ( PieceType.KING == SUM.resolvePieceType( sqOfPiece, pos ) ) {
            return accessibleKingsSquares( sqOfPiece, pos );
        } else {
            throw new Exception( "PieceType constant is null" );
        }
    }

    // Generates the set of pseudo-legal moves for an individual piece
    private static Set<Move> generateMoveSetForPiece(
        Square squareOfPiece, EnumSet<Square> destSquaresOfPiece,
        Position contextPos ) {
        Set<Move> moves = new LinkedHashSet<>();

        for ( Square destSquare : destSquaresOfPiece ) {
            moves.add( new Move( squareOfPiece, destSquare, contextPos ) );
        }

        return moves;
    } // end generateMoveSetForPiece()

    //
    // ========================================================
    // == Pseudo-legal move generators for individual pieces ==
    // ========================================================
    //
    //
    /**
     TODO: Javadoc
    
     @param sq
     @param pos
     @return
     @throws Exception 
     */
    public static EnumSet<Square> pawnMoveGenerator( Square sq, Position pos )
        throws Exception {
        EnumSet<Square> pawnDestSquares = EnumSet.noneOf( Square.class );
        pawnDestSquares.addAll( passivePawnDestSqs( sq, pos ) );
        pawnDestSquares.addAll( aggressivePawnDestSqs( sq, pos ) );

        return pawnDestSquares;
    }

    /**
     TODO: Javadoc
    
     @param sq
     @param pos
     @return
     @throws Exception 
     */
    public static EnumSet<Square> bishopMoveGenerator( Square sq, Position pos )
        throws Exception {
        EnumSet<Square> bishopDestSquares = EnumSet.noneOf( Square.class );
        Direction[] dirs = { Direction.NORTHEAST, Direction.SOUTHEAST,
            Direction.SOUTHWEST, Direction.NORTHWEST };

        long friendlyPieces
            = ( pos.turn() == Colour.WHITE ) ? pos.whiteArmy() : pos.blackArmy(),
            enemyPieces
            = ( pos.turn() == Colour.WHITE ) ? pos.blackArmy() : pos.whiteArmy();
        for ( Direction dir : dirs ) {
            // Dest squares in a single diagonal direction (NE, SE, SW or NW)
            EnumSet<Square> destSquaresInDir
                = bishopMoveGeneratorSingleDir(
                    dir, sq, friendlyPieces, enemyPieces );
            bishopDestSquares.addAll( destSquaresInDir ); // Set union
        }

        return bishopDestSquares;
    }

    /**
     TODO: Javadoc
    
     @param sq
     @param pos
     @return
     @throws Exception 
     */
    public static EnumSet<Square> knightMoveGenerator(
        Square sq, Position pos ) throws Exception {
        // To begin with the destination squares include any square a knight
        // could move to (on an empty board) from the square in question
        EnumSet<Square> knightDestSquares = knightsSquares( sq );

        long friendlyPieces
            = ( pos.turn() == Colour.WHITE )
                ? pos.whiteArmy() : pos.blackArmy();

        // From the pseudo-legal moves point of view, the only thing that
        // limits the knight's mobility is a friendly piece on a potential
        // destination square. We'll now remove the squares of friendly pieces
        // from the list of potential destination squares.
        knightDestSquares.removeAll( SUM.bitboardToSqSet( friendlyPieces ) );

        return knightDestSquares;
    }

    /**
     TODO: Javadoc
    
     @param sq
     @param pos
     @return
     @throws Exception 
     */
    public static EnumSet<Square> queenMoveGenerator( Square sq, Position pos )
        throws Exception {
        EnumSet<Square> queenDestSquares = EnumSet.noneOf( Square.class ),
            bishopDestSquares = bishopMoveGenerator( sq, pos ),
            rookDestSquares = accessibleRooksSquares( sq, pos );

        queenDestSquares.addAll( rookDestSquares );
        queenDestSquares.addAll( bishopDestSquares );

        return queenDestSquares;
    }

    //
    // =======================================================
    // == {pawns,bishops,knights,rooks,queens,kings}Squares ==
    // =======================================================
    //
    //
    /**
     TODO: Javadoc
    
     @param sq
     @return
     @throws Exception 
     */
    public static EnumSet<Square> knightsSquares( Square sq )
        throws Exception {
        EnumSet<Square> sqSet = EnumSet.noneOf( Square.class );

        // Loop over the four cardinal directions: NORTH, EAST, SOUTH and WEST
        for ( Direction dir : Direction.cardinalDirections() ) {
            Square nextSq = sq; // Set the starting point
            // Then move two squares forward on a file or rank
            for ( int i = 1; i <= 2; i++ ) {
                nextSq = SUM.adjacentSquare( nextSq, dir );
                // There's no next square in a particular direction
                if ( nextSq == null ) {
                    break;
                } else if ( i == 2 ) { // After having moved two squares fwd
                    knightsSquaresSwitch( dir, nextSq, sqSet );
                }
            }
        } // end for

        return sqSet;
    }

    //
    // (END: {pawns,bishops,knights,rooks,queens,kings}Squares)
    //
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
     * @throws Exception
     */
    public static EnumSet<Square> rooksSquares( Square square )
        throws Exception {
        // XOR'ing the relevant file and rank is all that is needed to produce
        // the set of rook's squares
        long rooksSquaresBB
            = SUM.fileOfSquare( square ) ^ SUM.rankOfSquare( square );
        return SUM.bitboardToSqSet( rooksSquaresBB );
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
                else if ( ( position.turn() == Colour.WHITE )
                    && ( position.whiteArmy() & nextSquare.bit() ) != 0 ) {
                    break;
                } // Black chessman cannot capture another black one
                else if ( ( position.turn() == Colour.BLACK )
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
    // =============================
    // == Private utility methods ==
    // =============================
    //
    //
    // Method: bishopMoveGeneratorSingleDir()
    //
    // Finds the passive pawn destination squares. These are the squares
    // a pawn can move to on the same file. As they involve no capture,
    // they are passive (non-aggressive) in nature. I couldn't figure out
    // how to divide this method into smaller parts.
    // Generates destination squares for a bishop in a single diagonal
    // direction (NE, SE, SW, NW). The process starts from the square next
    // to the bishop in the given direction (assuming such a square exists).
    // Provided the square is not occupied by a friendly piece it is added
    // to the list of destination squares. If the square is empty, the
    // process moves to the next square on the diagonal -- one more square
    // away from the bishop.
    //
    // The following serves as an example. The bishop is on A1 and the
    // direction of the search for destination squares is NE (northeast).
    // The X indicates some piece of opposite color (enemy piece). First the
    // square B2 is considered. Being empty (or being a square not occupied by
    // a friendly piece) it is added to the list of destination squares. The
    // next square considered is C3. It too is added to the list. The seach
    // is concluded at this point because the bishop cannot crash through
    // or jump over the enemy piece even though it can capture it.
    //
    //   +---------------+
    // 4 |   |   |   |   |
    //   |---------------|
    // 3 |   |   | X |   |
    //   |---------------|
    // 2 |   | * |   |   |
    //   |---------------|
    // 1 | B |   |   |   |
    //   +---------------+
    //     A   B   C   D
    //
    private static EnumSet<Square> bishopMoveGeneratorSingleDir(
        Direction dir, Square sq, long friendlyPieces, long enemyPieces )
        throws Exception {
        EnumSet<Square> destSquaresInDir = EnumSet.noneOf( Square.class );
        Square nextSquare = sq;
        while ( true ) {
            // Move away from the bishop one square at a time in the given
            // direction
            nextSquare = SUM.adjacentSquare( nextSquare, dir );
            if ( nextSquare == null // null: search went overboard
                // ...OR if there's a friendly piece on nextSquare
                || ( nextSquare.bit() & friendlyPieces ) != 0 ) {
                // Break out of the loop without adding to square to
                // the list of possible destination squares for the
                // bishop
                break;
            }
            destSquaresInDir.add( nextSquare );

            // Enemy piece on square
            if ( ( nextSquare.bit() & enemyPieces ) != 0 ) {
                // Although the bishop can capture the enemy piece,
                // it can't crash through it
                break;
            }
        }
        return destSquaresInDir;
    }

    // Part of knightsSquares()
    private static void knightsSquaresSwitch( Direction dir, Square nextSq,
        EnumSet<Square> sqSet ) throws Exception {
        switch ( dir ) {
            case NORTH:
            case SOUTH:
                if ( SUM.adjacentSquare( nextSq, Direction.EAST ) != null ) {
                    sqSet.add( SUM.squareBitToSquare( nextSq.bit() << 1 ) );
                }
                if ( SUM.adjacentSquare( nextSq, Direction.WEST ) != null ) {
                    sqSet.add( SUM.squareBitToSquare( nextSq.bit() >>> 1 ) );
                }
                break;
            case EAST:
            case WEST:
                if ( SUM.adjacentSquare( nextSq, Direction.NORTH ) != null ) {
                    sqSet.add( SUM.squareBitToSquare( nextSq.bit() << 8 ) );
                }
                if ( SUM.adjacentSquare( nextSq, Direction.SOUTH ) != null ) {
                    sqSet.add( SUM.squareBitToSquare( nextSq.bit() >>> 8 ) );
                }
                break;
            default:
                throw new Exception(
                    "Default case of switch executed: " + dir );
        }
    }

    // Finds pawn destination squares that don't involve piece capture.
    // Such dest squares are always on the same file as the pawn being moved.
    private static EnumSet<Square> passivePawnDestSqs( // destination squares
        Square sq, Position pos ) throws Exception {
        EnumSet<Square> passiveSqs = EnumSet.noneOf( Square.class );
        Direction dir = ( pos.turn() == Colour.WHITE )
            ? Direction.NORTH : Direction.SOUTH;

        Square sqFwd = SUM.adjacentSquare( sq, dir ); // (one) square forward
        // Either there's no road ahead (1st or 8th rank) or it is blocked
        if ( sqFwd == null || ( pos.bothArmies() & sqFwd.bit() ) != 0 ) {
            // Do nothing
        } else if ( dir == Direction.NORTH ) { // White's turn
            passiveSqs.add( sqFwd );
            sqFwd = SUM.adjacentSquare( sqFwd, dir ); // Another step forward
            if ( ( sq.bit() & CSS.RANK_2 ) != 0 // Pawn on 2nd rank
                // No obstacle two squares ahead
                && ( sqFwd.bit() & pos.bothArmies() ) == 0 ) {
                passiveSqs.add( sqFwd ); // Two-rank pawn move, e.g., E2-E4
            }
        } else if ( dir == Direction.SOUTH ) { // Black's turn
            passiveSqs.add( sqFwd );
            sqFwd = SUM.adjacentSquare( sqFwd, dir ); // See previous else if
            if ( ( sq.bit() & CSS.RANK_7 ) != 0
                && ( sqFwd.bit() & pos.bothArmies() ) == 0 ) {
                passiveSqs.add( sqFwd );
            }
        }

        return passiveSqs;
    }

    // Finds the aggressive pawn destination squares, i.e., the ones that
    // involve a capture by the pawn.
    private static EnumSet<Square> aggressivePawnDestSqs( // destination squares
        Square sq, Position pos ) throws Exception {
        EnumSet<Square> aggressiveSqs = EnumSet.noneOf( Square.class );

        // Directions of aggression (the two diagonal pawn capture squares)
        Direction[] dirsOfAggression // Let's guess the active color is White
            = { Direction.NORTHEAST, Direction.NORTHWEST };

        if ( pos.turn() == Colour.BLACK ) { // Correct the color if we were wrong
            dirsOfAggression[ 0 ] = Direction.SOUTHEAST;
            dirsOfAggression[ 1 ] = Direction.SOUTHWEST;
        }
        for ( Direction dir : dirsOfAggression ) {
            Square hostileSq = aggressivePawnDestSqsSel( sq, pos, dir );
            if ( hostileSq != null ) {
                aggressiveSqs.add( hostileSq );
            }
        }

        return aggressiveSqs;
    }

    // Part of aggressivePawnDestSqs()
    private static Square aggressivePawnDestSqsSel( // SELection
        Square sq, Position pos, Direction dir ) throws Exception {
        Square hostileSq = SUM.adjacentSquare( sq, dir );
        if ( hostileSq == null ) {
            // Do nothing
        } else if ( ( hostileSq.bit() & pos.blackArmy() ) != 0 // Victim
            && ( dir == Direction.NORTHEAST // White's turn
            || dir == Direction.NORTHWEST ) ) {
            return hostileSq;
        } else if ( ( hostileSq.bit() & pos.whiteArmy() ) != 0 // Victim
            && ( dir == Direction.SOUTHEAST // Black's turn
            || dir == Direction.SOUTHWEST ) ) {
            return hostileSq;
        }
        return null;
    }

    private static EnumSet<Square> surroundingSquaresOfCorners( Square square )
        throws Exception {
        EnumSet<Square> ss; // surrounding squares

        switch ( square ) {
            case A1:
                ss = SUM.bitboardToSqSet( CSS.A2 | CSS.B2 | CSS.B1 );
                break;
            case A8:
                ss = SUM.bitboardToSqSet( CSS.A7 | CSS.B7 | CSS.B8 );
                break;
            case H1:
                ss = SUM.bitboardToSqSet( CSS.H2 | CSS.G2 | CSS.G1 );
                break;
            case H8:
                ss = SUM.bitboardToSqSet( CSS.G8 | CSS.G7 | CSS.H7 );
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

        return SUM.bitboardToSqSet(
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

        return SUM.bitboardToSqSet(
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

        return SUM.bitboardToSqSet(
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

        return SUM.bitboardToSqSet(
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

        return SUM.bitboardToSqSet(
            minus1 | minus7 | minus8 | minus9 | plus1 | plus7 | plus8 | plus9 );
    }

    private static EnumSet<Square> accessibleKingsSquares(
        Square kingsSquare, Position position ) throws Exception {
        // The initial assumption: all of the king's squares are accessible
        EnumSet<Square> accessibleKingsSquares = kingsSquares( kingsSquare );

        // The chessmen of the king's own color
        long kingsOwnMenBB
            = ( position.turn() == Colour.WHITE )
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
            = ( position.turn() == Colour.BLACK )
                ? ( position.whiteRooks() | position.whiteQueens() )
                : ( position.blackRooks() | position.blackQueens() );
        // Conversion to a Square-based EnumSet
        EnumSet<Square> enemyRooksAndQueens
            = SUM.bitboardToSqSet( enemyRooksAndQueensBB );

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
}
