package com.lucasdrr.space;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.TreeMap;

public class Chat {

	private JFrame frame;
	private static ClientChat clientChat;
	private JTextArea textAreaTextToSend;
	private JTextArea textAreaChat;
	private JTextField textFieldNick;
	private JTable table;
	private DefaultTableModel model;
	private String receiver;
	private JButton btnConectar;
	private JButton btnEnviar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chat window = new Chat();
					window.frame.setVisible(true);
					clientChat = new ClientChat(window);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Chat() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 550, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panelMsg = new JPanel();
		panelMsg.setBorder(new TitledBorder(null, "Mensagens", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelMsg.setBounds(10, 92, 360, 267);
		frame.getContentPane().add(panelMsg);
		panelMsg.setLayout(null);

		textAreaChat = new JTextArea();
		textAreaChat.setEditable(false);
		textAreaChat.setBounds(10, 23, 280, 266);

		JScrollPane scrollPaneAreaChat = new JScrollPane(textAreaChat);
		scrollPaneAreaChat.setBounds(10, 23, 338, 233);
		scrollPaneAreaChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		panelMsg.add(scrollPaneAreaChat);


		JPanel panelTxtMsg = new JPanel();
		panelTxtMsg.setBorder(new TitledBorder(null, "Digite sua mensagem:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelTxtMsg.setBounds(10, 373, 390, 80);
		frame.getContentPane().add(panelTxtMsg);
		panelTxtMsg.setLayout(null);

		textAreaTextToSend = new JTextArea();
		textAreaTextToSend.setEnabled(false);
		textAreaTextToSend.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					if (textAreaTextToSend.getText().length() > 1) {
						clientChat.sendMessage(null, textAreaTextToSend.getText());
						textAreaChat.append("eu: " + textAreaTextToSend.getText());
					}
					textAreaTextToSend.setText(null);
				}
			}
		});
		textAreaTextToSend.setBounds(10, 22, 280, 47);

		JScrollPane scrollPaneText = new JScrollPane(textAreaTextToSend);
		scrollPaneText.setBounds(10, 22, 370, 47);
		scrollPaneText.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		panelTxtMsg.add(scrollPaneText);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Dados do Usu\u00E1rio", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 10, 360, 71);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		textFieldNick = new JTextField();
		textFieldNick.setBounds(12, 40, 100, 20);
		textFieldNick.setColumns(10);
		textFieldNick.setToolTipText("NickName");
		panel.add(textFieldNick);

		String[] latitudeArray = new String[361];
		for (int i = 0, j = 180; i < 361; i++, j--) {
			latitudeArray[i] = "" + j;
		}
		JComboBox comboBoxLatitude = new JComboBox(latitudeArray);
		comboBoxLatitude.setBounds(234, 35, 50, 25);
		comboBoxLatitude.setSelectedIndex(180);
		panel.add(comboBoxLatitude);

		String[] longitudeArray = new String[181];
		for (int i = 0, j = 90; i < 181; i++, j--) {
			longitudeArray[i] = "" + j;
		}
		JComboBox comboBoxLongitude = new JComboBox(longitudeArray);
		comboBoxLongitude.setBounds(296, 35, 50, 25);
		comboBoxLongitude.setSelectedIndex(90);
		panel.add(comboBoxLongitude);

		btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnConectar.setEnabled(false);
				textAreaTextToSend.setEnabled(true);
				btnEnviar.setEnabled(true);
				clientChat.setNickName(textFieldNick.getText());
				clientChat.initServices();
				refreshTable();
				new Thread(new ScheduledTask()).start();
			}
		});
		btnConectar.setBounds(124, 35, 98, 25);
		panel.add(btnConectar);

		JLabel lblNickname = new JLabel("Nickname:");
		lblNickname.setBounds(20, 23, 73, 15);
		panel.add(lblNickname);

		JLabel lblLong = new JLabel("Long.");
		lblLong.setBounds(234, 10, 40, 15);
		panel.add(lblLong);

		JLabel lblLat = new JLabel("Latit.");
		lblLat.setBounds(302, 10, 38, 15);
		panel.add(lblLat);

		JPanel panelUsers = new JPanel();
		panelUsers.setBorder(new TitledBorder(null, "Usu\u00E1rios Online", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelUsers.setBounds(382, 11, 154, 348);
		frame.getContentPane().add(panelUsers);
		panelUsers.setLayout(null);

		model = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Usu\u00E1rio", "Status"
				}
				);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				int row = table.getSelectedRow();
				receiver = model.getValueAt(row, 0).toString();
			}
		});
		table.setModel(model);
		table.setBounds(10, 25, 134, 312);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 25, 134, 312);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		panelUsers.add(scrollPane);

		btnEnviar = new JButton("Enviar");
		btnEnviar.setEnabled(false);
		btnEnviar.setBounds(412, 406, 77, 25);
		frame.getContentPane().add(btnEnviar);
		
	}

	/**
	 * @return the textAreaChat
	 */
	public JTextArea getTextAreaChat() {
		return textAreaChat;
	}

	/**
	 * @param textAreaChat the textAreaChat to set
	 */
	public void setTextAreaChat(JTextArea textAreaChat) {
		this.textAreaChat = textAreaChat;
	}
	/**
	 * 
	 */
	private void refreshTable() {

		TreeMap<String, String> list = clientChat.getListContacts();
		int count = model.getRowCount();
		for (int i = count-1; i >= 0; i--) {
			model.removeRow(i);
		}
		for (String key : list.keySet()) {
			String [] values = {key, list.get(key)};

			model.addRow(values);
		}

	}
	/**
	 * @author Lucas Diego Reboucas Rocha
	 *
	 */
	private class ScheduledTask implements Runnable {
		public void run() {
			while (true) {
				try {
					Thread.sleep(30*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshTable();
			}
		}
	}
}