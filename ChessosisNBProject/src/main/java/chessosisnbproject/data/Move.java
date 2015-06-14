package chessosisnbproject.data;

import java.util.Objects;

// Javadoc: OK
// JUnit: OK
//
/**
 * Move objects represent possible moves in a particular position. Each move
 * object corresponds to a single move such as E2-E4. When treated as a String
 * a Move object returns its string representation in coordinate notation
 * form.
 *
 * @author Henrik Lindberg
 */
public class Move {

    // Every move involves a source ("from") and a destination ("to") square
    private final Square fromSquare, toSquare;

    // Each Move object is made truly unique by specifying the position
    // in which it was discovered
    private final Position context;

    /**
     Initialize a Move object.
    
     @param fromSquare source square
     @param toSquare destination square
     @param contextPos context position (where the move was discovered)
     */
    public Move( Square fromSquare, Square toSquare, Position contextPos ) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        this.context = contextPos;
    }

    /**
     Accessor method.
    
     @return source ("from") square
     */
    public Square from() {
        return this.fromSquare;
    }

    /**
     Accessor method.
    
     @return destination ("to") square
     */
    public Square to() {
        return this.toSquare;
    }

    /**
     Accessor method.
    
     @return context position
     */
    public Position context() {
        return this.context;
    }

    /**
     * Chessosis uses simple coordinate notation (e.g., E2-E4) when
     * representing chess moves as strings.
     *
     * @return the string representation of a move
     */
    @Override
    public String toString() {
        // Coordinate notation, e.g., E2-E4
        return this.from() + "-" + this.to();
    }

    /**
     * Generated automatically by NetBeans. It was necessary to override
     * Object's method equals(), so also hashCode() had to be overridden.
     * This is because equals() and hashCode() are tied to each other
     * by a certain requirement: any two objects found to be equal by
     * equals() must have the same hashCode() value.
     *
     * @return the hash code of the Object
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode( this.fromSquare );
        hash = 41 * hash + Objects.hashCode( this.toSquare );
        hash = 41 * hash + Objects.hashCode( this.context );
        return hash;
    }

    /**
     * Generated automatically by NetBeans. In Chessosis Move objects are
     * frequently placed in sets (as in interface Set). As sets cannot contain
     * duplicate elements, placing one in a set implies comparison. The
     * overridden equals() is used to determine whether two Move objects
     * represent the same move.
     *
     * @return the comparison result
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
        return Objects.equals( this.context, other.context );
    }
}
