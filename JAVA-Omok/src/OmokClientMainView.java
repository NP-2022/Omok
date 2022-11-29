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
	private JTextField txtInput;
	private String UserName;
	private JButton btnSend;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private JLabel lblUserName;
	// private JTextArea textArea;
	private JTextPane textArea;

	private Frame frame;
	private FileDialog fd;
	private JButton imgBtn;

	JPanel panel;
	private Graphics gc;
	private int pen_size = 2; // minimum 2
	// 그려진 Image를 보관하는 용도, paint() 함수에서 이용한다.
	private Image panelImage = null; 
	private Graphics gc2 = null;
	
	public OmokClientMainView mainView = null; // 현재 화면에 대한 레퍼런스
//	public JavaGameClientView2 gameClientView = null; // 게임 화면 레퍼런스
	public Vector<OmokClientGameView> gameView = new Vector();

	private JScrollPane roomListScrollPane; 
	private JList roomList;
	private DefaultListModel roomListModel; // 방 목록
	private JButton insertButton;

	
	/**
	 * Create the frame.
	 * @throws BadLocationException 
	 */
	public OmokClientMainView(String username, String ip_addr, String port_no)  {
		
		mainView = this;

		UserName = username;
		Ip_addr = ip_addr;
		Port_no = port_no;
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //창 닫히면 프로세스 종료
		setBounds(100, 100, 800, 634);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 61, 352, 471);
		contentPane.add(scrollPane);

		textArea = new JTextPane();
		textArea.setEditable(true);
		textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
		scrollPane.setViewportView(textArea);

		txtInput = new JTextField();
		txtInput.setBounds(12, 540, 276, 40);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		btnSend = new JButton("\uC804\uC1A1");
		btnSend.setFont(new Font("굴림", Font.PLAIN, 14));
		btnSend.setBounds(295, 539, 69, 40);
		contentPane.add(btnSend);

		lblUserName = new JLabel("User");
		lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUserName.setBackground(Color.WHITE);
		lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setBounds(376, 539, 149, 40);
		contentPane.add(lblUserName);
		setVisible(true);

		AppendText("User " + username + " connecting " + ip_addr + " " + port_no);
		lblUserName.setText(UserName);

		imgBtn = new JButton("이미지 변경");
		imgBtn.setFont(new Font("굴림", Font.PLAIN, 16));
		imgBtn.setBounds(376, 324, 149, 40);
		contentPane.add(imgBtn);

		JButton btnNewButton = new JButton("종 료");
		btnNewButton.setFont(new Font("굴림", Font.PLAIN, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
				SendObject(msg);
				System.exit(0);
			}
		});
		btnNewButton.setBounds(672, 539, 100, 40);
		contentPane.add(btnNewButton);

		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setBounds(376, 374, 149, 154);
		contentPane.add(panel);
		gc = panel.getGraphics();
		
		// Image 영역 보관용. paint() 에서 이용한다.
		Image defaultimg =new ImageIcon(OmokClientMainView.class.getResource("default.png")).getImage();
		panelImage = createImage(panel.getWidth(), panel.getHeight());
		gc2 = panelImage.getGraphics();
		gc2.setColor(panel.getBackground());
		gc2.fillRect(0,0, panel.getWidth(),  panel.getHeight());
		gc2.setColor(Color.BLACK);
		gc2.drawRect(0,0, panel.getWidth()-1,  panel.getHeight()-1);
		gc2.drawImage(defaultimg,  0,  0, panel.getWidth(), panel.getHeight(), panel);
		
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
		
		JLabel roomListTitleLabel = new JLabel("\uBC29 \uB9AC\uC2A4\uD2B8");
		roomListTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		roomListTitleLabel.setFont(new Font("굴림", Font.BOLD, 14));
		roomListTitleLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		roomListTitleLabel.setBackground(Color.WHITE);
		roomListScrollPane.setColumnHeaderView(roomListTitleLabel);

		roomListModel = new DefaultListModel();
		roomList = new JList(roomListModel);
		roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		insertButton = new JButton("방 입장");
		insertButton.setBounds(566, 283, 97, 23);
		
		contentPane.add(insertButton);
		
		insertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = roomList.getSelectedIndex();
				if(index == -1) return;
				requestInsertRoom(index);
				System.out.println("go to "+index+" requested.");
			}
			
		});
		
		roomListScrollPane.setViewportView(roomList);
		
		JButton btnNewButton_1 = new JButton("방 만들기");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OmokClientRoomCreateView roomcreate = new OmokClientRoomCreateView(mainView, username, ip_addr, port_no);
				setVisible(true);
			}
		});
		btnNewButton_1.setBounds(675, 283, 97, 23);
		contentPane.add(btnNewButton_1);
		
		
		
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(537, 374, 235, 154);
		contentPane.add(scrollPane_2);
		
		lblUserName_1_2 = new JLabel("전적");
		scrollPane_2.setColumnHeaderView(lblUserName_1_2);
		lblUserName_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName_1_2.setFont(new Font("굴림", Font.BOLD, 14));
		lblUserName_1_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUserName_1_2.setBackground(Color.WHITE);
		

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
			btnSend.addActionListener(action);
			txtInput.addActionListener(action);
			txtInput.requestFocus();
			ImageSendAction action2 = new ImageSendAction();
			imgBtn.addActionListener(action2);
			MyMouseEvent mouse = new MyMouseEvent();
			panel.addMouseMotionListener(mouse);
			panel.addMouseListener(mouse);
			MyMouseWheelEvent wheel = new MyMouseWheelEvent();
			panel.addMouseWheelListener(wheel);


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
		// Image 영역이 가려졌다 다시 나타날 때 그려준다.
		gc.drawImage(panelImage, 0, 0, this);
	}
	
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
						msg = String.format("[%s]\n%s", cm.UserName, cm.data);
					} else
						continue;
					switch (cm.code) {
					case "200": // chat message
						if (cm.UserName.equals(UserName))
							AppendTextR(msg); // 내 메세지는 우측에
						else
							AppendText(msg);
						break;
					case "300": // Image 첨부
//						if (cm.UserName.equals(UserName))
//							AppendTextR("[" + cm.UserName + "]");
//						else
//							AppendText("[" + cm.UserName + "]");
						AppendImage(cm.img);
						break;
					case "500": // Mouse Event 수신
						DoMouseEvent(cm);
						break;
					case "600": // 새로운 방 생성 됨
						break;
					case "700": // 누군가가 방에 입장
						if(cm.UserName.equals(UserName)) // 방에 입장한 클라이언트 본인일 경우
							insertRoom(cm);
						break;
					case "702": // 방 리스트 갱신
						roomListUpdate(cm);
						break;
					case "703": // 유저 리스트 갱신
						for(int i = 0; i < gameView.size(); i++) {
							gameView.get(i).userListUpdate(cm);
						}
						break;
					case "900":
						for(int i = 0; i < gameView.size(); i++) {
							gameView.get(i).drawStone(cm);
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


	
	private void requestInsertRoom(int index) {
		ChatMsg msg = new ChatMsg(UserName, "700", "");
		msg.roomNumber = index;
		SendObject(msg);
	}
	
	private void insertRoom(ChatMsg msg) { // 방 입장하기
		OmokClientGameView view = new OmokClientGameView(mainView, UserName, Ip_addr, Port_no, msg.gameMode, msg.roomName, msg.roomMax);
		mainView.gameView.add(view);
	}
	
	public void roomListUpdate(ChatMsg cm) {
		roomListModel.removeAllElements();
		String list[] = cm.data.split("\n");
		for(String item: list) {
			roomListModel.addElement(item);
		}
		System.out.println("roomList updated");
	}

	// Mouse Event 수신 처리
	public void DoMouseEvent(ChatMsg cm) {
		Color c;
		if (cm.UserName.matches(UserName)) // 본인 것은 이미 Local 로 그렸다.
			return;
		c = new Color(255, 0, 0); // 다른 사람 것은 Red
		gc2.setColor(c);
		gc2.fillOval(cm.mouse_e.getX() - pen_size/2, cm.mouse_e.getY() - cm.pen_size/2, cm.pen_size, cm.pen_size);
		gc.drawImage(panelImage, 0, 0, panel);
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
			if (e.getWheelRotation() < 0) { // 위로 올리는 경우 pen_size 증가
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
			//lblMouseEvent.setText(e.getButton() + " mouseDragged " + e.getX() + "," + e.getY());// 좌표출력가능
			Color c = new Color(0,0,255);
			gc2.setColor(c);
			gc2.fillOval(e.getX()-pen_size/2, e.getY()-pen_size/2, pen_size, pen_size);
			// panelImnage는 paint()에서 이용한다.
			gc.drawImage(panelImage, 0, 0, panel);
			SendMouseEvent(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			//lblMouseEvent.setText(e.getButton() + " mouseMoved " + e.getX() + "," + e.getY());
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			//lblMouseEvent.setText(e.getButton() + " mouseClicked " + e.getX() + "," + e.getY());
			Color c = new Color(0,0,255);
			gc2.setColor(c);
			gc2.fillOval(e.getX()-pen_size/2, e.getY()-pen_size/2, pen_size, pen_size);
			gc.drawImage(panelImage, 0, 0, panel);
			SendMouseEvent(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			//lblMouseEvent.setText(e.getButton() + " mouseEntered " + e.getX() + "," + e.getY());
			// panel.setBackground(Color.YELLOW);

		}

		@Override
		public void mouseExited(MouseEvent e) {
			//lblMouseEvent.setText(e.getButton() + " mouseExited " + e.getX() + "," + e.getY());
			// panel.setBackground(Color.CYAN);

		}

		@Override
		public void mousePressed(MouseEvent e) {
			//lblMouseEvent.setText(e.getButton() + " mousePressed " + e.getX() + "," + e.getY());

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			//lblMouseEvent.setText(e.getButton() + " mouseReleased " + e.getX() + "," + e.getY());
			// 드래그중 멈출시 보임

		}
	}

	// keyboard enter key 치면 서버로 전송
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == btnSend || e.getSource() == txtInput) {
				String msg = null;
			
				msg = txtInput.getText();
				SendMessage(msg);
				txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}
		}
	}

	class ImageSendAction implements ActionListener { //이미지 전송
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == imgBtn) {
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

	ImageIcon icon1 = new ImageIcon("src/icon1.jpg");
	private JLabel lblUserName_1_2;
	private JScrollPane scrollPane_2;

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
			doc.insertString(doc.getLength(), msg+"\n", left );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		//textArea.replaceSelection("\n");


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
			doc.insertString(doc.getLength(),msg+"\n", right );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		//textArea.replaceSelection("\n");

	}
	
	public void AppendImage(ImageIcon ori_icon) {
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len); // place caret at the end (with no selection)
		Image ori_img = ori_icon.getImage();
		Image new_img;
		ImageIcon new_icon;
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();


		gc2.drawImage(ori_img,  0,  0, panel.getWidth(), panel.getHeight(), panel);
		gc.drawImage(panelImage, 0, 0, panel.getWidth(), panel.getHeight(), panel);
	}

	// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
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