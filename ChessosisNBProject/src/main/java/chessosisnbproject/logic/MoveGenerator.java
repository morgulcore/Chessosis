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
        return legalMoves( pseudoLegalMoves );
    }

    private static Set<Move> legalMoves( Set<Move> pseudoLegalMoves )
        throws Exception {
        Set<Move> legalMoves = new LinkedHashSet<>();

        for ( Move move : pseudoLegalMoves ) {
            Position posAfterMove = Game.newPos(
                move.context(), move.from().bit(), move.to().bit() );
            Square inactiveKing = SUM.squareBitToSquare(
                ( posAfterMove.turn() == Colour.WHITE )
                    ? posAfterMove.blackKing() : posAfterMove.whiteKing() );
            // Regicides are pieces of the active color that can capture
            // the enemy king. The presence of regicides means that the
            // position is illegal.
            EnumSet<Square> regicides
                = SUM.pseudoLegalAccess( inactiveKing, posAfterMove );
            if ( regicides.size() == 0 ) {
                legalMoves.add( move );
            }
        }
        return legalMoves;
    }

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
    public static EnumSet<Square> rookMoveGenerator( Square sq, Position pos )
        throws Exception {
        EnumSet<Square> rookDestSquares = EnumSet.noneOf( Square.class );

        long friendlyPieces // Pieces the rook can never capture
            = ( pos.turn() == Colour.WHITE ) ? pos.whiteArmy() : pos.blackArmy();

        // Loop over the directions NORTH, EAST, SOUTH and WEST
        for ( Direction dir : Direction.cardinalDirections() ) {
            Square nextSq = sq; // Starting point
            while ( true ) {
                nextSq = SUM.adjacentSquare( nextSq, dir ); // One square fwd
                // There's no next square in a particular direction or a
                // piece of own color is blocking the way
                if ( nextSq == null || ( friendlyPieces & nextSq.bit() ) != 0 ) {
                    break;
                }

                rookDestSquares.add( nextSq );
                // The square just added to the set contained a piece
                // of opposing color
                if ( ( pos.bothArmies() & nextSq.bit() ) != 0 ) {
                    break;
                }
            }
        }
        return rookDestSquares;
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
            rookDestSquares = rookMoveGenerator( sq, pos );

        queenDestSquares.addAll( rookDestSquares );
        queenDestSquares.addAll( bishopDestSquares );

        return queenDestSquares;
    }

    /**
     TODO: Javadoc
    
     @param sq
     @param pos
     @return
     @throws Exception 
     */
    public static EnumSet<Square> kingMoveGenerator( Square sq, Position pos )
        throws Exception {
        EnumSet<Square> kingDestSquares = EnumSet.noneOf( Square.class );

        long friendlyPieces
            = ( pos.turn() == Colour.WHITE ) ? pos.whiteArmy() : pos.blackArmy();

        for ( Direction dir : Direction.values() ) { // The eight directions
            Square kingsSq = SUM.adjacentSquare( sq, dir );
            // The square exists and is not occupied by a friendly piece
            if ( kingsSq != null && ( kingsSq.bit() & friendlyPieces ) == 0 ) {
                kingDestSquares.add( kingsSq );
            }
        }

        return kingDestSquares;
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

    /**
     * Returns the rook's squares of the square parameter. The rook's squares
     * mean the squares to which a rook could move to on an empty board.
     *
     * @param sq
     * @return 
     * @throws Exception
     */
    public static EnumSet<Square> rooksSquares( Square sq )
        throws Exception {
        // XOR'ing the relevant file and rank is all that is needed to produce
        // the set of rook's squares
        long rooksSquaresBB
            = SUM.fileOfSquare( sq ) ^ SUM.rankOfSquare( sq );
        return SUM.bitboardToSqSet( rooksSquaresBB );
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

    // Generates the set of pseudo-legal moves available in the position
    // parameter. Pseudo-legal move generation is the first step in the
    // whole move generation process.
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
    }

    // Resolves the piece type of the piece on the square parameter and calls
    // the appropriate move generator
    private static EnumSet<Square> pseudoLegalMGPieceTypeSel( // SELection
        Square sq, Position pos ) throws Exception {
        if ( PieceType.PAWN == SUM.resolvePieceType( sq, pos ) ) {
            return pawnMoveGenerator( sq, pos );
        } else if ( PieceType.BISHOP == SUM.resolvePieceType( sq, pos ) ) {
            return bishopMoveGenerator( sq, pos );
        } else if ( PieceType.KNIGHT == SUM.resolvePieceType( sq, pos ) ) {
            return knightMoveGenerator( sq, pos );
        } else if ( PieceType.ROOK == SUM.resolvePieceType( sq, pos ) ) {
            return rookMoveGenerator( sq, pos );
        } else if ( PieceType.QUEEN == SUM.resolvePieceType( sq, pos ) ) {
            return queenMoveGenerator( sq, pos );
        } else if ( PieceType.KING == SUM.resolvePieceType( sq, pos ) ) {
            return kingMoveGenerator( sq, pos );
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
    }
}
