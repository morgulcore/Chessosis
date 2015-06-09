package chessosisnbproject.chessosisnbproject;

/**
 * A simple enum type for representing directions on the board. Going north
 * on the board means going up a file (e.g., E1, E2, ..., E8) whereas going
 * east means going towards the right of the board along a rank (A1, B1, ...,
 * H1).
 *
 * @author Henrik Lindberg
 */
public enum Direction {

    NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;

    /**
     * Return the set of the four cardinal directions conveniently packed
     * as an array.
     *
     * @return the four cardinal directions
     */
    public static Direction[] cardinalDirections() {
        return new Direction[]{
            NORTH, EAST, SOUTH, WEST };
    }
}
