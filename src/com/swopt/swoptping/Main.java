package com.swopt.swoptping;

import java.awt.EventQueue;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.WindowConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.crazytech.io.IOUtil;
import com.swopt.swoptping.config.AppConfig;
import res.locale.LangMan;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

public class Main extends JFrame {
	
	private JTextField txtIpAddress;
	private AppConfig config;
	private LangMan lang;
	private JPanel panel;
	private JScrollPane scrollPane;
	
	public Main() throws HeadlessException {
		super();
		loadConfig();
		init();
	}
	
	private void loadConfig(){
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(AppConfig.class);
			if(new File("SwoptPing.dat").exists()){
					Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
					String xmlStr = IOUtil.readFile("SwoptPing.dat");
					StringReader reader = new StringReader(xmlStr);
					config = (AppConfig) unmarshaller.unmarshal(reader);
			} else {
				Marshaller marshaller = jaxbContext.createMarshaller();
				config = new AppConfig();
				config.setLocale("en");;
				config.setRstaTheme("Default");
				List<String> hosts = new ArrayList<String>();
				hosts.add("127.0.0.1");
				config.setHosts(hosts);
				marshaller.marshal(config, new File("SwoptPing.dat"));
			}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void init(){
		lang = new LangMan(getLocale());
		setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/logo.png")));
		setSize(330, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("Swopt Ping");
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1,BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JButton btnAddServer = new JButton("ADD");
		panel_1.add(btnAddServer, BorderLayout.EAST);
		
		txtIpAddress = new JTextField();
		txtIpAddress.setText("Enter IP or Hostname");
		txtIpAddress.setToolTipText("Enter IP or Hostname");
		panel_1.add(txtIpAddress, BorderLayout.CENTER);
		txtIpAddress.setColumns(10);
		
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setPreferredSize(new Dimension(300, 5000));
		txtIpAddress.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btnAddServer.doClick();
				txtIpAddress.selectAll();
			}
		});
		txtIpAddress.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				txtIpAddress.selectAll();
			}
		});
		btnAddServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!txtIpAddress.getText().contains("Enter IP or Hostname")) {
					repopulatePanel(panel,txtIpAddress.getText());
				}
			}
		});
		for (String host : config.getHosts()) {
			repopulatePanel(panel,host);
		}
		scrollPane = new JScrollPane(panel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(scrollPane,BorderLayout.CENTER);
		
		
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
				confirmExit();
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
	
	public static int optionDialog(Component component, String msg, String title) {
		return JOptionPane.showConfirmDialog(component, msg, title, JOptionPane.OK_CANCEL_OPTION);
	}
	
	private void confirmExit(){
		switch (optionDialog(getContentPane(), lang.getString("confirm_exit"), lang.getString("exit"))) {
		case 0:
			List<String> hosts = new ArrayList<String>();
			for (int i = 0; i < panel.getComponentCount(); i++) {
				ServerItemPanel sip= (ServerItemPanel)panel.getComponent(i);
				hosts.add(sip.getIpLabel().getText());
			}
			config.setHosts(hosts);
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(AppConfig.class);
				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				marshaller.marshal(config, new File("SwoptPing.dat"));
			} catch (JAXBException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(0);
			break;

		default:
			break;
		}
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
