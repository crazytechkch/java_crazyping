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
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import net.miginfocom.swing.MigLayout;
import res.locale.LangMan;

import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import javax.swing.SwingConstants;

public class ServerItemPanel extends JPanel {
	private JButton btnX;
	private String server,prevStatus;
	private JLabel statusLabel;
	private JPanel parent;
	private int posInParent;
	private Thread pingThread;
	private final Color COLOR_SUCCESS = Color.decode("#098c10");
	private final Color COLOR_ERROR = Color.RED;
	private final Color COLOR_WARN = Color.decode("#ff5f0f");
	//private RectDraw rect;
	
	public ServerItemPanel(JPanel parent, String server, int status) {
		this.server = server;
		this.parent = parent;
		Dimension dimen = new Dimension(40, 40);
		
		//rect = new RectDraw(Color.ORANGE);
		revalidate();
		prevStatus = "PINGING";
		
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		panel.setBackground(COLOR_WARN);
		//panel.add(rect,BorderLayout.CENTER);
		
		statusLabel = new JLabel(label(server, "PINGING"));
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statusLabel.setForeground(Color.WHITE);
		panel.add(statusLabel, BorderLayout.CENTER);
		pingThread = new Thread(pingRunnable(panel));
		btnX = new JButton();
		btnX.setBackground(COLOR_WARN);
		btnX.setToolTipText(server);
		btnX.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				confirmDelete(pingThread);
			}
		});
		setLayout(new BorderLayout(0, 0));
		btnX.add(panel);
		add(btnX);
		//panel.add(btnX, "cell 0 0,alignx right,aligny center");
		pingThread.start();
	}
	
	/*public void setImagePanel(JPanel panel, Color color) {
		panel.remove(rect);
		rect = new RectDraw(color);
		panel.add(rect,BorderLayout.CENTER, 0);
		panel.revalidate();
	}*/

	public JButton getBtnX() {
		return btnX;
	}
	
	private Runnable pingRunnable(JPanel panel) {
		return new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					try {
						ping(panel, server);
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
	}
	
	private void ping(JPanel panel, String ipHost){
		String line = "";
		try {
			Process process = Runtime.getRuntime().exec("ping "+ipHost+" -n 1");
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			int pings = 0;
			int timeouts = 0;
			String time = "";
            while((line = streamReader.readLine()) != null) {
                //print each line that is read
                //System.out.println(line);
                //check if line starts with "Reply from"
                if(line.indexOf("Packets: Sent = 1, Received = 1, Lost = 0 (0% loss)")!=-1) {
                    //This is a positive response so we increment pings
                	
                    pings++;
                } else {
                    //This is a negative response
                	timeouts++;
                }
                if(line.indexOf("time")!=-1&&line.indexOf("ms")!=-1){
                	time = line.substring(line.indexOf("time")+4, line.indexOf("ms ")+3);
                	System.out.println(time);
                }
                System.out.println(line);
            }
            if(pings > 0) changeStatus(panel, time, COLOR_SUCCESS);
            else throw new UnknownHostException();
		} catch (UnknownHostException e) {
			changeStatus(panel, "TIMEOUT", COLOR_ERROR);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void changeStatus(JPanel panel, String status, Color color){
		statusLabel.setText(label(server, status));
		/*if(!statusLabel.getText().equals(prevStatus)){
			statusLabel.setForeground(color);
			setImagePanel(panel, color);
		}*/
		panel.setBackground(color);
		btnX.setBackground(color);
		prevStatus = statusLabel.getText();
	}
	
	private String label(String server, String status) {
		String serv = server;
		if (server.length()>9)serv = server.substring(0, 2)+"…"+server.substring(server.length()-3);
		String text = "<span style=\"font-size:15\">"+serv+"</span><br/>"
				+ "<span style='font-size:10pt'>"+status.replace("=", "")+"</span>";
		return "<html><div style='text-align:center'>"+text+"</div></html>";
	}
	
	private void confirmDelete(Thread thread) {
		LangMan lang = new LangMan(getLocale());
		int option = JOptionPane.showConfirmDialog(this, lang.getString("confirm_delete"), Main.APPNAME, JOptionPane.OK_CANCEL_OPTION);
		switch (option) {
		case 0:
			thread.stop();
			parent.remove(posInParent);
			parent.repaint();
			break;
		default: break;
		}
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
	
	public String getServer() {
		return server;
	}

	public int getPosInParent() {
		return posInParent;
	}

	public void setPosInParent(int posInParent) {
		this.posInParent = posInParent;
	}

	public Thread getPingThread() {
		return pingThread;
	}

	public void setPingThread(Thread pingThread) {
		this.pingThread = pingThread;
	}
	
	
}
