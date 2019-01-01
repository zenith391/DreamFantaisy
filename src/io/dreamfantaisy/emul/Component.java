package io.dreamfantaisy.emul;

/**
 * Some components are allowed to define extra methods for having more easy and fast operations.
 * @author zenith391
 *
 */
public class Component {
	
	public void sendDirect(int b) {
//		try {
//			Thread.sleep(2);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
	
	public void sendUrgent(int b) {
		// TODO handling urgent send
		sendDirect(b);
	}
	
	public int receive() {
//		try {
//			Thread.sleep(1);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		return 0;
	}

}
