package chessosisnbproject.data;

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
        Direction north = Direction.NORTH, east = Direction.EAST,
            south = Direction.SOUTH, west = Direction.WEST;

        return new Direction[]{ north, east, south, west };
    }

    /**
     Return the set of the four intermediate directions packed as an array.
    
     @return the four intermediate directions (NE, SE, SW and NW)
     */
    public static Direction[] intermediateDirections() {
        Direction ne = Direction.NORTHEAST, se = Direction.SOUTHEAST,
            sw = Direction.SOUTHWEST, nw = Direction.NORTHWEST;

        return new Direction[]{ ne, se, sw, nw };
    }
}
