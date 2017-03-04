package edu.oregonstate.cs361.battleship;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Samuel on 3/2/2017.
 */
public class BattleshipModelTest {
    BattleshipModel model = new BattleshipModel();
    Ship currentShip;

    @Test
    public void getShip() throws Exception {
        String[] ships = {"aircraftCarrier","battleship","submarine","clipper","dinghy"};

        //Attempts to get a ship object, checks if a ship object is returned
        for (String ship : ships)
        {
            currentShip = model.getShip(ship);
            assertTrue(currentShip instanceof Ship);
        }

        //Attempts to get an invalid ship, checks if null is returned
        currentShip = model.getShip("ShipDoesntExist");
        assertNull(currentShip);
    }

    @Test
    public void placeEnemyShip() throws Exception {
        //Places enemy battleship
        currentShip = model.placeEnemyShip("battleship",4,0);
        assertTrue(currentShip instanceof Ship);
    }

    @Test
    public void placeShip() throws Exception {
        String result = "";
        String outBounds = "Ship Placement out of bounds";
        String overlap = "Placement overlaps another ship";

        //Places a ship at 1,1 should return null
        result = model.placeShip("battleship","1","1","horizontal");
        assertNull(result);

        //Places a ship overlapping at the same point
        result = model.placeShip("submarine","1","1","vertical");
        assertEquals(overlap,result);

        //Place a submarine at 2,1 vertical
        result = model.placeShip("submarine","2","1","vertical");
        assertNull(result);

        //Place civilian ship clipper at 4,4
        result = model.placeShip("clipper","4","4","horizontal");
        assertNull(result);

        //Place civilian ship dinghy at 5,4
        result = model.placeShip("dinghy","5","4","vertical");
        assertNull(result);

        //Places a ship out of bounds vertical
        result = model.placeShip("aircraftCarrier","20","20","vertical");
        assertEquals(outBounds,result);

        //Place a ship in bounds with its endpoint out of bounds vertical
        result = model.placeShip("aircraftCarrier","9","9","vertical");
        assertEquals(outBounds,result);

        //Places a ship out of bounds horizontal
        result = model.placeShip("aircraftCarrier","11","10","horizontal");
        assertEquals(outBounds,result);

        //Place a ship in bounds with its endpoint out of bounds horizontal
        result = model.placeShip("aircraftCarrier","10","11","horizontal");
        assertEquals(outBounds,result);

        //Place a ship in bounds with its endpoint out of bounds horizontal
        result = model.placeShip("aircraftCarrier","9","20","horizontal");
        assertEquals(outBounds,result);
    }

    @Test
    public void overlappingShip(){

        String overlap = "Placement overlaps another ship";
        String result = model.placeShip("aircraftCarrier","1","1","horizontal");
        String resultOverlap = model.placeShip("submarine","1","1","horizontal");
        assertEquals(resultOverlap, overlap);

    }


    @Test
    public void sinkAllEnemyShips(){
        MilitaryShip[] myMilitary = model.getEnemyMilitaryShips();
        CivilianShip[] myCivilian = model.getEnemyCivilianShips();

        for(int row = 1; row < 11; row++){
            for(int col = 1; col < 11; col++){
                model.shootAtComputer(row, col);
            }

        }

        ArrayList<Ship> sunkenShips = model.getEnemySunkShips();
        assertEquals(sunkenShips.contains(myMilitary[0]), true);
        assertEquals(sunkenShips.contains(myMilitary[1]), true);
        assertEquals(sunkenShips.contains(myMilitary[2]), true);
        assertEquals(sunkenShips.contains(myCivilian[0]), true);
        assertEquals(sunkenShips.contains(myCivilian[1]), true);


    }


    @Test
    public void shootAtComputer() throws Exception {
        String result = "";
        String offBoard = "That Shot is off the board!";
        String alreadyFired = "You have already fired there!";
        int i,j;

        //Attempts a shot at 1,1. Should return null
        result = model.shootAtComputer(1,1);
        assertNull(result);

        //Attempts a shot at 1,1 again. Should return already fired message
        result = model.shootAtComputer(1,1);
        assertEquals(alreadyFired,result);

        //Attempts a shot off board. Should return off board message
        result = model.shootAtComputer(20,20);
        assertEquals(offBoard,result);

        //A test for hitting a computer ship? Placement is random unfortunately
    }

    @Test
    public void shootAtPlayer() throws Exception {
        int i;

        //Places a player ship
        model.placeShip("battleship","1","1","horizontal");
        model.placeShip("aircraftCarrier","2","1","horizontal");
        model.placeShip("submarine","3","1","horizontal");
        model.placeShip("clipper","4","1","horizontal");
        model.placeShip("dinghy","5","1","horizontal");

        //Fire randomly 50 times to hit player ships, likely need something to test here but variables are unaccessible
        for(i=0;i<50;i++)
            model.shootAtPlayer();

    }

    @Test
    public void getScanResult() throws Exception {
        boolean result;
        //Should return false by default
        result = model.getScanResult();
        assertFalse(result);
    }

    @Test
    public void scan() throws Exception {
        boolean result;

        //Scan main board, may return true or false
        model.scan(1,1);
        result = model.getScanResult();
        assertNotNull(result);
    }

}