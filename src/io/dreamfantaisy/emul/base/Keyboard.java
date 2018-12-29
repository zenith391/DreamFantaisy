package io.dreamfantaisy.emul.base;

import io.dreamfantaisy.emul.Component;
import io.dreamfantaisy.emul.IGraphicsScreen;
import io.dreamfantaisy.emul.IGraphicsScreen.KeyListener;

public class Keyboard extends Component implements KeyListener {
	
	private int state;
	private int character;
	private char ch;
	private boolean isPressed;
	private boolean isReleased;
	
	private boolean numpad;
	private boolean caps;
	
	public Keyboard(IGraphicsScreen igs) {
		igs.addKeyListener(this);
	}
	
	public boolean isCapsEnabled() {
		return caps;
	}
	
	public boolean isNumpadEnabled() {
		return numpad;
	}
	
	@Override
	public void sendDirect(int b) {
		if (b == 101) {
			// caps   =  ON
			caps = true;
			System.out.println("[Keyboard] Caps enabled");
		}
		if (b == 100) {
			// caps   =  OFF
			caps = false;
			System.out.println("[Keyboard] Caps disabled");
		}
		if (b == 99) {
			// numpad =  ON
			numpad = true;
			System.out.println("[Keyboard] Numpad enabled");
		}
		if (b == 98) {
			// numpad  = OFF
			numpad = false;
			System.out.println("[Keyboard] Numpad disabled");
		}
	}
	
	@Override
	public int receive() {
		if (isPressed) {
			state = 1;
			isPressed = false;
			return 1;
		}
		if (state == 1) {
			state = 2;
			return character;
		}
		if (state == 2) {
			state = 0;
			return ch;
		}
		if (isReleased) {
			state = 1;
			isReleased = false;
			return 2;
		}
		return 0;
	}

	@Override
	public void keyPressed(int code, char ch) {
		character = code;
		this.ch = ch;
		isPressed = true;
	}

	@Override
	public void keyReleased(int code, char ch) {
		character = code;
		this.ch = ch;
		isReleased = true;
	}

}
