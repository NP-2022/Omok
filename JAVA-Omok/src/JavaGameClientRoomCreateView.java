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

public class JavaGameClientRoomCreateView extends JFrame {

	

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

	/**
	 * Create the frame.
	 */
	public JavaGameClientRoomCreateView(String username, String ip_addr, String port_no) {
		
		Username = username;
		Ip_addr = ip_addr;
		Port_no = port_no;
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(900, 100, 254, 321);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblGameLabel = new JLabel("����");
		lblGameLabel.setBounds(12, 39, 82, 33);
		contentPane.add(lblGameLabel);
		
		txtGame = new JTextField();
		txtGame.setText("����");
		txtGame.setHorizontalAlignment(SwingConstants.CENTER);
		txtGame.setColumns(10);
		txtGame.setBounds(101, 39, 116, 33);
		contentPane.add(txtGame);
		txtGame.setColumns(10);
		
		JLabel lblRoom = new JLabel("�� �̸�");
		lblRoom.setBounds(12, 100, 82, 33);
		contentPane.add(lblRoom);
		
		txtRoom = new JTextField();
		txtRoom.setHorizontalAlignment(SwingConstants.CENTER);
		txtRoom.setColumns(10);
		txtRoom.setBounds(101, 100, 116, 33);
		contentPane.add(txtRoom);
		txtRoom.setColumns(10);
		

		JLabel lblPersonNum = new JLabel("�ο�");
		lblPersonNum.setBounds(12, 163, 82, 33);
		contentPane.add(lblPersonNum);
		
		txtPersonNum = new JTextField();
		txtPersonNum.setHorizontalAlignment(SwingConstants.CENTER);
		txtPersonNum.setColumns(10);
		txtPersonNum.setBounds(101, 163, 116, 33);
		contentPane.add(txtPersonNum);
		txtPersonNum.setColumns(10);
		
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(12, 223, 205, 38);
		contentPane.add(btnConnect);
		Roomaction action = new Roomaction();
		btnConnect.addActionListener(action);
		txtGame.addActionListener(action);
		txtRoom.addActionListener(action);
		txtPersonNum.addActionListener(action);
		
		setVisible(true);
		
	}

	class Roomaction implements ActionListener // ����Ŭ������ �׼� �̺�Ʈ ó�� Ŭ����
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String Game = txtGame.getText().trim();
			String Room = txtRoom.getText().trim();
			String Person = txtPersonNum.getText().trim();
			JavaGameClientView2 view = new JavaGameClientView2(Username, Ip_addr, Port_no, Game, Room, Person);
			setVisible(false);
		}
	}
}

