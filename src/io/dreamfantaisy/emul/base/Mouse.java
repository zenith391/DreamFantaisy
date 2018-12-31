package io.dreamfantaisy.emul.base;

import io.dreamfantaisy.emul.Component;
import io.dreamfantaisy.emul.IGraphicsScreen;
import io.dreamfantaisy.emul.IGraphicsScreen.MouseListener;

public class Mouse extends Component implements MouseListener {

	int state;
	int x, y;
	
	public Mouse(IGraphicsScreen igs) {
		igs.addMouseListener(this);
	}
	
	@Override
	public void mouseMoved(int x, int y) {
		this.x = x;
		this.y = y;
		state = 1;
	}

	@Override
	public void mousePressed() {
		state = 2;
	}

	@Override
	public void mouseReleased() {
		state = 3;
	}
	
	@Override
	public void sendDirect(int b) {
		
	}
	
	@Override
	public int receive() {
		if (state == 1) {
			state = 4;
			return 1;
		}
		if (state == 4) {
			state = 5;
			return x;
		}
		if (state == 5) {
			state = 0;
			return y;
		}
		if (state == 2) {
			state = 0;
			return 2;
		}
		if (state == 3) {
			state = 0;
			return 3;
		}
		return 0;
	}

}
