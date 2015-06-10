package chessosisnbproject.main;

import chessosisnbproject.logic.Square;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 *
 * @author henrik
 */
public class ChessosisSimpleGUI {

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI() {
        // GridLayout(int rows, int cols, int hgap, int vgap)
        // Creates a grid layout with the specified number of rows and
        // columns. In addition, the horizontal and vertical gaps are set
        // to the specified values. Horizontal gaps are places between each
        // of columns. Vertical gaps are placed between each of the rows.
        // One, but not both, of rows and cols can be zero, which means
        // that any number of objects can be placed in a row or in a column.
        JPanel chessboard = new JPanel( new GridLayout( 0, 8, 1, 1 ) );
        chessboard.setBackground( Color.DARK_GRAY );
        chessboard.setBorder( new LineBorder( Color.DARK_GRAY ) );

        for ( int row = 0; row < 8; row++ ) {
            for ( int column = 0; column < 8; column++ ) {
                String chessCharString
                    = unicodeChessCharacterTable( row, column );
                Color color = Color.LIGHT_GRAY;
                JLabel label = new JLabel( chessCharString );
                label.setFont( label.getFont().deriveFont( 50f ) );
                if( ((row % 2 == 0) && (column % 2 == 0))
                    || ((row % 2 != 0) && (column % 2 != 0)) ) {
                    color = Color.WHITE;
                }
                label.setBackground( color );
                label.setOpaque( true );
                chessboard.add( label );
            }
        }

        JOptionPane.showMessageDialog( null, chessboard,
            "Click two squares to move from/to",
            JOptionPane.INFORMATION_MESSAGE );
    }

    private static String unicodeChessCharacterTable( int row, int column ) {
        String[][] table = {
            { "\u265c", "", "", "", "", "", "", "\u265c" },
            { "", "", "", "", "", "", "", "" },
            { "", "", "", "", "", "", "", "" },
            { "", "", "", "", "", "", "", "" },
            { "", "", "", "", "", "", "", "" },
            { "", "", "", "", "", "", "", "" },
            { "", "", "", "", "", "", "", "" },
            { "", "", "", "", "", "", "", "" }
        };

        return table[ row ][ column ];
    }
}
