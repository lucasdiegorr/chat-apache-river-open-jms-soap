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

import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Chat {

	private JFrame frame;
	private static ClientChat clientChat;
	private JTextArea textAreaTextToSend;
	private JTextArea textAreaChat;
	private JTextField textFieldNick;
	private JButton btnConectar;
	private JButton btnEnviar;
	private JTextField textFieldReceiver;
	private JTextField textField;
	private JTextField textField_1;
	private JTextArea textAreaVision;

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
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				clientChat.disconnect();
			}
		});
		frame.setBounds(100, 100, 712, 411);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panelMsg = new JPanel();
		panelMsg.setBorder(new TitledBorder(null, "Mensagens", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelMsg.setBounds(182, 22, 262, 222);
		frame.getContentPane().add(panelMsg);
		panelMsg.setLayout(null);

		textAreaChat = new JTextArea();
		textAreaChat.setEditable(false);
		textAreaChat.setBounds(10, 23, 280, 266);

		JScrollPane scrollPaneAreaChat = new JScrollPane(textAreaChat);
		scrollPaneAreaChat.setBounds(10, 23, 240, 184);
		scrollPaneAreaChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		panelMsg.add(scrollPaneAreaChat);


		JPanel panelTxtMsg = new JPanel();
		panelTxtMsg.setBorder(new TitledBorder(null, "Digite sua mensagem:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelTxtMsg.setBounds(182, 256, 266, 118);
		frame.getContentPane().add(panelTxtMsg);
		panelTxtMsg.setLayout(null);

		textAreaTextToSend = new JTextArea();
		textAreaTextToSend.setEnabled(false);
		textAreaTextToSend.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					if (textAreaTextToSend.getText().length() > 1) {
						clientChat.sendMessage(textFieldReceiver.getText(), textAreaTextToSend.getText());
						textAreaChat.append("eu: " + textAreaTextToSend.getText());
					}
					textAreaTextToSend.setText(null);
				}
			}
		});
		textAreaTextToSend.setBounds(10, 22, 280, 47);

		JScrollPane scrollPaneText = new JScrollPane(textAreaTextToSend);
		scrollPaneText.setBounds(12, 59, 242, 47);
		scrollPaneText.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		panelTxtMsg.add(scrollPaneText);
		
		JLabel lblPara = new JLabel("Para:");
		lblPara.setBounds(12, 25, 70, 15);
		panelTxtMsg.add(lblPara);
		
		textFieldReceiver = new JTextField();
		textFieldReceiver.setBounds(61, 28, 114, 19);
		panelTxtMsg.add(textFieldReceiver);
		textFieldReceiver.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Dados do Usu\u00E1rio", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(460, 26, 238, 140);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		textFieldNick = new JTextField();
		textFieldNick.setBounds(12, 40, 100, 20);
		textFieldNick.setColumns(10);
		textFieldNick.setToolTipText("NickName");
		panel.add(textFieldNick);

		btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnConectar.setEnabled(false);
				textAreaTextToSend.setEnabled(true);
				btnEnviar.setEnabled(true);
				clientChat.setNickName(textFieldNick.getText());
				clientChat.initServices();
			}
		});
		btnConectar.setBounds(124, 35, 98, 25);
		panel.add(btnConectar);

		JLabel lblNickname = new JLabel("Nickname:");
		lblNickname.setBounds(20, 23, 73, 15);
		panel.add(lblNickname);
		
		textField = new JTextField();
		textField.setText("0");
		textField.setBounds(124, 109, 45, 19);
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setText("0");
		textField_1.setBounds(177, 109, 45, 19);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblLat = new JLabel("Lat.");
		lblLat.setBounds(124, 82, 45, 15);
		panel.add(lblLat);
		
		JLabel lblLong = new JLabel("Long.");
		lblLong.setBounds(177, 82, 49, 15);
		panel.add(lblLong);
		
		JButton btnLocalizao = new JButton("Mudar Local");
		btnLocalizao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clientChat.changeLocation(textField.getText(),textField_1.getText());
			}
		});
		btnLocalizao.setBounds(12, 82, 100, 49);
		panel.add(btnLocalizao);

		JPanel panelVision = new JPanel();
		panelVision.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Campo de vis\u00E3o", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelVision.setBounds(20, 12, 154, 362);
		frame.getContentPane().add(panelVision);
		panelVision.setLayout(null);
		
		setTextAreaVision(new JTextArea());
		getTextAreaVision().setBounds(12, 22, 130, 328);
		panelVision.add(getTextAreaVision());

		btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textAreaTextToSend.getText().length() > 1) {
					clientChat.sendMessage(textFieldReceiver.getText(), textAreaTextToSend.getText());
					textAreaChat.append("eu: " + textAreaTextToSend.getText());
				}
				textAreaTextToSend.setText(null);
			}
		});
		btnEnviar.setEnabled(false);
		btnEnviar.setBounds(460, 302, 77, 25);
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

	public JTextArea getTextAreaVision() {
		return textAreaVision;
	}

	public void setTextAreaVision(JTextArea textAreaVision) {
		this.textAreaVision = textAreaVision;
		textAreaVision.setEditable(false);
	}
}