package chessosisnbproject.gui;

import chessosisnbproject.logic.Game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.*;

/**
 *
 * @author Henrik Lindberg
 */
public class ChessosisGUI extends JFrame {

    private final JTextArea messageArea;
    private final JMenuBar menuBar;
    private Game game;
    private final Chessboard chessboard;

    public ChessosisGUI() {
        Container container = getContentPane();
        container.setLayout( new BorderLayout() );

        chessboard = new Chessboard();
        container.add( chessboard, BorderLayout.CENTER );

        this.messageArea = new JTextArea( 10, 20 );
        this.messageArea.setEditable( false );
        this.messageArea.setFont(
            this.messageArea.getFont().deriveFont( 14f ) );
        //container.add( this.messageArea, BorderLayout.SOUTH );
        container.add( new JScrollPane( this.messageArea ),
            BorderLayout.SOUTH );

        this.menuBar = new JMenuBar();
        menuBar.setOpaque( true );
        menuBar.setBackground( Color.BLACK );
        menuBar.setPreferredSize( new Dimension( 200, 20 ) );
        this.setJMenuBar( menuBar );
    }

    public void sendMessage( String message ) {
        this.messageArea.append( message );
    }

    protected void play( Game game ) {
        this.game = game;
        chessboard.setGUIRef( this );
        Game.setDebugMsgRef( this );
        chessboard.squaretaker( Chessboard.Task.DISPLAY_POS );
        sendMessage( "Welcome!\n\n" );
    }

    protected Game getGame() {
        return this.game;
    }

    public static void main( String[] args ) {
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                ChessosisGUI chessosisGUI = new ChessosisGUI();
                chessosisGUI.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                chessosisGUI.setSize( 525, 700 );
                chessosisGUI.setResizable( false );
                chessosisGUI.setTitle( "Chessosis" );
                //chessosisGUI.pack();
                chessosisGUI.setVisible( true );

                chessosisGUI.play( new Game() );
            }
        } );
    }
}
