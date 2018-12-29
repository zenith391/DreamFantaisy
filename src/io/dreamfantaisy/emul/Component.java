package io.dreamfantaisy.emul;

public class Component {
	
	public void sendDirect(int b) {
		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void sendUrgent(int b) {
		// TODO handling urgent send
		sendDirect(b);
	}
	
	public int receive() {
		return 0;
	}

}
