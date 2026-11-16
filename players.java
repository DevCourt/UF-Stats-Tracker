/*
    This class manages the player data and interacts with the SQLite database.
    It allows to add, remove, update, and load players.

    @author Courtney Ingersoll

*/
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
    Creates a player object and manages the player data.
*/
public class players {
    private ArrayList<createPlayer> playerList;
    private final String databaseURL = "jdbc:sqlite:players.db";

    public players() {
        playerList = new ArrayList<>();

        try {
            Class.forName("org.sqlite.JDBC");
            createTable();
            loadPlayers();

            if (playerList.isEmpty()) {
                addPlayer(new createPlayer("Alex Condon", 5, 10, 3, 7, 38));
                addPlayer(new createPlayer("Boogie Fland", 8, 12, 5, 10, 53));
                addPlayer(new createPlayer("Thomas Haugh", 3, 8, 2, 5, 27));
            }

        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    /*
        This method establishes a connection to the SQLite database.
        @throws SQLException if an error occurs.
        @return the database connection.
    */
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(databaseURL);
    }

    /*
        This method creates the players table in the SQLite database.
        @throws SQLException if an error occurs.
    */
    private void createTable() throws SQLException {

        String sql = "CREATE TABLE IF NOT EXISTS players ("+ "playerName TEXT PRIMARY KEY COLLATE NOCASE, " + "threePointers INTEGER NOT NULL, " + "twoPointers INTEGER NOT NULL, " + "layups INTEGER NOT NULL, " + "assists INTEGER NOT NULL, " + "totalPoints INTEGER NOT NULL)";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);
        }
    }

    /*
        This method loads players from the SQLite database.
        @throws SQLException if an error occurs.
        @return the loaded players.
    */
    private void loadPlayers() throws SQLException {
        playerList.clear();
        String sql = "SELECT * FROM players";

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(sql)) {

            while (results.next()) {
                createPlayer player = new createPlayer(results.getString("playerName"), results.getInt("threePointers"), results.getInt("twoPointers"), results.getInt("layups"), results.getInt("assists"), results.getInt("totalPoints"));
                playerList.add(player);
            }
        }
    }
    /*
        @return the list of players.
    */
    public ArrayList<createPlayer> getPlayerList() {
        return playerList;
    }


    /*
        This method adds a player to the SQLite database.
        @param player the player to add.
        @return true if the player was added successfully, false if the player was failed to be added.
    */
    public boolean addPlayer(createPlayer player) {
        String sql = "INSERT INTO players " + "(playerName, threePointers, twoPointers, layups, assists, totalPoints) " + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, player.getPlayerName());
            statement.setInt(2, player.getThreePointers());
            statement.setInt(3, player.getTwoPointers());
            statement.setInt(4, player.getLayups());
            statement.setInt(5, player.getAssists());
            statement.setInt(6, player.getTotalPoints());
            statement.executeUpdate();
            playerList.add(player);
            return true;

        } catch (SQLException e) {
            System.out.println("Could not add player: " + e.getMessage());
            return false;
        }
    }
    /*
        This method removes a player from the SQLite database.
        @param playerName  is called to remove the player
        @return true if the player was removed successfully, false if the player failed to be removed.
    */
    public boolean removePlayer(String playerName) {
        String sql = "DELETE FROM players WHERE playerName = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerName);
            int deleted = statement.executeUpdate();

            if (deleted > 0) {
                playerList.removeIf(player ->
                        player.getPlayerName().equalsIgnoreCase(playerName));
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Could not remove player: " + e.getMessage());
        }
        return false;
    }
    /*
        This method updates a player in the SQLite database.
        @param updatedPlayer the updated player.
        @return true if the player was updated successfully, false if the player failed to be updated.
        @throws SQLExpception if an error occurs.
    */
    public boolean updatePlayer(createPlayer updatedPlayer) {
        String sql = "UPDATE players SET " + "threePointers = ?, " + "twoPointers = ?, " + "layups = ?, " + "assists = ?, " + "totalPoints = ? " + "WHERE playerName = ?";
        
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, updatedPlayer.getThreePointers());
            statement.setInt(2, updatedPlayer.getTwoPointers());
            statement.setInt(3, updatedPlayer.getLayups());
            statement.setInt(4, updatedPlayer.getAssists());
            statement.setInt(5, updatedPlayer.getTotalPoints());
            statement.setString(6, updatedPlayer.getPlayerName());

            int updated = statement.executeUpdate();

            if (updated > 0) {

                for (int i = 0; i < playerList.size(); i++) {
                    if (playerList.get(i).getPlayerName()
                            .equalsIgnoreCase(updatedPlayer.getPlayerName())) {
                        playerList.set(i, updatedPlayer);
                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Could not update player: " + e.getMessage());
        }
        return false;
    }
    /*
        This method uploads player from a text file.
        @param filePath the path to the text file.
        @return true if the players were uploaded successfully, false if the players failed to be uploaded.
    */
    public boolean uploadPlayersFromFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");

                if (data.length != 5 && data.length != 6) {
                    System.out.println("Invalid data format: " + line);
                    continue;
                }

                try {
                    String playerName = data[0].trim();

                    int threePointers = Integer.parseInt(data[1].trim());
                    int twoPointers = Integer.parseInt(data[2].trim());
                    int layups = Integer.parseInt(data[3].trim());
                    int assists = Integer.parseInt(data[4].trim());

                    int totalPoints = (threePointers * 3) + (twoPointers * 2) + layups;
                    createPlayer newPlayer = new createPlayer(playerName, threePointers, twoPointers, layups, assists, totalPoints);
                    addPlayer(newPlayer);

                } catch (NumberFormatException e) {
                    System.out.println("Invalid numbers in file: " + line);
                }
            }

            loadPlayers();
            return true;

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
            return false;

        } catch (SQLException e) {
            System.out.println("Database import error: " + e.getMessage());
            return false;
        }
    }
}
