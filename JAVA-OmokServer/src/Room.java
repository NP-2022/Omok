import java.util.Vector;


class Stone {
	public int x, y;
}

public class Room {
	public String roomName;
	public int roomNumber;
	public int roomMax; // 최대 인원수 (2, 3, 4)
	public Vector<OmokServer.UserService> playerList = new Vector<>();
	public Vector<OmokServer.UserService> watcherList = new Vector<>();
	public Vector<Stone> stoneList = new Vector<>();
	public OmokServer.UserService owner;
	public String gameMode;
	public String ownerName;
	public int undoCount = 0;
	
	public Room(String roomName, OmokServer.UserService owner, int roomMax, String gameMode) {
		this.roomName = roomName;
		this.owner = owner;
		this.roomMax = roomMax;
		this.gameMode = gameMode;
		this.ownerName = owner.userName;
		playerList.add(owner);
	}
	
	
	public boolean isFull() {
		return playerList.size() >= roomMax;
	}
	
	public void addStone(Stone stone) {
		this.stoneList.add(stone);
	}
	
	public void undoStone() {
		this.stoneList.remove(stoneList.size()-1);
	}


	public boolean hasName(String userName) {
		for (OmokServer.UserService user : playerList) {
			if (user.userName.equals(userName)) {
				return true;
			}
		}
		return false;
	}


	public void addUser(OmokServer.UserService userService) {
		playerList.add(userService);
	}


	public int getPlayerCount() {
		return playerList.size();
	}
}
