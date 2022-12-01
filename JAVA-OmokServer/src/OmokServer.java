//JavaObjServer.java ObjectStream 기반 채팅 Server

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class OmokServer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;
	private JTextField txtPortNumber;

	private ServerSocket socket; // 서버소켓
	private Socket client_socket; // accept() 에서 생성된 client 소켓
	private Vector<UserService> userVec = new Vector(); // 연결된 사용자를 저장할 벡터
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의

	private Vector<Room> roomVec = new Vector(); // 생성된 방을 저장할 벡터

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OmokServer frame = new OmokServer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public OmokServer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창이 닫히면 프로세스 종료
		setBounds(100, 100, 338, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 300, 298);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setBounds(13, 318, 87, 26);
		contentPane.add(lblNewLabel);

		txtPortNumber = new JTextField();
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setText("30000");
		txtPortNumber.setBounds(112, 318, 199, 26);
		contentPane.add(txtPortNumber);
		txtPortNumber.setColumns(10);

		JButton btnServerStart = new JButton("Server Start");
		btnServerStart.addActionListener(new ActionListener() { // 버튼 눌리면 서버 시작
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				AppendText("Chat Server Running..");
				btnServerStart.setText("Chat Server Running..");
				btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
				txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// 새로운 참가자 accept() 하고 user thread를 새로 생성한다.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
					AppendText("새로운 참가자 from " + client_socket);
					// User 당 하나씩 Thread 생성
					UserService new_user = new UserService(client_socket);
					userVec.add(new_user); // 새로운 참가자 배열에 추가
					new_user.start(); // 만든 객체의 스레드 실행
					AppendText("현재 참가자 수 " + userVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		// textArea.append("사용자로부터 들어온 메세지 : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(ChatMsg msg) {
		// textArea.append("사용자로부터 들어온 object : " + str+"\n");
		textArea.append("code = " + msg.code + "\n");
		textArea.append("id = " + msg.userName + "\n");
		textArea.append("data = " + msg.data + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User 당 생성되는 Thread
	// Read One 에서 대기 -> Write All
	class UserService extends Thread {
		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;

		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private Socket client_socket;
		private Vector user_vc;
		public String userName = "";
		public String userStatus;

		public int roomNumber;
		public boolean ready = false;
		// public boolean userTurn;

		public UserService(Socket client_socket) {
			// TODO Auto-generated constructor stub
			// 매개변수로 넘어온 자료 저장
			this.client_socket = client_socket;
			this.user_vc = userVec;
			try {
				oos = new ObjectOutputStream(client_socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(client_socket.getInputStream());

			} catch (Exception e) {
				AppendText("userService error");
			}
		}

		public void Login() {
			AppendText("새로운 참가자 " + userName + " 입장.");
			WriteOne("Welcome to Java chat server\n");
			WriteOne(userName + "님 환영합니다.\n"); // 연결된 사용자에게 정상접속을 알림
			String msg = "[" + userName + "]님이 입장 하였습니다.\n";
			WriteOthers(msg); // 아직 user_vc에 새로 입장한 user는 포함되지 않았다.
		}

		public void Logout() {
			String msg = "[" + userName + "]님이 퇴장 하였습니다.\n";
			userVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
			WriteAll(msg); // 나를 제외한 다른 User들에게 전송
			AppendText("사용자 " + "[" + userName + "] 퇴장. 현재 참가자 수 " + userVec.size());
		}

		// 모든 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public void WriteAll(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.userStatus == "O")
					user.WriteOne(str);
			}
		}

		// 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
		public void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.userStatus == "O")
					user.WriteOneObject(ob);
			}
		}

		// 나를 제외한 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public void WriteOthers(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this && user.userStatus == "O")
					user.WriteOne(str);
			}
		}

		// UserService Thread가 담당하는 Client 에게 1:1 전송
		public void WriteOne(String msg) {
			try {
				ChatMsg obcm = new ChatMsg("SERVER", "200", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}

		// 귓속말 전송
		public void WritePrivate(String msg) {
			try {
				ChatMsg obcm = new ChatMsg("귓속말", "200", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}

		public void WriteOneObject(Object ob) {
			try {
				oos.writeObject(ob);
			} catch (IOException e) {
				AppendText("oos.writeObject(ob) error");
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout();
			}
		}
		
		public Room getRoom(ChatMsg msg) {
			int roomnum = -1;
			for (int i = 0; i < roomVec.size(); i++) {
				if (msg.roomName.equals(roomVec.get(i).roomName)) {
					roomnum = i;
					break;
				}
			}

			msg.roomNumber = roomnum;
			Room room = roomVec.get(roomnum);
			return room;
		}

		public void createRoom(ChatMsg msg) {
			Room room = new Room(msg.roomName, this, msg.roomMax, msg.gameMode); // 방 생성 (방 번호, 방 이름, 방장 이름, 방 최대 인원수)
			roomVec.add(room); // 방 벡터에 방 추가
			int vecIndex = roomVec.indexOf(room);
			room.roomNumber = vecIndex;
			this.roomNumber = vecIndex; // 이 유저의 방 번호 지정
			msg.roomNumber = vecIndex;
			AppendText(roomNumber + "번 방 생성 완료. 현재 방 개수 " + roomVec.size());

			for (UserService user : room.playerList)
				user.updateUserList(msg);

		}

		public void exitRoom(ChatMsg msg) {
			int roomnum = -1;
			for (int i = 0; i < roomVec.size(); i++) {
				if (msg.roomName.equals(roomVec.get(i).roomName)) {
					roomnum = i;
					break;
				}
			}
			System.out.println(msg.userName + "가 방에서 퇴장했습니다.");

			Room room = roomVec.get(roomnum);
			System.out.println("전, 방의 인원은 : " + room.playerList.size());
			for (int i = 0; i < room.playerList.size(); i++) {
				OmokServer.UserService user = room.playerList.get(i);
				if (user.userName.equals(msg.userName)) {
					room.playerList.remove(i);
					break;
				}
			}
			System.out.println("후, 방의 인원은 : " + room.playerList.size());
			if (room.playerList.size() != 0) {
				room.owner = room.playerList.get(0);  //방장 바꾸기
				room.ownerName = room.owner.userName; 
				for (UserService user : room.playerList) { // 방에 있는 모든 유저에게 유저 목록 리스트를 갱신
					user.updateUserList(msg);
				}
				ChatMsg cm = new ChatMsg(userName, "201", "");
				cm.roomName = room.roomName;
				cm.roomNumber = room.roomNumber;
				cm.data = "[" + userName + "] 님이 퇴장했습니다.";
				sendGameMessage(cm);
			} else {
				for (int i = 0; i < roomVec.size(); i++) {
					Room emptyroom = roomVec.get(i);
					if (emptyroom.roomName.equals(msg.roomName)) {
						roomVec.remove(i);
						break;
					}
				}
				System.out.println(msg.roomName + "방이 비었습니다.");
			}
		}

		public void insertRoom(ChatMsg msg) {
			Room room = roomVec.get(msg.roomNumber); // 방 번호로 입장 (방 목록 리스트는 서버의 방 배열과 인덱스를 공유함)
			if (room.isFull()) {
				WriteOne("방이 꽉 찼습니다!");
				return;
			} else if (room.hasName(msg.userName)) { // 방에 유저의 이름이 이미 있는 경우
				WriteOne("이미 들어가 있는 방입니다!");
				return;
			} else { // 유저에게 방의 정보를 주고 입장시킴
				AppendText("플레이어 " + userName + " " + room.roomNumber + "번 방 입장.");
				msg = new ChatMsg(userName, "700", "");
				msg.roomMax = room.roomMax;
				msg.roomName = room.roomName;
				msg.gameMode = room.gameMode;
				msg.roomNumber = room.roomNumber;
				room.addUser(this); // // 방 벡터에 유저를 추가
				WriteAllObject(msg);
				for (UserService user : room.playerList) { // 방에 있는 모든 유저에게 유저 목록 리스트를 갱신
					user.updateUserList(msg);
				}
				ChatMsg cm = new ChatMsg(userName, "201", "");
				cm.roomName = room.roomName;
				cm.roomNumber = room.roomNumber;
				cm.data = "[" + userName + "] 님이 입장했습니다.";
				sendGameMessage(cm);
			}
		}

		public void updateRoomList() { // 방 목록을 갱신하는 String을 모두에게 전역으로 보냄
			ChatMsg msg = new ChatMsg(userName, "702", "");
			StringBuffer data = new StringBuffer();
			for (Room room : roomVec) {
				data.append(String.format("[No.%d] [%s] [mode:%s] [%d/%d] [방장:%s]\n", room.roomNumber, room.roomName,
						room.gameMode, room.getPlayerCount(), room.roomMax, room.ownerName));
			}
			msg.data = data.toString();
			WriteAllObject(msg);
		}

		public void updateUserList(ChatMsg msg) { // 이 함수를 호출한 UserService에게만 유저 목록을 갱신하는 String을 보냄
			ChatMsg cm = new ChatMsg(userName, "703", "");
			cm.roomNumber = msg.roomNumber;
			cm.roomName = msg.roomName;
			Room room = roomVec.get(cm.roomNumber);
			StringBuffer data = new StringBuffer();
			int i = 0;
			String stoneName = "";
			for (UserService user : room.playerList) {
				if (room.roomMax == 4) {
					switch (i) {
					case 0:
						stoneName = "흑돌";
						break;
					case 1:
						stoneName = "백돌";
						break;
					case 2:
						stoneName = "흑돌";
						break;
					case 3:
						stoneName = "백돌";
						break;
					}
				} else {
					switch (i) {
					case 0:
						stoneName = "흑돌";
						break;
					case 1:
						stoneName = "백돌";
						break;
					case 2:
						stoneName = "적돌";
						break;
					// case 3: stoneName = "미정"; break;
					}
				}
				data.append(String.format("[Player%d] [이름:%s] [돌:%s]\n", i + 1, user.userName, stoneName));
				i++;
			}
			cm.data = data.toString();
			System.out.println("11111111111--  " + msg.code);
			WriteOneObject(cm);
		}

		public void finishGame(ChatMsg msg) { // 게임 종료를 알림
			int roomnum = -1;
			for (int i = 0; i < roomVec.size(); i++) {
				if (msg.roomName.equals(roomVec.get(i).roomName)) {
					roomnum = i;
					break;
				}
			}
			Room room = roomVec.get(roomnum);
			room.stoneList.clear();
		}

		public void drawStone(ChatMsg msg) { // 벡터에서
			int roomnum = -1;
			for (int i = 0; i < roomVec.size(); i++) {
				if (msg.roomName.equals(roomVec.get(i).roomName)) {
					roomnum = i;
					break;
				}
			}
			System.out.println(roomnum + "room DrawStone");
			msg.roomNumber = roomnum;
			Room room = roomVec.get(roomnum);
			
			if(!room.isStarted) {
				ChatMsg cm = new ChatMsg(msg.userName, "201", "");
				cm.roomName = msg.roomName;
				cm.data = "게임 시작 전입니다.";
				WriteOneObject(cm);
				return;
			}
				
			
			Stone stone = new Stone();
			stone.y = msg.y;
			stone.x = msg.x;
//			room.addStone(stone);
			int color;
			int usernum = -1;
			for (color = 0; color < room.playerList.size(); color++) {
				if (msg.userName.equals(room.playerList.get(color).userName)) {
					usernum = color;
					color = color + 1;
					if (room.roomMax == 4) {
						color = (color % 2);
						if (color == 0)
							color = 2;
					}
					break;
				}
			}
//			if (room.roomMax == 4) {
//				color = color % 2;
//			}
			msg.stone = color;
//			WriteAllObject(msg);
			if (room.roomMax == room.playerList.size()) {
				if ((room.stoneList.size() % room.roomMax) == usernum) {
					room.addStone(stone);
					System.out.println("stone Size : " + room.stoneList.size());
					for (UserService user : room.playerList) // 방에 있는 모든 유저에게 바둑돌 전송
						user.WriteOneObject(msg);
					ChatMsg cm = new ChatMsg(msg.userName, "201", "");
					cm.roomName = msg.roomName;
					cm.data = "[" + msg.userName + "]님이 돌을 놓았습니다.";
					sendGameMessage(cm);
				} else {
					System.out.println("차례가 아님");
				}
			}
		}

		public void undoStone(ChatMsg msg) {
			int roomnum = -1;
			for (int i = 0; i < roomVec.size(); i++) {
				if (msg.roomName.equals(roomVec.get(i).roomName)) {
					roomnum = i;
					break;
				}
			}

			msg.roomNumber = roomnum;
			Room room = roomVec.get(roomnum);
			room.undoCount++;
			if (room.undoCount == room.roomMax) {
				room.undoStone();
				System.out.println(roomnum + " room undoStone");
				room.undoCount = 0;
			}
			System.out.println("stone Size : " + room.stoneList.size());
			for (UserService user : room.playerList) // 방에 있는 모든 유저에게 undo 전송
				user.WriteOneObject(msg);
		}

		public void sendGameMessage(ChatMsg msg) {
			System.out.println("sendGameMessage Called : " + msg.code + " " + msg.roomName + " " + msg.userName);
			int roomnum = -1;
			for (int i = 0; i < roomVec.size(); i++) {
				if (msg.roomName.equals(roomVec.get(i).roomName)) {
					roomnum = i;
					break;
				}
			}

			msg.roomNumber = roomnum;
			Room room = roomVec.get(roomnum);

			for (UserService user : room.playerList) { // 방에 있는 모든 유저에게 message 전송
				user.WriteOneObject(msg);
				System.out.println(
						"sendGameMessage WriteOneObject : " + msg.code + " " + msg.roomName + " " + msg.userName);
			}
		}
		
		public void startGame(ChatMsg msg) { // 게임 시작 메소드
			Room room = getRoom(msg);
			
			if(!room.isFull()) {
				ChatMsg cm = new ChatMsg(userName, "201", "게임 시작 인원이 부족합니다.");
				cm.roomName = room.roomName;
				sendGameMessage(cm);
				return;
			}
			
			if(!room.isReady()) {
				ChatMsg cm = new ChatMsg(userName, "201", "모든 인원이 준비해야 시작 할 수 있습니다.");
				cm.roomName = room.roomName;
				sendGameMessage(cm);
				return;
			}
			
			room.isStarted = true;
			ChatMsg startCm = new ChatMsg(userName, "201", "게임을 시작합니다.");
			startCm.roomName = room.roomName;
			sendGameMessage(startCm);
			
			ChatMsg cm = new ChatMsg(userName, "800", "");
			cm.roomName = room.roomName;
			for (UserService user : room.playerList) { // 방에 있는 모든 유저에게 전송
				user.WriteOneObject(cm);
			}
		}
		
		public void readyMethod(ChatMsg cm) {
			// 준비 상태 바꾸고 msg 전달
			String msg = ready ? "["+userName+"]님 준비 취소" : "["+userName+"]님 준비 완료";
			
			ready = !ready; // ready 토글
						
			Room room = getRoom(cm);
			
			ChatMsg readyCm = new ChatMsg(userName, "201", msg);
			readyCm.roomName = room.roomName;
			
			String readyState = ready ? "true" : "false";
			
			ChatMsg readyToggleCm = new ChatMsg(userName, "801", readyState); // 준비 버튼 토글시키기
			readyToggleCm.roomName = room.roomName;
			
			for (UserService user : room.playerList) { // 방에 있는 모든 유저에게 전송
				user.WriteOneObject(readyCm);
			}
			WriteOneObject(readyToggleCm); // 당사자 버튼만 변경
				
		}

		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {

					Object obcm = null;
					String msg = null;
					ChatMsg cm = null;
					if (socket == null)
						break;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						AppendObject(cm);
					} else
						continue;
					if (cm.code.matches("100")) {
						userName = cm.userName;
						userStatus = "O"; // Online 상태
						Login();
						updateRoomList();
					} else if (cm.code.matches("200")) {
						msg = String.format("[%s] %s", cm.userName, cm.data);
						AppendText(msg); // server 화면에 출력
						String[] args = msg.split(" "); // 단어들을 분리한다.
						if (args.length == 1) { // Enter key 만 들어온 경우 Wakeup 처리만 한다.
							userStatus = "O";
						} else if (args[1].matches("/exit")) {
							Logout();
							break;
						} else if (args[1].matches("/list")) {
							WriteOne("User list\n");
							WriteOne("Name\tStatus\n");
							WriteOne("-----------------------------\n");
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								WriteOne(user.userName + "\t" + user.userStatus + "\n");
							} 
							WriteOne("-----------------------------\n");
						} else if (args[1].matches("/sleep")) {
							userStatus = "S";
						} else if (args[1].matches("/wakeup")) {
							userStatus = "O";
						} else if (args[1].matches("/to")) { // 귓속말
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								if (user.userName.matches(args[2]) && user.userStatus.matches("O")) {
									String msg2 = "";
									for (int j = 3; j < args.length; j++) {// 실제 message 부분
										msg2 += args[j];
										if (j < args.length - 1)
											msg2 += " ";
									}
									// /to 빼고.. [귓속말] [user1] Hello user2..
									user.WritePrivate(args[0] + " " + msg2 + "\n");
									// user.WriteOne("[귓속말] " + args[0] + " " + msg2 + "\n");
									break;
								}
							}
						} else { // 일반 채팅 메시지
							userStatus = "O";
							// WriteAll(msg + "\n"); // Write All
							WriteAllObject(cm);
						}
					} else if (cm.code.matches("201")) {
						sendGameMessage(cm);
					} else if (cm.code.matches("400")) { // logout message 처리
						Logout();
						break;
					} else if (cm.code.matches("600")) { // 방 생성 처리
						createRoom(cm);
						WriteAllObject(cm);
						updateRoomList();
					}

					else if (cm.code.matches("700")) { // 방 입장 처리
						insertRoom(cm);
						updateRoomList();
					} else if (cm.code.matches("701")) {
						exitRoom(cm);
						updateRoomList();
					}
					else if (cm.code.matches("800")){
						startGame(cm);
					}
					else if (cm.code.matches("801")){
						readyMethod(cm);
					}
					else if (cm.code.matches("802")) { // 게임 종료
						finishGame(cm);
						System.out.println(cm.data);
					} else if (cm.code.matches("900")) { // 바둑돌 입력 처리
						System.out.println("y: " + cm.y + "x: " + cm.x + "name: " + cm.roomName);
						drawStone(cm);
					} else if (cm.code.matches("901")) { // 바둑돌 undo 처리 (무르기)
						System.out.println("Stone Undo -> name: " + cm.roomName);
						undoStone(cm);
					} else { // 300, 500, ... 기타 object는 모두 방송한다.
						WriteAllObject(cm);
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
						ois.close();
						oos.close();
						client_socket.close();
						Logout(); // 에러가난 현재 객체를 벡터에서 지운다
						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝
			} // while
		} // run
	}

}
