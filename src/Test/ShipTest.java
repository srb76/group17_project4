/**
 * Created by tnoelcke on 3/1/2017.
 */
package edu.oregonstate.cs361.battleship;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class ShipTest {
    @Test
    void testGetName(){
        Ship test = new Ship("test", 0);
        String name = test.getName();
        assertEquals("test", name);
    }

    @Test
    void testIsSunkAndNewShipType(){
        Ship testedNewShip = new Ship("boat",0);
        assertEquals(true, testedNewShip.isSunk());
    }
    @Test
    void testVisible(){
        Ship testShip = new Ship("Dinghy",1, new Coordinate(0,0),new Coordinate(0,0),true);
        assertEquals(true,testShip.isVisible());
    }
    @Test
    void testContainsPoint(){
        Coordinate array [] = new Coordinate[1];
        Coordinate test = new Coordinate(0,0);
        array[0] = test;
        Ship testShip = new Ship("Dinghy",1, test,test,false);
        testShip.setPoints(array);
        assertEquals(true,testShip.containsPoint(test));
    }

}
