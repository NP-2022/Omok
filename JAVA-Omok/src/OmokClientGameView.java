import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Component;

import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JMenuBar;
import java.awt.ScrollPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Panel;
import java.awt.AlphaComposite;
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
	private String userName;
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
	public String roomName;
	public int maxPlayer; // �ִ� �ο���
	public int currentPlayer;
	public JTextPane textArea;
	public JButton chatSendButton;
	public JPanel panel;
	public Button startReadyButton;
	public Button undoButton;
	public JButton kickButton;
	public JButton chatLimitButton;
	public boolean isOwner; // ��������?
	public boolean time = false; // �ð� �帧 ���� �� true �ƴϸ� false
	public int nowtime = 30;
	public JLabel timeLabel;
	public boolean watcher = false;
	public int stonenum = 999;

	private JList userList;
	private DefaultListModel userListModel; // �� ���
	
	JLabel user1NameLabel; 
	JLabel user2NameLabel; 
	JLabel user3NameLabel; 
	JLabel user4NameLabel;
	
	JLabel user1ImageLabel;
	JLabel user2ImageLabel;
	JLabel user3ImageLabel;
	JLabel user4ImageLabel;
	
	ImageIcon defaultImage;
	
	public Vector<JLabel> nameLabelVector = new Vector<>();
	public Vector<JLabel> imageLabelVector = new Vector<>();

	public OmokClientMainView mainView;
	/**
	 * Create the frame.
	 */
	public OmokClientGameView(OmokClientMainView mainView, String username, String ip_addr, String port_no,
			String gameModeName, String roomName, int maxPlayer, boolean isOwner) {
		this.userName = username;
		this.mainView = mainView;
		this.roomName = roomName;
		this.maxPlayer = maxPlayer;
		this.isOwner = isOwner;
		this.setTitle(roomName);
		currentPlayer = 0;

		setResizable(false);
		setBounds(900, 100, 1036, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane chatScrollPane = new JScrollPane();
		chatScrollPane.setBounds(659, 350, 352, 278);
		contentPane.add(chatScrollPane);

		textArea = new JTextPane();
		textArea.setFont(new Font("����ü", Font.PLAIN, 14));
		textArea.setEditable(true);
		textArea.setCaretPosition(0);
		chatScrollPane.setViewportView(textArea);

		chatTextField = new JTextField();
		chatTextField.setColumns(10);
		chatTextField.setBounds(659, 638, 271, 40);
		contentPane.add(chatTextField);

		chatSendButton = new JButton("����");
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
		userList.setCellRenderer(new myListRenderer());

		
		gamePanel = new TablePanel1();
		gamePanel.setBounds(12, 10, 627, 628);
		gamePanel.setBackground(new Color(206, 167, 61));
		gamePanel.init();

		contentPane.add(gamePanel);
		gc = gamePanel.getGraphics();

		startReadyButton = new Button("����");
		if (!isOwner)
			startReadyButton.setLabel("�غ�");
		startReadyButton.setBounds(220, 644, 107, 27);
		contentPane.add(startReadyButton);

		undoButton = new Button("������");
		undoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (undoButton.getLabel().equals("����")) { // ������ ��ư
					System.out.println("���� ��ư ����");
					ChatMsg next = new ChatMsg(userName, "902", "����");
					next.roomName = roomName;
					next.stoneNum = stonenum;
					mainView.SendObject(next);
				} 
				else if (undoButton.getLabel().equals("������")) { // ������
					ChatMsg undoReq = new ChatMsg(userName, "903", "������ ��û");
					undoReq.roomName = roomName;
					mainView.SendObject(undoReq);
				}
			}
		});
		undoButton.setBounds(343, 644, 107, 27);
		contentPane.add(undoButton);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		Button exitButton = new Button("������");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				time = false;
				ChatMsg msg = new ChatMsg(userName, "701", "������");
				msg.roomName = roomName;
				for (int i = 0; i < mainView.gameView.size(); i++) {
					if (mainView.gameView.get(i).roomName.equals(roomName)) {
						mainView.gameView.remove(i);
					}

				}
				mainView.SendObject(msg);
			}
		});

		exitButton.setBounds(470, 644, 107, 27);
		contentPane.add(exitButton);

		timeLabel = new JLabel("���� �ð� : " + nowtime);
		timeLabel.setBounds(90, 643, 107, 28);
		contentPane.add(timeLabel);
		
		kickButton = new JButton("��������");
		kickButton.setBounds(730, 323, 91, 23);
		contentPane.add(kickButton);
		if(!isOwner) kickButton.setVisible(false);
		
		chatLimitButton = new JButton("ä�ñ���");
		chatLimitButton.setBounds(857, 323, 91, 23);
		contentPane.add(chatLimitButton);
		
		ImageIcon emptyImage = new ImageIcon(OmokClientMainView.class.getResource("/profileImg/empty.png"));
		Image changeImg = emptyImage.getImage().getScaledInstance(84, 74, Image.SCALE_SMOOTH);
		defaultImage = new ImageIcon(changeImg);
		
		user1ImageLabel = new JLabel();
		user1ImageLabel.setBounds(659, 10, 84, 74);
		contentPane.add(user1ImageLabel);
		user1ImageLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		user1ImageLabel.setIcon(defaultImage);
		imageLabelVector.add(user1ImageLabel);

		user2ImageLabel = new JLabel();
		user2ImageLabel.setBounds(749, 10, 84, 74);
		user2ImageLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(user2ImageLabel);
		user2ImageLabel.setIcon(defaultImage);
		imageLabelVector.add(user2ImageLabel);
		
		user3ImageLabel = new JLabel();
		user3ImageLabel.setBounds(839, 10, 84, 74);
		contentPane.add(user3ImageLabel);
		user3ImageLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		user3ImageLabel.setIcon(defaultImage);
		imageLabelVector.add(user3ImageLabel);
		
		user4ImageLabel = new JLabel();
		user4ImageLabel.setBounds(929, 10, 84, 74);
		contentPane.add(user4ImageLabel);
		user4ImageLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		user4ImageLabel.setIcon(defaultImage);
		imageLabelVector.add(user4ImageLabel);
		
		user1NameLabel = new JLabel("�������");
		user1NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		user1NameLabel.setFont(new Font("����", Font.BOLD, 14));
		user1NameLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		user1NameLabel.setBackground(Color.WHITE);
		user1NameLabel.setBounds(669, 85, 66, 20);
		contentPane.add(user1NameLabel);
		nameLabelVector.add(user1NameLabel);
		
		user2NameLabel = new JLabel("�������");
		user2NameLabel.setBounds(759, 85, 66, 20);
		contentPane.add(user2NameLabel);
		user2NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		user2NameLabel.setFont(new Font("����", Font.BOLD, 14));
		user2NameLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		user2NameLabel.setBackground(Color.WHITE);
		nameLabelVector.add(user2NameLabel);
				
		user3NameLabel = new JLabel("�������");
		user3NameLabel.setBounds(849, 85, 66, 20);
		contentPane.add(user3NameLabel);
		user3NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		user3NameLabel.setFont(new Font("����", Font.BOLD, 14));
		user3NameLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		user3NameLabel.setBackground(Color.WHITE);
		nameLabelVector.add(user3NameLabel);
		
		user4NameLabel = new JLabel("�������");
		user4NameLabel.setBounds(939, 85, 66, 20);
		contentPane.add(user4NameLabel);
		user4NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		user4NameLabel.setFont(new Font("����", Font.BOLD, 14));
		user4NameLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		user4NameLabel.setBackground(Color.WHITE);
		nameLabelVector.add(user4NameLabel);
						
						
		if(!isOwner) chatLimitButton.setVisible(false);

		MyMouseEvent mouse = new MyMouseEvent();

		gamePanel.addMouseListener(mouse);

		TextSendAction action = new TextSendAction();
		chatSendButton.addActionListener(action);
		chatTextField.addActionListener(action);
		chatTextField.requestFocus();

		MyAction myAction = new MyAction();
		startReadyButton.addActionListener(myAction);
		
		OwnerAction ownerAction = new OwnerAction();
		kickButton.addActionListener(ownerAction);
		chatLimitButton.addActionListener(ownerAction);
		

		undoButton.addActionListener(myAction);
		
		
		repaint();
		setVisible(true);

	}

	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button�� �����ų� �޽��� �Է��ϰ� Enter key ġ��
			if (e.getSource() == chatSendButton || e.getSource() == chatTextField) {
				String msg = null;
				msg = String.format("[%s] %s", userName, chatTextField.getText());
				ChatMsg cm = new ChatMsg(userName, "201", msg);
				cm.roomName = roomName; 
				mainView.SendObject(cm);
				chatTextField.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
				chatTextField.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
				if (msg.contains("/exit")) // ���� ó��
					System.exit(0);
			}
		}
	}

	class MyAction implements ActionListener { // ����, �غ� ��ư Action ����
		@Override
		public void actionPerformed(ActionEvent e) {
			if (startReadyButton.getLabel().equals("����")) { // ������ ��ư ������
				System.out.println("���� ��ư ����");
				ChatMsg previous = new ChatMsg(userName, "901", "����");
				previous.roomName = roomName;
				previous.stoneNum = stonenum;
				mainView.SendObject(previous);
			} else {
				String label = ((Button) e.getSource()).getLabel();
				if (label.equals("����")) {
					ChatMsg cm = new ChatMsg(userName, "800", "");
					cm.roomName = roomName;
					mainView.SendObject(cm);
				} else if (label.equals("�غ�") || label.equals("�غ� ���")) {
					ChatMsg cm = new ChatMsg(userName, "801", "");
					cm.roomName = roomName;
					mainView.SendObject(cm);
				}
			}
		}
	}
	
	class myListRenderer extends DefaultListCellRenderer { // ����Ʈ�� �̹����� �����Ͽ� Ŀ���� ������
		
		@Override
		public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            
            String name = value.toString().split("�̸�:")[1].split("]")[0];
            
            String path;
            try {
            	path = mainView.profileImageSrcHashMap.get(name);
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
	
	class OwnerAction implements ActionListener { // ��������, ä�ñ��� Action
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == kickButton) { // ���� ����
				System.out.println("�������� ��ư ����"); 
				int index = userList.getSelectedIndex();
				if (index == -1 || index == 0)
					return;
				String selectedName = userList.getSelectedValue().toString().split("�̸�:")[1].split("]")[0]; // �̸��� ]�� ���ٰ� ����
				int result = JOptionPane.showConfirmDialog(gamePanel, selectedName + "�� �������� ��ų���?", "��������", JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION) {
					ChatMsg cm = new ChatMsg(userName, "705", selectedName);
					cm.roomName = roomName;
					mainView.SendObject(cm);
				}
			}
			else if (e.getSource() == chatLimitButton) {
				System.out.println("ä�ñ��� ��ư ����");
				int index = userList.getSelectedIndex();
				if (index == -1 || index == 0)
					return;
				String selectedName = userList.getSelectedValue().toString().split("�̸�:")[1].split("]")[0]; // �̸��� ]�� ���ٰ� ����
				ChatMsg cm = new ChatMsg(userName, "706", selectedName);
				cm.roomName = roomName;
				mainView.SendObject(cm);
			}
		}
	}

	class MyMouseEvent implements MouseListener, MouseMotionListener {
		int x;
		int y;

		@Override
		public void mouseDragged(MouseEvent e) {}

		@Override
		public void mouseMoved(MouseEvent e) {}

		@Override
		public void mouseClicked(MouseEvent e) {


			x = (int) Math.round(e.getX() / (double) 30) - 1;
			y = (int) Math.round(e.getY() / (double) 30);

			ChatMsg msg = new ChatMsg(userName, "900", "Stone");
			msg.y = y;
			msg.x = x;
			msg.roomName = roomName;

			if (watcher == false) {
				if (gamePanel.isFilled(y, x)) { // ���� �̹� ������ return
					AppendText("���� �̹� �ִ� Ÿ���Դϴ�.");
					return;
				}

				if (gamePanel.Three(y, x)) {
					AppendText("�̰��� 33 �꿡 ���� ���� ���� �� �����ϴ�.");
					return;
				}

				if (msg.y == 0) {
					return;
				}
			} else {
				if (gamePanel.isFilled(y, x)) { // ���� �̹� ������ return
					return;
				}
				if (msg.y == 0) {
					return;
				}
			}
			mainView.SendObject(msg);

		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
	}

	public void userListUpdate(ChatMsg msg) {
		if (roomName.equals(msg.roomName)) {
			nowtime = 30;
			userListModel.removeAllElements();
			String list[] = msg.data.split("\n");
			for (String item : list) {
				userListModel.addElement(item);
			}
			//System.out.println("userList updated");
			String owner[] = list[0].split(" ");
			//System.out.println(owner[1].substring(4, owner[1].length() - 1) + "   �����̸�<<<<<<<<<");
			if (userName.equals(owner[1].substring(4, owner[1].length() - 1))) {
				startReadyButton.setLabel("����");
				startReadyButton.setEnabled(true);
				kickButton.setVisible(true);
				chatLimitButton.setVisible(true);
				this.isOwner = true;
			}
			
			for(JLabel label : imageLabelVector) 
				label.setIcon(defaultImage);
			for(JLabel label : nameLabelVector) 
				label.setText("�������");
			
			int i = 0;
			for(String item : list) {
				String name = item.split("�̸�:")[1].split("]")[0];
				nameLabelVector.get(i).setText(name);
				ImageIcon image = new ImageIcon(OmokClientMainView.class.getResource(mainView.profileImageSrcHashMap.get(name)));
				Image changeImg = image.getImage().getScaledInstance(84, 74, Image.SCALE_SMOOTH);
				imageLabelVector.get(i).setIcon(new ImageIcon(changeImg));
				i++;
			}
			repaint();
		}
	}

	public void timeProcess() {
		timeLabel.setText("���� �ð� : " + nowtime);

		if (nowtime < 10)
			timeLabel.setForeground(Color.RED);
		else if (nowtime < 21)
			timeLabel.setForeground(Color.BLUE);
		else
			timeLabel.setForeground(Color.BLACK);

		if (nowtime == 0) {
			AppendText("���� �ð��� �������ϴ�. ���� ���ʷ� �Ѿ���ϴ�.");
			ChatMsg msg = new ChatMsg(userName, "900", "Stone");
			msg.y = 0;
			msg.x = 0;
			msg.roomName = roomName;
			mainView.SendObject(msg);
			System.out.println("���� �ð��� �������ϴ�.");
			nowtime = 31;
		}

	}

	public void drawStone(ChatMsg cm) {
		time = true;
		if (cm.stone == 999) {
			if (roomName.equals(cm.roomName)) {
				System.out.println("�Ƽ��� �Ӵϴ�.");
				gamePanel.removeBackMap();
				gamePanel.setMap(cm.y, cm.x, cm.stone);
				gamePanel.repaint();
				System.out.println(" �ٵϾ� �Էµ�.");
			}
		} else {
			if (roomName.equals(cm.roomName)) {
				System.out.println("Client draw Stone : player" + cm.stone);
				gamePanel.setMap(cm.y, cm.x, cm.stone);
				currentPlayer = (currentPlayer + 1) % maxPlayer; // �÷��̾�� 1~max ( �� ���� +1 )
				System.out.println(currentPlayer);
				/*
				 * if (gamePanel.Three(cm.y, cm.x)) { cm.code = "901"; // 33�̸� �Է��� �ٵϵ� �ǵ�����
				 * mainView.SendObject(cm); return; }
				 */
				if (gamePanel.Rule(cm.y, cm.x)) {
					gamePanel.setMap(cm.y, cm.x, cm.stone);
					gamePanel.repaint();
					gameEnd(cm);
					time = false;
					nowtime = 30;
					gamePanel.repaint();
					timeLabel.setText("���� �ð� : " + nowtime);

					if (startReadyButton.getLabel().equals("�غ� ���")) {
						startReadyButton.setLabel("�غ�");
					}
				} else {
					gamePanel.repaint();
					System.out.println(" �ٵϾ� �Էµ�.");
				}
			}
		}
	}
	
	public void previous(int y, int x) {
		gamePanel.setZero(y, x);
		gamePanel.repaint();

		System.out.println(" ���� �ٵϾ� ");
	}

	public void next(int y, int x, int color) {
		if(y == 0 && x == 0) {
			gamePanel.setMap(y, x, 0);
		}else {
			gamePanel.setMap(y, x, color);
		}
		gamePanel.repaint();

		System.out.println(" ���� �ٵϾ� ");
	}

	public void undoStoneVote(ChatMsg cm) {
		
		int result = JOptionPane.showConfirmDialog(gamePanel, "�� �� �������?", "������ ��ǥ", JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.YES_OPTION) {
			ChatMsg msg = new ChatMsg(userName, "904", "agree");
			msg.roomName = roomName;
			mainView.SendObject(msg);
		}
		else {
			ChatMsg msg = new ChatMsg(userName, "904", "disagree");
			msg.roomName = roomName;
			mainView.SendObject(msg);
		}
	}
	
	public void undoStone(ChatMsg cm) {
		gamePanel.setZero(cm.y, cm.x);
		gamePanel.repaint();

		System.out.println(" ������ : �ٵϾ� ���ŵ�.");
	}

	public void receiveGameMessage(ChatMsg cm) {
		if (!cm.roomName.equals(roomName))
			return;
		AppendText(cm.data);

	}

	public void gameStart(ChatMsg cm) {
		if (!cm.roomName.equals(roomName))
			return;

		time = true;
		gamePanel.init();
		startReadyButton.setEnabled(false);
		if (watcher == true) {
			startReadyButton.setEnabled(true);
		}
	}

	public void gameReady(ChatMsg cm) {
		if (isOwner)
			return;

		String label = cm.data.equals("true") ? "�غ� ���" : "�غ�";

		startReadyButton.setLabel(label);
	}

	public void gameEnd(ChatMsg cm) {
		time = false;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gamePanel.setAll(cm.stone);
		gamePanel.repaint();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gamePanel.init();
		ChatMsg msg = new ChatMsg(userName, "802", cm.userName + "�� �¸�");
		msg.roomName = roomName;

		if (isOwner)
			mainView.SendObject(msg); // ���� ���Ḧ ������ �˸�
		// ���常 ������ ���� : �Ѹ� ������ �ϱ� ���ؼ�

		nowtime = 30;
		startReadyButton.setEnabled(true);
	}

	public void gameStop(ChatMsg cm) {
		time = false;
		nowtime = 30;
		gamePanel.init();
		repaint();
		AppendText("���� ��Ż�� ������ �ߴ��մϴ�.");

		startReadyButton.setEnabled(true);
		startReadyButton.setLabel(isOwner ? "����" : "�غ�");

	}
	
	public void kicked() {
		setVisible(false);
		time = false;
		JOptionPane.showMessageDialog(mainView, "���忡 ���� �������� �Ǿ����ϴ�.", "��������", JOptionPane.ERROR_MESSAGE);
		for (int i = 0; i < mainView.gameView.size(); i++) {
			if (mainView.gameView.get(i).roomName.equals(roomName)) {
				mainView.gameView.remove(i);
			}
		}
	}

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

	///////////////// ���� �г� �κ� ////////////////////

	class TablePanel1 extends JPanel {

		class MapSize {
			private final int CELL = 30;
			private final int SIZE = 20;

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

		Image Black = new ImageIcon(OmokClientMainView.class.getResource("stoneImg/Black.png")).getImage();
		Image White = new ImageIcon(OmokClientMainView.class.getResource("stoneImg/White.png")).getImage();
		Image Red = new ImageIcon(OmokClientMainView.class.getResource("stoneImg/Red.png")).getImage();

		public void init() {
			for (int i = 0; i < MaxSize + 1; i++) {
				for (int j = 0; j < MaxSize; j++) {
					Map[i][j] = 0;
				}
			}
			currentPlayer = 0;
		}

		public void setAll(int color) {
			for (int i = 0; i < MaxSize + 1; i++) {
				for (int j = 0; j < MaxSize; j++) {
					Map[i][j] = color;
				}
			}
			currentPlayer = 0;
		}

		public void removeBackMap() {
			for (int i = 0; i < MaxSize + 1; i++) {
				for (int j = 0; j < MaxSize; j++) {
					if (Map[i][j] == 999)
						Map[i][j] = 0;
				}
			}
		}

		public void setMap(int y, int x, int color) {
			if (Map[y][x] == 0 || Map[y][x] == 999)
				Map[y][x] = color;
		}

		public void setZero(int y, int x) {
			Map[y][x] = 0;
		}

		public Boolean isFilled(int y, int x) {
			if (Map[y][x] == 999) {
				return Map[y][x] != 999;
			}
			return Map[y][x] != 0;
		}

		public Boolean Three(int y, int x) {
			Map[y][x] = (currentPlayer + 1); // ( current�� 0 ~ max-1 �� ���� ���� )
			//System.out.println("33 check : Player" + (currentPlayer + 1));
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
					else if (Map[y][x] == 999)
						drawBack(g, x, y);
//						g.drawOval(x, y, 28, 28); //�� �׸���
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

		public void drawBack(Graphics g, int x, int y) { // �Ƽ��� �������� �� �׸���
//			g.drawImage(White, x * size.getCell() + 15, y * size.getCell() - 15, 28, 28, this);
			//Graphics2D g2 = (Graphics2D)g;
			((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			g.drawImage(Black, x * size.getCell() + 15, y * size.getCell() - 15, 28, 28, this);
			((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}

	}
}
