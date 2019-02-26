package io.dreamfantaisy.emul;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import li.cil.repack.com.naef.jnlua.JavaFunction;
import li.cil.repack.com.naef.jnlua.LuaState;

public class ComputerLib {

	public static final int SHUTDOWN = 1;
	public static final int SLEEP = 2;
	private int yieldAction;
	private int yieldArg1;
	private static long start;
	private Computer comp;
	
	public int getYieldAction() {
		return yieldAction;
	}
	
	public int getYieldArgInt1() {
		return yieldArg1;
	}
	
	public void resetYieldAction() {
		this.yieldAction = 0;
	}
	
	public ComputerLib(LuaState state, Computer comp) {
		this.comp = comp;
		start = System.currentTimeMillis();
		state.newTable();
		state.pushJavaFunction(new JavaFunction() {

			@Override
			public int invoke(LuaState lua) {
				lua.newTable();
				int ri = 1;
				for (int i = 0; i < 64; i++) {
					lua.pushNumber(ri);
					lua.pushJavaObject(Components.COMPONENTS[i]);
					//System.out.println(i + ": " + Components.COMPONENTS[i]);
					lua.rawSet(-3);
					ri++;
				}
				
				return 1;
			}
			
		});
		state.setField(-2, "components");
		
		state.pushJavaFunction(new JavaFunction() {

			@Override
			public int invoke(LuaState lua) {
				lua.checkString(1);
				String str = lua.toString(1);
				BufferedInputStream bis = null;
				try {
					if (str.equals("data.bin")) {
						throw new Exception(); // The program shouldn't know it is a file.
					}
					bis = new BufferedInputStream(new FileInputStream("filesystems/rom/" + str));
					byte[] b = bis.readAllBytes();
					StringBuilder sb = new StringBuilder();
					for (byte b1 : b) {
						sb.append((char) b1);
					}
					lua.pushString(sb.toString());
					bis.close();
					return 1;
				} catch (Exception e) {
					e.printStackTrace();
					lua.pushNil();
					lua.pushString("file not found");
					return 1;
				}
			}
			
		});
		state.setField(-2, "loadFromROM");
		
		state.pushJavaFunction(new JavaFunction() {

			@Override
			public int invoke(LuaState lua) {
				try {
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream("filesystems/rom/data.bin"));
					byte[] b = bis.readAllBytes();
					StringBuilder sb = new StringBuilder();
					for (byte b1 : b) {
						sb.append((char) b1);
					}
					lua.pushString(sb.toString());
					bis.close();
				} catch (Exception e) {
					lua.pushString("");
					return 1;
				}
				return 1;
			}
			
		});
		state.setField(-2, "getROMData");
		
		state.pushJavaFunction(new JavaFunction() {

			@Override
			public int invoke(LuaState lua) {
				long uptime = System.currentTimeMillis() - start;
				lua.pushInteger((int) uptime);
				return 1;
			}
			
		});
		state.setField(-2, "uptime");
		
		state.pushJavaFunction(new JavaFunction() {

			@Override
			public int invoke(LuaState lua) {
				lua.checkString(1);
				String str = lua.toString(1);
				if (str.length() > 512) {
					lua.pushBoolean(false);
					lua.pushString("data too long");
				}
				// TODO
				lua.pushBoolean(false);
				lua.pushString("cannot edit PROM");
				return 1;
			}
			
		});
		state.setField(-2, "setROMData");
		
		state.pushJavaFunction(new JavaFunction() {

			@Override
			public int invoke(LuaState lua) {
				lua.checkString(1);
				lua.checkString(2);
				String drv = lua.toString(1);
				String str = lua.toString(2);
				BufferedInputStream bis = null;
				try {
					if (drv.equals("rom")) {
						throw new Exception();
					}
					bis = new BufferedInputStream(new FileInputStream("filesystems/" + drv + "/" + str));
					byte[] b = bis.readAllBytes();
					StringBuilder sb = new StringBuilder();
					for (byte b1 : b) {
						sb.append((char) b1);
					}
					lua.pushString(sb.toString());
					lua.pushNil();
					bis.close();
					return 2;
				} catch (Exception e) {
					//e.printStackTrace();
					lua.pushNil();
					lua.pushString("file (" + str + ") or drive (" + drv + ") not found");
					return 2;
				}
			}
			
		});
		state.setField(-2, "loadFromDrive");
		
		state.pushJavaFunction(new JavaFunction() {

			@Override
			public int invoke(LuaState arg0) {
				yieldAction = SHUTDOWN;
				arg0.yield(0);
				return 0;
			}
			
		});
		state.setField(-2, "shutdown");
		
		state.pushJavaFunction(new JavaFunction() {

			@Override
			public int invoke(LuaState lua) {
				lua.checkInteger(1);
				yieldArg1 = lua.toInteger(1);
				yieldAction = SLEEP;
				lua.yield(0);
				return 0;
			}
			
		});
		state.setField(-2, "sleep");
		
		state.setGlobal("computer");
	}

}
