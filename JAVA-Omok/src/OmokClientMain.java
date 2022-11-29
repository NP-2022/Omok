// JavaObjClient.java
// ObjecStream 사용하는 채팅 Client

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OmokClientMain extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField userNameTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OmokClientMain frame = new OmokClientMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public OmokClientMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 254, 321);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel userNameLabel = new JLabel("ID");
		userNameLabel.setBounds(35, 80, 19, 33);
		contentPane.add(userNameLabel);
		
		userNameTextField = new JTextField();
		userNameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		userNameTextField.setBounds(84, 80, 144, 33);
		contentPane.add(userNameTextField);
		userNameTextField.setColumns(10);
		
		MyAction action = new MyAction();
		
		JButton loginButton = new JButton("로그인");
		loginButton.setBounds(51, 166, 144, 33);
		contentPane.add(loginButton);
		loginButton.addActionListener(action);
		userNameTextField.addActionListener(action);
	}
	class MyAction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = userNameTextField.getText().trim();
			String ip_addr = "127.0.0.1";
			String port_no = "30000";
			OmokClientMainView view = new OmokClientMainView(username, ip_addr, port_no);
			setVisible(false);
		}
	}
}


