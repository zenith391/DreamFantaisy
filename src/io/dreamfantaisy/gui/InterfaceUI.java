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
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import com.jidesoft.swing.JideButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JMenuItem;

public class InterfaceUI extends JFrame {

	 static JPanel contentPane;
	 static JideButton jdbtnStop = null;
	 static JideButton jdbtnRun = null;

	/**
	 * Create the frame.
	 */
	public InterfaceUI() {
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		setTitle("DreamFantaisy Playground");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 720);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmLoadRemovableMedium = new JMenuItem("Load Removable Medium..");
		mnFile.add(mntmLoadRemovableMedium);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener((event) -> {
			System.exit(0);
		});
		mnFile.add(mntmExit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		jdbtnRun = new JideButton();
		jdbtnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DreamFantaisy.getInstance().start();
				jdbtnRun.setEnabled(false);
				jdbtnStop.setEnabled(true);
			}
		});
		jdbtnRun.setText("Run");
		jdbtnRun.setButtonStyle(JideButton.TOOLBOX_STYLE);
		toolBar.add(jdbtnRun);
		
		jdbtnStop = new JideButton("Stop");
		jdbtnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jdbtnRun.setEnabled(true);
				jdbtnStop.setEnabled(false);
				DreamFantaisy.getInstance().stop();
			}
		});
		jdbtnStop.setEnabled(false);
		jdbtnStop.setButtonStyle(JideButton.TOOLBOX_STYLE);
		toolBar.add(jdbtnStop);
		
		JToggleButton vmPause = new JToggleButton("Pause");
		vmPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (DreamFantaisy.getInstance().getComputer() != null) {
					DreamFantaisy.getInstance().getComputer().setPaused(vmPause.isSelected());
				}
			}
		});
		
		Component horizontalGlue = Box.createHorizontalGlue();
		toolBar.add(horizontalGlue);
		toolBar.add(vmPause);
		
		
	}

}
