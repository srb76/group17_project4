package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by michaelhilton on 1/4/17.
 */
public class BattleshipModel {

    private Ship aircraftCarrier = new Ship("AircraftCarrier",5, new Coordinate(0,0),new Coordinate(0,0));
    private Ship battleship = new Ship("Battleship",4, new Coordinate(0,0),new Coordinate(0,0));
    private Ship cruiser = new Ship("Cruiser",3, new Coordinate(0,0),new Coordinate(0,0));
    private Ship destroyer = new Ship("Destroyer",2, new Coordinate(0,0),new Coordinate(0,0));
    private Ship submarine = new Ship("Submarine",2, new Coordinate(0,0),new Coordinate(0,0));

    private Ship computer_aircraftCarrier = new Ship("Computer_AircraftCarrier",5, new Coordinate(2,2),new Coordinate(2,7));
    private Ship computer_battleship = new Ship("Computer_Battleship",4, new Coordinate(2,8),new Coordinate(6,8));
    private Ship computer_cruiser = new Ship("Computer_Cruiser",3, new Coordinate(4,1),new Coordinate(4,4));
    private Ship computer_destroyer = new Ship("Computer_Destroyer",2, new Coordinate(7,3),new Coordinate(7,5));
    private Ship computer_submarine = new Ship("Computer_Submarine",2, new Coordinate(9,6),new Coordinate(9,8));

    private ArrayList<Coordinate> playerHits;
    private ArrayList<Coordinate> playerMisses;
    private ArrayList<Coordinate> computerHits;
    private ArrayList<Coordinate> computerMisses;

    private ArrayList<Coordinate> playerShipPoints;



    public BattleshipModel() {
        playerHits = new ArrayList<>();
        playerMisses= new ArrayList<>();
        computerHits = new ArrayList<>();
        computerMisses= new ArrayList<>();

        playerShipPoints = new ArrayList<>();
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



    public String placeShip(String shipName, String row, String col, String orientation) {
        int rowInt = Integer.parseInt(row);
        int colInt = Integer.parseInt(col);
        if(rowInt > 10 || colInt > 10)
            return "Ship Placement out of bounds";
        int size;
        int endRowInt;
        int endColInt;
        size = getShip(shipName).getLength();
        Ship testShip = new Ship("test", size);
        if(orientation.equals("vertical")){
            endRowInt = rowInt + size - 1;
            endColInt = colInt;
            if(endRowInt > 10)
                return "Ship Placement out of bounds";
            Coordinate start = new Coordinate(rowInt, colInt);
            Coordinate end = new Coordinate(endRowInt, endColInt);
            testShip.setLocation(start, end);
            for(int i = 0; i > playerShipPoints.size(); i++){
                if(testShip.covers(playerShipPoints.get(i)));
                    return "Placement overlaps another ship";
            }
            System.out.println("Size: " + size);
            for(int i = 0; i < size; i++){
                Coordinate toAdd = new Coordinate(rowInt + i, colInt);
                toAdd.display();
                playerShipPoints.add(toAdd);
            }
            getShip(shipName).setLocation(start, end);
        } else { //horizantal
            if((colInt + size -1) > 10)
                return "Ship Placement out of bounds";
            endRowInt = rowInt;
            endColInt = colInt + size - 1;
            if(endColInt > 10 )
                return "Ship placement out of bounds";
            Coordinate start = new Coordinate(rowInt, colInt);
            Coordinate end = new Coordinate(endRowInt, endColInt);
            testShip.setLocation(start, end);
            for(int i = 0; i < playerShipPoints.size(); i++){
                if(testShip.covers(playerShipPoints.get(i)))
                    return "Placement overlaps another ship";
            }

            for(int i = 0; i < size; i++){
                Coordinate toAdd = new Coordinate(rowInt, colInt + i);
                toAdd.display();
                playerShipPoints.add(toAdd);
            }
            getShip(shipName).setLocation(start, end);
        }

        return null;
    }

    public String shootAtComputer(int row, int col) {
        if(row > 10 || col > 10)
            return "That Shot is off the board!";
        for(int i = 0; i < computerMisses.size(); i++){
            if(row == computerMisses.get(i).getDown() && col == computerMisses.get(i).getAcross())
                return "You have already fired there!";
        }
        for(int i = 0; i < computerHits.size(); i ++){
            if(row == computerHits.get(i).getDown() && col == computerHits.get(i).getAcross())
                return "You have already fired there!";
        }
        Coordinate coor = new Coordinate(row,col);
        if(computer_aircraftCarrier.covers(coor)){
            computerHits.add(coor);
        }else if (computer_battleship.covers(coor)){
            computerHits.add(coor);
        }else if (computer_cruiser.covers(coor)){
            computerHits.add(coor);
        }else if (computer_destroyer.covers(coor)){
            computerHits.add(coor);
        }else if (computer_submarine.covers(coor)){
            computerHits.add(coor);
        } else {
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


        if(aircraftCarrier.covers(coor)){
            playerHits.add(coor);
        }else if (battleship.covers(coor)){
            playerHits.add(coor);
        }else if (cruiser.covers(coor)){
            playerHits.add(coor);
        }else if (destroyer.covers(coor)){
            playerHits.add(coor);
        }else if (submarine.covers(coor)){
            playerHits.add(coor);
        } else {
            playerMisses.add(coor);
        }
    }
}