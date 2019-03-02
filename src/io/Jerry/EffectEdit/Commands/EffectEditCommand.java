package io.Jerry.EffectEdit.Commands;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.luaj.vm2.compiler.LexState;

import io.Jerry.EffectEdit.Main;
import io.Jerry.EffectEdit.Util.EffectLua;
import io.Jerry.EffectEdit.Util.I18n;

public class EffectEditCommand implements CommandExecutor {
	public static final String name = "¡±6" + Main.PL.getName() + "> ¡±f";

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender.isOp() == false){
			return false;  
		}
		
		if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
				 Main.globals = new EffectLua();
				 sender.sendMessage(name + I18n.t("Command.reload", Main.globals.getCount()));
				 return true;
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("unload")){
				if(!Main.globals.hasScript(args[1])){
					sender.sendMessage(name + I18n.t("Command.find"));
					return true;
				}
				Main.globals.unload(args[1]);
				sender.sendMessage(name + I18n.t("Command.unload"));
				return true;
			}
			
			if(args[0].equalsIgnoreCase("load")){
				File f = new File(Main.PL.getDataFolder(),args[1]);
				
				if(!f.exists()){
					sender.sendMessage(name + I18n.t("Command.find"));
					return true;
				}
				Main.globals.load(args[1], f);
				
				if(Main.globals.hasScript(args[1])){
					sender.sendMessage(name + I18n.t("Command.load"));
				}else{
					sender.sendMessage(name + I18n.t("Command.fail"));
				}
				
				return true;
			}
		}
	
		if(args.length > 1 && args[0].equalsIgnoreCase("run")){
			if(!Main.globals.hasScript(args[1])){
				sender.sendMessage(name + I18n.t("Command.find"));
				return true;
			}
			
			String prefix = "";
			if(args.length > 2){
				int i = 2;
				if(args[i].startsWith("loc:")){
					try{
						String[] locInfo = args[i].substring(4).split(",");
						if(locInfo.length == 1 && locInfo[0].equals("here")){
							Location l = ((Player)sender).getLocation();
							prefix += LexState.luaX_tokens[12] + " loc = " + I18n.t("LuaLocation.Title") + "(\"" + 
									l.getWorld().getName() + "\"," +
									l.getX() + "," +
									l.getY() + "," +
									l.getZ() + ");";
						}else{
							prefix += LexState.luaX_tokens[12] + " loc = " + I18n.t("LuaLocation.Title") + "(\"" + 
									locInfo[0] + "\"," +
									locInfo[1] + "," +
									locInfo[2] + "," +
									locInfo[3] + ");";
						}
					}catch(Exception ex){
						sender.sendMessage(name + I18n.t("Command.location"));
						return true;
					}

					i++;
				}
				
				while(i < args.length){
					prefix += args[i] + " ";
					i++;
				}
			}
			
			final String script = prefix;
			new Thread(){
				public void run(){
					try{
						Main.globals.run(args[1], script);
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}.start();
			return true;
		}
		
		sender.sendMessage(name + I18n.t("Command.help"));
		sender.sendMessage("¡±6/" + label + " ¡±erun <name> [loc:<world>,<x>,<y>,<z>] {PREFIX}¡±f - " + I18n.t("Command.help.run"));
		sender.sendMessage("¡±6/" + label + " ¡±eload <name>¡±f - " + I18n.t("Command.help.load"));
		sender.sendMessage("¡±6/" + label + " ¡±eunload <name>¡±f - " + I18n.t("Command.help.unload"));
		sender.sendMessage("¡±6/" + label + " ¡±ereload¡±f - " + I18n.t("Command.help.reload"));
		return true;
	}
	
}
