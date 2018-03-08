package com.swopt.swoptping;

import java.awt.EventQueue;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.WindowConstants;import javax.swing.border.Border;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.crazytech.io.IOUtil;
import com.swopt.swoptping.config.AppConfig;
import res.locale.LangMan;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import net.miginfocom.swing.MigLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import javax.swing.border.LineBorder;

public class Main extends JFrame {
	
	private JTextField txtIpAddress;
	private AppConfig config;
	private LangMan lang;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JCheckBoxMenuItem alwaysOnTop;
	public static final String APPNAME = "SwoptPing";
	
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
				config.setAlwaysOnTop(0);
				config.setWidth(300);
				config.setHeight(480);
				List<String> hosts = new ArrayList<String>();
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
		lang = new LangMan(new Locale(config.getLocale()));
		setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/pingicon.png")));
		setSize(config.getWidth()!=null?config.getWidth():640, config.getHeight()!=null?config.getHeight():480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle(APPNAME);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1,BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JButton btnAddServer = new JButton(lang.getString("add"));
		panel_1.add(btnAddServer, BorderLayout.EAST);
		
		txtIpAddress = new JTextField();
		txtIpAddress.setText("Enter IP or Hostname");
		txtIpAddress.setToolTipText("Enter IP or Hostname");
		panel_1.add(txtIpAddress, BorderLayout.CENTER);
		txtIpAddress.setColumns(10);
		
		
		panel = new JPanel();
		GridLayout grid = new GridLayout(0, 3, 0, 0);
		grid.setVgap(0);grid.setHgap(0);
		panel.setLayout(grid);
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
		List<String> hosts = scanNetwork();
		Collections.sort(hosts, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.toLowerCase().compareTo(o2.toLowerCase());
			}
		});
		for (String host : hosts) {
			repopulatePanel(panel,host);
		}
		scrollPane = new JScrollPane(panel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(scrollPane,BorderLayout.CENTER);
		
		alwaysOnTop = new JCheckBoxMenuItem(lang.getString("always_on_top"));
		alwaysOnTop.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (alwaysOnTop.isSelected()) setAlwaysOnTop(true);
				else setAlwaysOnTop(false);
			}
		});
		switch (config.getAlwaysOnTop()) {
		case 0:
			alwaysOnTop.setSelected(false);
			break;

		default:
			alwaysOnTop.setSelected(true);
			break;
		}
		JPopupMenu popMenu = new JPopupMenu();
		popMenu.add(alwaysOnTop);
		panel.setComponentPopupMenu(popMenu);
		
		
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
	
	private List<String> scanNetwork() {
		String line = "";
		if(config.getHosts().isEmpty()){
			List<String> hosts = new ArrayList<String>();
			hosts.add("8.8.8.8");
			hosts.add("git.swopt.com");
			try {
				Process process = Runtime.getRuntime().exec("net view");
				BufferedReader streamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				while((line = streamReader.readLine())!=null){
					if(line.indexOf("\\\\")!=-1)hosts.add(line.replace("\\\\", "").trim());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return hosts;
		}
		return config.getHosts();
	}
	
	private void repopulatePanel(JPanel panel,String ipHost) {
		ServerItemPanel sip = new ServerItemPanel(config, panel, ipHost, 1);
		int pos = panel.getComponentCount();
		panel.add(sip,pos);
		sip = (ServerItemPanel)panel.getComponent(pos);
		sip.setPosInParent(pos);
		panel.add(sip,pos);
		panel.revalidate();
	}
	
	public int optionDialog(Component component, String msg, String title) {
		JOptionPane optPane = new JOptionPane();
		optPane.setDefaultLocale(new Locale(config.getLocale()));
		if(config.getLocale().equals("zh"))optPane.setDefaultLocale(new Locale("zh","CN"));
		return optPane.showConfirmDialog(component, msg, title, JOptionPane.YES_NO_CANCEL_OPTION);
	}
	
	private void confirmExit(){
		int option = optionDialog(getContentPane(), lang.getString("save_and_close"), APPNAME);
		switch (option) {
		case 0:
			if (alwaysOnTop.isSelected())config.setAlwaysOnTop(1);
			else config.setAlwaysOnTop(0);
			Integer width = new Double(this.getSize().getWidth()).intValue();
			Integer height = new Double(this.getSize().getHeight()).intValue();
			config.setWidth(width);
			config.setHeight(height);
			List<String> hosts = new ArrayList<String>();
			for (int i = 0; i < panel.getComponentCount(); i++) {
				ServerItemPanel sip= (ServerItemPanel)panel.getComponent(i);
				hosts.add(sip.getServer());
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
			exit();
			break;
		case 1:
			exit();
			break;

		default:
			break;
		}
	}
	
	private void exit() {
		for (Component component : panel.getComponents()) {
			ServerItemPanel sip = (ServerItemPanel)component;
			sip.getPingThread().stop();
		}
		System.exit(0);
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
