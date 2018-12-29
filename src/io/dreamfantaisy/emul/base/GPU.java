
package io.dreamfantaisy.emul.base;

import io.dreamfantaisy.emul.Component;
import io.dreamfantaisy.emul.IGraphicsScreen;

public class GPU extends Component {

	private IGraphicsScreen gs;
	private int op = 0;
	private int mop = 0;
	private int a1, a2, a3, a4, a5, a6;
	private String str = "";
	
	public GPU(IGraphicsScreen igs) {
		gs = igs;
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
