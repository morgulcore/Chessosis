package chessosisnbproject.chessosisnbproject;

/**
 *
 * @author Henrik Lindberg
 */
public class ChessosisNBProject {

    public static void main( String[] args ) throws Exception {
        System.out.println( SUM.adjacentSquare( Square.E4, Direction.NORTH) );
        System.out.println( SUM.adjacentSquare( Square.E4, Direction.NORTHEAST) );
        System.out.println( SUM.adjacentSquare( Square.E4, Direction.EAST) );
        System.out.println( SUM.adjacentSquare( Square.E4, Direction.SOUTHEAST) );
        System.out.println( SUM.adjacentSquare( Square.E4, Direction.SOUTH) );
        System.out.println( SUM.adjacentSquare( Square.E4, Direction.SOUTHWEST) );
        System.out.println( SUM.adjacentSquare( Square.E4, Direction.WEST) );
        System.out.println( SUM.adjacentSquare( Square.E4, Direction.NORTHWEST) );
    }
}
