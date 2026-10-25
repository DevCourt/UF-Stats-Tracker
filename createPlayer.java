public class createPlayer {
	private String playerName;
	private int threePointers;
	private int twoPointers;
	private int layups;
	private int assists;
	private int totalPoints;

	public createPlayer(String playerName, int threePointers, int twoPointers, int layups, int assists, int totalPoints) {
		this.playerName = playerName;
		this.threePointers = threePointers;
		this.twoPointers = twoPointers;
		this.layups = layups;
		this.assists = assists;
		this.totalPoints = totalPoints;
	}

	public String getPlayerName() {
    	return playerName;
	}

	public int getThreePointers() {
		return threePointers;
	}

	public int getTwoPointers() {
		return twoPointers;
	}

	public int getLayups() {
		return layups;
	}

	public int getAssists() {
		return assists;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public createPlayer updatePlayerStats(String playerName, int threePointers, int twoPointers, int layups, int assists) {
		int totalPoints = (threePointers * 3) + (twoPointers * 2) + layups;
		return new createPlayer(playerName, threePointers, twoPointers, layups, assists, totalPoints);
	}
}
