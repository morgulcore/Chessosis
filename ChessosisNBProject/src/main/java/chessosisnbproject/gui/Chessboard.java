package chessosisnbproject.gui;

import chessosisnbproject.data.Square;
import chessosisnbproject.data.Position;
import chessosisnbproject.data.Move;
import chessosisnbproject.logic.SUM;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.EnumSet;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 *
 * @author Henrik Lindberg
 */
public class Chessboard extends JPanel {

    private ChessosisGUI chessosisGUIRef;
    private EnumSet<Square> highlightedDest; // highlighted destination squares

    public Chessboard() {
        super( new GridLayout( 0, 8, 1, 1 ) );
        this.setBackground( Color.DARK_GRAY );
        this.setBorder( new LineBorder( Color.DARK_GRAY ) );
        squaretaker( Task.INIT_SQUARES );
    }

    protected void setGUIRef( ChessosisGUI ref ) {
        this.chessosisGUIRef = ref;
    }

    // Get (Chessosis) GUI reference
    protected ChessosisGUI getGUIRef() {
        return this.chessosisGUIRef;
    }

    protected void sendMessage( String message ) {
        getGUIRef().sendMessage( message );
    }

    protected void activeSquareSet( SquareOnGUI sq ) throws Exception {
        Position pos = getGUIRef().getGame().getPos();
        long squareBit = sq.name().bit();
        if ( friendlySquareActivated( pos, squareBit ) ) {
            Set<Move> moves
                = getGUIRef().getGame().getMoves();
            this.highlightedDest
                = extractDestSquares( moves, sq.name() );
            squaretaker( Task.HIGHLIGHT_DEST );
        }
    }

    protected boolean rightClickedHighlighted( Square name ) {
        if ( this.highlightedDest == null ) {
            return false;
        }
        return this.highlightedDest.contains( name );
    }

    private int tableCellToIndex( int row, int col ) {
        return 8 * row + col;
    }

    private EnumSet<Square> extractDestSquares( Set<Move> moves, Square sq ) {
        EnumSet<Square> destSquares = EnumSet.noneOf( Square.class );
        for ( Move move : moves ) {
            if ( move.from() == sq ) {
                destSquares.add( move.to() );
            }
        }
        return destSquares;
    }

    // Returns true if the player clicked on a friendly square (chessman)
    private boolean friendlySquareActivated( Position pos, long sqBit ) {
        return ( pos.turn() == chessosisnbproject.data.Color.WHITE
            && ( ( pos.whiteArmy() & sqBit ) != 0 ) )
            || ( pos.turn() == chessosisnbproject.data.Color.BLACK
            && ( pos.blackArmy() & sqBit ) != 0 );
    }

    public enum Task {

        INIT_SQUARES, DISPLAY_POS, HIGHLIGHT_DEST, UNHIGHLIGHT
    }

    // If the method is not final, NetBeans will warn about an overridable
    // method call in the constructor
    /**
     *
     * @param task
     */
    protected final void squaretaker( Task task ) {
        for ( int row = 0; row < 8; row++ ) {
            for ( int col = 0; col < 8; col++ ) {
                int index = tableCellToIndex( row, col );
                SquareOnGUI viSq;
                switch ( task ) {
                    case INIT_SQUARES:
                        Square name = SUM.tableCellToSquare( row, col );
                        SquareOnGUI squareOnGUI = new SquareOnGUI( name );
                        this.add( squareOnGUI );
                        // 'this.addMouseListener( this )' in the constructor
                        // causes NetBeans to warn about a "leaking this".
                        squareOnGUI.addMouseListener( squareOnGUI );
                        break; // end INIT_SQUARES
                    case DISPLAY_POS:
                        Position pos = this.getGUIRef().getGame().getPos();
                        String[][] table = SUM.unicodeChessSymbolTable( pos );
                        // viSq, visual square as opposed to enum type Square
                        viSq = (SquareOnGUI) this.getComponent( index );
                        viSq.setText( table[ row ][ col ] );
                        SquareOnGUI.repaintSquare( viSq );
                        break; // end DISPLAY_POS
                    case HIGHLIGHT_DEST:
                        // viSq, visual square
                        viSq = (SquareOnGUI) this.getComponent( index );
                        if ( this.highlightedDest.contains( viSq.name() ) ) {
                            viSq.setBackground(
                                ( viSq.darkSquare() )
                                    ? Color.ORANGE : Color.YELLOW );
                            SquareOnGUI.repaintSquare( viSq );
                        }
                        break; // end HIGHLIGHT_DEST
                    case UNHIGHLIGHT:
                        viSq = (SquareOnGUI) this.getComponent( index );
                        viSq.setBackground(
                            viSq.squareColor() );
                        this.highlightedDest = null;
                        break; // end UNHIGHLIGHT
                    default:
                        break;
                }
            }
        }
    }
}
