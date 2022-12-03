//JavaObjServer.java ObjectStream ��� ä�� Server

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
import java.util.Timer;
import java.util.TimerTask;
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

	private ServerSocket socket; // ��������
	private Socket client_socket; // accept() ���� ������ client ����
	private Vector<UserService> userVec = new Vector(); // ����� ����ڸ� ������ ����
	private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����
	private TimerTask task;
	private Vector<Room> roomVec = new Vector(); // ������ ���� ������ ����

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // â�� ������ ���μ��� ����
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
		btnServerStart.addActionListener(new ActionListener() { // ��ư ������ ���� ����
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				AppendText("Chat Server Running..");
				btnServerStart.setText("Chat Server Running..");
				btnServerStart.setEnabled(false); // ������ ���̻� �����Ű�� �� �ϰ� ���´�
				txtPortNumber.setEnabled(false); // ���̻� ��Ʈ��ȣ ������ �ϰ� ���´�
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();

			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// ���ο� ������ accept() �ϰ� user thread�� ���� �����Ѵ�.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept�� �Ͼ�� �������� ���� �����
					AppendText("���ο� ������ from " + client_socket);
					// User �� �ϳ��� Thread ����
					UserService new_user = new UserService(client_socket);
					userVec.add(new_user); // ���ο� ������ �迭�� �߰�
					new_user.start(); // ���� ��ü�� ������ ����
					AppendText("���� ������ �� " + userVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		// textArea.append("����ڷκ��� ���� �޼��� : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(ChatMsg msg) {
		// textArea.append("����ڷκ��� ���� object : " + str+"\n");
		textArea.append("code = " + msg.code + "\n");
		textArea.append("id = " + msg.userName + "\n");
		textArea.append("data = " + msg.data + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User �� �����Ǵ� Thread
	// Read One ���� ��� -> Write All
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

		public UserService(Socket client_socket) {
			// TODO Auto-generated constructor stub
			// �Ű������� �Ѿ�� �ڷ� ����
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
			AppendText("���ο� ������ " + userName + " ����.");
			WriteOne("Welcome to Java chat server\n");
			WriteOne(userName + "�� ȯ���մϴ�.\n"); // ����� ����ڿ��� ���������� �˸�
			String msg = "[" + userName + "]���� ���� �Ͽ����ϴ�.\n";
			WriteOthers(msg); // ���� user_vc�� ���� ������ user�� ���Ե��� �ʾҴ�.
		}

		public void Logout() {
			String msg = "[" + userName + "]���� ���� �Ͽ����ϴ�.\n";
			userVec.removeElement(this); // Logout�� ���� ��ü�� ���Ϳ��� �����
			WriteAll(msg); // ���� ������ �ٸ� User�鿡�� ����
			AppendText("����� " + "[" + userName + "] ����. ���� ������ �� " + userVec.size());
		}

		// ��� User�鿡�� ���. ������ UserService Thread�� WriteONe() �� ȣ���Ѵ�.
		public void WriteAll(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.userStatus == "O")
					user.WriteOne(str);
			}
		}

		// ��� User�鿡�� Object�� ���. ä�� message�� image object�� ���� �� �ִ�
		public void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.userStatus == "O")
					user.WriteOneObject(ob);
			}
		}

		// ���� ������ User�鿡�� ���. ������ UserService Thread�� WriteONe() �� ȣ���Ѵ�.
		public void WriteOthers(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this && user.userStatus == "O")
					user.WriteOne(str);
			}
		}

		// UserService Thread�� ����ϴ� Client ���� 1:1 ����
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
				Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
			}
		}

		// �ӼӸ� ����
		public void WritePrivate(String msg) {
			try {
				ChatMsg obcm = new ChatMsg("�ӼӸ�", "200", msg);
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
				Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
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
			Room room = new Room(msg.roomName, this, msg.roomMax, msg.gameMode); // �� ���� (�� ��ȣ, �� �̸�, ���� �̸�, �� �ִ� �ο���)
			roomVec.add(room); // �� ���Ϳ� �� �߰�
			int vecIndex = roomVec.indexOf(room);
			room.roomNumber = vecIndex;
			this.roomNumber = vecIndex; // �� ������ �� ��ȣ ����
			msg.roomNumber = vecIndex;
			AppendText(roomNumber + "�� �� ���� �Ϸ�. ���� �� ���� " + roomVec.size());

			for (UserService user : room.playerList)
				user.updateUserList(msg);

		}

		public void exitRoom(ChatMsg msg) {
			Room room = getRoom(msg);
			room.updateReadyState(msg.userName, false); // ready �迭 ����
			System.out.println(msg.userName + "�� �濡�� �����߽��ϴ�.");

			System.out.println("��, ���� �ο��� : " + room.playerList.size());
			for (int i = 0; i < room.playerList.size(); i++) {
				OmokServer.UserService user = room.playerList.get(i);
				if (user.userName.equals(msg.userName)) {
					room.playerList.remove(i);
					break;
				}
			}
			for (int i = 0; i < room.watcherList.size(); i++) {
				if (msg.userName.equals(room.watcherList.get(i).userName)) {
					room.watcherList.remove(i);
					ChatMsg cm = new ChatMsg(userName, "201", "");
					cm.roomName = room.roomName;
					cm.roomNumber = room.roomNumber;
					cm.data = "[������][" + userName + "] ���� �����߽��ϴ�.";
					sendGameMessage(cm);
					return;
				}
			}
			System.out.println("��, ���� �ο��� : " + room.playerList.size());
			if (room.playerList.size() != 0) {
				for (UserService user : room.playerList) { // �غ���� ����
					room.updateReadyState(user.userName, false);
				}
				room.owner = room.playerList.get(0); // ���� �ٲٱ�
				room.ownerName = room.owner.userName;
				for (UserService user : room.playerList) { // �濡 �ִ� ��� �������� ���� ��� ����Ʈ�� ����
					user.updateUserList(msg);
				}
				for (UserService user : room.watcherList) { // �濡 �ִ� ��� �������� ���� ��� ����Ʈ�� ����
					user.updateUserList(msg);
				}
				ChatMsg cm = new ChatMsg(userName, "201", "");
				cm.roomName = room.roomName;
				cm.roomNumber = room.roomNumber;
				cm.data = "[" + userName + "] ���� �����߽��ϴ�.";
				sendGameMessage(cm);
				stopGame(msg);
				if (room.isStarted)
					stopGame(cm); // ���� ���� ���� ��Ż �� ������ �ߴ���.
				room.isStarted = false;
			} else {
				for (int i = 0; i < roomVec.size(); i++) {
					Room emptyroom = roomVec.get(i);
					if (emptyroom.roomName.equals(msg.roomName)) {
						roomVec.remove(i);
						break;
					}
				}
				System.out.println(msg.roomName + "���� ������ϴ�.");
			}
		}

		public void insertRoom(ChatMsg msg) {
			Room room = roomVec.get(msg.roomNumber); // �� ��ȣ�� ���� (�� ��� ����Ʈ�� ������ �� �迭�� �ε����� ������)
			room.updateReadyState(msg.userName, false); // ready �迭 ����
			if (room.isStarted) {
				WriteOne("������ �������� ���Դϴ�!");
				AppendText("������" + userName + " " + room.roomNumber + "�� �� ����.");
				msg = new ChatMsg(userName, "704", "");
				msg.roomMax = room.roomMax;
				msg.roomName = room.roomName;
				msg.gameMode = room.gameMode;
				msg.roomNumber = room.roomNumber;
				room.addWatcher(this); // // �� ���Ϳ� ������ �߰�
				WriteAllObject(msg);
//				for (UserService user : room.playerList) { // �濡 �ִ� ��� �������� ���� ��� ����Ʈ�� ����
//					user.updateUserList(msg);
//				}
				for (UserService user : room.watcherList) { // �濡 �ִ� ��� �������� ���� ��� ����Ʈ�� ����
					user.updateUserList(msg);
				}
				ChatMsg cm = new ChatMsg(userName, "201", "");
				cm.roomName = room.roomName;
				cm.roomNumber = room.roomNumber;
				cm.data = "[" + userName + "] ���� �������� �����߽��ϴ�.";
				sendGameMessage(cm);

				for (int i = 0; i < room.stoneList.size(); i++) {
					ChatMsg stone = new ChatMsg(msg.userName, "900", "���� ���");
					stone.roomName = msg.roomName;
					stone.x = room.stoneList.get(i).x;
					stone.y = room.stoneList.get(i).y;
					stone.stone = (i % room.roomMax) + 1;
					if (stone.x == 0 && stone.y == 0) {
						stone.stone = 0;
					}
					WriteOneObject(stone);
				}
				return;
			}
			if (room.isFull()) {
				WriteOne("���� �� á���ϴ�! �������� �����ϰڽ��ϴ�.");
				AppendText("������" + userName + " " + room.roomNumber + "�� �� ����.");
				msg = new ChatMsg(userName, "704", "");
				msg.roomMax = room.roomMax;
				msg.roomName = room.roomName;
				msg.gameMode = room.gameMode;
				msg.roomNumber = room.roomNumber;
				room.addWatcher(this); // // �� ���Ϳ� ������ �߰�
				WriteAllObject(msg);
				for (UserService user : room.playerList) { // �濡 �ִ� ��� �������� ���� ��� ����Ʈ�� ����
					user.updateUserList(msg);
				}
				for (UserService user : room.watcherList) { // �濡 �ִ� ��� �������� ���� ��� ����Ʈ�� ����
					user.updateUserList(msg);
				}
				ChatMsg cm = new ChatMsg(userName, "201", "");
				cm.roomName = room.roomName;
				cm.roomNumber = room.roomNumber;
				cm.data = "[" + userName + "] ���� �������� �����߽��ϴ�.";
				sendGameMessage(cm);
				return;
			} else if (room.hasName(msg.userName)) { // �濡 ������ �̸��� �̹� �ִ� ���
				WriteOne("�̹� �� �ִ� ���Դϴ�!");
				return;
			} else { // �������� ���� ������ �ְ� �����Ŵ
				AppendText("�÷��̾� " + userName + " " + room.roomNumber + "�� �� ����.");
				msg = new ChatMsg(userName, "700", "");
				msg.roomMax = room.roomMax;
				msg.roomName = room.roomName;
				msg.gameMode = room.gameMode;
				msg.roomNumber = room.roomNumber;
				room.addUser(this); // // �� ���Ϳ� ������ �߰�
				WriteAllObject(msg);
				for (UserService user : room.playerList) { // �濡 �ִ� ��� �������� ���� ��� ����Ʈ�� ����
					user.updateUserList(msg);
				}
				for (UserService user : room.watcherList) { // �濡 �ִ� ��� �������� ���� ��� ����Ʈ�� ����
					user.updateUserList(msg);
				}
				ChatMsg cm = new ChatMsg(userName, "201", "");
				cm.roomName = room.roomName;
				cm.roomNumber = room.roomNumber;
				cm.data = "[" + userName + "] ���� �����߽��ϴ�.";
				sendGameMessage(cm);
			}
		}

		public void updateRoomList() { // �� ����� �����ϴ� String�� ��ο��� �������� ����
			ChatMsg msg = new ChatMsg(userName, "702", "");
			StringBuffer data = new StringBuffer();
			for (Room room : roomVec) {
				data.append(String.format("[No.%d] [%s] [mode:%s] [%d/%d] [����:%s]\n", room.roomNumber, room.roomName,
						room.gameMode, room.getPlayerCount(), room.roomMax, room.ownerName));
			}
			msg.data = data.toString();
			WriteAllObject(msg);
		}

		public void updateUserList(ChatMsg msg) { // �� �Լ��� ȣ���� UserService���Ը� ���� ����� �����ϴ� String�� ����
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
						stoneName = "�浹";
						break;
					case 1:
						stoneName = "�鵹";
						break;
					case 2:
						stoneName = "�浹";
						break;
					case 3:
						stoneName = "�鵹";
						break;
					}
				} else {
					switch (i) {
					case 0:
						stoneName = "�浹";
						break;
					case 1:
						stoneName = "�鵹";
						break;
					case 2:
						stoneName = "����";
						break;
					// case 3: stoneName = "����"; break;
					}
				}
				data.append(String.format("[Player%d] [�̸�:%s] [��:%s]\n", i + 1, user.userName, stoneName));
				i++;
			}
			cm.data = data.toString();
			System.out.println("11111111111--  " + msg.code);
			WriteOneObject(cm);
		}

		public void drawStone(ChatMsg msg) { // ���Ϳ���
			Room room = getRoom(msg);
			System.out.println(msg.roomNumber + "room DrawStone");
			if (!room.isStarted) {
				ChatMsg cm = new ChatMsg(msg.userName, "201", "");
				cm.roomName = msg.roomName;
				cm.data = "���� ���� ���Դϴ�.";
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

			for (int i = 0; i < room.watcherList.size(); i++) {
				if (msg.userName.equals(room.watcherList.get(i).userName)) {
					usernum = 999;
					break;
				}
			}

			if (stone.y == 0 && stone.x == 0) { // ���� �ð� ����� �Ѿ�� �ٵϾ�ó��
				msg.stone = 0;
				if (room.roomMax == room.playerList.size()) {
					room.addStone(stone);
					System.out.println("2stone Size : " + room.stoneList.size());
				}
			} else {
				if (usernum == 999) {
					msg.stone = 999;
					System.out.println("�Ƽ��� �׽��ϴ�.");
					for (UserService user : room.playerList) // �濡 �ִ� ��� �������� �ٵϵ� ����
						user.WriteOneObject(msg);
					for (UserService user : room.watcherList) // �濡 �ִ� ��� �������� �ٵϵ� ����
						user.WriteOneObject(msg);
					ChatMsg cm = new ChatMsg(msg.userName, "201", "");
					cm.roomName = msg.roomName;
					cm.data = "[" + msg.userName + "]���� ������ ���ݴϴ�.";
					sendGameMessage(cm);
				} else {
					msg.stone = color;
					if (room.roomMax == room.playerList.size()) {
						if ((room.stoneList.size() % room.roomMax) == usernum) {
							room.addStone(stone);
							System.out.println("3stone Size : " + room.stoneList.size());
							for (UserService user : room.playerList) // �濡 �ִ� ��� �������� �ٵϵ� ����
								user.WriteOneObject(msg);
							for (UserService user : room.watcherList) {// �濡 �ִ� ��� �������� �ٵϵ� ����
								for (int i = 0; i < room.stoneList.size(); i++) {
									ChatMsg stonemsg = new ChatMsg(user.userName, "900", "���� ���");
									stonemsg.roomName = msg.roomName;
									stonemsg.x = room.stoneList.get(i).x;
									stonemsg.y = room.stoneList.get(i).y;
									stonemsg.stone = (i % room.roomMax) + 1;
									if (stonemsg.x == 0 && stonemsg.y == 0) {
										stonemsg.stone = 0;
									}
									user.WriteOneObject(stonemsg);
								}
								msg.stoneNum = 999;
								user.WriteOneObject(msg);
							}
								
							ChatMsg cm = new ChatMsg(msg.userName, "201", "");
							cm.roomName = msg.roomName;
							cm.data = "[" + msg.userName + "]���� ���� ���ҽ��ϴ�.";
							sendGameMessage(cm);
						} else {
							System.out.println("���ʰ� �ƴ�");
						}
					}
				}
			}
		}

		public void undoStone(ChatMsg msg) {
			Room room = getRoom(msg);
			room.undoCount++;
			if (room.undoCount == room.roomMax) {
				room.undoStone();
				System.out.println(msg.roomNumber + " room undoStone");
				room.undoCount = 0;
			}
			System.out.println("stone Size : " + room.stoneList.size());
			for (UserService user : room.playerList) // �濡 �ִ� ��� �������� undo ����
				user.WriteOneObject(msg);
			for (UserService user : room.watcherList) // �濡 �ִ� ��� �������� undo ����
				user.WriteOneObject(msg);
		}

		public void sendGameMessage(ChatMsg msg) {
			System.out.println("sendGameMessage Called : " + msg.code + " " + msg.roomName + " " + msg.userName);

			Room room = getRoom(msg);

			for (UserService user : room.playerList) { // �濡 �ִ� ��� �������� message ����
				user.WriteOneObject(msg);
				System.out.println(
						"sendGameMessage WriteOneObject : " + msg.code + " " + msg.roomName + " " + msg.userName);
			}
			for (UserService user : room.watcherList) { // �濡 �ִ� ��� �������� message ����
				user.WriteOneObject(msg);
				System.out.println(
						"sendGameMessage WriteOneObject : " + msg.code + " " + msg.roomName + " " + msg.userName);
			}
		}

		public void startGame(ChatMsg msg) { // ���� ���� �޼ҵ�
			Room room = getRoom(msg);

			if (!room.isFull()) {
				ChatMsg cm = new ChatMsg(userName, "201", "���� ���� �ο��� �����մϴ�.");
				cm.roomName = room.roomName;
				sendGameMessage(cm);
				return;
			}

			if (!room.isReady()) {
				ChatMsg cm = new ChatMsg(userName, "201", "��� �ο��� �غ��ؾ� ���� �� �� �ֽ��ϴ�.");
				cm.roomName = room.roomName;
				sendGameMessage(cm);
				return;
			}

			room.isStarted = true;
			ChatMsg startCm = new ChatMsg(userName, "201", "������ �����մϴ�.");
			startCm.roomName = room.roomName;
			sendGameMessage(startCm);

			ChatMsg cm = new ChatMsg(userName, "800", "");
			cm.roomName = room.roomName;
			for (UserService user : room.playerList) { // �濡 �ִ� ��� �������� ����
				user.WriteOneObject(cm);
			}
			for (UserService user : room.watcherList) { // �濡 �ִ� ��� �������� ����
				user.WriteOneObject(cm);
			}
		}

		public void readyMethod(ChatMsg cm) {
			// �غ� ���� �ٲٰ� msg ����
			Room room = getRoom(cm);
			String msg = room.getReadyState(userName) ? "[" + userName + "]�� �غ� ���" : "[" + userName + "]�� �غ� �Ϸ�";

			room.toggleReadyState(userName);

			ChatMsg readyCm = new ChatMsg(userName, "201", msg); // �غ� ���� ���� �� ä������ ����
			readyCm.roomName = room.roomName;

			String readyState = room.getReadyState(userName) ? "true" : "false"; // �غ� ��ư ��۽�Ű�� ���� ���ڿ�

			ChatMsg readyToggleCm = new ChatMsg(userName, "801", readyState); // �غ� ��ư ��۽�Ű��
			readyToggleCm.roomName = room.roomName;

			for (UserService user : room.playerList) { // �濡 �ִ� ��� �������� ����
				user.WriteOneObject(readyCm);
			}
			WriteOneObject(readyToggleCm); // ����� ��ư�� ����

		}

		public void finishGame(ChatMsg msg) { // ���� ���Ḧ �˸�
			Room room = getRoom(msg);
			room.stoneList.clear();
			room.isStarted = false;

			for (UserService user : room.playerList) { // �濡 �ִ� ��� �������� ����
				room.updateReadyState(user.userName, false);
				// ChatMsg readyToggleCm = new ChatMsg(userName, "801", "false"); // �غ� ��ư ��۽�Ű��
				// readyToggleCm.roomName = room.roomName;
				// user.WriteOneObject(readyToggleCm); // ���� �غ� ��ư�� ��� ��ҽ�Ŵ

				ChatMsg finishCm = new ChatMsg(userName, "201", "[" + userName + "] �¸�, ���� ����");
				finishCm.roomName = room.roomName;
				user.WriteOneObject(finishCm);
			}
			for (UserService user : room.watcherList) { // �濡 �ִ� ��� �������� ����
				ChatMsg finishCm = new ChatMsg(userName, "201", "[" + userName + "] �¸�, ���� ����");
				finishCm.roomName = room.roomName;
				user.WriteOneObject(finishCm);
			}
		}

		public void stopGame(ChatMsg msg) { // ������ ���߿� ������ ������ �ߴܽ�Ű�� �޼ҵ�
			Room room = getRoom(msg);
			room.stoneList.clear();
			room.isStarted = false;
			System.out.println("�ߵ� ���� --->   " + room.stoneList.size());
			for (UserService user : room.playerList) { // �濡 �ִ� ��� �������� ����
				room.updateReadyState(user.userName, false);
				ChatMsg readyToggleCm = new ChatMsg(userName, "801", "false"); // �غ� ��ư ��۽�Ű��
				readyToggleCm.roomName = room.roomName;
				user.WriteOneObject(readyToggleCm); // ���� �غ� ��� ��ҽ�Ŵ

				ChatMsg stopCm = new ChatMsg(userName, "803", ""); // ���� �ߴ�
				stopCm.roomName = room.roomName;
				user.WriteOneObject(stopCm);
			}
			for (UserService user : room.watcherList) { // �濡 �ִ� ��� �������� ����
				ChatMsg stopCm = new ChatMsg(userName, "803", ""); // ���� �ߴ�
				stopCm.roomName = room.roomName;
				user.WriteOneObject(stopCm);
			}
		}

		public void run() {
			if (task == null) {
				task = new TimerTask() {
					@Override
					public void run() {
						ChatMsg time = new ChatMsg(userName, "000", "1��");
						System.out.print("1��/");
						WriteAllObject(time);
					}
				};
				new Timer().scheduleAtFixedRate(task, 0l, 1000);
			}
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
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
						userStatus = "O"; // Online ����
						Login();
						updateRoomList();
					} else if (cm.code.matches("200")) {
						msg = String.format("[%s] %s", cm.userName, cm.data);
						AppendText(msg); // server ȭ�鿡 ���
						String[] args = msg.split(" "); // �ܾ���� �и��Ѵ�.
						if (args.length == 1) { // Enter key �� ���� ��� Wakeup ó���� �Ѵ�.
							userStatus = "O";
						} else if (args[1].matches("/exit")) {
//							task.cancel();
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
						} else if (args[1].matches("/to")) { // �ӼӸ�
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								if (user.userName.matches(args[2]) && user.userStatus.matches("O")) {
									String msg2 = "";
									for (int j = 3; j < args.length; j++) {// ���� message �κ�
										msg2 += args[j];
										if (j < args.length - 1)
											msg2 += " ";
									}
									// /to ����.. [�ӼӸ�] [user1] Hello user2..
									user.WritePrivate(args[0] + " " + msg2 + "\n");
									// user.WriteOne("[�ӼӸ�] " + args[0] + " " + msg2 + "\n");
									break;
								}
							}
						} else { // �Ϲ� ä�� �޽���
							userStatus = "O";
							// WriteAll(msg + "\n"); // Write All
							WriteAllObject(cm);
						}
					} else if (cm.code.matches("201")) { // ���ӹ� ä��
						sendGameMessage(cm);
					} else if (cm.code.matches("400")) { // logout message ó��
						Logout();
						break;
					} else if (cm.code.matches("600")) { // �� ���� ó��
						createRoom(cm);
						WriteAllObject(cm);
						updateRoomList();
					}

					else if (cm.code.matches("700")) { // �� ���� ó��
						insertRoom(cm);
						updateRoomList();
					} else if (cm.code.matches("701")) { // �� ���� ó��
						exitRoom(cm);
						updateRoomList();
					} else if (cm.code.matches("800")) { // ���� ����
						startGame(cm);
					} else if (cm.code.matches("801")) { // ���� �غ�
						readyMethod(cm);
					} else if (cm.code.matches("802")) { // ���� ����
						finishGame(cm);
						System.out.println(cm.data);
					} else if (cm.code.matches("900")) { // �ٵϵ� �Է� ó��
						System.out.println("y: " + cm.y + " x: " + cm.x + " name: " + cm.roomName);
						if (cm.y == 0 && cm.x == 0) {
							for (int i = 0; i < roomVec.size(); i++) {
								if (cm.roomName.equals(roomVec.get(i).roomName)) {
									if (cm.userName.equals(roomVec.get(i).ownerName)) {
										drawStone(cm);
									}
								}
							}
						} else
							drawStone(cm);
					} else if (cm.code.matches("901")) { // �ٵϵ� undo ó�� (������)
						System.out.println("���� ��ư ����");
						System.out.println("���� �ٵϾ�");
						Room room = null;
						if (cm.stoneNum == 999) {
							for (int i = 0; i < roomVec.size(); i++) {
								if (cm.roomName.equals(roomVec.get(i).roomName)) {
									room = roomVec.get(i);
									break;
								}
							}
							cm.stoneNum = room.stoneList.size() - 1;
							cm.x = room.stoneList.get(cm.stoneNum).x;
							cm.y = room.stoneList.get(cm.stoneNum).y;
						} else {
							for (int i = 0; i < roomVec.size(); i++) {
								if (cm.roomName.equals(roomVec.get(i).roomName)) {
									room = roomVec.get(i);
									break;
								}
							}
							cm.stoneNum -= 1;
							if (cm.stoneNum == -1)
								cm.stoneNum = 0;

							cm.x = room.stoneList.get(cm.stoneNum).x;
							cm.y = room.stoneList.get(cm.stoneNum).y;

						}
						WriteOneObject(cm);
					} else if (cm.code.matches("902")) {
						System.out.println("���� ��ư ����");
						System.out.println("���� �ٵϾ� ����");
						Room room = null;
						if (cm.stoneNum == 999) {
							for (int i = 0; i < roomVec.size(); i++) {
								if (cm.roomName.equals(roomVec.get(i).roomName)) {
									room = roomVec.get(i);
									break;
								}
							}
							cm.stoneNum = room.stoneList.size() - 1;
							cm.x = room.stoneList.get(cm.stoneNum).x;
							cm.y = room.stoneList.get(cm.stoneNum).y;
						} else {
							for (int i = 0; i < roomVec.size(); i++) {
								if (cm.roomName.equals(roomVec.get(i).roomName)) {
									room = roomVec.get(i);
									break;
								}
							}
							
							cm.x = room.stoneList.get(cm.stoneNum).x;
							cm.y = room.stoneList.get(cm.stoneNum).y;
							cm.stone = (cm.stoneNum % room.roomMax) + 1;
							
							cm.stoneNum += 1;
							if(cm.stoneNum == room.stoneList.size())
								cm.stoneNum -= 1;
						}
						WriteOneObject(cm);
					} else { // 300, 500, ... ��Ÿ object�� ��� ����Ѵ�.
						WriteAllObject(cm);
					}

				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
						ois.close();
						oos.close();
						client_socket.close();
//						task.cancel();
						Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
						break;
					} catch (Exception ee) {
						break;
					} // catch�� ��
				} // �ٱ� catch����
			} // while
		} // run
	}

}
