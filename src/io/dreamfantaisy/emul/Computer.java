package io.dreamfantaisy.emul;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import li.cil.repack.com.naef.jnlua.LuaRuntimeException;
import li.cil.repack.com.naef.jnlua.LuaStackTraceElement;
import li.cil.repack.com.naef.jnlua.LuaState;
import li.cil.repack.com.naef.jnlua.LuaState.Library;
import li.cil.repack.com.naef.jnlua.NativeSupport;
import li.cil.repack.com.naef.jnlua.NativeSupport.Loader;

public class Computer {

	private LuaState luaState;
	private boolean ended = false;
	private boolean stop = false;
	private boolean pause;
	private CpState state;
	private ArrayList<Drive> drives;
	private static String arch;
	
	static {
		arch = "32";
		if (System.getProperty("os.arch", "32").contains("64")) {
			arch = "64";
		}
		NativeSupport.getInstance().setLoader(new Loader() {

			@Override
			public void load() {
				System.out.println(arch + "-bit Lua library loading..");
				System.loadLibrary("native." + arch);
			}

		});
	}
	
	public static class CpState {
		public boolean crashed;
		public int iteration;
	}

	public Computer() {
		this.drives = new ArrayList<>();
	}
	
	public void stop() {
		stop = true;
	}
	
	public int getFreeMemory() {
		return luaState.getFreeMemory();
	}
	
	public int getUsedMemory() {
		return luaState.getTotalMemory() - luaState.getFreeMemory();
	}
	
	public int getTotalMemory() {
		return luaState.getTotalMemory();
	}
	
	public boolean isStarted() {
		return luaState != null;
	}
	
	public boolean isEnded() {
		return ended;
	}
	
	public void setPaused(boolean pause) {
		this.pause = pause;
	}
	
	public CpState getState() {
		return state;
	}
	
	public Drive[] getDrives() {
		return drives.toArray(new Drive[drives.size()]);
	}
	
	public void addDrive(Drive drive) {
		drives.add(drive);
	}
	
	public void removeDrive(Drive drive) {
		drives.remove(drive);
	}

	public CpState run(int memory) {
		state = new CpState();
		luaState = new LuaState(memory);
		luaState.openLib(Library.BASE);
		luaState.openLib(Library.BIT32);
		luaState.openLib(Library.MATH);
		luaState.openLib(Library.DEBUG);
		//luaState.openLib(Library.ERIS);
		luaState.openLib(Library.STRING);
		luaState.openLib(Library.TABLE);
		luaState.openLib(Library.COROUTINE);
		luaState.pop(luaState.getTop());
		System.out.println("[DreamFantaisy] Running on Lua " + LuaState.lua_version());
		System.out.println("=-=-=-=- Launching Computer =-=-=-=-=");

		try {
			luaState.load(new FileInputStream("filesystems/rom/computer.lua"), "=computer", "t");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ComputerLib lib = new ComputerLib(luaState, this);
		
		Components.init();

		luaState.call(0, 1);
		
		while (true) {
			if (!pause) {
				try {
					state.iteration++;
					luaState.resume(1, 0);
				} catch (RuntimeException e) {
					if (e instanceof LuaRuntimeException) {
						System.err.println("Lua error!\nLua Stacktrace:");
						LuaRuntimeException le = (LuaRuntimeException) e;
						for (LuaStackTraceElement lst : le.getLuaStackTrace()) {
							System.err.println("\t" + lst.getSourceName() + ";" + lst.getFunctionName() +
									":" + lst.getLineNumber());
						}
					}
					System.err.println("Java Stacktrace:");
					e.printStackTrace();
					state.crashed = true;
					break;
				}
			}
			if (lib.getYieldAction() == ComputerLib.SHUTDOWN || luaState.status(1) == 0 || stop) {
				break;
			}
			if (lib.getYieldAction() == ComputerLib.SLEEP) {
				try {
					Thread.sleep(lib.getYieldArgInt1());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			lib.resetYieldAction();
			Thread.onSpinWait();
		}
		
		ended = true;
		System.out.println((luaState.getTotalMemory() - luaState.getFreeMemory()) / 1024 + "KB used.");
		luaState.close();
		return state;
	}

}
