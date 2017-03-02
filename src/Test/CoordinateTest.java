/**
 * Created by tnoelcke on 3/1/2017.
 */

import edu.oregonstate.cs361.battleship.Coordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CoordinateTest {
    @Test
    void testGettersForAcrossAndDown(){
        Coordinate test = new Coordinate(2, 7);
        int across = test.getAcross();
        int down = test.getDown();

        assertEquals(across, 2);
        assertEquals(down, 7);
    }
}
