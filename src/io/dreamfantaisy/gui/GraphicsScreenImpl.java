package io.dreamfantaisy.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComponent;

import io.dreamfantaisy.Bits;
import io.dreamfantaisy.emul.IGraphicsScreen;

public class GraphicsScreenImpl extends JComponent implements IGraphicsScreen, java.awt.event.KeyListener, MouseMotionListener, java.awt.event.MouseListener {

	private VolatileImage out;
	private Graphics2D g2d;
	private int mode = 0;

	private ArrayList<BufferedImage> images = new ArrayList<>();
	private ArrayList<KeyListener> keyListeners = new ArrayList<>();
	private ArrayList<MouseListener> mouseListeners = new ArrayList<>();
	
	private char[][] textVideoBuffer; // YX buffer
	private int[][] textVideoRGBBuffer;
	
	private Font font;

	public void paintComponent(Graphics g) {
		g.drawImage(out, ((getWidth() - out.getWidth()) / 2), ((getHeight() - out.getHeight()) / 2), null);
	}
	
	@Override
	public void init() {
		setResolution(640, 480);
		try {
			font = Font.createFonts(new File("res/unifont.ttf"))[0].deriveFont(14.f);
			
			//fis.close();
		} catch (IOException | FontFormatException e) {
			System.err.println("Error while loading font!");
			e.printStackTrace();
		}
		
	}

	@Override
	public void fillRect(int x, int y, int w, int h, int rgb) {
		g2d.setColor(new Color(rgb));
		g2d.fillRect(x, y, w, h);
	}

	@Override
	public void drawImage(int x, int y, int encoding, int[] data) {
		int id = storeImage(encoding, data);
		drawStoredImage(x, y, id);
		deleteStoredImage(id);
	}

	@Override
	public void setPixel(int x, int y, int rgb) {
		fillRect(x, y, 1, 1, rgb);
	}

	private BufferedImage decode(int encoding, int[] data) {
		if (encoding == 0) {
			int width = data[0];
			int height = data[1];
			BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			int i = 0;
			for (int j = 0; j < width; j++) {
				for (int k = 0; k < height; k++) {
					i++;
					img.setRGB(j, k, data[2 + i]);
				}
			}
			return img;
		} else {
			return null;
		}
	}

	@Override
	public int storeImage(int encoding, int[] data) {
		BufferedImage img = decode(encoding, data);
		images.add(img);
		return images.indexOf(img);
	}

	@Override
	public void deleteStoredImage(int id) {
		images.remove(id);
	}

	@Override
	public void drawStoredImage(int x, int y, int id) {
		g2d.drawImage(images.get(id), x, y, null);
	}

	@Override
	public int getMaxWidth() {
		return 640;
	}

	@Override
	public int getMaxHeight() {
		return 480;
	}

	@Override
	public void setResolution(int width, int height) {
		if (width > getMaxWidth() || height > getMaxHeight()) {
			System.out.println("[GPU] Warning! OS intended to set a resolution higher than maximum supported"
					+ " by GPU (" + getMaxWidth() + "x" + getMaxHeight() + ")");
			return;
		}
		out = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
				.createCompatibleVolatileImage(width, height);
		out.setAccelerationPriority(1f);
		g2d = out.createGraphics();
		g2d.setColor(Color.BLACK);
		//g2d.fillRect(0, 0, out.getWidth(), out.getHeight(), width, height);
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void refresh() {
		if (mode == MODE_CONSOLE) {
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, out.getWidth(), out.getHeight());
			g2d.setColor(Color.WHITE);
			if (font != null) {
				g2d.setFont(font);
			}
			for (int y = 0; y < textVideoBuffer.length; y++) {
				for (int x = 0; x < 80; x++) {
					char ch = textVideoBuffer[y][x];
					if (ch != 0) {
						g2d.setColor(new Color(textVideoRGBBuffer[y][x]));
						g2d.drawString(String.valueOf(ch), x * 8, y * 16 + 16);
					}
				}
				//g2d.drawChars(textVideoBuffer[y], 0, 80, 0, (y * 12) + 12);
			}
		}
		//System.gc();
		repaint();
	}

	@Override
	public void drawString(String str, int x, int y, int rgb) {
		if (mode == MODE_CONSOLE) {
			for (int x1 = x; x1 < x + str.length(); x1++) {
				textVideoBuffer[y][x1] = str.charAt(x1 - x);
				textVideoRGBBuffer[y][x1] = rgb;
			}
		} else {
			g2d.setColor(new Color(rgb));
			g2d.drawString(str, x * 8, y * 16);
		}
	}

	@Override
	public void addKeyListener(KeyListener lis) {
		keyListeners.add(lis);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		for (KeyListener lis : keyListeners) {
			lis.keyPressed(e.getKeyCode(), e.getKeyChar());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		for (KeyListener lis : keyListeners) {
			lis.keyReleased(e.getKeyCode(), e.getKeyChar());
		}
	}

	@Override
	public void setMode(int mode) {
		this.mode = mode;
		if (mode == MODE_CONSOLE) {
			textVideoBuffer = new char[30][80];
			textVideoRGBBuffer = new int[30][80];
			setResolution(80 * 8, 30 * 16);
		} else if (mode == MODE_PGPU) {
			textVideoBuffer = null;
			setResolution(getMaxWidth(), getMaxHeight());
		}
	}

	@Override
	public void addMouseListener(MouseListener lis) {
		mouseListeners.add(lis);
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		for (MouseListener lis : mouseListeners) {
			lis.mousePressed();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (MouseListener lis : mouseListeners) {
			lis.mouseReleased();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		int ox = ((getWidth() - out.getWidth()) / 2);
		int oy = ((getHeight() - out.getHeight()) / 2);
		for (MouseListener lis : mouseListeners) {
			int x = e.getX() - ox;
			int y = e.getY() - oy;
			if (x < 0 || x > out.getWidth())
				break;
			if (y < 0 || y > out.getHeight())
				break;
			
			lis.mouseMoved(x, y);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int ox = ((getWidth() - out.getWidth()) / 2);
		int oy = ((getHeight() - out.getHeight()) / 2);
		for (MouseListener lis : mouseListeners) {
			int x = e.getX() - ox;
			int y = e.getY() - oy;
			if (x < 0 || x > out.getWidth())
				break;
			if (y < 0 || y > out.getHeight())
				break;
			
			lis.mouseMoved(x, y);
		}
	}

}
