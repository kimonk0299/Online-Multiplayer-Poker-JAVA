package pokerapplication;
import java.io.*;
import java.net.*;

import javax.swing.*;

import java.awt.*;
import java.util.Date;

public class PokerServer extends JFrame implements PokerConstant{
	
	 
	 private ServerSocket serverSocket;
	 private Socket player1;
	 private Socket player2;
	 ObjectOutputStream toClient;
	 ObjectOutputStream toClient2;

	 
	 Deck d = new Deck();
	 Card[] player1Hand = new Card[2];
	 Card[] player2Hand = new Card[2];
	 Card[] flopHand = new Card[5];
	 private GameSession game;
	 

	 public PokerServer(){
		 ChatServer cs= new ChatServer();
		 Counter.reset();
		 JTextArea serverLog = new JTextArea();
		 serverLog.setEditable(false);
		 JScrollPane scrollPane = new JScrollPane(serverLog);
		 
		 JTextArea playerHandLog = new JTextArea();
		 playerHandLog.setEditable(false);
		 playerHandLog.setRows(10);
		 playerHandLog.setText("Players Hands Log shown here: \n");
		 JScrollPane scrollPaneHand = new JScrollPane(playerHandLog);
		 
		 add(scrollPane, BorderLayout.CENTER);
		 add(scrollPaneHand, BorderLayout.PAGE_END);
		 
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 setSize(300, 400);
		 setResizable(false);
		 setTitle("My poker Server");
		 setVisible(true);
		 
		 try{
			 
			 int port = 8000;
			 serverSocket = new ServerSocket(port);
			 
			 serverLog.append(new Date() + ": Server started at: " + port+ "\n");
			 
			 

				 serverLog.append(new Date() + ": Wait for players to join session \n");
				 

				 player1 = serverSocket.accept();
				 serverLog.append(new Date() + ": Player1 joined. \n");
				 serverLog.append(new Date() + ": Player1 IP address is: " + player1.getInetAddress().getHostAddress()+ "\n");
				 toClient = new ObjectOutputStream(player1.getOutputStream());
				 toClient.writeInt(PLAYER1);
				 toClient.flush();
				 						 

				 player2 = serverSocket.accept();
				 serverLog.append(new Date() +": Player2 joined \n");
				 serverLog.append(new Date() +": Player2 IP address is: "+ player2.getInetAddress().getHostAddress()+ "\n");
				 toClient2 = new ObjectOutputStream(player2.getOutputStream());
				 toClient2.writeInt(PLAYER2);
				 toClient2.flush();
				     		
                game = new GameSession(player1, player2, toClient, toClient2, playerHandLog);
                	

	            new Thread(game).start();
	             
	            serverLog.append(new Date() + ": Game Session started. ");
			 //}
		 }catch(IOException ex){
			 System.err.println(ex);
		 }
	 }
	

	public static void main(String[] args) {
		PokerServer serverSide = new PokerServer();
		
	}

}
