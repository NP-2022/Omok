
// JavaObjClientView.java ObjecStram 기반 Client
//실질적인 채팅 창
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.ImageObserver;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Component;

import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import java.awt.Canvas;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

//import JavaGameServer.UserService;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.ListModel;

public class OmokClientMainView extends JFrame {
	/**
	 * 
	 */

	private String Ip_addr;
	private String Port_no;
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField chatTextInput;
	private String UserName;
	private JButton chatSendButton;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private JLabel userNameLabel;
	// private JTextArea textArea;
	private JTextPane textArea;

	private Frame frame;
	private FileDialog fd;
	private JButton imageChangeButton;
	private Graphics gc;
	private int pen_size = 2; // minimum 2
	// 그려진 Image를 보관하는 용도, paint() 함수에서 이용한다.

	public OmokClientMainView mainView = null; // 현재 화면에 대한 레퍼런스
//	public JavaGameClientView2 gameClientView = null; // 게임 화면 레퍼런스
	public Vector<OmokClientGameView> gameView = new Vector();

	private JScrollPane roomListScrollPane;
	private JList roomList;
	private DefaultListModel roomListModel; // 방 목록
	private JButton roomInsertButton;
	
	private JLabel serverUserListTitleLabel;
	private JList serverUserList;
	private DefaultListModel serverUserListModel;

	public HashMap<String, String> profileImageSrcHashMap = new HashMap<>();
	
	/**
	 * Create the frame.
	 * 
	 * @throws BadLocationException
	 */
	public OmokClientMainView(String username, String ip_addr, String port_no) {

		mainView = this;

		UserName = username;
		Ip_addr = ip_addr;
		Port_no = port_no;

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 닫히면 프로세스 종료
		setBounds(100, 100, 800, 634);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane chatScrollPane = new JScrollPane();
		chatScrollPane.setBounds(12, 61, 352, 471);
		contentPane.add(chatScrollPane);

		textArea = new JTextPane();
		textArea.setEditable(true);
		textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
		chatScrollPane.setViewportView(textArea);

		chatTextInput = new JTextField();
		chatTextInput.setBounds(12, 540, 276, 40);
		contentPane.add(chatTextInput);
		chatTextInput.setColumns(10);

		chatSendButton = new JButton("전송");
		chatSendButton.setFont(new Font("굴림", Font.PLAIN, 14));
		chatSendButton.setBounds(295, 539, 69, 40);
		contentPane.add(chatSendButton);

		userNameLabel = new JLabel("User");
		userNameLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		userNameLabel.setBackground(Color.WHITE);
		userNameLabel.setFont(new Font("굴림", Font.BOLD, 14));
		userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userNameLabel.setBounds(645, 469, 127, 30);
		contentPane.add(userNameLabel);
		setVisible(true);

		AppendText("User " + username + " connecting " + ip_addr + " " + port_no);
		userNameLabel.setText(UserName);

		imageChangeButton = new JButton("프로필 수정");
		imageChangeButton.setFont(new Font("굴림", Font.PLAIN, 16));
		imageChangeButton.setBounds(645, 509, 127, 23);
		contentPane.add(imageChangeButton);

		JButton exitButton = new JButton("종 료");
		exitButton.setFont(new Font("굴림", Font.PLAIN, 14));

		exitButton.setBounds(645, 539, 127, 40);
		contentPane.add(exitButton);
 
		JLabel titleLabel = new JLabel("<Omok Game>");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("굴림", Font.BOLD, 14));
		titleLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		titleLabel.setBackground(Color.WHITE);
		titleLabel.setBounds(12, 11, 760, 40);
		contentPane.add(titleLabel);

		roomListScrollPane = new JScrollPane();
		roomListScrollPane.setBounds(376, 61, 396, 212);
		contentPane.add(roomListScrollPane);

		JLabel roomListTitleLabel = new JLabel("방 리스트");
		roomListTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		roomListTitleLabel.setFont(new Font("굴림", Font.BOLD, 14));
		roomListTitleLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		roomListTitleLabel.setBackground(Color.WHITE);
		roomListScrollPane.setColumnHeaderView(roomListTitleLabel);

		roomListModel = new DefaultListModel();
		roomList = new JList(roomListModel);
		roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		roomInsertButton = new JButton("방 입장");
		roomInsertButton.setBounds(566, 283, 97, 23);

		contentPane.add(roomInsertButton);

		JButton roomCreateButton = new JButton("방 만들기");
		roomListScrollPane.setViewportView(roomList);
		roomCreateButton.setBounds(675, 283, 97, 23);
		contentPane.add(roomCreateButton);
		
		profileImageLabel = new JLabel("");
		profileImageLabel.setBounds(645, 339, 127, 120);
		contentPane.add(profileImageLabel);
		
		
		ImageIcon defaultImg = new ImageIcon(OmokClientMainView.class.getResource("profileImg/default.png"));
		setProfileImage(defaultImg);
		
		
		JScrollPane serverUserListScrollPane = new JScrollPane();
		serverUserListScrollPane.setBounds(376, 335, 263, 245);
		contentPane.add(serverUserListScrollPane);
		
		serverUserListTitleLabel = new JLabel("유저 리스트");
		serverUserListTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		serverUserListTitleLabel.setFont(new Font("굴림", Font.BOLD, 14));
		serverUserListTitleLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		serverUserListTitleLabel.setBackground(Color.WHITE);
		serverUserListScrollPane.setColumnHeaderView(serverUserListTitleLabel);
		
		serverUserListModel = new DefaultListModel();
		serverUserList = new JList(serverUserListModel);
		serverUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		serverUserListScrollPane.setViewportView(serverUserList);
		serverUserList.setCellRenderer(new myListRenderer());
		
		
	
		

		// 게임 종료 set
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
				SendObject(msg);
				System.exit(0);
			}
		});

		// 방 생성 set
		roomCreateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OmokClientRoomCreateView roomcreate = new OmokClientRoomCreateView(mainView, username, ip_addr,
						port_no);
				setVisible(true);
			}
		});

		// 방 입장 set
		roomInsertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = roomList.getSelectedIndex();
				if (index == -1)
					return;
				requestInsertRoom(index);
				//System.out.println("go to " + index + " requested.");
			}

		});

		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			// SendMessage("/login " + UserName);
			ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");
			SendObject(obcm);

			ListenNetwork net = new ListenNetwork();
			net.start();
			TextSendAction action = new TextSendAction();
			chatSendButton.addActionListener(action);
			chatTextInput.addActionListener(action);
			chatTextInput.requestFocus();
			ChangeImageAction changeImageAction = new ChangeImageAction();
			imageChangeButton.addActionListener(changeImageAction);

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
		}

	}
	
	class myListRenderer extends DefaultListCellRenderer { // 리스트를 이미지를 포함하여 커스텀 렌더링
		
		@Override
		public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            
            String name = value.toString().split("이름:")[1].split("]")[0];
            
            String path;
            try {
            	path = profileImageSrcHashMap.get(name);
            	System.out.println(path);
            } catch(IndexOutOfBoundsException e) { path = "profileImg/default.png"; }
            ImageIcon newIcon;
            try {
            newIcon = new ImageIcon(OmokClientMainView.class.getResource(path));
           } catch(NullPointerException e) {
            	newIcon = new ImageIcon(OmokClientMainView.class.getResource("profileImg/default.png"));
           }
            Image changeImg = newIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    		ImageIcon changeIcon = new ImageIcon(changeImg);
            label.setIcon(changeIcon);
            return label;
        }
	}
	
	private void setProfileImage(ImageIcon profileImageIcon) {
		Image changeImg = profileImageIcon.getImage().getScaledInstance(profileImageLabel.getWidth(), profileImageLabel.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImg);
		profileImageLabel.setIcon(changeIcon);
	}

//	public void paint(Graphics g) {
//		super.paint(g);
//	}

	// Server Message를 수신해서 화면에 표시
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						msg = String.format("[%s]\n%s", cm.userName, cm.data);
						//System.out.println(String.format("received ChatMsg : name:[%s] code:[%s]", cm.userName, cm.code));
					} else
						continue;
					switch (cm.code) {
					case "000":
						for (int i = 0; i < gameView.size(); i++) {
							OmokClientGameView gv = gameView.get(i);
							if (gv.time == true) {
								gv.nowtime -= 1;
								gv.timeProcess();
							}
						}
						break;
					case "200": // chat message
						if (cm.userName.equals(UserName))
							AppendTextR(msg); // 내 메세지는 우측에
						else
							AppendText(msg);
						break;
					case "201": // 게임 내부 채팅 메세지
						for (int i = 0; i < gameView.size(); i++) {
							if (cm.roomName.equals(gameView.get(i).roomName)) {
								gameView.get(i).receiveGameMessage(cm);
							}
						}
						break;
					case "101": // 대기방 유저목록 갱신
						serverUserListUpdate(cm);
						break;
					case "300": // profileImageSrc 배열 관리
						updateProfileImageSrcVector(cm);
						break;
					case "500": // Mouse Event 수신
						//DoMouseEvent(cm);
						break;
					case "600": // 새로운 방 생성 됨
						break;
					case "700": // 누군가가 방에 입장
						if (cm.userName.equals(UserName)) // 방에 입장한 클라이언트 본인일 경우
							insertRoom(cm);
						for (int i = 0; i < gameView.size(); i++) {
							if (gameView.get(i).roomName.equals(cm.roomName)) {
								gameView.get(i).gamePanel.init();
								gameView.get(i).gamePanel.repaint();
								gameView.get(i).nowtime = 31;
							}
						}
						break;
					case "702": // 방 리스트 갱신
						roomListUpdate(cm);
						break; 
					case "703": // 유저 리스트 갱신
						for (int i = 0; i < gameView.size(); i++) {
							if (cm.roomName.equals(gameView.get(i).roomName)) {
								gameView.get(i).userListUpdate(cm);
								gameView.get(i).nowtime = 31;
								gameView.get(i).time = false;
							}
						}
						break;
					case "704":
						if (cm.userName.equals(UserName)) // 방에 입장한 클라이언트 본인일 경우
							insertRoomWatcher(cm);
						break;
					case "705":
						if (cm.data.equals(UserName)) // kick 당한 유저 본인일 경우
							for (int i = 0; i < gameView.size(); i++) {
								if (cm.roomName.equals(gameView.get(i).roomName)) {
									gameView.get(i).kicked();
								}
							}
						break;
					case "800": // 게임 시작
						for (int i = 0; i < gameView.size(); i++) {
							if (cm.roomName.equals(gameView.get(i).roomName)) {
								gameView.get(i).gameStart(cm);
								OmokClientGameView gv = gameView.get(i);
							}
						}
						break;
					case "801":
						for (int i = 0; i < gameView.size(); i++) {
							if (cm.roomName.equals(gameView.get(i).roomName)) {
								gameView.get(i).gameReady(cm);
							}
						}
						break;
					case "803": // 유저 이탈로 게임 중단
						for (int i = 0; i < gameView.size(); i++) {
							if (cm.roomName.equals(gameView.get(i).roomName)) {
								gameView.get(i).gameStop(cm);
								gameView.get(i).time = false;
								gameView.get(i).nowtime = 31;
							}
						}
						break;
					case "900": // 바둑돌 입력 수신
						for (int i = 0; i < gameView.size(); i++) {
							if (cm.roomName.equals(gameView.get(i).roomName)) {
								gameView.get(i).drawStone(cm);
								if(cm.stone != 999) {
									gameView.get(i).nowtime = 31;
								}
								if(cm.stoneNum == 999) {
									gameView.get(i).stonenum = 999;
								}
							}
						}
						break;
					case "901": // 복기
						for (int i = 0; i < gameView.size(); i++) {
							if (cm.roomName.equals(gameView.get(i).roomName)) {
								gameView.get(i).stonenum = cm.stoneNum;
								gameView.get(i).previous(cm.y, cm.x);
							}
						}
						break;
					case "902":
						for (int i = 0; i < gameView.size(); i++) {
							if (cm.roomName.equals(gameView.get(i).roomName)) {
								gameView.get(i).stonenum = cm.stoneNum;
								gameView.get(i).next(cm.y, cm.x, cm.stone);
							}
						}
						break;
						
					case "903":
						for (int i = 0; i < gameView.size(); i++) {
							if (cm.roomName.equals(gameView.get(i).roomName)) {
								gameView.get(i).undoStoneVote(cm);
							}
						}
						break;
					case "904":
						for (int i = 0; i < gameView.size(); i++) {
							if (cm.roomName.equals(gameView.get(i).roomName)) {
								gameView.get(i).undoStone(cm);
							}
						}
						break;
					}

				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝
			}
		}
	}

	private void requestInsertRoom(int index) { // 방 입장 요청함
		ChatMsg msg = new ChatMsg(UserName, "700", "");
		msg.roomNumber = index;
		SendObject(msg);
	}

	private void insertRoom(ChatMsg msg) { // 방 입장하기
		//System.out.println("방 입장 중111111");
		OmokClientGameView view = new OmokClientGameView(mainView, UserName, Ip_addr, Port_no, msg.gameMode,
				msg.roomName, msg.roomMax, false);
		mainView.gameView.add(view);
		System.out.println(mainView.gameView.size());
	}
	
	private void insertRoomWatcher(ChatMsg msg) { // 방 입장하기
		//System.out.println("방 입장 중222222");
		OmokClientGameView view = new OmokClientGameView(mainView, UserName, Ip_addr, Port_no, msg.gameMode,
				msg.roomName, msg.roomMax, false);
		view.startReadyButton.setEnabled(true);
		view.undoButton.setEnabled(true);
		view.watcher = true;
		view.nowtime = 999;
		view.startReadyButton.setLabel("이전");
		view.undoButton.setLabel("다음");
		mainView.gameView.add(view);
		//System.out.println(mainView.gameView.size());
	}
	
	public void updateProfileImageSrcVector(ChatMsg cm) {
		String list[] = cm.data.split("\n");
		for (String item : list) {
			String name = item.split(" ")[0];
			String src = item.split(" ")[1];
			profileImageSrcHashMap.put(name, src);
		}
	}

	public void serverUserListUpdate(ChatMsg cm) {
		serverUserListModel.removeAllElements(); // 방 리스트를 모두 지우고, 다시 업데이트 한다.
		String list[] = cm.data.split("\n");
		for (String item : list) {
			serverUserListModel.addElement(item);
		}
	}
	
	public void roomListUpdate(ChatMsg cm) { // 방 목록 갱신
		roomListModel.removeAllElements(); // 방 리스트를 모두 지우고, 다시 업데이트 한다.
		String list[] = cm.data.split("\n");
		for (String item : list) {
			roomListModel.addElement(item);
		}
		//System.out.println("roomList updated");
	}

	// keyboard enter key 치면 서버로 전송
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == chatSendButton || e.getSource() == chatTextInput) {
				String msg = null;

				msg = chatTextInput.getText();
				SendMessage(msg);
				chatTextInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				chatTextInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}
		}
	}

	class ImageSendAction implements ActionListener { // 이미지 전송
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == imageChangeButton) {
				frame = new Frame("이미지첨부");
				fd = new FileDialog(frame, "이미지 선택", FileDialog.LOAD);
				fd.setVisible(true);

				if (fd.getDirectory().length() > 0 && fd.getFile().length() > 0) {
					ChatMsg obcm = new ChatMsg(UserName, "300", "IMG");
					ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile());
					obcm.img = img;
					SendObject(obcm);
				}
			}
		}
	}
	
	class ChangeImageAction implements ActionListener { // 이미지 전송
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == imageChangeButton) {
				frame = new Frame("이미지첨부");
				fd = new FileDialog(frame, "이미지 선택", FileDialog.LOAD);
				fd.setVisible(true);
				
				if (fd.getDirectory().length() > 0 && fd.getFile().length() > 0) {
					String path = "profileImg\\" + fd.getFile();
					ChatMsg obcm = new ChatMsg(UserName, "300", path);
					ImageIcon profileImage = new ImageIcon(fd.getDirectory() + fd.getFile());
					obcm.img = profileImage;
					SendObject(obcm);
					
					setProfileImage(profileImage);
					repaint();
				}
			}
		}
	}

	ImageIcon icon1 = new ImageIcon("src/icon1.jpg");
	private JLabel profileImageLabel;

	public void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}

	// 화면에 출력
	public void AppendText(String msg) {

		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.

		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(left, Color.BLACK);
		doc.setParagraphAttributes(doc.getLength(), 1, left, false);
		try {
			doc.insertString(doc.getLength(), msg + "\n", left);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		// textArea.replaceSelection("\n");

	}

	// 화면 우측에 출력
	public void AppendTextR(String msg) {
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet right = new SimpleAttributeSet();
		StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setForeground(right, Color.BLUE);
		doc.setParagraphAttributes(doc.getLength(), 1, right, false);
		try {
			doc.insertString(doc.getLength(), msg + "\n", right);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		// textArea.replaceSelection("\n");

	}


	// Server에게 network으로 전송
	public void SendMessage(String msg) {
		try {
			ChatMsg obcm = new ChatMsg(UserName, "200", msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			AppendText("oos.writeObject() error");
			try {
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			AppendText("SendObject Error");
		}
	}
}