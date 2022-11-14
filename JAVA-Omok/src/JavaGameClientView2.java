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
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JComboBox;

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
	public JavaGameClientView2(String username, String ip_addr, String port_no, String Game, String Room, int Person) {
		setResizable(false);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(900, 100, 950, 645);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("°ÔÀÓ È­¸é");
		lblNewLabel.setBounds(97, 10, 574, 49);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(624, 352, 298, 185);
		contentPane.add(scrollPane);
		
		JTextPane textArea = new JTextPane();
		textArea.setFont(new Font("±¼¸²Ã¼", Font.PLAIN, 14));
		textArea.setEditable(true);
		textArea.setCaretPosition(0);
		scrollPane.setViewportView(textArea);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(674, 547, 167, 40);
		contentPane.add(textField);
		
		JLabel lblUserName = new JLabel("<dynamic>");
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setFont(new Font("±¼¸²", Font.BOLD, 14));
		lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUserName.setBackground(Color.WHITE);
		lblUserName.setBounds(624, 546, 38, 40);
		contentPane.add(lblUserName);
		
		JButton btnSend = new JButton("\uC804\uC1A1");
		btnSend.setFont(new Font("±¼¸²", Font.PLAIN, 14));
		btnSend.setBounds(853, 550, 69, 40);
		contentPane.add(btnSend);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(624, 69, 298, 273);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(141, 115, 75));
		panel_1.setBounds(12, 69, 500, 500);
		contentPane.add(panel_1);
		setVisible(true);
		//¹Ì¿Ï¼º
	}
}

