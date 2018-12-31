package io.dreamfantaisy.emul;

import io.dreamfantaisy.emul.base.Keyboard;

public class Components {

	static {
		COMPONENTS = new Component[64];
	}
	
	public static void init() {
		
	}
	
	public static void setComponent(Component c, int port) {
		COMPONENTS[port] = c;
	}
	
	public static Component getComponent(int id) {
		return COMPONENTS[id];
	}
	
	public static final Component[] COMPONENTS;
	

}
