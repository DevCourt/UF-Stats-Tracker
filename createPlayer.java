/*
    This class represents a player and stores their basketball stats.

    @author Courtney Ingersoll
*/

public class createPlayer {
    private String playerName;
    private int threePointers;
    private int twoPointers;
    private int layups;
    private int assists;
    private int totalPoints;

    /*
        This is the constructor for the createPlayer class.
    */
    public createPlayer(String playerName, int threePointers,
            int twoPointers, int layups, int assists, int totalPoints) {

        this.playerName = playerName;
        this.threePointers = threePointers;
        this.twoPointers = twoPointers;
        this.layups = layups;
        this.assists = assists;
        this.totalPoints = totalPoints;
    }

    /*
        This method returns the player's name.
        @return the player's name
    */
    public String getPlayerName() {
        return playerName;
    }

    /*
        This method returns the player's number of three pointers.
        @return the player's number of three pointers.
    */
    public int getThreePointers() {
        return threePointers;
    }

    /*
        This method returns the player's number of two pointers.
        @return the player's number of two pointers.
    */
    public int getTwoPointers() {
        return twoPointers;
    }

    /*
        This method returns the player's number of layups.
        @return the player's number of layups.
    */
    public int getLayups() {
        return layups;
    }

    /*
        This method returns the player's number of assists.
        @return the player's number of assists.
    */
    public int getAssists() {
        return assists;
    }

    /*
        This method returns the player's total points.
        @return the player's total points.
    */
    public int getTotalPoints() {
        return totalPoints;
    }
}