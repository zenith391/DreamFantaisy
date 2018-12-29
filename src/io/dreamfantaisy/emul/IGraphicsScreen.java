package io.dreamfantaisy.emul;

public interface IGraphicsScreen {

	public static abstract interface KeyListener {
		public void keyPressed(int code, char ch);
		public void keyReleased(int code, char ch);
	}
	
	public static final int MODE_CONSOLE = 1337; // hackerz
	public static final int MODE_PGPU = 7796;
	
	public void fillRect(int x, int y, int w, int h, int rgb);
	public void drawImage(int x, int y, int encoding, int[] data);
	public void setPixel(int x, int y, int rgb);
	public int storeImage(int encoding, int[] data);
	public void deleteStoredImage(int id);
	public void drawStoredImage(int x, int y, int id);
	public int getMaxWidth();
	public void drawString(String str, int x, int y, int rgb);
	public int getMaxHeight();
	public void setResolution(int width, int height);
	public void init();
	public void refresh();
	public void addKeyListener(KeyListener lis);
	public void setMode(int mode);
	
}
