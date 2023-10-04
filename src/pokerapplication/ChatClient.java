package pokerapplication;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


public class ChatClient extends JInternalFrame implements Runnable {

	private JMenuBar menuBar;
	private JMenuItem Connect;
	private	 JMenuItem Exit;
	private JMenu File;
	private JTextField inputField;
	private JTextArea chatArea;
	private static int WIDTH = 400;
	private static int HEIGHT = 300;
	private DataInputStream read;
	private DataOutputStream write;
	private Socket socket;
	private String name; 
	
	public ChatClient(String name) { 
		super("Chat");
		this.name = name;
//		menuBar = new JMenuBar();
//		Connect = new JMenuItem("Connect");
//		Exit = new JMenuItem("Exit");
//		File = new JMenu("File");
		inputField = new JTextField();
		chatArea = new JTextArea(10, 30);
		chatArea.setEditable(false);
		 Color color = Color.decode("#FFFEFA");
		chatArea.setBackground(color);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
        chatArea.setBorder(border);
		JScrollPane scrollPane = new JScrollPane(chatArea);
		scrollPane.setEnabled(false);

	
			try {
				this.connect();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		
//		Exit.addActionListener((e) -> System.exit(0));
		chatArea.setEditable(true);
		chatArea.append("Welcome\n");
		inputField.setSize(400, 50);
		inputField.setColumns(30);
		inputField.setEditable(true);
		inputField.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
					String message = inputField.getText();
					chatArea.append("You: " + message + "\n");
					inputField.setText("");
					send(message);
				}
			}
		});
//		File.add(Connect);
//		File.add(Exit);
//		menuBar.add(File);

		getContentPane().add(scrollPane);
		getContentPane().add(inputField, BorderLayout.SOUTH);
		this.setJMenuBar(menuBar);

		this.setSize(ChatClient.WIDTH, ChatClient.HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		Thread thread = new Thread(this);
		thread.start();
	}


	public void connect() throws IOException {
		if (socket != null && socket.isConnected()) {
			chatArea.append("Already connected to server.\n");
			return;
		}
//		chatArea.append("Connecting to server...\n");
		socket = new Socket("localhost", 9898);
		read = new DataInputStream(socket.getInputStream());
		write = new DataOutputStream(socket.getOutputStream());
		if (socket.isConnected()) {
//			chatArea.append("Connected.\n");
		}
	}

	public void send(String message) {
		try {
			write.writeUTF(name+": "+message);
			System.out.print("Hello SEND FUNCTION"+message);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void run() {
		System.out.print("INSIDE RUN");
		while (true) {
			if (socket == null || !socket.isConnected()) {
				System.out.print("SOCKET IS NULL");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				continue;
			}
			try {
				String message = read.readUTF();
				System.out.print(message+"INSIDE READ RUN");
				chatArea.append(message + "\n");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
//	public static void main(String[] args) {
//		ChatClient chatClient = new ChatClient();
//		chatClient.run();
//	}
}