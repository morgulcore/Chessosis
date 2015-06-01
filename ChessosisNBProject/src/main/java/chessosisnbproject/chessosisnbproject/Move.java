package chessosisnbproject.chessosisnbproject;

import java.util.Objects;

/**
 *
 * @author henrik
 */
public class Move {

    private final Square fromSquare, toSquare;

    public Move( Square fromSquare, Square toSquare ) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
    }

    public Square from() {
        return this.fromSquare;
    }

    public Square to() {
        return this.toSquare;
    }

    @Override
    public String toString() {
        // Coordinate notation, e.g., E2-E4
        return this.from() + "-" + this.to();
    }

    /**
     * Generated automatically by NetBeans.
     *
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode( this.fromSquare );
        hash = 67 * hash + Objects.hashCode( this.toSquare );
        return hash;
    }

    /**
     * Generated automatically by NetBeans.
     *
     * @return 
     */
    @Override
    public boolean equals( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Move other = (Move) obj;
        if ( this.fromSquare != other.fromSquare ) {
            return false;
        }
        if ( this.toSquare != other.toSquare ) {
            return false;
        }
        return true;
    }
}
