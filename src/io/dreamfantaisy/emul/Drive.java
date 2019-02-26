package io.dreamfantaisy.emul;

import java.util.Objects;

public class Drive {

	String path;
	String id;
	
	public Drive(String path, String id) {
		Objects.requireNonNull(path, "path");
		Objects.requireNonNull(id, "id");
		this.path = path;
		this.id = id;
	}
	
	public String getDirectory() {
		return path;
	}
	
	public String getIdentifier() {
		return id;
	}
	
}
