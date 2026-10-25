import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class players {
	private ArrayList<createPlayer> playerList;

	public players() {
		playerList = new ArrayList<createPlayer>();
		playerList.add(new createPlayer("Alex Condon", 5, 10, 3, 7, 35));
		playerList.add(new createPlayer("Boogie Fland", 8, 12, 5, 10, 50));
		playerList.add(new createPlayer("Thomas Haugh", 3, 8, 2, 5, 25));
	}

	public ArrayList<createPlayer> getPlayerList() {
		return playerList;
	}

	public boolean uploadPlayersFromFile(String filePath) {
		try {
			File file = new File(filePath);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] data = line.split(",");
				if (data.length != 6) {
					System.out.println("Invalid data format in file: " + line);
					continue;
				}
				String playerName = data[0].trim();
				int threePointers = Integer.parseInt(data[1].trim());
				int twoPointers = Integer.parseInt(data[2].trim());
				int layups = Integer.parseInt(data[3].trim());
				int assists = Integer.parseInt(data[4].trim());
				int totalPoints = (threePointers * 3) + (twoPointers * 2) + layups;
				createPlayer newPlayer = new createPlayer(playerName, threePointers, twoPointers, layups, assists, totalPoints);
				boolean playerExists = false;
				for (createPlayer player : playerList) {
					if (player.getPlayerName().equalsIgnoreCase(playerName)) {
						playerExists = true;
						break;
					}
				}
				if (!playerExists) {
					playerList.add(newPlayer);
				} else {
					System.out.println("Player is already in the database.");
				}
			}
			scanner.close();
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + filePath);
			return false;
		}
	}
}
