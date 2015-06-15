package chessosisnbproject.data;

/**
 * Represents the six piece types available in chess. In Chessosis terminology
 * "piece type" is more general than "piece" -- there are 12 distinct pieces
 * such as WHITE_PAWN, WHITE_ROOK, BLACK_KING, etc.
 *
 * @author Henrik Lindberg
 */
public enum PieceType {

    PAWN, BISHOP, KNIGHT, ROOK, QUEEN, KING;

    /**
     * Returns an array of the types of chessmen available in standard chess.
     *
     * @return a Chessman array containing the six types of chessmen
     */
    public static PieceType[] availableChessmanTypes() {
        // Let's pass copies in the array rather than the real thing,
        // just to be on the safe side
        PieceType pawn = PieceType.PAWN;
        PieceType bishop = PieceType.BISHOP;
        PieceType knight = PieceType.KNIGHT;
        PieceType rook = PieceType.ROOK;
        PieceType queen = PieceType.QUEEN;
        PieceType king = PieceType.KING;
        
        return new PieceType[]{ pawn, bishop, knight, rook, queen, king };
    }
}
