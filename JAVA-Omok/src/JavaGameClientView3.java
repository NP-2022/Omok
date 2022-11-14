import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JMenuBar;
import java.awt.ScrollPane;
import javax.swing.JMenuItem;
import java.awt.Panel;
import java.awt.Button;

public class JavaGameClientView3 extends JFrame {


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

	/**
	 * Create the frame.
	 */
	public JavaGameClientView3(String username, String ip_addr, String port_no, String Game, String Room, int PersonNum) {
		setResizable(false);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		TablePanel panel_4 = new TablePanel();
		panel_4.setBounds(12, 10, 627, 628);
		panel_4.setBackground(new Color(206,167,61));
		contentPane.add(panel_4);
		
		Button button = new Button("시작");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button.setBounds(220, 644, 107, 27);
		contentPane.add(button);
		
		Button button_1 = new Button("무르기 요청");
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
		
		
		setVisible(true);

	}
}

class TablePanel extends JPanel {
	private MapSize size = new MapSize();
	
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i=1;i<=size.getSize();i++){
			g.drawLine(size.getCell(), i*size.getCell(), size.getCell()*size.getSize(), i*size.getCell()); 
			g.drawLine(i*size.getCell(), size.getCell(), i*size.getCell() , size.getCell()*size.getSize()); 
		}
    }
}

