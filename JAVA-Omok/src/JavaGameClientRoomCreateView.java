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
import javax.swing.JComboBox;
import javax.swing.JSpinner;

public class JavaGameClientRoomCreateView extends JFrame {

	

	/**
	 * Launch the application.
	 */
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox gameModeComboBox;
	private JComboBox maxPlayerComboBox;
	private JTextField txtRoom;
	private JLabel lblRoom;
	private String Username;
	private String Ip_addr;
	private String Port_no;
	
	JavaGameClientView mainClientView; // ois와 oos를 생성한 최초 Class에서 처리

	/**
	 * Create the frame.
	 */
	public JavaGameClientRoomCreateView(JavaGameClientView mainClientView, String username, String ip_addr, String port_no) {
		
		this.mainClientView = mainClientView;
		
		Username = username;
		Ip_addr = ip_addr;
		Port_no = port_no;
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(900, 100, 254, 321);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblGameLabel = new JLabel("게임");
		lblGameLabel.setBounds(12, 39, 82, 33);
		contentPane.add(lblGameLabel);
		
		String[] gameModes = {"오목", "알까기"};
		gameModeComboBox = new JComboBox(gameModes);
		gameModeComboBox.setBounds(101, 39, 116, 33);
		contentPane.add(gameModeComboBox);
		
		lblRoom = new JLabel("방 이름");
		lblRoom.setBounds(12, 100, 82, 33);
		contentPane.add(lblRoom);
		
		txtRoom = new JTextField();
		txtRoom.setHorizontalAlignment(SwingConstants.CENTER);
		txtRoom.setColumns(10);
		txtRoom.setBounds(101, 100, 116, 33);
		contentPane.add(txtRoom);
		txtRoom.setColumns(10);
		

		JLabel lblPersonNum = new JLabel("인원");
		lblPersonNum.setBounds(12, 163, 82, 33);
		contentPane.add(lblPersonNum);
		
		String[] maxPlayers = {"2", "3", "4"};
		maxPlayerComboBox = new JComboBox(maxPlayers);
		maxPlayerComboBox.setBounds(101, 169, 47, 22);
		contentPane.add(maxPlayerComboBox);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(12, 223, 205, 38);
		contentPane.add(btnConnect);
		
		
		Roomaction action = new Roomaction();
		btnConnect.addActionListener(action);
		setVisible(true);
		
	}

	class Roomaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String Game = gameModeComboBox.getSelectedItem().toString();
			String Room = txtRoom.getText().trim();
			int maxPlayer = Integer.parseInt(maxPlayerComboBox.getSelectedItem().toString());
			
			JavaGameClientView2 view = new JavaGameClientView2(mainClientView, Username, Ip_addr, Port_no, Game, Room, maxPlayer);
			mainClientView.gameClientView.add(view);
			setVisible(false);
			
			ChatMsg msg = new ChatMsg(Username, "600", Game);
			msg.roomMax = maxPlayer;
			msg.roomName = txtRoom.getText();
			msg.gameMode = Game;
			mainClientView.SendObject(msg);
		}
	}
}