import java.util.HashMap;
import java.util.Map;
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
	public int undoDisagreeCount = 0;
	
	public boolean isStarted = false;
	public HashMap<String, Boolean> readyMap = new HashMap<>();
	public HashMap<String, Boolean> chatLimitMap = new HashMap<>();
	
	public Room(String roomName, OmokServer.UserService owner, int roomMax, String gameMode) {
		this.roomName = roomName;
		this.owner = owner;
		this.roomMax = roomMax;
		this.gameMode = gameMode;
		this.ownerName = owner.userName;
		playerList.add(owner);
		readyMap.put(owner.userName, false);
		chatLimitMap.put(owner.userName, false);
	}
	
	// 이름으로 준비 상태 해시맵으로 저장
	public void updateReadyState(String userName, Boolean readyState) {
		readyMap.put(userName, readyState);
	}
	
	// 이름으로 준비 상태 해시맵에서 가져옴
	public boolean getReadyState(String userName) {
		return readyMap.get(userName);
	}
	
	// 준비 상태 바꿈
	public void toggleReadyState(String userName) {
		if(getReadyState(userName) == true) 
			readyMap.put(userName, false);
		else
			readyMap.put(userName, true); 
	}
	
	public void updateChatLimitState(String userName, Boolean chatLimitState) {
		chatLimitMap.put(userName, chatLimitState);
	}
	
	// 채팅 금지 상태 가져오기
	public boolean getChatLimitState(String userName) {
		return chatLimitMap.get(userName);
	}
	
	// 채팅 금지 상태 바꿈
	public void toggleChatLimitState(String userName) {
		if(getChatLimitState(userName) == true) 
			chatLimitMap.put(userName, false);
		else
			chatLimitMap.put(userName, true); 
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
	
	public Stone undoStone() {
		return this.stoneList.remove(stoneList.size()-1);
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
	
	public void addWatcher(OmokServer.UserService userService) {
		watcherList.add(userService);
	}


	public int getPlayerCount() {
		return playerList.size();
	}
}
