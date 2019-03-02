package io.Jerry.EffectEdit;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import io.Jerry.EffectEdit.Util.Metrics;
import io.Jerry.EffectEdit.Util.Updater;
import io.Jerry.EffectEdit.Commands.EffectEditCommand;
import io.Jerry.EffectEdit.Util.EffectLua;
import io.Jerry.EffectEdit.Util.I18n;

public class Main extends JavaPlugin implements Listener{
	public static String Name;
	public static Plugin PL;
	public static FileConfiguration c;
	public static EffectLua globals;
	
	public static void main(String[] args){}

	public void onEnable(){		
		PL = this;
		Name = this.getName();
		c = getConfig();
		c.addDefault("Lang", "en_US");
		c.options().copyDefaults(true);
		saveConfig();
		
		for(String str : getDescription().getCommands().keySet()){
			getCommand(str).setExecutor(new EffectEditCommand());
		}
		getServer().getPluginManager().registerEvents(this, this);
		
		I18n.run(c.getString("Lang","en_US"));
		globals = new EffectLua();
		
		try {
			new Metrics(this).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		updateCheck(Bukkit.getConsoleSender());
	}
	
	public void onDisable(){
		
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e){
		updateCheck(e.getPlayer());
	}
	
//	public void updateCheck(){
//		getLogger().info(I18n.t("Main.Done"));
//		
//		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
//			@Override
//			public void run() {
//				getLogger().info(I18n.t("Main.Checking"));
//				String oldVer = Main.this.getDescription().getVersion();
//				if(!Updater.check()){
//					getLogger().info(I18n.t("Main.CheckWrong"));
//					return;
//				}
//				
//				String newVer = Updater.getVersion();
//				if(newVer.equals(oldVer)){
//					getLogger().info(oldVer + " " + I18n.t("Main.TheNew"));
//				}else{
//					getLogger().info(I18n.t("Main.FoundNew", newVer));
//					if(Main.c.getBoolean("AutoUpdate")){
//						getLogger().info(I18n.t("Main.TryToUpdate"));
//						if(Updater.update(Main.this.getFile())){
//							getLogger().info(I18n.t("Main.UpdateDone"));
//							getLogger().info(I18n.t("Main.UpdateDone2",Updater.getInfoUrl()));
//						}else{
//							getLogger().info(I18n.t("Main.UpdateWrong"));
//							getLogger().info(Updater.getInfoUrl());
//						}
//						getLogger().info(I18n.t("Main.AutoUpdate"));
//					}else{
//						getLogger().info(I18n.t("Main.StopAutoUpdate"));
//						getLogger().info(Updater.getInfoUrl());
//					}
//				}
//				
//				
//			}
//		});
//		
//		
//	}

	public static List<Player> getOnlinePlayers() {
	    List<Player> list = Lists.newArrayList();
	    for (World world : Bukkit.getWorlds()) {
	        list.addAll(world.getPlayers());
	    }
	    return Collections.unmodifiableList(list);
	}
	
	public static void updateCheck(CommandSender sender){
		if(sender.isOp() == false){
			return;
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(PL, new Runnable(){
			@Override
			public void run() {
				if(!Main.c.getBoolean("AutoUpdate")){
					return;
				}
				
				sender.sendMessage(EffectEditCommand.name + "Updater");
				sender.sendMessage(I18n.t("Main.Checking"));
				if(!Updater.check()){
					sender.sendMessage(I18n.t("Main.CheckWrong"));
					return;
				}
				
				String oldVer = PL.getDescription().getVersion();
				String newVer = Updater.getVersion();
				if(newVer.equals(oldVer)){
					sender.sendMessage(oldVer + " " + I18n.t("Main.TheNew"));
				}else{
					sender.sendMessage(I18n.t("Main.FoundNew", newVer));
				}
				
				
			}
		});
	}
}
