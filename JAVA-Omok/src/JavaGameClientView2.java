import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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

public class JavaGameClientView2 extends JFrame {

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
	private JTextField textField;
	private JLabel lblMouseEvent;
	private Graphics gc2 = null;
	private int pen_size = 2; // minimum 2
	// 그려진 Image를 보관하는 용도, paint() 함수에서 이용한다.
	private Image panelImage = null;
	private Graphics gc;
	private TablePanel1 panel_4;
	JPanel panel;
	
	public JavaGameClientView mainClientView;

	/**
	 * Create the frame.
	 */
	public JavaGameClientView2(JavaGameClientView mainClientView, String username, String ip_addr, String port_no, String Game, String Room,
			int PersonNum) {
		
		this.mainClientView = mainClientView;
		
		setResizable(false);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(900, 100, 1036, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(659, 328, 352, 300);
		contentPane.add(scrollPane);

		JTextPane textArea = new JTextPane();
		textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
		textArea.setEditable(true);
		textArea.setCaretPosition(0);
		scrollPane.setColumnHeaderView(textArea);

		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(659, 638, 271, 40);
		contentPane.add(textField);

		JButton btnSend = new JButton("전송");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnSend.setFont(new Font("굴림", Font.PLAIN, 14));
		btnSend.setBounds(942, 637, 69, 40);
		contentPane.add(btnSend);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(659, 107, 352, 212);
		contentPane.add(scrollPane_1);

		JLabel lblUserName_1_1 = new JLabel("유저리스트");
		lblUserName_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName_1_1.setFont(new Font("굴림", Font.BOLD, 14));
		lblUserName_1_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUserName_1_1.setBackground(Color.WHITE);
		scrollPane_1.setColumnHeaderView(lblUserName_1_1);

		JMenuItem mntmEx_1 = new JMenuItem("ex: 프사/이름/전적/관전");
		scrollPane_1.setViewportView(mntmEx_1);

		Panel panel = new Panel();
		panel.setBounds(659, 10, 84, 91);
		contentPane.add(panel);

		JLabel lblUserName_1_1_1 = new JLabel("유저 이름");
		lblUserName_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName_1_1_1.setFont(new Font("굴림", Font.BOLD, 14));
		lblUserName_1_1_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUserName_1_1_1.setBackground(Color.WHITE);
		panel.add(lblUserName_1_1_1);

		Panel panel_1 = new Panel();
		panel_1.setBounds(749, 10, 84, 91);
		contentPane.add(panel_1);

		JLabel lblUserName_1_1_1_1 = new JLabel("유저 이름");
		lblUserName_1_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName_1_1_1_1.setFont(new Font("굴림", Font.BOLD, 14));
		lblUserName_1_1_1_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUserName_1_1_1_1.setBackground(Color.WHITE);
		panel_1.add(lblUserName_1_1_1_1);

		Panel panel_2 = new Panel();
		panel_2.setBounds(839, 10, 84, 91);
		contentPane.add(panel_2);

		JLabel lblUserName_1_1_1_2 = new JLabel("유저 이름");
		lblUserName_1_1_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName_1_1_1_2.setFont(new Font("굴림", Font.BOLD, 14));
		lblUserName_1_1_1_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUserName_1_1_1_2.setBackground(Color.WHITE);
		panel_2.add(lblUserName_1_1_1_2);

		Panel panel_3 = new Panel();
		panel_3.setBounds(929, 10, 84, 91);
		contentPane.add(panel_3);

		JLabel lblUserName_1_1_1_3 = new JLabel("유저이름");
		lblUserName_1_1_1_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName_1_1_1_3.setFont(new Font("굴림", Font.BOLD, 14));
		lblUserName_1_1_1_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUserName_1_1_1_3.setBackground(Color.WHITE);
		panel_3.add(lblUserName_1_1_1_3);

		panel_4 = new TablePanel1();
		panel_4.setBounds(12, 10, 627, 628);
		panel_4.setBackground(new Color(206, 167, 61));
		panel_4.init();
		// gc1 = panel_4.getGraphics();
		contentPane.add(panel_4);
		gc = panel_4.getGraphics();
//		
//
//		// Image 영역 보관용. paint() 에서 이용한다.
//		Image defaultimg = new ImageIcon(JavaGameClientView.class.getResource("default.png")).getImage();
//		panelImage = createImage(panel_4.getWidth(), panel_4.getHeight());
//		gc2 = panelImage.getGraphics();
//		gc2.setColor(panel_4.getBackground());
//		gc2.fillRect(0,0, panel_4.getWidth(),  panel_4.getHeight());
//		gc2.setColor(Color.BLACK);
//		gc2.drawRect(0,0, panel_4.getWidth()-1,  panel_4.getHeight()-1);
//		gc2.drawImage(defaultimg,  0,  0, panel_4.getWidth(), panel_4.getHeight(), panel_4);

		Button button = new Button("시작");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button.setBounds(220, 644, 107, 27);
		contentPane.add(button);

		Button button_1 = new Button("알까기");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button_1.setBounds(343, 644, 107, 27);
		contentPane.add(button_1);

		Button button_1_1 = new Button("나가기");
		button_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button_1_1.setBounds(470, 644, 107, 27);
		contentPane.add(button_1_1);

		JLabel lblNewLabel = new JLabel("남은 시간 : 30");
		lblNewLabel.setBounds(90, 643, 107, 28);
		contentPane.add(lblNewLabel);

		MyMouseEvent mouse = new MyMouseEvent();
		// panel_4.addMouseMotionListener(mouse);
		panel_4.addMouseListener(mouse);
		setVisible(true);

	}

	class MyMouseEvent implements MouseListener, MouseMotionListener {
		int x;
		int y;

		@Override
		public void mouseDragged(MouseEvent e) {
			// lblMouseEvent.setText(e.getButton() + " mouseDragged " + e.getX() + "," +
			// e.getY());// 좌표출력가능
//			Color c = new Color(0,0,255);
//			gc2.setColor(c);
//			gc2.fillOval(e.getX()-pen_size/2, e.getY()-pen_size/2, pen_size, pen_size);
//			// panelImnage는 paint()에서 이용한다.
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

			panel_4.setMap(y, x, 1);
			if (panel_4.Three(y, x)) {
				panel_4.setZero(y, x);
			}

			// panel_4.drawStone(gc);
			if (panel_4.Rule(y, x)) {
				panel_4.init();
				panel_4.repaint();
			} else
				panel_4.repaint();
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
			// 드래그중 멈출시 보임
		}
	}
}

class TablePanel1 extends JPanel {
	private MapSize size = new MapSize();
	private final int STONE_SIZE = 28;
	private int MaxSize = 20;
	private int Map[][] = new int[MaxSize + 1][MaxSize];

	Image Black = new ImageIcon(JavaGameClientView.class.getResource("Black.png")).getImage();
	Image White = new ImageIcon(JavaGameClientView.class.getResource("White.png")).getImage();
	Image Red = new ImageIcon(JavaGameClientView.class.getResource("Red.png")).getImage();
	Image Custom = new ImageIcon(JavaGameClientView.class.getResource("Custom.png")).getImage();

	public void init() {
		for (int i = 0; i < MaxSize; i++) {
			for (int j = 0; j < MaxSize; j++) {
				Map[i][j] = 0;
			}
		}
	}

	public void setMap(int y, int x, int color) {
		if (Map[y][x] == 0)
			Map[y][x] = color;
	}

	public void setZero(int y, int x) {
		Map[y][x] = 0;
	}

	public Boolean Three(int y, int x) {
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
		// g.setColor(Color.RED);
		// g.fillOval(x*size.getCell()+15, y*size.getCell()-15, STONE_SIZE, STONE_SIZE);
		g.drawImage(Red, x * size.getCell() + 15, y * size.getCell() - 15, 28, 28, this);
	}

	public void drawBlack(Graphics g, int x, int y) {
		// g.setColor(Color.BLACK);
		// g.fillOval(x*size.getCell()+15, y*size.getCell()-15, STONE_SIZE, STONE_SIZE);
		g.drawImage(Custom, x * size.getCell() + 15, y * size.getCell() - 15, 28, 28, this);
	}

	public void drawWhite(Graphics g, int x, int y) {
		// g.setColor(Color.WHITE);
		// g.fillOval(x*size.getCell()+15, y*size.getCell()-15, STONE_SIZE, STONE_SIZE);
		g.drawImage(White, x * size.getCell() + 15, y * size.getCell() - 15, 28, 28, this);
	}

}
