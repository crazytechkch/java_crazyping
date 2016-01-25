package com.swopt.swoptping;

import java.awt.EventQueue;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.BoxLayout;

public class Main extends JFrame {
	
	private JTextField txtIpAddress;
	
	public Main() throws HeadlessException {
		super();
		init();
	}
	
	private void init(){
		getContentPane().setLayout(new BorderLayout(0, 0));
		setSize(300, 500);
		
		JPanel panel = new JPanel();
		setTitle("Swopt Ping");
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JButton btnAddServer = new JButton("ADD SERVER");
		panel_1.add(btnAddServer, BorderLayout.EAST);
		
		txtIpAddress = new JTextField();
		panel_1.add(txtIpAddress, BorderLayout.CENTER);
		txtIpAddress.setColumns(10);
		txtIpAddress.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btnAddServer.doClick();
			}
		});
		btnAddServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				repopulatePanel(panel,txtIpAddress.getText());
			}
		});
		repopulatePanel(panel,"192.168.0.150");
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void repopulatePanel(JPanel panel,String ipHost) {
		ServerItemPanel sip = new ServerItemPanel(ipHost, 1);
		int pos = panel.getComponentCount();
		panel.add(sip,pos);
		sip = (ServerItemPanel)panel.getComponent(pos);
		sip.getBtnX().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.remove(pos);
				panel.repaint();
			}
		});
		panel.add(sip,pos);
		panel.revalidate();
	}
	
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
}
