package chessosisnbproject.gui;

import chessosisnbproject.data.CSS;
import chessosisnbproject.data.Square;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

/**
 *
 * @author Henrik Lindberg
 */
public class SquareOnGUI extends JLabel implements MouseListener {

    private static SquareOnGUI activeSquare = null;

    private final Square square;
    private final boolean darkSquare;

    public SquareOnGUI( Square square ) {
        super( "", JLabel.CENTER );
        this.square = square;
        this.darkSquare = ( ( this.square.bit() & CSS.DARK_SQUARES ) != 0 );
        this.setFont( this.getFont().deriveFont( 55f ) );
        this.setBackground( squareColor() );
        this.setOpaque( true );
        //this.setPreferredSize( new Dimension(100,100) );
        //this.addMouseListener( this );
    }

    public static SquareOnGUI activeSquare() {
        return SquareOnGUI.activeSquare;
    }

    private Color squareColor() {
        return this.darkSquare() ? Color.LIGHT_GRAY : Color.WHITE;
    }

    public Square name() {
        return this.square;
    }

    public boolean darkSquare() {
        return this.darkSquare;
    }

    private Chessboard getChessboardRef() {
        return (Chessboard) this.getParent();
    }

    private void sendMessage( String message ) {
        getChessboardRef().sendMessage( message );
    }

    public static void repaintSquare( JLabel label ) {
        SquareOnGUI thisSquare = (SquareOnGUI) label;
        if ( SquareOnGUI.activeSquare() == thisSquare ) {
            thisSquare.setBorder(
                BorderFactory.createLineBorder( Color.PINK, 3 ) );
        } else {
            thisSquare.setBorder(
                BorderFactory.createEmptyBorder() );
        }
        label.repaint();
    }

    @Override
    public void mouseClicked( MouseEvent e ) {
        // Left mouse button
        if ( e.getButton() == MouseEvent.BUTTON1 ) {
            if ( flipOrSetActiveSquare() ) {
                getChessboardRef().activeSquareSet(
                    SquareOnGUI.activeSquare() );
            }
        }
    }

    @Override
    public void mouseEntered( MouseEvent e ) {
    }

    @Override
    public void mouseExited( MouseEvent e ) {
    }

    @Override
    public void mousePressed( MouseEvent e ) {
    }

    @Override
    public void mouseReleased( MouseEvent e ) {
    }

    // Returns true if a square was activated
    private boolean flipOrSetActiveSquare() {
        boolean squareActivated = true;

        // There was no active square on the board before the click...
        if ( SquareOnGUI.activeSquare == null ) {
            /*sendMessage( "Brand new active square: "
                + this.name() + "\n" );*/
            SquareOnGUI.activeSquare = this;
        } // There was an active square but it was not this square
        else if ( SquareOnGUI.activeSquare != this ) {
            SquareOnGUI formerActiveSquare = SquareOnGUI.activeSquare;
            SquareOnGUI.activeSquare = this;
            /*sendMessage( "New active square "
                + SquareOnGUI.activeSquare.name() + ", old was "
                + formerActiveSquare.name() + "\n" );*/
            SquareOnGUI.repaintSquare( formerActiveSquare );
        } // The active square was clicked
        else {
            SquareOnGUI.activeSquare = null;
            squareActivated = false;
            /*sendMessage( "The active square was clicked and now equals: "
                + SquareOnGUI.activeSquare + "\n" );*/
        }
        SquareOnGUI.repaintSquare( this );

        return squareActivated;
    }
}
