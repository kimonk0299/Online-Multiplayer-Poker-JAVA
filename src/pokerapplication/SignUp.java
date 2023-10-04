package pokerapplication;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class SignUp extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField passwordField_1;
	private JPasswordField passwordField;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignUp frame = new SignUp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public SignUp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 600);
		contentPane = new JPanel();
		Color color = Color.decode("#FF6961");
		contentPane.setBackground(color);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Enter new username");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(85, 102, 179, 47);
		contentPane.add(lblNewLabel);
		
		JLabel lblEnterPassword = new JLabel("Enter Password");
		lblEnterPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEnterPassword.setBounds(85, 181, 179, 47);
		contentPane.add(lblEnterPassword);
		
		JLabel lblConfirmPassword = new JLabel("Confirm Password");
		lblConfirmPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblConfirmPassword.setBounds(85, 264, 179, 47);
		contentPane.add(lblConfirmPassword);
		
		textField = new JTextField();
		textField.setBounds(330, 111, 130, 32);
		contentPane.add(textField);
		textField.setColumns(10);
		
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(330, 190, 130, 32);
		contentPane.add(passwordField_1);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(330, 273, 130, 32);
		contentPane.add(passwordField);
		
		JLabel lblSignUp = new JLabel("Sign Up");
		lblSignUp.setHorizontalAlignment(SwingConstants.CENTER);
		lblSignUp.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblSignUp.setBounds(243, 26, 179, 47);
		contentPane.add(lblSignUp);
		
		JButton btnNewButton = new JButton("Register");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/pokergame","root","");
					Statement stmt=con.createStatement();
					String sql="insert into userlist values('"+textField.getText()+"','"+passwordField_1.getText().toString()+"')";
					PreparedStatement ps=con.prepareStatement(sql);
					if(passwordField_1.getText().toString().equals(passwordField.getText().toString()) && textField.getText()!=null && passwordField.getText()!=null ) {
						int affectedRows = ps.executeUpdate();
					    if(affectedRows > 0) {
					        JOptionPane.showMessageDialog(null,"User inserted successfully");
					        LoginFrame lg= new LoginFrame();
					        lg.setVisible(true);
					        dispose();
					    } else {
					        JOptionPane.showMessageDialog(null,"User insertion failed");
					    }
						
					}
					else{
						JOptionPane.showMessageDialog(null,"Both passwords don't match");
					}
					
					con.close();
					
				} catch(Exception e1) {
					System.out.println(e1);
					JOptionPane.showMessageDialog(null,"Username already exists");
				}
			}
		});
		btnNewButton.setBounds(278, 380, 89, 23);
		contentPane.add(btnNewButton);
	}

}
