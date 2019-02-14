package io.dreamfantaisy.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

public class InterfaceUI extends JFrame {

	 static JPanel contentPane;
	 static JMenuItem btnStop = null;
	 static JMenuItem btnRun = null;
	 static JTabbedPane tabbedPane;

	/**
	 * Create the frame.
	 */
	public InterfaceUI() {
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		setTitle("DreamFantaisy: Playground");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280, 720);
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		JMenu mnEmu = new JMenu("Playground");
		menuBar.add(mnEmu);
		JMenuItem mntmLoadRemovableMedium = new JMenuItem("Load Removable Medium..");
		mnFile.add(mntmLoadRemovableMedium);
		mnFile.addSeparator();
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener((event) -> {
			System.exit(0);
		});
		mnFile.add(mntmExit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		btnRun = new JMenuItem("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DreamFantaisy.getInstance().start();
				btnRun.setEnabled(false);
				btnStop.setEnabled(true);
			}
		});
		mnEmu.add(btnRun);
		
		btnStop = new JMenuItem("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnStop.setEnabled(true);
				btnStop.setEnabled(false);
				DreamFantaisy.getInstance().stop();
			}
		});
		btnStop.setEnabled(false);
		mnEmu.add(btnStop);
		
		mnEmu.addSeparator();
		
		JCheckBoxMenuItem vmPause = new JCheckBoxMenuItem("Pause");
		vmPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (DreamFantaisy.getInstance().getComputer() != null) {
					DreamFantaisy.getInstance().getComputer().setPaused(vmPause.isSelected());
				}
			}
		});
		mnEmu.add(vmPause);
		
	}

}
