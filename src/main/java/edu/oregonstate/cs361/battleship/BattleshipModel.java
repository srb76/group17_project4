package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by michaelhilton on 1/4/17.
 */
public class BattleshipModel {
    private Ship aircraftCarrier = new MilitaryShip("AircraftCarrier",5, new Coordinate(0,0),new Coordinate(0,0),false);
    private Ship battleship = new MilitaryShip("Battleship",4, new Coordinate(0,0),new Coordinate(0,0),true);
    private Ship clipper = new CivilianShip("Clipper",3, new Coordinate(0,0),new Coordinate(0,0),false);
    private Ship dinghy = new CivilianShip("Dinghy",1, new Coordinate(0,0),new Coordinate(0,0),false);
    private Ship submarine = new MilitaryShip("Submarine",2, new Coordinate(0,0),new Coordinate(0,0),true);



    private Ship computer_aircraftCarrier;
    private Ship computer_battleship;
    private Ship computer_clipper;
    private Ship computer_dinghy;
    private Ship computer_submarine;

    private Ship[] myShips;
    private int shipPlaceIndex = 0;
    private Ship[] enemyShips;
    private ArrayList<Coordinate> playerHits;
    private ArrayList<Coordinate> playerMisses;
    private ArrayList<Coordinate> computerHits;
    private ArrayList<Coordinate> computerMisses;
    //private ArrayList<Coordinate> computerScore;
    private ArrayList<Coordinate> playerShipPoints;
    private ArrayList<Coordinate> computerShipPoints;
    private boolean scanResult;
    // will be used to store the names of sunk ships.
    private String mySunkShips;
    private String enemySunkShips;

    public BattleshipModel() {
        playerHits = new ArrayList<>();
        playerMisses= new ArrayList<>();
        computerHits = new ArrayList<>();
        computerMisses= new ArrayList<>();
        playerShipPoints = new ArrayList<>();
        computerShipPoints = new ArrayList<>();
        myShips = new Ship[5];
        enemyShips = new Ship[5];
        mySunkShips = new String();
        mySunkShips = null;
        enemySunkShips = new String();
        enemySunkShips = null;
        computer_aircraftCarrier = placeEnemyShip("computer_aircraftCarrier", 5, "MilitaryShip");
        computer_battleship = placeEnemyShip("Computer_Battleship",4, "MilitaryShip");
        computer_clipper = placeEnemyShip("Computer_Clipper",3, "CivilianShip");
        computer_dinghy = placeEnemyShip("Computer_Dinghy",1, "CivilianShip");
        computer_submarine = placeEnemyShip("Computer_Submarine",2, "MilitaryShip");
        enemyShips[0] = computer_aircraftCarrier;
        enemyShips[1] = computer_battleship;
        enemyShips[2] = computer_clipper;
        enemyShips[3] = computer_dinghy;
        enemyShips[4] = computer_submarine;
        myShips[0] = aircraftCarrier;
        myShips[1] = battleship;
        myShips[2] = clipper;
        myShips[3] = dinghy;
        myShips[4] = submarine;
        scanResult = false;
    }

    public static BattleshipModel ofStatus(String statusStr) {
        System.out.println("STRING");
        return null;
    }

    public Ship getShip(String shipName) {
        if (shipName.equalsIgnoreCase("aircraftcarrier")) {
            return aircraftCarrier;
        } if(shipName.equalsIgnoreCase("battleship")) {
            return battleship;
        } if(shipName.equalsIgnoreCase("Clipper")) {
        return clipper;
        } if(shipName.equalsIgnoreCase("dinghy")) {
            return dinghy;
        }if(shipName.equalsIgnoreCase("submarine")) {
            return submarine;
        } else {
            return null;
        }
    }

    public Ship placeEnemyShip(String name, int length, String type){

        boolean valid = false;
        boolean visibleShip = true;

        int orientation = 0;
        int row = 0;
        int col = 0;
        while(!valid) {
            //1 for vertical
            //2 for horizontal

            orientation = (Math.random() <= 0.5) ? 1 : 2;
            row = (int) (Math.random() * 10) + 1;
            col = (int) (Math.random() * 10) + 1;

            valid = isValidMove(length, row, col, orientation);

        }
        Coordinate startCoordinate;
        Coordinate endCoordinate;

        //vertical, change row
        if(orientation == 1){
            startCoordinate = new Coordinate(row, col);
            endCoordinate = new Coordinate(row+length-1, col);
        }
        else { //horizontal change col
            startCoordinate = new Coordinate(row, col);
            endCoordinate = new Coordinate(row, col+length-1);
        }

        Coordinate[] myPoints = new Coordinate[length];
        for (int i = 0; i < length; i++) {
            if (orientation == 1) {
                myPoints[i] = new Coordinate((row+i), (col));
                computerShipPoints.add(myPoints[i]);

            } else if (orientation == 2) {
                myPoints[i] = new Coordinate((row), (col+i));
                computerShipPoints.add(myPoints[i]);
            }
        }
        Ship currentShip;
        //Give stealth to Computer_Battleship and Computer_Submarine
        if(name == "Computer_Battleship" || name == "Computer_Submarine")
            visibleShip = false;
        if(type.equals("MilitaryShip")) {
            currentShip = new MilitaryShip(name, length, startCoordinate, endCoordinate, visibleShip);
        } else { //(is CivlianShip
            currentShip = new CivilianShip(name, length, startCoordinate, endCoordinate, visibleShip);
        }
            return currentShip;
    }

    private boolean isValidMove(int length, int row, int col, int orientation){
        //1 for vertical
        //2 for horizontal


        if(orientation == 1){
            if(row + length >= 10)
                return false;
        }
        else if(orientation == 2){
            if(col + length >= 10)
                return false;
        }


            Coordinate[] myPoints = new Coordinate[length];
            for (int i = 0; i < length; i++) {
                if (orientation == 1) {
                    myPoints[i] = new Coordinate((row+i), (col));
                } else if (orientation == 2) {
                    myPoints[i] = new Coordinate((row), (col+i));
                }
                for(int j = 0; j < computerShipPoints.size(); j++){
                    if((computerShipPoints.get(j).getAcross() == myPoints[i].getAcross()) && (computerShipPoints.get(j).getDown() == myPoints[i].getDown()) ){

                        return false;
                    }
                }
            }




            return true;

    }


    public String placeShip(String shipName, String AcrossS, String DownS, String orientation) {

        int Across = Integer.parseInt(AcrossS);
        int Down = Integer.parseInt(DownS);
        if(Down > 10 || Across > 10)
            return "Ship Placement out of bounds";
        int size;
        int endDown;
        int endAcross;
        size = getShip(shipName).getLength();
        MilitaryShip testShip;
        if(orientation.equals("vertical")){
            endDown = Down;
            endAcross = Across + size - 1;
            if(endAcross > 10)
                return "Ship Placement out of bounds";
            Coordinate start = new Coordinate(Across, Down);
            Coordinate end = new Coordinate(endAcross, endDown);
            System.out.println("setting up test ship");
            testShip = new MilitaryShip("test", size, start, end, false);
            for(int i = 0; i < playerShipPoints.size(); i++){
                System.out.println("using test ship");
                if(testShip.covers(playerShipPoints.get(i)))
                    return "Placement overlaps another ship";
            }
            for(int i = 0; i < size; i++){
                Coordinate toAdd = new Coordinate(Across + i, Down);
                playerShipPoints.add(toAdd);
            }
            getShip(shipName).setLocation(start, end);
            myShips[shipPlaceIndex] = getShip(shipName);
            shipPlaceIndex++;
        } else { //horizantal
            if((Down + size -1) > 10)
                return "Ship Placement out of bounds";
            endDown = Down + size -1;
            endAcross = Across;
            if(endDown > 10 )
                return "Ship placement out of bounds";
            Coordinate start = new Coordinate(Across, Down);
            Coordinate end = new Coordinate(endAcross, endDown);
            testShip = new MilitaryShip("test", size, start, end, false);
            for(int i = 0; i < playerShipPoints.size(); i++){
                if(testShip.covers(playerShipPoints.get(i)))
                    return "Placement overlaps another ship";
            }
            for(int i = 0; i < size; i++){
                Coordinate toAdd = new Coordinate( Across, Down + i);
                playerShipPoints.add(toAdd);
            }
            getShip(shipName).setLocation(start, end);
            myShips[shipPlaceIndex] = getShip(shipName);
            shipPlaceIndex++;
        }

        return null;
    }

    public String shootAtComputer(int row, int col) {

        enemySunkShips = null;
        //Note: Reversed order for checking computerHits and computerMisses
        if(row > 10 || col > 10)
            return "That Shot is off the board!";
        for(int i = 0; i < computerMisses.size(); i++){
            if(row == computerMisses.get(i).getAcross() && col == computerMisses.get(i).getDown())
                return "You have already fired there!";
        }
        for(int i = 0; i < computerHits.size(); i ++){
            if(row == computerHits.get(i).getAcross() && col == computerHits.get(i).getDown())
                return "You have already fired there!";
        }
        Coordinate coor = new Coordinate(row,col);

        boolean hit = false;
        for(int i = 0; i < 5; i++){
            if(enemyShips[i].covers(coor)){
                computerHits.add(coor);
                hit = true;
                enemyShips[i].addHit();
                if(enemyShips[i].isSunk()){
                    enemySunkShips = new String(enemyShips[i].getName());
                    System.out.println("SunkShips: " + enemySunkShips);
                }
            }
        }
        if(!hit){
            computerMisses.add(coor);
        }
        return null;

    }

    public void shootAtPlayer() {
        mySunkShips = null;
        double randomRow = Math.random() * 10 + 1;
        double randomCol = Math.random() * 10 + 1;
        int max = 10;
        int min = 1;
        Random random = new Random();
        int randRow = random.nextInt(max - min + 1) + min;
        int randCol = random.nextInt(max - min + 1) + min;

        Coordinate coor = new Coordinate(randRow,randCol);
        if(playerMisses.contains(coor)){
            System.out.println("Dupe");
        }
        boolean hit = false;
        for(int i = 0; i < 5; i++){
            if(myShips[i].covers(coor)){
                playerHits.add(coor);
                hit = true;
                myShips[i].addHit();
                CivilianShip test;
                test = (CivilianShip) myShips[i];
                if(myShips[i].isSunk()){
                    mySunkShips = myShips[i].getName();
                }
            }
        }
        if(!hit){
            playerMisses.add(coor);
        }
    }

    public void scan(int row, int col){
        scanResult = false;
        Coordinate scanCoord = new Coordinate(row, col);
        Coordinate up = new Coordinate(row-1, col);
        Coordinate down = new Coordinate(row+1, col);
        Coordinate left = new Coordinate(row, col-1);
        Coordinate right = new Coordinate(row, col+1);
        ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
        Ship shipToCheck;

        //Add Coordinates to arraylist coords
        coords.add(up);
        coords.add(down);
        coords.add(left);
        coords.add(right);

        //For each coordinate, get the ship that is at that coordinate and then check if it has stealth
        for(Coordinate coord : coords)
        {
            shipToCheck = getShipFromCoordinate(coord);
            if (shipToCheck != null && shipToCheck.isVisible() )
                scanResult = true;
        }

/*
        if(
                getShipFromCoordinate(scanCoord) != null
                    || getShipFromCoordinate(up) != null
                    || getShipFromCoordinate(down) != null
                    || getShipFromCoordinate(left) != null
                    || getShipFromCoordinate(right)!= null){
                scanResult = true;
        }
*/

    }

    //this will return the ship object that is on the cooordinate parameter
    //null otherwise
    private Ship getShipFromCoordinate(Coordinate c){
        for(int i = 0; i < 5; i++){
            if(enemyShips[i].containsPoint(c)){
                return enemyShips[i];
            }
        }
        return null;
    }


}//end class