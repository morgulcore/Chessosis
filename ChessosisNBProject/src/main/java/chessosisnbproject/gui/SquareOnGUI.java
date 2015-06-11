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

    private final Square square;
    private final boolean darkSquare;

    public SquareOnGUI( Square square ) {
        super( square.toString() );
        this.square = square;
        this.darkSquare = ( ( this.square.bit() & CSS.DARK_SQUARES ) != 0 );
        this.setFont( this.getFont().deriveFont( 50f ) );
        this.setBackground( this.darkSquare ? Color.LIGHT_GRAY : Color.WHITE );
        this.setOpaque( true );
        //this.addMouseListener( this );
    }

    public Square name() {
        return this.square;
    }

    public boolean darkSquare() {
        return this.darkSquare;
    }

    @Override
    public void mouseClicked( MouseEvent e ) {
        this.setText( "--" );
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
}
