package io.dreamfantaisy.emul.base;

import java.util.ArrayDeque;
import java.util.Deque;

import io.dreamfantaisy.emul.Component;

public class SerialPort extends Component {

	Deque<Integer> inputQueue = new ArrayDeque<>();
	Deque<Integer> outputQueue = new ArrayDeque<>();
	
	public void _host_send(int b) {
		inputQueue.add(b);
	}
	
	public boolean _host_isOutputAvailable() {
		return !outputQueue.isEmpty();
	}
	
	public int _host_receive() {
		if (!_host_isOutputAvailable()) {
			return 0;
		}
		return outputQueue.pop();
	}
	
	public int receive() {
		if (sm == 2) {
			sm = 0;
			if (inputQueue.isEmpty()) {
				return 1;
			} else {
				return 2;
			}
		} else if (sm == 3) {
			return inputQueue.size();
		}
		else {
			if (inputQueue.isEmpty()) {
				return 0;
			}
			return inputQueue.pop();
		}
	}
	
	int sm = 0;
	public void sendDirect(int b) {
		if (sm == 0) {
			if (b == 2) { // output mode
				sm = 1;
				return;
			}
			if (b == 5) { // (for read) is read available
				sm = 2;
				return;
			}
			if (b == 9) { // (for read) remaining data
				sm = 3;
				return;
			}
		}
		if (sm == 1) {
			outputQueue.add(b);
			sm = 0;
		}
	}
	
}
