package chessosisnbproject.logic;

import chessosisnbproject.data.Move;
import chessosisnbproject.data.Square;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    private Game game;

    public GameTest() {
    }

    @Before
    public void setUp() {
        game = new Game();
    }

    @After
    public void tearDown() {
        game = null;
    }

    @Test
    public void getPosTest() {
        Position expectedPos = new Position(); // Standard starting position
        assertEquals( expectedPos, game.getPos() );
    }

    @Test
    public void newMoveTest() throws Exception {
        game.newMove( new Move( Square.E2, Square.E4, game.getPos() ) );
        Position expectedPos = Position.fENToPosition(
            "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1" );
        assertEquals( expectedPos, game.getPos() );
    }
    
    @Test
    public void getMovesTest() throws Exception {
        assertEquals( 20, game.getMoves().size() );
    }
}
