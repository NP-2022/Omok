import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.border.LineBorder;


import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JMenuBar;
import java.awt.ScrollPane;
import javax.swing.JMenuItem;
import java.awt.Panel;
import java.awt.Button;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

public class OmokClientGameView extends JFrame {

	/**
	 * Launch the application.
	 */

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtGame;
	private JTextField txtRoom;
	private JTextField txtPersonNum;
	private String Username;
	private String Ip_addr;
	private String Port_no;
	private JTextField chatTextField;
	private JLabel lblMouseEvent;
	private Graphics gc2 = null;
	private int pen_size = 2; // minimum 2
	// �׷��� Image�� �����ϴ� �뵵, paint() �Լ����� �̿��Ѵ�.
	private Image panelImage = null;
	private Graphics gc;
	public TablePanel1 gamePanel;
	public String roomname;
	public int maxPlayer; // �ִ� �ο���
	public int currentPlayer;
	JPanel panel;
	
	private JList userList;
	private DefaultListModel userListModel; // �� ���
	
	public OmokClientMainView mainView;

	/**
	 * Create the frame.
	 */
	public OmokClientGameView(OmokClientMainView mainView, String username, String ip_addr, String port_no, String gameModeName, String roomName, int maxPlayer) {
		this.Username = username;
		this.mainView = mainView;
		this.roomname = roomName;
		this.maxPlayer = maxPlayer;
		
		currentPlayer = 0;
		
		setResizable(false);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(900, 100, 1036, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane chatScrollPane = new JScrollPane();
		chatScrollPane.setBounds(659, 328, 352, 300);
		contentPane.add(chatScrollPane);

		JTextPane textArea = new JTextPane();
		textArea.setFont(new Font("����ü", Font.PLAIN, 14));
		textArea.setEditable(true);
		textArea.setCaretPosition(0);
		chatScrollPane.setColumnHeaderView(textArea);

		chatTextField = new JTextField();
		chatTextField.setColumns(10);
		chatTextField.setBounds(659, 638, 271, 40);
		contentPane.add(chatTextField);

		JButton chatSendButton = new JButton("����");
		chatSendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		chatSendButton.setFont(new Font("����", Font.PLAIN, 14));
		chatSendButton.setBounds(942, 637, 69, 40);
		contentPane.add(chatSendButton);

		JScrollPane userListScrollPane = new JScrollPane();
		userListScrollPane.setBounds(659, 107, 352, 212);
		contentPane.add(userListScrollPane);

		JLabel userListTitleLabel = new JLabel("��������Ʈ");
		userListTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userListTitleLabel.setFont(new Font("����", Font.BOLD, 14));
		userListTitleLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		userListTitleLabel.setBackground(Color.WHITE);
		userListScrollPane.setColumnHeaderView(userListTitleLabel);
		
		userListModel = new DefaultListModel();
		userList = new JList(userListModel);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userListScrollPane.setViewportView(userList);

		Panel user1Pannel = new Panel();
		user1Pannel.setBounds(659, 10, 84, 91);
		contentPane.add(user1Pannel);

		JLabel user1NameLabel = new JLabel("���� �̸�");
		user1NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		user1NameLabel.setFont(new Font("����", Font.BOLD, 14));
		user1NameLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		user1NameLabel.setBackground(Color.WHITE);
		user1Pannel.add(user1NameLabel);

		Panel user2Pannel = new Panel();
		user2Pannel.setBounds(749, 10, 84, 91);
		contentPane.add(user2Pannel);

		JLabel user2NameLabel = new JLabel("���� �̸�");
		user2NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		user2NameLabel.setFont(new Font("����", Font.BOLD, 14));
		user2NameLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		user2NameLabel.setBackground(Color.WHITE);
		user2Pannel.add(user2NameLabel);

		Panel user3Pannel = new Panel();
		user3Pannel.setBounds(839, 10, 84, 91);
		contentPane.add(user3Pannel);

		JLabel user3NameLabel = new JLabel("���� �̸�");
		user3NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		user3NameLabel.setFont(new Font("����", Font.BOLD, 14));
		user3NameLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		user3NameLabel.setBackground(Color.WHITE);
		user3Pannel.add(user3NameLabel);

		Panel user4Pannel = new Panel();
		user4Pannel.setBounds(929, 10, 84, 91);
		contentPane.add(user4Pannel);

		JLabel user4NameLabel = new JLabel("�����̸�");
		user4NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		user4NameLabel.setFont(new Font("����", Font.BOLD, 14));
		user4NameLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		user4NameLabel.setBackground(Color.WHITE);
		user4Pannel.add(user4NameLabel);

		gamePanel = new TablePanel1();
		gamePanel.setBounds(12, 10, 627, 628);
		gamePanel.setBackground(new Color(206, 167, 61));
		gamePanel.init();

		contentPane.add(gamePanel);
		gc = gamePanel.getGraphics();

		

		Button startButton = new Button("����");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		startButton.setBounds(220, 644, 107, 27);
		contentPane.add(startButton);

		Button undoButton = new Button("������");
		undoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		undoButton.setBounds(343, 644, 107, 27);
		contentPane.add(undoButton);

		Button exitButton = new Button("������");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		exitButton.setBounds(470, 644, 107, 27);
		contentPane.add(exitButton);

		JLabel timeLabel = new JLabel("���� �ð� : 30");
		timeLabel.setBounds(90, 643, 107, 28);
		contentPane.add(timeLabel);

		MyMouseEvent mouse = new MyMouseEvent();

		gamePanel.addMouseListener(mouse);
		setVisible(true);


	}

	class MyMouseEvent implements MouseListener, MouseMotionListener {
		int x;
		int y;

		@Override
		public void mouseDragged(MouseEvent e) {
			// lblMouseEvent.setText(e.getButton() + " mouseDragged " + e.getX() + "," +
			// e.getY());// ��ǥ��°���
//			Color c = new Color(0,0,255);
//			gc2.setColor(c);
//			gc2.fillOval(e.getX()-pen_size/2, e.getY()-pen_size/2, pen_size, pen_size);
//			// panelImnage�� paint()���� �̿��Ѵ�.
//			gc.drawImage(panelImage, 0, 0, panel);
			// SendMouseEvent(e);
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
//			Color c = new Color(0,0,255);
//			gc2.setColor(c);
//			gc2.fillOval(e.getX()-pen_size/2, e.getY()-pen_size/2, pen_size, pen_size);
//			gc.drawImage(panelImage, 0, 0, panel);
			

			x = (int) Math.round(e.getX() / (double) 30) - 1;
			y = (int) Math.round(e.getY() / (double) 30);
			
			
			
			ChatMsg msg = new ChatMsg(Username, "900", "Stone");
			msg.y = y;
			msg.x = x;
			msg.roomName = roomname;
			
			if (gamePanel.isFilled(y, x)) { // ���� �̹� ������ return
				return;
			}
			
			if (gamePanel.Three(y, x)) {
				System.out.println("33�Դϴ�.");
				return;
			}
			
			mainView.SendObject(msg);
			
//			gamePanel.setMap(y, x, 1);
//			if (gamePanel.Three(y, x)) {
//				gamePanel.setZero(y, x);
//			}
//
//			if (gamePanel.Rule(y, x)) {
//				gamePanel.init();
//				gamePanel.repaint();
//			} else
//				gamePanel.repaint();
			// SendMouseEvent(e);
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
	
	public void userListUpdate(ChatMsg msg) {
		userListModel.removeAllElements();
		String list[] = msg.data.split("\n");
		for(String item: list) {
			userListModel.addElement(item);
		}
		System.out.println("userList updated");
	}
	
	public void drawStone(ChatMsg cm) {
		System.out.println("Client draw Stone : player"+cm.stone);
		gamePanel.setMap(cm.y, cm.x, cm.stone);
		currentPlayer = (currentPlayer+1) % maxPlayer; // �÷��̾�� 1~max ( �� ���� +1 )
		
		/*
		if (gamePanel.Three(cm.y, cm.x)) {
			cm.code = "901"; // 33�̸� �Է��� �ٵϵ� �ǵ�����
			mainView.SendObject(cm);
			return;	
		}	
		*/
		
		if (gamePanel.Rule(cm.y, cm.x)) {
			gamePanel.init(); // ���� �ʿ�, ������ ������ �ٷ� ���� �ٽ� ����
		}
		gamePanel.repaint();
		System.out.println(" �ٵϾ� �Էµ�.");
	}
	
	public void undoStone(ChatMsg cm) {
		gamePanel.setZero(cm.y , cm.x);
		gamePanel.repaint();
		
		System.out.println(" �ٵϾ� ���ŵ�.");
	}
	
	
	
	
	class TablePanel1 extends JPanel {
		
		class MapSize {
			private final int CELL =30;
			private final int SIZE =20;
			public int getCell() {
				return CELL;
			}
			public int getSize() {
				return SIZE;
			}
		}
		
		private MapSize size = new MapSize();
		private final int STONE_SIZE = 28;
		private int MaxSize = 20;
		private int Map[][] = new int[MaxSize + 1][MaxSize];

		Image Black = new ImageIcon(OmokClientMainView.class.getResource("Black.png")).getImage();
		Image White = new ImageIcon(OmokClientMainView.class.getResource("White.png")).getImage();
		Image Red = new ImageIcon(OmokClientMainView.class.getResource("Red.png")).getImage();
		Image Custom = new ImageIcon(OmokClientMainView.class.getResource("Custom.png")).getImage();
		
		public void init() {
			for (int i = 0; i < MaxSize; i++) {
				for (int j = 0; j < MaxSize; j++) {
					Map[i][j] = 0;
				}
			}
			currentPlayer = 0;
		}

		public void setMap(int y, int x, int color) {
			if (Map[y][x] == 0)
				Map[y][x] = color;
		}

		public void setZero(int y, int x) {
			Map[y][x] = 0;
		}
		
		public Boolean isFilled(int y, int x) {
			return Map[y][x] != 0;
		}

		public Boolean Three(int y, int x) {
			Map[y][x] = (currentPlayer+1); // ( current�� 0 ~ max-1 �� ���� ���� )
			System.out.println("33 check : Player"+ (currentPlayer+1));
			int nowColor = Map[y][x];
			int dir[][] = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { -1, 1 }, { 1, -1 }, { -1, -1 }, { 1, 1 } };
			int three = 0;

			for (int i = 0; i < 8; i = i + 2) {
				int same_cnt = 1;
				int cunY = y;
				int cunX = x;

				for (int j = 0; j < 3; j++) {
					cunY = cunY + dir[i][0];
					cunX = cunX + dir[i][1];
					if (cunY < 0 || cunY >= MaxSize + 1 || cunX < 0 || cunX >= MaxSize)
						break;
					if (nowColor != Map[cunY][cunX])
						break;

					same_cnt++;
				}
				cunY = y;
				cunX = x;
				for (int j = 0; j < 3; j++) {
					cunY = cunY + dir[i + 1][0];
					cunX = cunX + dir[i + 1][1];
					if (cunY < 0 || cunY >= MaxSize + 1 || cunX < 0 || cunX >= MaxSize)
						break;
					if (nowColor != Map[cunY][cunX])
						break;
					same_cnt++;
				}
				if (same_cnt == 3) {
					three += 1;
				}

			}
			Map[y][x] = 0;
			if (three == 2) {
				return true;
			} else
				return false;
		}

		public Boolean Rule(int y, int x) {
			int nowColor = Map[y][x];
			int dir[][] = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { -1, 1 }, { 1, -1 }, { -1, -1 }, { 1, 1 } };

			if (nowColor != 0) {
				for (int i = 0; i < 8; i = i + 2) {
					int same_cnt = 1;
					int cunY = y;
					int cunX = x;

					for (int j = 0; j < 5; j++) {
						cunY = cunY + dir[i][0];
						cunX = cunX + dir[i][1];
						if (cunY < 0 || cunY >= MaxSize + 1 || cunX < 0 || cunX >= MaxSize)
							break;
						if (nowColor != Map[cunY][cunX])
							break;

						same_cnt++;
					}
					cunY = y;
					cunX = x;
					for (int j = 0; j < 5; j++) {
						cunY = cunY + dir[i + 1][0];
						cunX = cunX + dir[i + 1][1];
						if (cunY < 0 || cunY >= MaxSize + 1 || cunX < 0 || cunX >= MaxSize)
							break;
						if (nowColor != Map[cunY][cunX])
							break;
						same_cnt++;
					}
					if (same_cnt >= 5) {
						return true;
					}
				}
			}
			return false;
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			for (int i = 1; i <= size.getSize(); i++) {
				g.drawLine(size.getCell(), i * size.getCell(), size.getCell() * size.getSize(), i * size.getCell());
				g.drawLine(i * size.getCell(), size.getCell(), i * size.getCell(), size.getCell() * size.getSize());
			}
			drawStone(g);
		}

		public void drawStone(Graphics g) {
			for (int y = 1; y < size.getSize() + 1; y++) {
				for (int x = 0; x < size.getSize(); x++) {
					if (Map[y][x] == 1)
						drawBlack(g, x, y);
					else if (Map[y][x] == 2)
						drawWhite(g, x, y);
					else if (Map[y][x] == 3)
						drawRed(g, x, y);
				}
			}
		}

		public void drawRed(Graphics g, int x, int y) {
			g.drawImage(Red, x * size.getCell() + 15, y * size.getCell() - 15, 28, 28, this);
		}

		public void drawBlack(Graphics g, int x, int y) {
			g.drawImage(Black, x * size.getCell() + 15, y * size.getCell() - 15, 28, 28, this);
		}

		public void drawWhite(Graphics g, int x, int y) {
			g.drawImage(White, x * size.getCell() + 15, y * size.getCell() - 15, 28, 28, this);
		}

	}
	
}



