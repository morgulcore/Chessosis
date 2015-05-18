package chessosisnbproject.chessosisnbproject;

/**
 *
 * @author Henrik Lindberg
 */
public class Position {

    // The 12 squares sets (SS's, bitboards) that correspond to the
    // different types of pieces of the game.
    private final long whitePawnSS, whiteBishopSS, whiteKnightSS,
        whiteRookSS, whiteQueenSS, whiteKingSS,
        blackPawnSS, blackBishopSS, blackKnightSS,
        blackRookSS, blackQueenSS, blackKingSS;

    // Indicates who's turn it is to move
    private final boolean whitesTurn;
    
    // Sometimes needed to check the legality of an en passant capture.
    private Position previousPosition;

    // Perhaps there should be an array of Positions here representing
    // the legal moves.
    // --No, I think that is not necessary.
    public Position( long whitePawnSS, long whiteBishopSS, long whiteKnightSS,
        long whiteRookSS, long whiteQueenSS, long whiteKingSS,
        long blackPawnSS, long blackBishopSS, long blackKnightSS,
        long blackRookSS, long blackQueenSS, long blackKingSS,
        boolean whitesTurn ) {
        // Initialize white pieces
        this.whitePawnSS = whitePawnSS;
        this.whiteBishopSS = whiteBishopSS;
        this.whiteKnightSS = whiteKnightSS;
        this.whiteRookSS = whiteRookSS;
        this.whiteQueenSS = whiteQueenSS;
        this.whiteKingSS = whiteKingSS;
        // Initialize black pieces
        this.blackPawnSS = blackPawnSS;
        this.blackBishopSS = blackBishopSS;
        this.blackKnightSS = blackKnightSS;
        this.blackRookSS = blackRookSS;
        this.blackQueenSS = blackQueenSS;
        this.blackKingSS = blackKingSS;
        this.whitesTurn = whitesTurn;
    }

    public long getWhitePawnSS() {
        return 0;
    }

    public long getWhiteBishopSS() {
        return 0;
    }

    public long getWhiteKnightSS() {
        return 0;
    }

    public long getWhiteRookSS() {
        return 0;
    }

    public long getWhiteQueenSS() {
        return 0;
    }

    public long getWhiteKingSS() {
        return 0;
    }

    public long getBlackPawnSS() {
        return 0;
    }

    public long getBlackBishopSS() {
        return 0;
    }

    public long getBlackKnightSS() {
        return 0;
    }

    public long getBlackRookSS() {
        return 0;
    }

    public long getBlackQueenSS() {
        return 0;
    }

    public long getBlackKingSS() {
        return 0;
    }

    public boolean whitesTurn() {
        return this.whitesTurn;
    }

    /*
     public void setWhitePawnSS() {
     }

     public void setWhiteBishopSS() {
     }

     public void setWhiteKnightSS() {
     }

     public void setWhiteRookSS() {
     }

     public void setWhiteQueenSS() {
     }

     public void setWhiteKingSS() {
     }

     public void setBlackPawnSS() {
     }

     public void setBlackBishopSS() {
     }

     public void setBlackKnightSS() {
     }

     public void setBlackRookSS() {
     }

     public void setBlackQueenSS() {
     }

     public void setBlackKingSS() {
     }
     */
}
