package chessosisnbproject.gui;

import chessosisnbproject.data.Square;
import chessosisnbproject.data.Position;
import chessosisnbproject.logic.SUM;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 *
 * @author Henrik Lindberg
 */
public class Chessboard extends JPanel {

    private ChessosisGUI chessosisGUIRef;

    public Chessboard() {
        super( new GridLayout( 0, 8, 1, 1 ) );
        this.setBackground( Color.DARK_GRAY );
        this.setBorder( new LineBorder( Color.DARK_GRAY ) );
        for ( int row = 0; row < 8; row++ ) {
            for ( int column = 0; column < 8; column++ ) {
                Square name = SUM.tableCellToSquare( row, column );
                SquareOnGUI squareOnGUI = new SquareOnGUI( name );
                this.add( squareOnGUI );
                // 'this.addMouseListener( this )' in the constructor
                // causes NetBeans to warn about a "leaking this".
                squareOnGUI.addMouseListener( squareOnGUI );
            }
        }
    }

    public void setChessosisGUIRef( ChessosisGUI ref ) {
        this.chessosisGUIRef = ref;
    }

    public ChessosisGUI getChessosisGUIRef() {
        return this.chessosisGUIRef;
    }

    protected void sendMessage( String message ) {
        getChessosisGUIRef().sendMessage( message );
    }

    protected void activeSquareSet( SquareOnGUI sq ) {
        Position pos = getChessosisGUIRef().gameObject().currentPosition();
        long squareBit = sq.name().bit();
        if( friendlySquareActivated( pos, squareBit ) ) {
            
        }
    }

    // Returns true if the player clicked on a friendly chessman
    private boolean friendlySquareActivated( Position pos, long bit ) {
        return ( pos.turn() == chessosisnbproject.data.Color.WHITE
            && ( ( pos.whiteArmy() & bit ) != 0 ) )
            || ( pos.turn() == chessosisnbproject.data.Color.BLACK
            && ( pos.blackArmy() & bit ) != 0 );
    }

    protected void displayPosition() {
        Position pos
            = this.getChessosisGUIRef().gameObject().currentPosition();
        String[][] table
            = SUM.unicodeChessSymbolTable( pos );

        for ( int row = 0; row < 8; row++ ) {
            for ( int column = 0; column < 8; column++ ) {
                int index = 8 * row + column;
                // "Note: This method should be called under AWT tree lock."
                JLabel label = (JLabel) this.getComponent( index );
                label.setText( table[ row ][ column ] );
                SquareOnGUI.repaintSquare( label );
            }
        }
    }
}
