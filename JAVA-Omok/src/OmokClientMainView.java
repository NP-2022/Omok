
// JavaObjClientView.java ObjecStram ��� Client
//�������� ä�� â
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
	private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����
	private Socket socket; // �������
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

	JPanel imagePanel;
	private Graphics gc;
	private int pen_size = 2; // minimum 2
	// �׷��� Image�� �����ϴ� �뵵, paint() �Լ����� �̿��Ѵ�.
	private Image panelImage = null;
	private Graphics gc2 = null;

	public OmokClientMainView mainView = null; // ���� ȭ�鿡 ���� ���۷���
//	public JavaGameClientView2 gameClientView = null; // ���� ȭ�� ���۷���
	public Vector<OmokClientGameView> gameView = new Vector();

	private JScrollPane roomListScrollPane;
	private JList roomList;
	private DefaultListModel roomListModel; // �� ���
	private JButton roomInsertButton;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // â ������ ���μ��� ����
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
		textArea.setFont(new Font("����ü", Font.PLAIN, 14));
		chatScrollPane.setViewportView(textArea);

		chatTextInput = new JTextField();
		chatTextInput.setBounds(12, 540, 276, 40);
		contentPane.add(chatTextInput);
		chatTextInput.setColumns(10);

		chatSendButton = new JButton("����");
		chatSendButton.setFont(new Font("����", Font.PLAIN, 14));
		chatSendButton.setBounds(295, 539, 69, 40);
		contentPane.add(chatSendButton);

		userNameLabel = new JLabel("User");
		userNameLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		userNameLabel.setBackground(Color.WHITE);
		userNameLabel.setFont(new Font("����", Font.BOLD, 14));
		userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userNameLabel.setBounds(376, 539, 149, 40);
		contentPane.add(userNameLabel);
		setVisible(true);

		AppendText("User " + username + " connecting " + ip_addr + " " + port_no);
		userNameLabel.setText(UserName);

		imageChangeButton = new JButton("�̹��� ����");
		imageChangeButton.setFont(new Font("����", Font.PLAIN, 16));
		imageChangeButton.setBounds(376, 324, 149, 40);
		contentPane.add(imageChangeButton);

		JButton exitButton = new JButton("�� ��");
		exitButton.setFont(new Font("����", Font.PLAIN, 14));

		exitButton.setBounds(672, 539, 100, 40);
		contentPane.add(exitButton);

		imagePanel = new JPanel();
		imagePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		imagePanel.setBackground(Color.WHITE);
		imagePanel.setBounds(376, 374, 149, 154);
		contentPane.add(imagePanel);
		gc = imagePanel.getGraphics();

		// Image ���� ������. paint() ���� �̿��Ѵ�.
		Image defaultimg = new ImageIcon(OmokClientMainView.class.getResource("default.png")).getImage();
		panelImage = createImage(imagePanel.getWidth(), imagePanel.getHeight());
		gc2 = panelImage.getGraphics();
		gc2.setColor(imagePanel.getBackground());
		gc2.fillRect(0, 0, imagePanel.getWidth(), imagePanel.getHeight());
		gc2.setColor(Color.BLACK);
		gc2.drawRect(0, 0, imagePanel.getWidth() - 1, imagePanel.getHeight() - 1);
		gc2.drawImage(defaultimg, 0, 0, imagePanel.getWidth(), imagePanel.getHeight(), imagePanel);

		JLabel titleLabel = new JLabel("<Omok Game>");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("����", Font.BOLD, 14));
		titleLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		titleLabel.setBackground(Color.WHITE);
		titleLabel.setBounds(12, 11, 760, 40);
		contentPane.add(titleLabel);

		roomListScrollPane = new JScrollPane();
		roomListScrollPane.setBounds(376, 61, 396, 212);
		contentPane.add(roomListScrollPane);

		JLabel roomListTitleLabel = new JLabel("�� ����Ʈ");
		roomListTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		roomListTitleLabel.setFont(new Font("����", Font.BOLD, 14));
		roomListTitleLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		roomListTitleLabel.setBackground(Color.WHITE);
		roomListScrollPane.setColumnHeaderView(roomListTitleLabel);

		roomListModel = new DefaultListModel();
		roomList = new JList(roomListModel);
		roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		roomInsertButton = new JButton("�� ����");
		roomInsertButton.setBounds(566, 283, 97, 23);

		contentPane.add(roomInsertButton);

		JButton roomCreateButton = new JButton("�� �����");
		roomListScrollPane.setViewportView(roomList);
		roomCreateButton.setBounds(675, 283, 97, 23);
		contentPane.add(roomCreateButton);

		gameLogScrollPane = new JScrollPane();
		gameLogScrollPane.setBounds(537, 374, 235, 154);
		contentPane.add(gameLogScrollPane);

		lblUserName_1_2 = new JLabel("����");
		gameLogScrollPane.setColumnHeaderView(lblUserName_1_2);
		lblUserName_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName_1_2.setFont(new Font("����", Font.BOLD, 14));
		lblUserName_1_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUserName_1_2.setBackground(Color.WHITE);

		// ���� ���� set
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
				SendObject(msg);
				System.exit(0);
			}
		});

		// �� ���� set
		roomCreateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OmokClientRoomCreateView roomcreate = new OmokClientRoomCreateView(mainView, username, ip_addr,
						port_no);
				setVisible(true);
			}
		});

		// �� ���� set
		roomInsertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = roomList.getSelectedIndex();
				if (index == -1)
					return;
				requestInsertRoom(index);
				System.out.println("go to " + index + " requested.");
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
			ImageSendAction action2 = new ImageSendAction();
			imageChangeButton.addActionListener(action2);
			MyMouseEvent mouse = new MyMouseEvent();
			imagePanel.addMouseMotionListener(mouse);
			imagePanel.addMouseListener(mouse);
			MyMouseWheelEvent wheel = new MyMouseWheelEvent();
			imagePanel.addMouseWheelListener(wheel);

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
		}

	}

	private Image getImage(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public void paint(Graphics g) {
		super.paint(g);
		// Image ������ �������� �ٽ� ��Ÿ�� �� �׷��ش�.
		gc.drawImage(panelImage, 0, 0, this);
	}

	// Server Message�� �����ؼ� ȭ�鿡 ǥ��
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
					if (obcm instanceof ChatMsg || true) {
						cm = (ChatMsg) obcm;
						msg = String.format("[%s]\n%s", cm.userName, cm.data);
						System.out
								.println(String.format("name:[%s] code:[%s] data:[%s]", cm.userName, cm.code, cm.data));
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
							AppendTextR(msg); // �� �޼����� ������
						else
							AppendText(msg);
						break;
					case "201": // ���� ���� ä�� �޼���
						for (int i = 0; i < gameView.size(); i++) {
							if (cm.roomName.equals(gameView.get(i).roomName)) {
								gameView.get(i).receiveGameMessage(cm);
							}
						}
						break;
					case "300": // Image ÷��
//						if (cm.UserName.equals(UserName))
//							AppendTextR("[" + cm.UserName + "]");
//						else
//							AppendText("[" + cm.UserName + "]");
						// AppendImage(cm.img);
						break;
					case "500": // Mouse Event ����
						DoMouseEvent(cm);
						break;
					case "600": // ���ο� �� ���� ��
						break;
					case "700": // �������� �濡 ����
						if (cm.userName.equals(UserName)) // �濡 ������ Ŭ���̾�Ʈ ������ ���
							insertRoom(cm);
						for (int i = 0; i < gameView.size(); i++) {
							if (gameView.get(i).roomName.equals(cm.roomName)) {
								gameView.get(i).gamePanel.init();
								gameView.get(i).gamePanel.repaint();
								gameView.get(i).nowtime = 31;
							}
						}
						break;
					case "702": // �� ����Ʈ ����
						roomListUpdate(cm);
						break;
					case "703": // ���� ����Ʈ ����
						for (int i = 0; i < gameView.size(); i++) {
							if (cm.roomName.equals(gameView.get(i).roomName)) {
								gameView.get(i).userListUpdate(cm);
								gameView.get(i).nowtime = 31;
								gameView.get(i).time = false;
							}
						}
						break;
					case "704":
						if (cm.userName.equals(UserName)) // �濡 ������ Ŭ���̾�Ʈ ������ ���
							insertRoomWatcher(cm);
						break;
					case "800": // ���� ����
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
					case "803": // ���� ��Ż�� ���� �ߴ�
						for (int i = 0; i < gameView.size(); i++) {
							if (cm.roomName.equals(gameView.get(i).roomName)) {
								gameView.get(i).gameStop(cm);
								gameView.get(i).time = false;
								gameView.get(i).nowtime = 31;
							}
						}
					case "900": // �ٵϵ� �Է� ����
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
					case "901": // �ٵϵ� undo ����
						for (int i = 0; i < gameView.size(); i++) {
							if (cm.roomName.equals(gameView.get(i).roomName)) {
								gameView.get(i).stonenum = cm.stoneNum;
								gameView.get(i).previous(cm.y, cm.x);
								System.out.println("���� �Ϸ�");
							}
						}
						break;
					case "902":
						for (int i = 0; i < gameView.size(); i++) {
							if (cm.roomName.equals(gameView.get(i).roomName)) {
								gameView.get(i).stonenum = cm.stoneNum;
								gameView.get(i).next(cm.y, cm.x, cm.stone);
								System.out.println("���� �Ϸ�");
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
					} // catch�� ��
				} // �ٱ� catch����
			}
		}
	}

	private void requestInsertRoom(int index) { // �� ���� ��û��
		ChatMsg msg = new ChatMsg(UserName, "700", "");
		msg.roomNumber = index;
		SendObject(msg);
	}

	private void insertRoom(ChatMsg msg) { // �� �����ϱ�
		System.out.println("�� ���� ��111111");
		OmokClientGameView view = new OmokClientGameView(mainView, UserName, Ip_addr, Port_no, msg.gameMode,
				msg.roomName, msg.roomMax, false);
		mainView.gameView.add(view);
		System.out.println(mainView.gameView.size());
	}
	
	private void insertRoomWatcher(ChatMsg msg) { // �� �����ϱ�
		System.out.println("�� ���� ��222222");
		OmokClientGameView view = new OmokClientGameView(mainView, UserName, Ip_addr, Port_no, msg.gameMode,
				msg.roomName, msg.roomMax, false);
		view.startReadyButton.setEnabled(true);
		view.undoButton.setEnabled(true);
		view.watcher = true;
		view.nowtime = 999;
		view.startReadyButton.setLabel("����");
		view.undoButton.setLabel("����");
		mainView.gameView.add(view);
		System.out.println(mainView.gameView.size());
	}

	public void roomListUpdate(ChatMsg cm) { // �� ��� ����
		roomListModel.removeAllElements(); // �� ����Ʈ�� ��� �����, �ٽ� ������Ʈ �Ѵ�.
		String list[] = cm.data.split("\n");
		for (String item : list) {
			roomListModel.addElement(item);
		}
		System.out.println("roomList updated");
	}

	// Mouse Event ���� ó��
	public void DoMouseEvent(ChatMsg cm) {
		Color c;
		if (cm.userName.matches(UserName)) // ���� ���� �̹� Local �� �׷ȴ�.
			return;
		c = new Color(255, 0, 0); // �ٸ� ��� ���� Red
		gc2.setColor(c);
		gc2.fillOval(cm.mouse_e.getX() - pen_size / 2, cm.mouse_e.getY() - cm.pen_size / 2, cm.pen_size, cm.pen_size);
		gc.drawImage(panelImage, 0, 0, imagePanel);
	}

	public void SendMouseEvent(MouseEvent e) {
		ChatMsg cm = new ChatMsg(UserName, "500", "MOUSE");
		cm.mouse_e = e;
		cm.pen_size = pen_size;
		SendObject(cm);
	}

	class MyMouseWheelEvent implements MouseWheelListener {
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			// TODO Auto-generated method stub
			if (e.getWheelRotation() < 0) { // ���� �ø��� ��� pen_size ����
				if (pen_size < 20)
					pen_size++;
			} else {
				if (pen_size > 2)
					pen_size--;
			}

		}

	}

	// Mouse Event Handler
	class MyMouseEvent implements MouseListener, MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent e) {
			// lblMouseEvent.setText(e.getButton() + " mouseDragged " + e.getX() + "," +
			// e.getY());// ��ǥ��°���
			Color c = new Color(0, 0, 255);
			gc2.setColor(c);
			gc2.fillOval(e.getX() - pen_size / 2, e.getY() - pen_size / 2, pen_size, pen_size);
			// panelImnage�� paint()���� �̿��Ѵ�.
			gc.drawImage(panelImage, 0, 0, imagePanel);
			SendMouseEvent(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// lblMouseEvent.setText(e.getButton() + " mouseMoved " + e.getX() + "," +
			// e.getY());
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// lblMouseEvent.setText(e.getButton() + " mouseClicked " + e.getX() + "," +
			// e.getY());
			Color c = new Color(0, 0, 255);
			gc2.setColor(c);
			gc2.fillOval(e.getX() - pen_size / 2, e.getY() - pen_size / 2, pen_size, pen_size);
			gc.drawImage(panelImage, 0, 0, imagePanel);
			SendMouseEvent(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// lblMouseEvent.setText(e.getButton() + " mouseEntered " + e.getX() + "," +
			// e.getY());
			// panel.setBackground(Color.YELLOW);

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// lblMouseEvent.setText(e.getButton() + " mouseExited " + e.getX() + "," +
			// e.getY());
			// panel.setBackground(Color.CYAN);

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// lblMouseEvent.setText(e.getButton() + " mousePressed " + e.getX() + "," +
			// e.getY());

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// lblMouseEvent.setText(e.getButton() + " mouseReleased " + e.getX() + "," +
			// e.getY());
			// �巡���� ����� ����

		}
	}

	// keyboard enter key ġ�� ������ ����
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button�� �����ų� �޽��� �Է��ϰ� Enter key ġ��
			if (e.getSource() == chatSendButton || e.getSource() == chatTextInput) {
				String msg = null;

				msg = chatTextInput.getText();
				SendMessage(msg);
				chatTextInput.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
				chatTextInput.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
				if (msg.contains("/exit")) // ���� ó��
					System.exit(0);
			}
		}
	}

	class ImageSendAction implements ActionListener { // �̹��� ����
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == imageChangeButton) {
				frame = new Frame("�̹���÷��");
				fd = new FileDialog(frame, "�̹��� ����", FileDialog.LOAD);
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

	ImageIcon icon1 = new ImageIcon("src/icon1.jpg");
	private JLabel lblUserName_1_2;
	private JScrollPane gameLogScrollPane;

	public void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		// ������ �̵�
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}

	// ȭ�鿡 ���
	public void AppendText(String msg) {

		msg = msg.trim(); // �յ� blank�� \n�� �����Ѵ�.

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

	// ȭ�� ������ ���
	public void AppendTextR(String msg) {
		msg = msg.trim(); // �յ� blank�� \n�� �����Ѵ�.
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

	/*
	 * public void AppendImage(ImageIcon ori_icon) { int len =
	 * textArea.getDocument().getLength(); textArea.setCaretPosition(len); // place
	 * caret at the end (with no selection) Image ori_img = ori_icon.getImage();
	 * Image new_img; ImageIcon new_icon; int width, height; double ratio; width =
	 * ori_icon.getIconWidth(); height = ori_icon.getIconHeight();
	 * 
	 * 
	 * gc2.drawImage(ori_img, 0, 0, imagePanel.getWidth(), imagePanel.getHeight(),
	 * imagePanel); gc.drawImage(panelImage, 0, 0, imagePanel.getWidth(),
	 * imagePanel.getHeight(), imagePanel); }
	 */

	// Windows ó�� message ������ ������ �κ��� NULL �� ����� ���� �Լ�
	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Server���� network���� ����
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

	public void SendObject(Object ob) { // ������ �޼����� ������ �޼ҵ�
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			AppendText("SendObject Error");
		}
	}
}