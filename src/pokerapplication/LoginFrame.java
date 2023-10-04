package pokerapplication;

import java.awt.EventQueue;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class LoginFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public LoginFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 701, 600);
		contentPane = new JPanel();
		Color color = Color.decode("#FF6961");
		contentPane.setBackground(color);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblUsername.setBounds(237, 121, 109, 33);
		contentPane.add(lblUsername);
		
		JLabel lblPasword = new JLabel("Password");
		lblPasword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPasword.setBounds(237, 207, 109, 33);
		contentPane.add(lblPasword);
		
		JLabel lblNewLabel_2 = new JLabel("POKER : TEXAS HOLD'EM");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(161, 37, 328, 58);
		contentPane.add(lblNewLabel_2);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.setBounds(232, 161, 177, 35);
		contentPane.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(232, 249, 177, 35);
		contentPane.add(passwordField);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/pokergame","root","");
					Statement stmt=con.createStatement();
					String sql="Select * from userlist where username='"+textField.getText()+"' and password='"+passwordField.getText().toString()+"'";
					ResultSet rs= stmt.executeQuery(sql);
					if(rs.next()) {
						
						//new USER session
						//user name populate
						
						PokerClient client = new PokerClient("Poker client",textField.getText());
						client.setSize(950, 600);
						client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						client.setVisible(true);
						client.setResizable(false);
						dispose();
					}
					else {
						JOptionPane.showMessageDialog(null,"Incorrect credentials");
					}
					con.close();
					
				} catch(Exception e1) {
					System.out.println(e1);
					
				}
			}
		});
		btnLogin.setBounds(193, 346, 109, 33);
		contentPane.add(btnLogin);
		
		JButton btnSign = new JButton("Sign In");
		btnSign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SignUp signUpFrame = new SignUp();
		        signUpFrame.setVisible(true);
		        dispose();
			}
		});
		btnSign.setBounds(343, 347, 115, 31);
		contentPane.add(btnSign);
	}
}
