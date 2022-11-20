import java.util.Vector;

class Stone {
	public int x, y;
}

public class Room {
	public Room(int roomNumber, String roomName, String owner, int roomMax) {
		this.roomNumber = roomNumber;
		this.roomName = roomName;
		this.owner = owner;
		this.roomMax = roomMax;
		playerList.add(owner);
	}
	public String roomName;
	public int roomNumber;
	public int roomMax; // 최대 인원수 (2, 3, 4)
	public Vector<String> playerList = new Vector<>();
	public Vector<String> watcherList = new Vector<>();
	public Vector<Stone> stoneList = new Vector<>();
	public String owner;
	
	public boolean isFull() {
		return playerList.size() == roomMax;
	}
	
	public void addStone(Stone stone) {
		this.stoneList.add(stone);
	}
}
