/**
 * Created by tnoelcke on 3/1/2017.
 */
package edu.oregonstate.cs361.battleship;
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

    @Test
    void testEquals(){
        Coordinate test = new Coordinate(2, 7);
        Coordinate test2 = new Coordinate(2, 7);
        Coordinate test3 = new Coordinate(1,1);
        assertEquals(test.equals(test2), true);
        assertEquals(test.equals(test3), false);
    }

    @Test
    void testSettersForAcrossAndDown(){
        Coordinate test = new Coordinate(2, 7);
        test.setAcross(3);
        test.setDown(5);
        assertEquals(test.getAcross(), 3);
        assertEquals(test.getDown(), 5);
    }
}
