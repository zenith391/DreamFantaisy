package io.dreamfantaisy.gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import io.dreamfantaisy.emul.base.SerialPort;

import java.awt.Color;
import javax.swing.JToolBar;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SerialPortUI extends JPanel {
	
	private JTextField input;
	private JTextPane output;
	private SerialPort peer;
	private int id;
	private Thread th;
	private JToolBar toolBar;
	private JButton btnn;
	private JButton btnb;
	
	public SerialPortUI(SerialPort peer, int id) {
		this();
		this.peer = peer;
		this.id = id;
		
	}

	public void start() {
		th.start();
	}
	
	public void stop() {
		th.interrupt();
	}
	
	/**
	 * Create the panel.
	 */
	public SerialPortUI() {
		setLayout(new BorderLayout(0, 0));
		input = new JTextField();
		input.setForeground(Color.WHITE);
		input.setBackground(Color.BLACK);
		input.addActionListener((event) -> {
			String text = input.getText();
			for (char c : text.toCharArray()) {
				peer._host_send(c);
			}
			peer._host_send(0);
			input.setText("");
		});
		add(input, BorderLayout.SOUTH);
		input.setColumns(10);
		
		output = new JTextPane();
		output.setForeground(Color.CYAN);
		output.setBackground(Color.BLACK);
		output.setText("serial console:\n");
		output.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(output);
		add(scrollPane, BorderLayout.CENTER);
		
		toolBar = new JToolBar();
		scrollPane.setColumnHeaderView(toolBar);
		
		btnn = new JButton("\\n");
		btnn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				peer._host_send('\n');
			}
		});
		toolBar.add(btnn);
		
		btnb = new JButton("\\b");
		btnb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				peer._host_send('\b');
			}
		});
		toolBar.add(btnb);

		th = new Thread(() -> {
			while (true) {
				if (Thread.interrupted()) {
					Thread.currentThread().interrupt();
					break;
				}
				if (peer._host_isOutputAvailable()) {
					char ch = (char) peer._host_receive();
					if (ch == '\0') {
						continue;
					}
					output.setText(output.getText() + ch);
				}
				Thread.onSpinWait();
			}
		});
		th.setName("serial-thread");
		th.setDaemon(true);
	}

}
