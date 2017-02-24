package edu.oregonstate.cs361.battleship;

/**
 * Created by Garrett on 2/24/2017.
 */
public class MilitaryShip {
    public class militaryShip extends Ship {

        militaryShip(String n, int l, Coordinate s, Coordinate e, boolean stealth) {
            super(n, l, s, e, stealth);
        }

        public void checkIfSunk() {
            if (length == counter)
                isSunk(true);
            else
                isSunk(false);
        }
    }
}
