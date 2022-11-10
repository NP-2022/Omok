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

	/**
	 * Create the frame.
	 */
	public JavaGameClientView2(String username, String ip_addr, String port_no, String Game, String Room, String Person) {
		setResizable(false);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(900, 100, 800, 634);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("°ÔÀÓ È­¸é");
		lblNewLabel.setBounds(97, 10, 574, 49);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(420, 355, 352, 185);
		contentPane.add(scrollPane);
		
		JTextPane textArea = new JTextPane();
		textArea.setFont(new Font("±¼¸²Ã¼", Font.PLAIN, 14));
		textArea.setEditable(true);
		textArea.setCaretPosition(0);
		scrollPane.setViewportView(textArea);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(494, 551, 197, 40);
		contentPane.add(textField);
		
		JLabel lblUserName = new JLabel("<dynamic>");
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setFont(new Font("±¼¸²", Font.BOLD, 14));
		lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUserName.setBackground(Color.WHITE);
		lblUserName.setBounds(203, 344, 62, 40);
		contentPane.add(lblUserName);
		
		JButton btnSend = new JButton("\uC804\uC1A1");
		btnSend.setFont(new Font("±¼¸²", Font.PLAIN, 14));
		btnSend.setBounds(703, 550, 69, 40);
		contentPane.add(btnSend);
		setVisible(true);
		//¹Ì¿Ï¼º
	}
}

