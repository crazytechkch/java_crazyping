package com.swopt.swoptping;

import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import net.miginfocom.swing.MigLayout;

public class ServerItemPanel extends JPanel {
	private JButton btnX;
	private String server,prevStatus;
	private JLabel ipLabel,statusLabel;
	private final Color COLOR_SUCCESS = Color.decode("#098c10");
	private final Color COLOR_ERROR = Color.RED;
	private RectDraw rect;
	
	public ServerItemPanel(String server, int status) {
		this.server = server;
		Dimension dimen = new Dimension(2000, 30);
		setMaximumSize(dimen);
		setLayout(new BorderLayout(0, 0));
		setBorder(new EmptyBorder(0, 0, 3, 0));
		
		rect = new RectDraw(Color.ORANGE);
		add(rect,BorderLayout.WEST, 0);
		revalidate();
		Thread pingThread = new Thread(pingRunnable());
		
		btnX = new JButton("x");
		btnX.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pingThread.stop();
			}
		});
		add(btnX, BorderLayout.EAST);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[50%][50%]", "[100%]"));
		ipLabel = new JLabel(server);
		panel.add(ipLabel, "cell 0 0,alignx left,aligny center");
		
		statusLabel = new JLabel("PINGING");
		statusLabel.setForeground(Color.ORANGE);
		prevStatus = "PINGING";
		panel.add(statusLabel, "cell 1 0,alignx left,aligny center");
		pingThread.start();
	}
	
	public void setImagePanel(Color color) {
		remove(rect);
		rect = new RectDraw(color);
		add(rect,BorderLayout.WEST, 0);
		revalidate();
	}

	public JButton getBtnX() {
		return btnX;
	}
	
	private Runnable pingRunnable() {
		return new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					try {
						ping(server);
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
	}
	
	private void ping(String ipHost){
		String line = "";
		try {
			Process process = Runtime.getRuntime().exec("ping "+ipHost+" -n 1");
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			int pings = 0;
			int timeouts = 0;
            while((line = streamReader.readLine()) != null) {
                //print each line that is read
                //System.out.println(line);
                //check if line starts with "Reply from"
                if(line.indexOf("time")!=-1) {
                    //This is a positive response so we increment pings
                    pings++;
                } else {
                    //This is a negative response
                	timeouts++;
                }
            }
            if(pings > 0) changeStatus("SUCCESS", COLOR_SUCCESS);
            else throw new UnknownHostException();
		} catch (UnknownHostException e) {
			changeStatus("TIMEOUT", COLOR_ERROR);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void changeStatus(String status, Color color){
		statusLabel.setText(status);
		if(!statusLabel.getText().equals(prevStatus)){
			statusLabel.setForeground(color);
			setImagePanel(color);
		}
		prevStatus = statusLabel.getText();
	}
	
	class RectDraw extends JPanel {
		private Color color;
		private Graphics g;
		public RectDraw(Color color) {
			super();
			this.color = color;
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(color);
			g.fillOval(0, 0, 24, 24);
			this.g = g;
		}
		
		
		public Dimension getPreferredSize() {
			return new Dimension(30, 30); // appropriate constants
		}

		public Graphics getG() {
			return g;
		}

		public void setG(Graphics g) {
			this.g = g;
		}
		
		
	}



	public JLabel getIpLabel() {
		return ipLabel;
	}

	public void setIpLabel(JLabel ipLabel) {
		this.ipLabel = ipLabel;
	}
}
