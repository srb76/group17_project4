package edu.oregonstate.cs361.battleship;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by michaelhilton on 1/4/17.
 */
public class BattleshipModel {
    private Ship aircraftCarrier = new Ship("AircraftCarrier",5, new Coordinate(0,0),new Coordinate(0,0),false);
    private Ship battleship = new Ship("Battleship",4, new Coordinate(0,0),new Coordinate(0,0),true);
    private Ship cruiser = new Ship("Cruiser",3, new Coordinate(0,0),new Coordinate(0,0),true);
    private Ship destroyer = new Ship("Destroyer",2, new Coordinate(0,0),new Coordinate(0,0),true);
    private Ship submarine = new Ship("Submarine",2, new Coordinate(0,0),new Coordinate(0,0),true);



    private Ship computer_aircraftCarrier;
    private Ship computer_battleship;
    private Ship computer_cruiser;
    private Ship computer_destroyer;
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

    public BattleshipModel() {
        playerHits = new ArrayList<>();
        playerMisses= new ArrayList<>();
        computerHits = new ArrayList<>();
        computerMisses= new ArrayList<>();
        playerShipPoints = new ArrayList<>();
        computerShipPoints = new ArrayList<>();
        myShips = new Ship[5];
        enemyShips = new Ship[5];

        computer_aircraftCarrier = placeEnemyShip("computer_aircraftCarrier", 5);
        computer_battleship = placeEnemyShip("Computer_Battleship",4);
        computer_cruiser = placeEnemyShip("Computer_Cruiser",3);
        computer_destroyer = placeEnemyShip("Computer_Destroyer",2);
        computer_submarine = placeEnemyShip("Computer_Submarine",2);
        enemyShips[0] = computer_aircraftCarrier;
        enemyShips[1] = computer_battleship;
        enemyShips[2] = computer_cruiser;
        enemyShips[3] = computer_destroyer;
        enemyShips[4] = computer_submarine;

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
        } if(shipName.equalsIgnoreCase("Cruiser")) {
        return cruiser;
        } if(shipName.equalsIgnoreCase("destroyer")) {
            return destroyer;
        }if(shipName.equalsIgnoreCase("submarine")) {
            return submarine;
        } else {
            return null;
        }
    }

    public Ship placeEnemyShip(String name, int length){

        boolean valid = false;

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
        Ship currentShip = new Ship(name, length, startCoordinate, endCoordinate, false);
        currentShip.setPoints(myPoints);

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
        Ship testShip = new Ship("test", size);
        if(orientation.equals("vertical")){
            endDown = Down;
            endAcross = Across + size - 1;
            if(endAcross > 10)
                return "Ship Placement out of bounds";
            Coordinate start = new Coordinate(Across, Down);
            Coordinate end = new Coordinate(endAcross, endDown);
            testShip.setLocation(start, end);
            for(int i = 0; i < playerShipPoints.size(); i++){
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
            testShip.setLocation(start, end);
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
            }
        }
        if(!hit){
            computerMisses.add(coor);
        }
        return null;

    }

    public void shootAtPlayer() {
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
        if(getShipFromCoordinate(scanCoord) != null || getShipFromCoordinate(up) != null
                    || getShipFromCoordinate(down) != null
                    || getShipFromCoordinate(left) != null
                    || getShipFromCoordinate(right)!= null){
                scanResult = true;
        }

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