package io.dreamfantaisy.gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideButton;

import io.dreamfantaisy.emul.Components;
import io.dreamfantaisy.emul.Computer;
import io.dreamfantaisy.emul.Computer.CpState;
import io.dreamfantaisy.emul.base.GPU;
import io.dreamfantaisy.emul.base.Keyboard;

public class DreamFantaisy {

	private Computer computer;
	private InterfaceUI gui;
	private static DreamFantaisy instance;
	private GraphicsScreenImpl gpuImpl;
	private JLabel debug;
	
	private Keyboard k = null;
	
	public static void main(String[] args) {
		instance = new DreamFantaisy();
	}
	
	public void start() {
		computer = new Computer();
		if (k == null) {
			k = new Keyboard(gpuImpl);
		}
		Components.setComponent(k, 1);
		Components.setComponent(new GPU(gpuImpl), 2);
		Thread th = new Thread(() -> {
			CpState state = computer.run((8 * (1024 * 1024)));
			InterfaceUI.jdbtnRun.setEnabled(true);
			InterfaceUI.jdbtnStop.setEnabled(false);
			if (state.crashed) {
				JOptionPane.showMessageDialog(gui, "Your DreamFantaisy® computer had to stop!\n" // dreamfantaisy is not registed (by me), it is just for a little more immersion.
				                                 + "Iteration N°: " + state.iteration, "Guru Meditation!", JOptionPane.ERROR_MESSAGE);
			}
		});
		th.start();
	}
	
	public Computer getComputer() {
		return computer;
	}
	
	public void stop() {
		computer.stop();
	}
	
	private DreamFantaisy() {
		
		for (LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()) {
			if (lafi.getName().equals("Windows Classic")) {
				try {
					UIManager.setLookAndFeel(lafi.getClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
			}
		}
		
		gui = new InterfaceUI();
		debug = new JLabel("...");
		gpuImpl = new GraphicsScreenImpl();
		gpuImpl.init();
		gpuImpl.fillRect(0, 0, gpuImpl.getMaxWidth(), gpuImpl.getMaxHeight(), 0xFFFFFF);
		gpuImpl.refresh();
		gui.add(BorderLayout.CENTER, gpuImpl);
		gui.add(BorderLayout.SOUTH, debug);
		gui.addKeyListener(gpuImpl);
		gpuImpl.addKeyListener(gpuImpl);
		gpuImpl.requestFocus();
		gui.setVisible(true);
		Thread th2 = new Thread(() -> {
			while (true) {
				gpuImpl.repaint();
				if (computer != null && computer.isStarted()) {
					debug.setText("Used RAM: " + computer.getUsedMemory() / 1024 + "/" + computer.getTotalMemory() / 1024 + "KB, " +
					             "Iteration: " + computer.getState().iteration);
					debug.repaint();
					gpuImpl.requestFocus();
				}
				try {
					Thread.sleep(1000/60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		th2.start();
	}
	
	public static final DreamFantaisy getInstance() {
		return instance;
	}
	
}
