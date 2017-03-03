package edu.oregonstate.cs361.battleship;

/**
 * Created by michaelhilton on 1/8/17.
 */
public class Coordinate {
    private int Across;
    private int Down;

    public Coordinate(int Across, int Down) {
        this.Across = Across;
        this.Down = Down;
    }

    public int getDown() {
        return Down;
    }

    public void setDown(int down) {
        Down = down;
    }

    public int getAcross() {
        return Across;
    }

    public void setAcross(int across) {
        Across = across;
    }

    public boolean equals(Coordinate toTest){
        if((this.Across == toTest.Across) && (this.Down == toTest.Down))
            return true;
        return false;
    }
}
