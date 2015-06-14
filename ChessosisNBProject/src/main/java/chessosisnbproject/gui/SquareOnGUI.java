package chessosisnbproject.gui;

import chessosisnbproject.data.CSS;
import chessosisnbproject.data.Square;
import chessosisnbproject.data.Move;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    }

    protected static SquareOnGUI activeSquare() {
        return SquareOnGUI.activeSquare;
    }

    protected Color squareColor() {
        return this.darkSquare() ? Color.LIGHT_GRAY : Color.WHITE;
    }

    public Square name() {
        return this.square;
    }

    public boolean darkSquare() {
        return this.darkSquare;
    }

    private Chessboard getCBRef() {
        return (Chessboard) this.getParent();
    }

    private void sendMessage( String message ) {
        getCBRef().sendMessage( message );
    }

    public static void repaintSquare( SquareOnGUI label ) {
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
            getCBRef().squaretaker( Chessboard.Task.UNHIGHLIGHT );
            if ( flipOrSetActiveSquare() ) {
                try {
                    getCBRef().activeSquareSet(
                        SquareOnGUI.activeSquare() );
                } catch ( Exception ex ) {
                    Logger.getLogger( SquareOnGUI.class.getName() ).log(
                        Level.SEVERE, null, ex );
                }
            }
        } else if ( e.getButton() == MouseEvent.BUTTON3 ) {
            if ( activeSquare() == null ) {
                // __TO-DO HERE__
                sendMessage(
                    "Please first select a piece of your own color\n" );
            } else if ( getCBRef().rightClickedHighlighted( this.name() ) ) {
                Move move = new Move(
                    activeSquare().name(), this.name(),
                    getCBRef().getGUIRef().getGame().getPos() );
                try {
                    if ( !getCBRef().getGUIRef().getGame().makeMove( move ) ) {
                        sendMessage( "ERROR: makeMove() refused to execute "
                            + move.toString() + "\n" );
                    }
                    SquareOnGUI.activeSquare = null;
                    getCBRef().squaretaker( Chessboard.Task.UNHIGHLIGHT );
                    getCBRef().squaretaker( Chessboard.Task.DISPLAY_POS );
                } catch ( Exception ex ) {
                    Logger.getLogger( SquareOnGUI.class.getName() ).log(
                        Level.SEVERE, null, ex );
                }
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
            SquareOnGUI.activeSquare = this;
        } // There was an active square but it was not this square
        else if ( SquareOnGUI.activeSquare != this ) {
            SquareOnGUI formerActiveSquare = SquareOnGUI.activeSquare;
            SquareOnGUI.activeSquare = this;
            SquareOnGUI.repaintSquare( formerActiveSquare );
        } // The active square was clicked
        else {
            SquareOnGUI.activeSquare = null;
            squareActivated = false;
        }
        SquareOnGUI.repaintSquare( this );

        return squareActivated;
    }
}
