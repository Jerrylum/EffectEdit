package io.Jerry.EffectEdit.Util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.jse.JsePlatform;

import io.Jerry.EffectEdit.Main;

public class EffectLua {
	private Globals globals;
	private HashMap<String,String> list;
	
	public EffectLua(){
		globals = JsePlatform.standardGlobals();
		list = new HashMap<String,String>();
		for(File subF : Main.PL.getDataFolder().listFiles()){
			load(subF.getName(),subF);
		}
		
	}
	
	public void load(String name,File f){
		if(f.isDirectory()){
			for(File subF : f.listFiles()){
				load(name + File.pathSeparator + subF.getName(),subF);
			}
		}else{
			String script = "";
			if(name.toLowerCase().endsWith(".lua")){
				try{
					for(String s : Files.readAllLines(f.toPath(), StandardCharsets.UTF_8)){
						script += s + "\r\n";
					}
					list.put(name.substring(0, name.length()-4), script);
					Main.PL.getLogger().info("loaded " + name);
				}catch(Exception ex){
					Main.PL.getLogger().info("failed to load " + name);
				}
			}
			
		}
		
	}
	
	public void load(String name,String script){
		list.put(name, script);
	}
	
	public void unload(String name){
		list.put(name, null);
	}
	
	public boolean hasScript(String name){
		return list.containsKey(name);
	}
	
	public int getCount(){
		return list.size();
	}
	
	public void run(String name,String str) throws Exception{
		String script = list.get(name);
		if(script == null || script.equals("")){
			throw new NullPointerException("script \"" + name + "\" content could not be null or empty");
		}
		
		Prototype p = LuaC.instance.compile(str + script, name);
		LuaClosure f = new LuaClosure(p, globals);
		f.call();
	}
}
