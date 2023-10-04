package pokerapplication;


import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class ChatServer extends JFrame implements Runnable {

	JTextArea textArea;
	private static int WIDTH = 400;
	private static int HEIGHT = 300;

	int port = 9898;
	String host = "serverHost";
	ServerSocket serverSocket;
	Socket socket;
	HashMap<Integer, Socket> clients;
	
	public ChatServer() {
		super("Chat Server");
		textArea = new JTextArea(10, 30);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);

		this.add(scrollPane);

		this.setSize(ChatServer.WIDTH, ChatServer.HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createMenu();
		this.setVisible(true);
		Thread thread = new Thread(this);
		thread.start();
		
	}
	
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener((e) -> System.exit(0));
		menu.add(exitItem);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
	

//	public static void main(String[] args) {
//		ChatServer chatServer = new ChatServer();
//		chatServer.run();
//	}

	@Override
	public void run() throws RuntimeException {
		clients = new HashMap<>();
		int noOfClients;
		try {
			serverSocket = new ServerSocket(port);
			textArea.append("Server started at " + new java.util.Date() + "\n");
			while (true) {
				// Listen for a new connection request
				socket = serverSocket.accept();
				ClientHandler thread = new ClientHandler(socket, clients);
				thread.start();
				noOfClients = thread.getMyCount();
				clients.put(noOfClients, socket);
				InetAddress inetAddress = socket.getInetAddress();
				int port = socket.getPort();
				textArea.append("Client " + noOfClients + "'s host name is " + inetAddress.getHostName() + "\n");
				textArea.append("Client " + noOfClients + "'s IP Address and port is " + inetAddress.getHostAddress() + ":" + port + "\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}}

