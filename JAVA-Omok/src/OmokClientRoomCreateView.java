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

public class OmokClientRoomCreateView extends JFrame {

	

	/**
	 * Launch the application.
	 */
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox gameModeComboBox;
	private JComboBox maxPlayerComboBox;
	private JTextField roomNameTextField;
	private JLabel roomNameLabel;
	private String Username;
	private String Ip_addr;
	private String Port_no;
	
	OmokClientMainView mainView; // ois와 oos를 생성한 최초 Class에서 처리

	/**
	 * Create the frame.
	 */
	public OmokClientRoomCreateView(OmokClientMainView mainView, String username, String ip_addr, String port_no) {
		
		this.mainView = mainView;
		
		Username = username;
		Ip_addr = ip_addr;
		Port_no = port_no;
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(900, 100, 254, 321);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel gameModeLabel = new JLabel("게임");
		gameModeLabel.setBounds(12, 39, 82, 33);
		contentPane.add(gameModeLabel);
		
		String[] gameModes = {"오목", "알까기"};
		gameModeComboBox = new JComboBox(gameModes);
		gameModeComboBox.setBounds(101, 39, 116, 33);
		contentPane.add(gameModeComboBox);
		
		roomNameLabel = new JLabel("방 이름");
		roomNameLabel.setBounds(12, 100, 82, 33);
		contentPane.add(roomNameLabel);
		
		roomNameTextField = new JTextField();
		roomNameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		roomNameTextField.setColumns(10);
		roomNameTextField.setBounds(101, 100, 116, 33);
		contentPane.add(roomNameTextField);
		roomNameTextField.setColumns(10);
		

		JLabel maxPlayerLabel = new JLabel("인원");
		maxPlayerLabel.setBounds(12, 163, 82, 33);
		contentPane.add(maxPlayerLabel);
		
		String[] maxPlayers = {"2", "3", "4"};
		maxPlayerComboBox = new JComboBox(maxPlayers);
		maxPlayerComboBox.setBounds(101, 169, 47, 22);
		contentPane.add(maxPlayerComboBox);
		
		JButton createButton = new JButton("방 생성");
		createButton.setBounds(12, 223, 205, 38);
		contentPane.add(createButton);
		
		
		RoomAction action = new RoomAction();
		createButton.addActionListener(action);
		setVisible(true);
		
	}

	class RoomAction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String gameModeName = gameModeComboBox.getSelectedItem().toString();
			String roomName = roomNameTextField.getText().trim();
			int maxPlayer = Integer.parseInt(maxPlayerComboBox.getSelectedItem().toString());
			
			OmokClientGameView view = new OmokClientGameView(mainView, Username, Ip_addr, Port_no, gameModeName, roomName, maxPlayer, true);
			mainView.gameView.add(view);
			setVisible(false);
			
			ChatMsg msg = new ChatMsg(Username, "600", gameModeName);
			msg.roomMax = maxPlayer;
			msg.roomName = roomNameTextField.getText();
			msg.gameMode = gameModeName;
			mainView.SendObject(msg);
		}
	}
}