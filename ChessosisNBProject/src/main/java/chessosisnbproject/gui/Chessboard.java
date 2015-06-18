package chessosisnbproject.gui;

import chessosisnbproject.data.Square;
import chessosisnbproject.logic.Position;
import chessosisnbproject.data.Move;
import chessosisnbproject.logic.SUM;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.EnumSet;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 The graphical representation of the board and pieces. Class Chessboard
 is a JPanel that contains 64 JLabel (or SquareOnGUI) objects that each
 correspond to a square on the board. Class Chessboard uses GridLayout to
 arrange the JLabels into an 8-by-8 matrix.

 @author Henrik Lindberg
 */
public class Chessboard extends JPanel {

    private ChessosisGUI chessosisGUIRef;
    private EnumSet<Square> highlightedDest; // highlighted destination squares

    /**
     Initialize a new Chessboard object.
     */
    public Chessboard() {
        super( new GridLayout( 0, 8, 1, 1 ) );
        this.setBackground( Color.DARK_GRAY );
        this.setBorder( new LineBorder( Color.DARK_GRAY ) );
        squaretaker( Task.INIT_SQUARES );
    }

    /**
     Called from the ChessosisGUI object to provide a means to call its methods,
     especially sendMessage().
    
     @param ref a reference to the main GUI object
     */
    protected void setGUIRef( ChessosisGUI ref ) {
        this.chessosisGUIRef = ref;
    }

    /**
     Returns the reference to the main GUI object.
    
     @return instance of class ChessosisGUI
     */
    protected ChessosisGUI getGUIRef() {
        return this.chessosisGUIRef;
    }

    /**
     This method simply calls the method with the same name in the parent
     object (of type ChessosisGUI). The message is relayed forward and displayed
     in the GUI's text area.
    
     @param message the message to display to the user
     */
    protected void sendMessage( String message ) {
        getGUIRef().sendMessage( message );
    }

    /**
     Handles the tasks associated with the setting of the active square.
     The active square is set when the user clicks on a square.
    
     @param sq the Square object that was set active
     @throws Exception 
     */
    protected void activeSquareSet( SquareOnGUI sq ) throws Exception {
        Position pos = getGUIRef().getGame().getPos();
        long squareBit = sq.name().bit();
        if ( friendlySquareActivated( pos, squareBit ) ) {
            Set<Move> moves
                = getGUIRef().getGame().getMoves();
            //sendMessage( "Moves available: " + moves.size() + "\n" );
            this.highlightedDest
                = extractDestSquares( moves, sq.name() );
            squaretaker( Task.HIGHLIGHT_DEST );
        }
    }

    /**
     Determines whether a square that was just clicked was highlighted.
    
     @param name right-clicked square
     @return true or false
     */
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
        return ( pos.turn() == chessosisnbproject.data.Colour.WHITE
            && ( ( pos.whiteArmy() & sqBit ) != 0 ) )
            || ( pos.turn() == chessosisnbproject.data.Colour.BLACK
            && ( pos.blackArmy() & sqBit ) != 0 );
    }

    /**
     Used in method squaretaker() to identify different tasks.
     */
    public enum Task {

        INIT_SQUARES, DISPLAY_POS, HIGHLIGHT_DEST, UNHIGHLIGHT
    }

    /**
     squaretaker(), the caretaker of many square-related things. All of the
     actions performed by squaretaker involve the use of an outer and inner
     for loop, both of which iterate eight times. This is an often used
     structure in this class. Indeed, the main motivation behind this lengthy
     method is to avoid repetition of code.
     <p>
     Note that the method is declared final. If it is not, NetBeans will warn
     about an overridable method call in the constructor.
     
     @param task the enum constant specifies the task to perform
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
