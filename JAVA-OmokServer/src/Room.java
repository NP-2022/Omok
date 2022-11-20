import java.util.Vector;

class Stone {
	public int x, y;
}

public class Room {
	public String roomName;
	public int roomNumber;
	public int roomMax; // �ִ� �ο��� (2, 3, 4)
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
}
