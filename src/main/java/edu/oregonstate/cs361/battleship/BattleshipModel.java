package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by michaelhilton on 1/4/17.
 */
public class BattleshipModel {

    private MilitaryShip aircraftCarrier = new MilitaryShip("AircraftCarrier",5, new Coordinate(0,0),new Coordinate(0,0),false);
    private MilitaryShip battleship = new MilitaryShip("Battleship",4, new Coordinate(0,0),new Coordinate(0,0),true);
    private MilitaryShip submarine = new MilitaryShip("Submarine",2, new Coordinate(0,0),new Coordinate(0,0),true);
    private CivilianShip clipper = new CivilianShip("Clipper",3, new Coordinate(0,0),new Coordinate(0,0),false);
    private CivilianShip dinghy = new CivilianShip("Dinghy",1, new Coordinate(0,0),new Coordinate(0,0),false);



    private MilitaryShip computer_aircraftCarrier;
    private MilitaryShip computer_battleship;
    private MilitaryShip computer_submarine;
    private CivilianShip computer_clipper;
    private CivilianShip computer_dinghy;


    private MilitaryShip[] playerMilitaryShips;
    private CivilianShip[] playerCivilianShips;
    private MilitaryShip[] enemyMilitaryShips;
    private CivilianShip[] enemyCivilianShips;
    private int militaryPlaceIndex = 0;
    private int civilianPlaceIndex = 0;

    private ArrayList<Coordinate> playerHits;
    private ArrayList<Coordinate> playerMisses;
    private ArrayList<Coordinate> computerHits;
    private ArrayList<Coordinate> computerMisses;

    //these arrays are for which ships have been sunk
    private ArrayList<Ship> computerShipsSunk;
    private ArrayList<Ship> playerShipsSunk;

    //private ArrayList<Coordinate> computerScore;
    private ArrayList<Coordinate> playerShipPoints;
    private ArrayList<Coordinate> computerShipPoints;
    private boolean scanResult;
    // will be used to store the names of sunk ships.
    private String mySunkShip;
    private String enemySunkShip;

    public BattleshipModel() {
        playerHits = new ArrayList<>();
        playerMisses= new ArrayList<>();
        computerHits = new ArrayList<>();
        computerMisses= new ArrayList<>();
        playerShipPoints = new ArrayList<>();
        computerShipPoints = new ArrayList<>();

        computerShipsSunk = new ArrayList<>();
        playerShipsSunk = new ArrayList<>();

        enemyMilitaryShips = new MilitaryShip[3];
        enemyCivilianShips = new CivilianShip[2];
        playerMilitaryShips = new MilitaryShip[3];
        playerCivilianShips = new CivilianShip[2];


        computer_aircraftCarrier = (MilitaryShip) placeEnemyShip("Computer_AircraftCarrier", 5, 0);
        computer_battleship = (MilitaryShip) placeEnemyShip("Computer_Battleship",4, 1);
        computer_submarine = (MilitaryShip) placeEnemyShip("Computer_Submarine",2, 2);
        computer_clipper = (CivilianShip) placeEnemyShip("Computer_Clipper",3, 3);
        computer_dinghy = (CivilianShip) placeEnemyShip("Computer_Dinghy",1, 4);

        enemyMilitaryShips[0] = (computer_aircraftCarrier);
        enemyMilitaryShips[1] = (computer_battleship);
        enemyMilitaryShips[2] = (computer_submarine);
        enemyCivilianShips[0] = (computer_clipper);
        enemyCivilianShips[1] = (computer_dinghy);

        mySunkShip = null;
        enemySunkShip = null;

        scanResult = false;
    }

    public Ship getShip(String shipName) {
        if (shipName.equalsIgnoreCase("aircraftCarrier")) {
            return aircraftCarrier;
        } if(shipName.equalsIgnoreCase("battleship")) {
            return battleship;
        } if(shipName.equalsIgnoreCase("submarine")) {
            return submarine;
        } if(shipName.equalsIgnoreCase("clipper")) {
            return clipper;
        }if(shipName.equalsIgnoreCase("dinghy")) {
            return dinghy;
        } else {
            return null;
        }
    }


    public Ship placeEnemyShip(String name, int length, int place_index){

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
        if(name == "Computer_Battleship" || name == "Computer_Submarine" || name == "Computer_AircraftCarrier"){

            currentShip = new MilitaryShip(name, length, startCoordinate, endCoordinate, visibleShip);
            currentShip.setPoints(myPoints);
        }
        else{

            currentShip = new CivilianShip(name, length, startCoordinate, endCoordinate, visibleShip);
            currentShip.setPoints(myPoints);

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
            testShip = new MilitaryShip("test", size, start, end, false);
            for(int i = 0; i < playerShipPoints.size(); i++){
                System.out.println("using test ship");
                if(testShip.covers(playerShipPoints.get(i)))
                    return "Placement overlaps another ship";
            }
            Coordinate[] myPoints = new Coordinate[size];
            for(int i = 0; i < size; i++){
                Coordinate toAdd = new Coordinate(Across + i, Down);
                playerShipPoints.add(toAdd);
                myPoints[i] = toAdd;
            }
            getShip(shipName).setLocation(start, end);
            if(shipName.equals("aircraftCarrier") || shipName.equals("battleship") || shipName.equals("submarine")){
                getShip(shipName).setLocation(start, end);
                getShip(shipName).setPoints(myPoints);
                playerMilitaryShips[militaryPlaceIndex] = (MilitaryShip) getShip(shipName);
                militaryPlaceIndex++;
            }
            else{
                getShip(shipName).setLocation(start, end);
                getShip(shipName).setPoints(myPoints);
                playerCivilianShips[civilianPlaceIndex] = (CivilianShip) getShip(shipName);
                civilianPlaceIndex++;
            }
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
            Coordinate[] myPoints = new Coordinate[size];
            for(int i = 0; i < size; i++){
                Coordinate toAdd = new Coordinate( Across, Down + i);
                playerShipPoints.add(toAdd);
                myPoints[i] = toAdd;
            }
            if(shipName.equalsIgnoreCase("aircraftCarrier") || shipName.equalsIgnoreCase("battleship") || shipName.equalsIgnoreCase("submarine")){
                getShip(shipName).setLocation(start, end);
                getShip(shipName).setPoints(myPoints);
                playerMilitaryShips[militaryPlaceIndex] = (MilitaryShip) getShip(shipName);
                militaryPlaceIndex++;
            }
            else{
                getShip(shipName).setLocation(start, end);
                getShip(shipName).setPoints(myPoints);
                playerCivilianShips[civilianPlaceIndex] = (CivilianShip) getShip(shipName);
                civilianPlaceIndex++;
            }
        }

        return null;
    }

    public String shootAtComputer(int row, int col) {

        enemySunkShip = null;
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

        for(int i = 0; i < 3; i++){
            if(enemyMilitaryShips[i].covers(coor)){
                if(!enemyMilitaryShips[i].isSunk()) {
                    computerHits.add(coor);
                    enemyMilitaryShips[i].addHit();

                    if (enemyMilitaryShips[i].isSunk()) {
                        computerShipsSunk.add(enemyMilitaryShips[i]);
                        enemySunkShip = enemyMilitaryShips[i].getName();
                    }

                    hit = true;

                }
            }
        }
        for(int i = 0; i < 2; i++){
            if(enemyCivilianShips[i].covers(coor)){
                if(!enemyCivilianShips[i].isSunk()) {
                    computerHits.add(coor);
                    enemyCivilianShips[i].addHit();

                    if (enemyCivilianShips[i].isSunk()) {
                        computerShipsSunk.add(enemyCivilianShips[i]);
                        enemySunkShip = enemyCivilianShips[i].getName();
                        Coordinate addpoints[] = enemyCivilianShips[i].getPoints();
                        for(int j = 0; j < addpoints.length; j++){

                            if(addpoints[j].equals(coor) == false){
                                computerHits.add(addpoints[j]);
                            }

                        }

                    }

                    hit = true;
                }
            }
        }
        if(!hit){
            computerMisses.add(coor);
        }
        return null;

    }

    private Coordinate getRandomCoordinate(){
        int max = 10;
        int min = 1;
        Random random = new Random();
        int randRow = random.nextInt(max - min + 1) + min;
        int randCol = random.nextInt(max - min + 1) + min;
        return new Coordinate(randRow,randCol);

    }


    public void shootAtPlayer() {
        mySunkShip = null;

        Coordinate coor =  getRandomCoordinate();
        boolean duplicate = true;
        while(duplicate){
            duplicate = false;
            for(int i = 0; i < playerMisses.size(); i++){
                if(playerMisses.get(i).equals(coor)) {
                    duplicate = true;
                }

            }
            for(int i = 0; i < playerHits.size(); i++){
                if(playerHits.get(i).equals(coor)){
                    duplicate = true;
                }
            }
            if(!duplicate){
                break;
            }
            else{
                coor = getRandomCoordinate();
            }


        }
        boolean hit = false;
        for(int i = 0; i < 3; i++){
            if(playerMilitaryShips[i].covers(coor)){
                if(!playerMilitaryShips[i].isSunk()) {
                    playerHits.add(coor);
                    playerMilitaryShips[i].addHit();

                    if (playerMilitaryShips[i].isSunk()) {
                        playerShipsSunk.add(playerMilitaryShips[i]);
                        mySunkShip = playerMilitaryShips[i].getName();
                    }

                    hit = true;
                }
            }
        }
        for(int i = 0; i < 2; i++){
            if(playerCivilianShips[i].covers(coor)){
                if(!playerCivilianShips[i].isSunk()) {
                    playerHits.add(coor);
                    playerCivilianShips[i].addHit();

                    if (playerCivilianShips[i].isSunk()) {
                        playerShipsSunk.add(playerCivilianShips[i]);
                        mySunkShip = playerCivilianShips[i].getName();
                        Coordinate addpoints[] = playerCivilianShips[i].getPoints();
                        for(int j = 0; j < addpoints.length; j++){

                            if(addpoints[j].equals(coor) == false){
                                playerHits.add(addpoints[j]);
                            }

                        }
                    }

                    hit = true;
                }
            }
        }
        if(!hit){
            playerMisses.add(coor);
        }
    }

    public boolean getScanResult(){

        return this.scanResult;
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
            if (shipToCheck != null)
                if(shipToCheck.isVisible())
                    scanResult = true;
        }


    }

    //this will return the ship object that is on the cooordinate parameter
    //null otherwise
    private Ship getShipFromCoordinate(Coordinate c){
        for(int i = 0; i < 3; i++){
            if(enemyMilitaryShips[i].containsPoint(c)){
                return enemyMilitaryShips[i];
            }
        }
        for(int i = 0; i < 2; i++){
            if(enemyCivilianShips[i].containsPoint(c)){
                return enemyCivilianShips[i];
            }
        }
        return null;
    }

    public ArrayList<Ship> getEnemySunkShips(){
        return computerShipsSunk;
    }
    public MilitaryShip[] getEnemyMilitaryShips(){
        return this.enemyMilitaryShips;
    }
    public CivilianShip[] getEnemyCivilianShips(){
        return this.enemyCivilianShips;
    }


}//end class