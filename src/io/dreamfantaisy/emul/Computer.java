package io.dreamfantaisy.emul;

import java.io.FileInputStream;
import java.io.IOException;

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
	
	public static class CpState {
		public boolean crashed;
		public int iteration;
	}

	public Computer() {
		
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

	public CpState run(int memory) {
		state = new CpState();
		NativeSupport.getInstance().setLoader(new Loader() {

			@Override
			public void load() {
				System.loadLibrary("native.64");
			}

		});
		luaState = new LuaState(memory);
		luaState.openLib(Library.BASE);
		luaState.openLib(Library.BIT32);
		luaState.openLib(Library.MATH);
		luaState.openLib(Library.DEBUG);
		//luaState.openLib(Library.ERIS);
		luaState.openLib(Library.STRING);
		luaState.openLib(Library.TABLE);
		luaState.openLib(Library.COROUTINE);
		luaState.pop(7);
		System.out.println("[DreamFantaisy] Running on Lua " + LuaState.lua_version());
		System.out.println("=-=-=-=- Launching Computer =-=-=-=-=");

		try {
			luaState.load(new FileInputStream("filesystems/rom/computer.lua"), "=computer", "t");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ComputerLib lib = new ComputerLib(luaState);
		
		Components.init();

		luaState.call(0, 1);
		
		while (true) {
			if (!pause) {
				try {
					state.iteration++;
					luaState.resume(1, 0);
				} catch (LuaRuntimeException e) {
					System.err.println("Lua error!\nLua Stacktrace:");
					for (LuaStackTraceElement lst : e.getLuaStackTrace()) {
						System.err.println("\t" + lst.getSourceName() + ";" + lst.getFunctionName() +
								":" + lst.getLineNumber());
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
