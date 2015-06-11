package chessosisnbproject.data;

/**
 * This is a simple enum type that represents the six types of chessmen
 * available in chess.
 *
 * @author Henrik Lindberg
 */
public enum Chessman {

    PAWN, BISHOP, KNIGHT, ROOK, QUEEN, KING;

    /**
     * Returns an array of the types of chessmen available in standard chess.
     *
     * @return a Chessman array containing the six types of chessmen
     */
    public static Chessman[] availableChessmanTypes() {
        return new Chessman[]{ PAWN, BISHOP, KNIGHT, ROOK, QUEEN, KING };
    }
}
