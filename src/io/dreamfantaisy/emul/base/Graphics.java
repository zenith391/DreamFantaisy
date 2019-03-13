
package io.dreamfantaisy.emul.base;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import io.dreamfantaisy.emul.Component;
import io.dreamfantaisy.emul.IGraphicsScreen;

/**
 * MME = Multimedia Graphical Extensions<br/>
 * Images are always in RGBA
 * @author zenith391
 *
 */
public class Graphics extends Component {

	private IGraphicsScreen gs;
	private int op = 0;
	private int mop = 0;
	private int a1, a2, a3, a4, a5, a6;
	private String str = "";
	
	public Graphics(IGraphicsScreen igs) {
		gs = igs;
	}
	
	/**
	 * MGE = Multimedia Graphical Extensions
	 */
	public boolean hasMGE() {
		return true;
	}
	
	public int storeImage(int encoding, int[] data) {
		if (hasMGE()) {
			return gs.storeImage(encoding, data);
		} else {
			return -1;
		}
	}
	
	/**
	 * Number of images that can be stored at the same time
	 */
	public int getMaxImages() {
		if (hasMGE()) {
			return 64;
		} else {
			return -1;
		}
	}
	
	public void drawStoredImage(int x, int y, int id) {
		if (hasMGE()) {
			gs.drawStoredImage(x, y, id);
		}
	}
	
	public void drawImage(int x, int y, int[] data) {
		if (hasMGE()) {
			gs.drawImage(x, y, 0, data);
		}
	}
	
	/**
	 * Image loading accelerator. Part of: Multimedia Graphical Extensions
	 * @param data raw binary data (png, bmp, jpeg, tiff, etc.)
	 * @return accelerated RGB image
	 */
	public int[] loadImage(String data) {
		if (hasMGE()) {
			try {
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(data.getBytes()));
				int[] array = new int[2 + (img.getWidth() * img.getHeight())];
				array[0] = img.getWidth();
				array[1] = img.getHeight();
				int i = 0;
				for (int j = 0; j < img.getWidth(); j++) {
					for (int k = 0; k < img.getHeight(); k++) {
						i++;
						array[2 + i] = img.getRGB(j, k);
					}
				}
				return array;
			} catch (IOException e) {
				e.printStackTrace();
				return new int[0];
			}
		} else {
			return new int[0];
		}
	}
	
	public void fillRect(int x, int y, int width, int height, int color) {
		gs.fillRect(x, y, width, height, color);
	}
	
	public void text(int x, int y, String text, int color) {
		gs.drawString(text, x, y, color);
	}
	
	public void updateScreen() {
		gs.refresh();
	}
	
	@Override
	public void sendDirect(int b) {
		super.sendDirect(b);
		if (mop == 0 && op == 0) {
			if (b == 53) {
				gs.refresh();
			}
			else if (b == 52) {
				op = 1;
				mop = 1;
			}
			else if (b == 54) {
				op = 0;
				mop = 2;
				return;
			}
			else if (b == 55) {
				gs.setMode(IGraphicsScreen.MODE_CONSOLE);
			}
			else if (b == 56) {
				gs.setMode(IGraphicsScreen.MODE_PGPU);
			}
		}
//		if (b == 52 && mop == 0) {
//			op = 1;
//			mop = 1;
//			return;
//		}
		else if (op == 1 && mop == 1) {
			a1 = b;
			if (mop == 1)
				op = 2;
			return;
		} else if (op == 2 && mop == 1) {
			a2 = b;
			if (mop == 1)
				op = 3;
			return;
		} else if (op == 3 && mop == 1) {
			a3 = b;
			if (mop == 1)
				op = 4;
			return;
		} else if (op == 4 && mop == 1) {
			a4 = b;
			if (mop == 1)
				op = 5;
			return;
		} else if (op == 5 && mop == 1) {
			a5 = b;
			if (mop == 1) {
				gs.fillRect(a1, a2, a3, a4, a5);
				mop = 0;
				op = 0;
			}
		} else if (op == 6) {
			a6 = b;
		}
		if (mop == 2) {
			if (op==3) {
				if (b == 0) {
					mop = 0;
					op = 0;
					gs.drawString(str, a1, a2, a3);
					str = "";
					return;
				} else {
					if (b != 0)
						str += (char) b;
					else {
						mop = 0;
						op = 0;
					}
					return;
				}
			}
			if (op == 2) {
				a3 = b;
				op = 3;
			}
			if (op == 1) {
				a2 = b;
				op = 2;
			}
			if (op == 0) {
				a1 = b;
				op = 1;
			}
		}
	}
	
	@Override
	public int receive() {
		return 0;
	}
}
