import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


class Stone {
	public int x, y;
}

public class Room {
	public String roomName;
	public int roomNumber;
	public int roomMax; // �ִ� �ο��� (2, 3, 4)
	public Vector<OmokServer.UserService> playerList = new Vector<>();
	public Vector<OmokServer.UserService> watcherList = new Vector<>();
	public Vector<Stone> stoneList = new Vector<>();
	public OmokServer.UserService owner;
	public String gameMode;
	public String ownerName;
	public int undoCount = 0;
	public boolean isStarted = false;
	public HashMap<String, Boolean> readyMap = new HashMap<>();
	
	public Room(String roomName, OmokServer.UserService owner, int roomMax, String gameMode) {
		this.roomName = roomName;
		this.owner = owner;
		this.roomMax = roomMax;
		this.gameMode = gameMode;
		this.ownerName = owner.userName;
		playerList.add(owner);
		readyMap.put(owner.userName, false);
	}
	
	// �̸����� �غ� ���� �ؽø����� ����
	public void updateReadyState(String userName, Boolean readyState) {
		readyMap.put(userName, readyState);
	}
	
	// �̸����� �غ� ���� �ؽøʿ��� ������
	public boolean getReadyState(String userName) {
		return readyMap.get(userName);
	}
	
	public void toggleReadyState(String userName) {
		if(getReadyState(userName) == true) 
			readyMap.put(userName, false);
		else
			readyMap.put(userName, true); 
	}
	
	
	public boolean isFull() {
		return playerList.size() >= roomMax;
	}
	
	public boolean isReady() {
		for(OmokServer.UserService user : playerList) {
			if(getReadyState(user.userName) == false && user != owner)
				return false;
		}
		return true;
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
