package chessosisnbproject.gui;

import chessosisnbproject.data.CSS;
import chessosisnbproject.data.Square;
import chessosisnbproject.data.Move;
import chessosisnbproject.logic.SUM;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 A class to represent graphical square objects or "squares on the GUI". The
 graphical chessboard contains 64 of these JLabel-extending objects. Each
 of them has their own action listeners attached and can therefore be
 interacted with by the user. The main way of interacting with SquareOnGUI
 objects is by clicking on them with the mouse buttons.

 @author Henrik Lindberg
 */
public class SquareOnGUI extends JLabel implements MouseListener {

    private static SquareOnGUI activeSquare = null;

    private final Square square;
    private final boolean darkSquare;

    /**
     Initialize a SquareOnGUI object. Note that the addMouseListener() method
     of each SquareOnGUI object is not called from the constructor but from
     the squaretaker() method of class Chessboard.
    
     @param square the Square constant associated with the object
     */
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

    // Relays the message forward so that it will eventually be displayed
    // in the GUI's text area
    private void sendMessage( String message ) {
        getCBRef().sendMessage( message );
    }

    /**
     Called whenever a SquareOnGUI object needs to be repainted.
    
     @param label specifies the graphical square to repaint
     */
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

    /**
     Used to handle mouse-induced events. Mouse-clicks are the main means
     of interacting with the Chessosis GUI.
    
     @param e information about the event
     */
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
        } else if ( e.getButton() == MouseEvent.BUTTON2 ) { // Middle mouse button
            chessosisnbproject.logic.Position pos
                = getCBRef().getGUIRef().getGame().getPos();
            chessosisnbproject.data.Piece piece
                = SUM.resolvePiece( square, pos );
            String pieceString = ( piece == null ) ? "EMPTY" : piece.toString();
            sendMessage(
                "Square " + this.name() + ": " + " " + pieceString + "\t" );
            try {
                sendMessage( "NAM: "
                    + getCBRef().getGUIRef().getGame().getMoves().size() + "\n" );
            } catch ( Exception ex ) {
                Logger.getLogger( SquareOnGUI.class.getName() ).log( Level.SEVERE, null, ex );
            }
        } else if ( e.getButton() == MouseEvent.BUTTON3 ) { // Right mouse button
            if ( activeSquare() == null ) {
                // __TO-DO HERE__
                sendMessage(
                    "Please first select a piece of your own color\n" );
            } else if ( getCBRef().rightClickedHighlighted( this.name() ) ) {
                Move move = new Move(
                    activeSquare().name(), this.name(),
                    getCBRef().getGUIRef().getGame().getPos() );
                try {
                    if ( !getCBRef().getGUIRef().getGame().newMove( move ) ) {
                        sendMessage( "ERROR: makeMove() refused to execute "
                            + move.toString() + "\n" );
                    } else {
                        sendMessage( move + "\n" );
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
