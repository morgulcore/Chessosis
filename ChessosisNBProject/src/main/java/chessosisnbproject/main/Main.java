package chessosisnbproject.main;

/**
 *
 * @author Henrik Lindberg
 */
public class Main {

    public static void main( String[] args ) throws Exception {
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                ChessosisSimpleGUI.createAndShowGUI();
            }
        } );
    }
}
