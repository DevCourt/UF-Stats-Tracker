import java.util.Scanner;
import java.util.ArrayList;

public class main {
	private String playerName;
	private int threePointers;
	private int twoPointers;
	private int layups;
	private int assists;
	private int totalPoints;
	private int option = 0;
	private ArrayList<createPlayer> playerList = new ArrayList<>();

	Scanner scanner = new Scanner(System.in);

	public main() {
		players players = new players();
		playerList = players.getPlayerList();

		while (option != 7) {
			System.out.println("Welcome to the UF Basketball Status Tracker!");
			System.out.println("Please select the number that corresponds with the stat.");
			System.out.println("1. Display All Players");
			System.out.println("2. Add a Player");
			System.out.println("3. Remove a Player");
			System.out.println("4. Update Player Stats");
			System.out.println("5. Show Player Stats");
			System.out.println("6. Upload a txt file of player's data");
			System.out.println("7. Exit");

			System.out.print("Enter your choice: ");
			if (scanner.hasNextInt()) {
				option = scanner.nextInt();
			} 
			else {
				System.out.println("Invalid number. Please enter a number bertween 1 and 7.");
				scanner.next();
				option = 0;
				continue;
			}

			if (option == 1) {
				System.out.println("Here are the list of players: " + "\n");

				for (createPlayer player : playerList) {
					System.out.println("Player Name: " + player.getPlayerName());
					System.out.println("Three Pointers: " + player.getThreePointers());
					System.out.println("Two Pointers: " + player.getTwoPointers());
					System.out.println("Layups: " + player.getLayups());
					System.out.println("Assists: " + player.getAssists());
					System.out.println("Total Points: " + player.getTotalPoints() + "\n");
				}
			}
			else if (option == 2) {
				System.out.print("Enter player name: ");
				playerName = scanner.next();
				System.out.print("Enter number of three pointers: ");
				threePointers = scanner.nextInt();
				System.out.print("Enter number of two pointers: ");
				twoPointers = scanner.nextInt();
				System.out.print("Enter number of layups: ");
				layups = scanner.nextInt();
				System.out.print("Enter number of assists: ");
				assists = scanner.nextInt();
				totalPoints = (threePointers * 3) + (twoPointers * 2) + layups;
				createPlayer newPlayer = new createPlayer(playerName, threePointers, twoPointers, layups, assists, totalPoints);

				for (createPlayer player : playerList) {
					if (player.getPlayerName().equalsIgnoreCase(playerName)) {
						System.out.println("Player already exists.");
						break;
					} else {
						playerList.add(newPlayer);
						System.out.println("Player added successfully.");
						break;
					}
				}
			}
			else if (option == 3) {
				System.out.print("Enter player name to remove: ");
				playerName = scanner.next();
				System.out.print("Are you sure you want to remove " + playerName + "? (Y/N): ");
				String confirmation = scanner.next();

				if (confirmation.equalsIgnoreCase("Y")) {
					for (createPlayer player : playerList) {
						if (player.getPlayerName().equalsIgnoreCase(playerName)) {
							playerList.remove(player);
							System.out.println("Player removed successfully.");
							break;
						}
					}
				} else {
					System.out.println("Player has not been removed.");
				}
			}
			else if (option == 4) {
				scanner.nextLine(); 
				System.out.print("Enter player name to update: ");
				playerName = scanner.nextLine().trim();
				boolean playerFound = false;

				for (createPlayer player : playerList) {
					if (player.getPlayerName().equalsIgnoreCase(playerName)) {
						playerFound = true;
						int updatedThreePointers = player.getThreePointers();
						int updatedTwoPointers = player.getTwoPointers();
						int updatedLayups = player.getLayups();
						int updatedAssists = player.getAssists();
						try {
							System.out.print("Which stat would you like to update? (1. Three Pointers, 2. Two Pointers, 3. Layups, 4. Assists): ");
							int option = scanner.nextInt();
							if (option == 1) {
								System.out.print("Enter new number of three pointers: ");
								updatedThreePointers = scanner.nextInt();
							} else if (option == 2) {
								System.out.print("Enter new number of two pointers: ");
								updatedTwoPointers = scanner.nextInt();
							} else if (option == 3) {
								System.out.print("Enter new number of layups: ");
								updatedLayups = scanner.nextInt();
							} else if (option == 4) {
								System.out.print("Enter new number of assists: ");
								updatedAssists = scanner.nextInt();
							} else {
								System.out.println("Invalid option.");
								break;
							}
						} catch (java.util.InputMismatchException e) {
							scanner.nextLine();
							System.out.println("Invalid input. Please enter whole numbers for all stats.");
							break;
						}
						if (updatedThreePointers < 0 || updatedTwoPointers < 0 || updatedLayups < 0 || updatedAssists < 0) {
							System.out.println("Invalid input. Please enter non-negative values for stats.");
							break;
						}
						totalPoints = (updatedThreePointers * 3) + (updatedTwoPointers * 2) + updatedLayups;
						int index = playerList.indexOf(player);
						createPlayer updatedPlayer = new createPlayer(player.getPlayerName(), updatedThreePointers, updatedTwoPointers, updatedLayups, updatedAssists, totalPoints);
						playerList.set(index, updatedPlayer);
						System.out.println(playerName + "'s stats have been updated successfully.");
						break;
					}
				}
				if (!playerFound) {
					System.out.println("Player not found.");
				}
			}
			else if (option == 5) {
				System.out.print("Enter player name to show stats: ");
				scanner.nextLine(); 
				playerName = scanner.nextLine().trim();
				boolean playerFound = false;

				for (createPlayer player : playerList) {
					if (player.getPlayerName().equalsIgnoreCase(playerName)) {
						System.out.println("Player Name: " + player.getPlayerName());
						System.out.println("Three Pointers: " + player.getThreePointers());
						System.out.println("Two Pointers: " + player.getTwoPointers());
						System.out.println("Layups: " + player.getLayups());
						System.out.println("Assists: " + player.getAssists());
						System.out.println("Total Points: " + player.getTotalPoints() + "\n");
						playerFound = true;
						break;
					}
				}
				if (!playerFound) {
					System.out.println("Player not found.");
				}
			}
			else if (option == 6) {
				System.out.print("Enter the path of the txt file: ");
				String filePath = scanner.next();
				boolean success = players.uploadPlayersFromFile(filePath);

				if (success) {
					System.out.println("Players uploaded successfully.");
				} else {
					System.out.println("Failed to upload players from file.");
				}
			}
			else if (option == 7) {
				System.out.println("Exiting the program.");
			}
			else {
				System.out.println("Invalid option. Please enter a number between 1 and 7.");
			}
		}
	}
}