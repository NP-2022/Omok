import java.util.Vector;


class Stone {
	public int x, y;
}

public class Room {
	public String roomName;
	public int roomNumber;
	public int roomMax; // 최대 인원수 (2, 3, 4)
	public Vector<JavaGameServer.UserService> playerList = new Vector<>();
	public Vector<JavaGameServer.UserService> watcherList = new Vector<>();
	public Vector<Stone> stoneList = new Vector<>();
	public JavaGameServer.UserService owner;
	public String gameMode;
	public String ownerName;
	
	public Room(String roomName, JavaGameServer.UserService owner, int roomMax, String gameMode) {
		this.roomName = roomName;
		this.owner = owner;
		this.roomMax = roomMax;
		this.gameMode = gameMode;
		this.ownerName = owner.UserName;
		playerList.add(owner);
	}
	
	
	public boolean isFull() {
		return playerList.size() >= roomMax;
	}
	
	public void addStone(Stone stone) {
		this.stoneList.add(stone);
	}


	public boolean hasName(String userName) {
		for (JavaGameServer.UserService user : playerList) {
			if (user.UserName.equals(userName)) {
				return true;
			}
		}
		return false;
	}


	public void addUser(JavaGameServer.UserService userService) {
		playerList.add(userService);
	}


	public int getPlayerCount() {
		return playerList.size();
	}
}
