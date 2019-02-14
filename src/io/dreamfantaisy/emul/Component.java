package io.dreamfantaisy.emul;

/**
 * Some components are allowed to define extra methods for having more easy and fast operations.
 * @author zenith391
 *
 */
public class Component {
	
	public void sendDirect(int b) {
	}
	
	public void sendUrgent(int b) {
		// TODO handling urgent send
		sendDirect(b);
	}
	
	public int receive() {
		return 0;
	}

}
