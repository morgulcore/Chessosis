package chessosisnbproject.gui;

import chessosisnbproject.logic.Game;
import chessosisnbproject.logic.SUM;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 *
 * @author Henrik Lindberg
 */
public class ChessosisGUI {

    public static void main( String[] args ) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                Chessboard chessboard = new Chessboard();
                JOptionPane.showMessageDialog( null, chessboard,
                    "ChessosisSimpleGUI",
                    JOptionPane.INFORMATION_MESSAGE );
            }
        } );
    }
}
