package io.dreamfantaisy.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import io.dreamfantaisy.emul.Components;
import io.dreamfantaisy.emul.Computer;
import io.dreamfantaisy.emul.Computer.CpState;
import io.dreamfantaisy.emul.base.GPU;
import io.dreamfantaisy.emul.base.Keyboard;
import io.dreamfantaisy.emul.base.Mouse;
import io.dreamfantaisy.emul.base.SerialPort;

public class DreamFantaisy {

	private Computer computer;
	private InterfaceUI gui;
	private JFrame frame;
	private static DreamFantaisy instance;
	private GraphicsScreenImpl gpuImpl;
	private JLabel debug;
	
	private Keyboard k = null;
	private Mouse m = null;
	
	public static boolean playgroundMode;
	
	public static void main(String[] args) {
		instance = new DreamFantaisy(args);
	}
	
	public void start() {
		computer = new Computer();
		if (k == null) {
			k = new Keyboard(gpuImpl);
		}
		if (m == null) {
			m = new Mouse(gpuImpl);
		}
		gpuImpl.requestFocus();
		Components.setComponent(k, 1);
		Components.setComponent(new GPU(gpuImpl), 2);
		Components.setComponent(m, 4);
		if  (!playgroundMode) {
			
		}
		Thread th = new Thread(() -> {
			CpState state = computer.run((int) (0.6f * (1024 * 1024)));
			if (gui != null) {
				InterfaceUI.btnRun.setEnabled(true);
				InterfaceUI.btnStop.setEnabled(false);
			}
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
	
	static JTabbedPane tabbedPane;
	private DreamFantaisy(String[] args) {
		for (String arg : args) {
			if (arg.equals("--playground")) {
				playgroundMode = true;
			}
		}
		
		for (LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()) {
			if (lafi.getName().equals("CDE/Motif")) {
				try {
					UIManager.setLookAndFeel(lafi.getClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
//				| UnsupportedLookAndFeelException e1) {
//			e1.printStackTrace();
//		}
		
		if (playgroundMode) {
			gui = new InterfaceUI();
			frame = gui;
		} else {
			frame = new JFrame();
			frame.setSize(1280, 720);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setTitle("DreamFantaisy");
			frame.setLocationRelativeTo(null);
		}
		frame.getContentPane().setBackground(new Color(0x2D2D2D));
		debug = new JLabel("...");
		gpuImpl = new GraphicsScreenImpl();
		gpuImpl.init();
		gpuImpl.fillRect(0, 0, gpuImpl.getMaxWidth(), gpuImpl.getMaxHeight(), 0x000000);
		gpuImpl.refresh();
		
		if (playgroundMode) {
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("screen", gpuImpl);
			frame.add(BorderLayout.CENTER, tabbedPane);
			
			// add serial0
			SerialPort port = new SerialPort();
			Components.setComponent(port, 50);
			SerialPortUI portui = new SerialPortUI(port, 0);
			portui.start();
			tabbedPane.addTab("serial0", portui);
		} else {
			frame.add(BorderLayout.CENTER, gpuImpl);
		}
		if (playgroundMode) frame.add(BorderLayout.SOUTH, debug);
		frame.addKeyListener(gpuImpl);
		gpuImpl.addMouseMotionListener(gpuImpl);
		gpuImpl.addMouseListener(gpuImpl);
		gpuImpl.addKeyListener(gpuImpl);
		gpuImpl.requestFocus();
		frame.setVisible(true);
		
		Thread th2 = new Thread(() -> {
			debug.setForeground(Color.WHITE);
			while (true) {
				if (computer != null && computer.isStarted()) {
					debug.setText("Used RAM: " + computer.getUsedMemory() / 1024 + "/" + computer.getTotalMemory() / 1024 + "KB, " +
					             "Iteration: " + computer.getState().iteration);
					debug.repaint();
					//frame.requestFocus();
				}
				try {
					Thread.sleep(1000/60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		if (playgroundMode) {
			th2.start();
		}
		
		if (!playgroundMode) {
			start();
		}
	}
	
	public static final DreamFantaisy getInstance() {
		return instance;
	}
	
}
