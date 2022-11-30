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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //â�� ������ ���μ��� ����
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
		btnServerStart.addActionListener(new ActionListener() { //��ư ������ ���� ����
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
		textArea.append("id = " + msg.UserName + "\n");
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
		public boolean ready;
		//public boolean userTurn;

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
			} 
			catch (IOException e) {
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
		
		public void createRoom(ChatMsg msg) {
			Room room = new Room(msg.roomName, this, msg.roomMax, msg.gameMode); // �� ���� (�� ��ȣ, �� �̸�, ���� �̸�, �� �ִ� �ο���)
			roomVec.add(room); // �� ���Ϳ� �� �߰�
			int vecIndex = roomVec.indexOf(room);
			room.roomNumber = vecIndex;
			this.roomNumber = vecIndex; // �� ������ �� ��ȣ ����
			msg.roomNumber = vecIndex;
			AppendText(roomNumber+"�� �� ���� �Ϸ�. ���� �� ���� "+roomVec.size());
			
			for(UserService user: room.playerList) 
				user.updateUserList();
			
		}
		
		public void insertRoom(ChatMsg msg) { 
			Room room = roomVec.get(msg.roomNumber); // �� ��ȣ�� ���� (�� ��� ����Ʈ�� ������ �� �迭�� �ε����� ������)
			if(room.isFull()) { 
				WriteOne("���� �� á���ϴ�!");
				return;
			}
			else if (room.hasName(msg.UserName)) { // �濡 ������ �̸��� �̹� �ִ� ���
				WriteOne("�̹� �� �ִ� ���Դϴ�!");
				return;
			}
			else { // �������� ���� ������ �ְ� �����Ŵ
				AppendText("�÷��̾� "+userName+" "+room.roomNumber+"�� �� ����.");
				msg = new ChatMsg(userName, "700", "");
				msg.roomMax = room.roomMax;
				msg.roomName = room.roomName;
				msg.gameMode = room.gameMode;
				msg.roomNumber = room.roomNumber;
				room.addUser(this); // // �� ���Ϳ� ������ �߰�
				WriteAllObject(msg);
				for(UserService user: room.playerList) // �濡 �ִ� ��� �������� ���� ��� ����Ʈ�� ���� 
					user.updateUserList();
			}
		}
		
		public void updateRoomList() { // �� ����� �����ϴ� String�� ��ο��� �������� ����
			ChatMsg msg = new ChatMsg(userName, "702", "");
			StringBuffer data = new StringBuffer();
			for(Room room : roomVec) {
				data.append(String.format("[No.%d] [%s] [mode:%s] [%d/%d] [����:%s]\n",room.roomNumber, room.roomName, room.gameMode, room.getPlayerCount(), room.roomMax, room.ownerName));
			}
			msg.data = data.toString();
			WriteAllObject(msg);
		}
		
		public void updateUserList() { // �� �Լ��� ȣ���� UserService���Ը� ���� ����� �����ϴ� String�� ����
			ChatMsg msg = new ChatMsg(userName, "703", "");
			Room room = roomVec.get(roomNumber);
			StringBuffer data = new StringBuffer();
			int i = 0;
			String stoneName = "";
			for(UserService user : room.playerList) {
				switch(i) {
				case 0: stoneName = "�浹"; break;
				case 1: stoneName = "�鵹"; break;
				case 2: stoneName = "����"; break;
				//case 3: stoneName = "����"; break;
				}
				data.append(String.format("[Player%d] [�̸�:%s] [��:%s]\n",i+1, user.userName, stoneName));
				i++;
			}
			msg.data = data.toString();
			WriteOneObject(msg);
		}
		
		public void drawStone(ChatMsg msg) { // ���Ϳ��� 
			int roomnum = -1;
			for(int i= 0; i < roomVec.size(); i++) {
				if(msg.roomName.equals(roomVec.get(i).roomName)) {
					roomnum = i;
					break;
				}
			}
			System.out.println(roomnum + "room DrawStone");
			msg.roomNumber = roomnum;
			Room room = roomVec.get(roomnum);
			Stone stone = new Stone();
			stone.y = msg.y;
			stone.x = msg.x;
//			room.addStone(stone);
			int color;
			int usernum = -1;
			for(color = 0; color < room.playerList.size(); color++) {
				if(msg.UserName.equals(room.playerList.get(color).userName)) {
					usernum = color;
					color = color + 1;
					if(room.roomMax == 4)
						color = (color % 2) + 1;
					break;
				}
			}
			if(room.roomMax == 4) {
				color = color % 2;
			}
			msg.stone = color;
//			WriteAllObject(msg);
			
			if((room.stoneList.size() % room.roomMax) == usernum) {
				room.addStone(stone);
				System.out.println("stone Size : " + room.stoneList.size());
				for(UserService user: room.playerList) // �濡 �ִ� ��� �������� �ٵϵ� ���� 
					user.WriteOneObject(msg);
			}else {
				System.out.println("���ʰ� �ƴ�");
			}
		}
		
		
		public void undoStone(ChatMsg msg) {
			int roomnum = -1;
			for(int i= 0; i < roomVec.size(); i++) {
				if(msg.roomName.equals(roomVec.get(i).roomName)) {
					roomnum = i;
					break;
				}
			}
			
			msg.roomNumber = roomnum;
			Room room = roomVec.get(roomnum);
			room.undoCount++;
			if(room.undoCount == room.roomMax) {
				room.undoStone();
				System.out.println(roomnum+" room undoStone");
				room.undoCount = 0;
			}
			System.out.println("stone Size : " + room.stoneList.size());
			for(UserService user: room.playerList) // �濡 �ִ� ��� �������� undo ���� 
				user.WriteOneObject(msg);
		}
		
		public void run() {
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
						userName = cm.UserName;
						userStatus = "O"; // Online ����
						Login();
						updateRoomList();
					} else if (cm.code.matches("200")) {
						msg = String.format("[%s] %s", cm.UserName, cm.data);
						AppendText(msg); // server ȭ�鿡 ���
						String[] args = msg.split(" "); // �ܾ���� �и��Ѵ�.
						if (args.length == 1) { // Enter key �� ���� ��� Wakeup ó���� �Ѵ�.
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
									//user.WriteOne("[�ӼӸ�] " + args[0] + " " + msg2 + "\n");
									break;
								}
							}
						} else { // �Ϲ� ä�� �޽���
							userStatus = "O";
							//WriteAll(msg + "\n"); // Write All
							WriteAllObject(cm);
						}
					} else if (cm.code.matches("400")) { // logout message ó��
						Logout();
						break;
					} else if(cm.code.matches("600")) { // �� ���� ó��
						createRoom(cm);
						WriteAllObject(cm);
						updateRoomList();
					}
					
					else if(cm.code.matches("700")) { // �� ���� ó��
						insertRoom(cm);
						updateRoomList();
					}
					else if(cm.code.matches("900")) { // �ٵϵ� �Է� ó��
						System.out.println("y: " + cm.y + "x: " + cm.x + "name: " + cm.roomName);
						drawStone(cm);
					}
					else if(cm.code.matches("901")) { // �ٵϵ� undo ó�� (������)
						System.out.println("Stone Undo -> name: " + cm.roomName);
						undoStone(cm);
					}
					else { // 300, 500, ... ��Ÿ object�� ��� ����Ѵ�.
						WriteAllObject(cm);
					} 
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
						ois.close();
						oos.close();
						client_socket.close();
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
