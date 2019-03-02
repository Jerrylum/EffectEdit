package io.Jerry.EffectEdit.effect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import io.Jerry.EffectEdit.Main;
import io.Jerry.EffectEdit.Util.Particle;
import io.Jerry.EffectEdit.lib.LuaLocation;

public class EffectUtil {
//	public static void display(Particle effect, Location location){
//		display(effect, location, null);
//	}
//		  
//	public static void display(Particle particle, Location location, Color color){
//		display(particle, location, color, 0, 1);
//	}
//		  
//	public static void display(Particle particle, Location location, float speed, int amount){
//		display(particle, location, null, speed, amount);
//	}
//		  
//	public static void display(Particle particle, Location location, Color color, float speed, int amount){
//		particle.display(particle.getData(null, null), location, color, 32, 0, 0, 0, speed, amount);
// 	}
//	
//	public static void main(String[] args) throws Exception   {   
//		display(new File("F:\\Server\\Backup\\ABC.java"));
//    }
//	
//	public static void display(File F) throws Exception{
//		
//	}

public static final class ParticlePacket{
	private static int version;
	private static boolean isKcauldron;
	private static Class<?> enumParticle;
    private static Constructor<?> packetConstructor;
    private static Method getHandle;
    private static Field playerConnection;
    private static Method sendPacket;
    private static boolean initialized;
    private final Particle effect;
    private final float offsetX;
    private final float offsetY;
    private final float offsetZ;
    private final float speed;
    private final int amount;
    private final boolean longDistance;
    //private final Particle.ParticleData data;
    private Object packet;
	    
    public ParticlePacket(Particle effect, float offsetX, float offsetY, float offsetZ, float speed, int amount, boolean longDistance/*, Particle.ParticleData data*/){
    	try {initialize();} catch (Exception e) {}
        this.effect = effect;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
        this.amount = amount;
  	    this.longDistance = longDistance;
        //this.data = data;
    }
		    
	public static void initialize() throws Exception{
		if (initialized) {
		    return;
		}
		
		try{
		    isKcauldron = false;
		    version = Integer.parseInt(Character.toString(ReflectionUtils.PackageType.getServerVersion().charAt(3)));
		    if (version > 7) {
		        enumParticle = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("EnumParticle");
		    }
		    Class<?> packetClass = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass(version < 7 ? "Packet63WorldParticles" : "PacketPlayOutWorldParticles");
		    packetConstructor = ReflectionUtils.getConstructor(packetClass, new Class[0]);
		    getHandle = ReflectionUtils.getMethod("CraftPlayer", ReflectionUtils.PackageType.CRAFTBUKKIT_ENTITY, "getHandle", new Class[0]);
		    playerConnection = ReflectionUtils.getField("EntityPlayer", ReflectionUtils.PackageType.MINECRAFT_SERVER, false, "playerConnection");
		    sendPacket = ReflectionUtils.getMethod(playerConnection.getType(), "sendPacket", new Class[] { ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("Packet") });
		}catch (Exception exception){
		    isKcauldron = true;
		    version = Integer.parseInt(Character.toString(ReflectionUtils.PackageType.getServerVersion().charAt(3)));
		    Class<?> packetClass = Class.forName("net.minecraft.network.play.server.S2APacketParticles");
		    packetConstructor = ReflectionUtils.getConstructor(packetClass, new Class[0]);
		    getHandle = ReflectionUtils.getMethod("CraftPlayer", ReflectionUtils.PackageType.CRAFTBUKKIT_ENTITY, "getHandle", new Class[0]);
		    playerConnection = Class.forName("net.minecraft.entity.player.EntityPlayerMP").getDeclaredField("field_71135_a");
		    sendPacket = playerConnection.getType().getDeclaredMethod("func_147359_a", new Class[] { Class.forName("net.minecraft.network.Packet") });
		}
		      
		initialized = true;
	}
		    
	public static int getVersion(){
		return version;
	}
		    
	public static boolean isInitialized(){
		return initialized;
	}
		    
	public void sendTo(LuaLocation center, Player player){
		try{
			if (isKcauldron) {
				sendToWithKCauldron(center, player);
			} else {
			    sendToWithBukkit(center, player);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
		    
	private void sendToWithKCauldron(LuaLocation center, Player player) throws Exception{
		if (this.packet == null) {
//			try{
		        this.packet = packetConstructor.newInstance(new Object[0]);
		        ReflectionUtils.setValue(this.packet, true, "field_149236_a", this.effect.name());
		        ReflectionUtils.setValue(this.packet, true, "field_149234_b", Float.valueOf((float)center.getX()));
		        ReflectionUtils.setValue(this.packet, true, "field_149235_c", Float.valueOf((float)center.getY()));
		        ReflectionUtils.setValue(this.packet, true, "field_149232_d", Float.valueOf((float)center.getZ()));
		        ReflectionUtils.setValue(this.packet, true, "field_149233_e", Float.valueOf(this.offsetX));
		        ReflectionUtils.setValue(this.packet, true, "field_149230_f", Float.valueOf(this.offsetY));
		        ReflectionUtils.setValue(this.packet, true, "field_149231_g", Float.valueOf(this.offsetZ));
		        ReflectionUtils.setValue(this.packet, true, "field_149237_h", Float.valueOf(this.speed));
		        ReflectionUtils.setValue(this.packet, true, "field_149238_i", Integer.valueOf(this.amount));
//		    }catch (Exception exception){
//		        throw new PacketInstantiationException("Packet instantiation failed", exception);
//		    }
		}
//		try{
		    sendPacket.invoke(playerConnection.get(getHandle.invoke(player, new Object[0])), new Object[] { this.packet });
//		}catch (Exception exception){
//		    throw new PacketSendingException("Failed to send the packet to player '" + player.getName() + "'", exception);
//		}
	}
		    
	private void sendToWithBukkit(LuaLocation center, Player player) throws Exception{
		if (this.packet == null) {
//		    try{
				this.packet = packetConstructor.newInstance(new Object[0]);
		        Object id;
		        if (version < 8) {
		            id = this.effect.name() + "";
		        } else {
		            id = enumParticle.getEnumConstants()[this.effect.ordinal()];
		        	//id = this.effect.ordinal();
		        }
		        ReflectionUtils.setValue(this.packet, true, "a", id);
		        ReflectionUtils.setValue(this.packet, true, "b", Float.valueOf((float)center.getX()));
		        ReflectionUtils.setValue(this.packet, true, "c", Float.valueOf((float)center.getY()));
		        ReflectionUtils.setValue(this.packet, true, "d", Float.valueOf((float)center.getZ()));
		        ReflectionUtils.setValue(this.packet, true, "e", Float.valueOf(this.offsetX));
		        ReflectionUtils.setValue(this.packet, true, "f", Float.valueOf(this.offsetY));
		        ReflectionUtils.setValue(this.packet, true, "g", Float.valueOf(this.offsetZ));
		        ReflectionUtils.setValue(this.packet, true, "h", Float.valueOf(this.speed));
		        ReflectionUtils.setValue(this.packet, true, "i", Integer.valueOf(this.amount));
		        if (version > 7){
		            ReflectionUtils.setValue(this.packet, true, "j", Boolean.valueOf(this.longDistance));
		            //ReflectionUtils.setValue(this.packet, true, "k", this.data == null ? new int[0] : this.data.getPacketData());
		        }
//		        }
//		        catch (Exception exception)
//		        {
//		          throw new PacketInstantiationException("Packet instantiation failed", exception);
//		        }
		}
//		      try
//		      {
		        sendPacket.invoke(playerConnection.get(getHandle.invoke(player, new Object[0])), new Object[] { this.packet });
//		      }
//		      catch (Exception exception)
//		      {
//		        throw new PacketSendingException("Failed to send the packet to player '" + player.getName() + "'", exception);
//		      }
	}
		    
		    public void sendTo(LuaLocation center, List<Player> players)
		      throws IllegalArgumentException
		    {
		      for (Player player : players) {
		        sendTo(center, player);
		      }
		    }
		    
		    public void sendTo(LuaLocation center, double range)
		      throws IllegalArgumentException
		    {

		    	String worldName = center.getWorld();
		    	Location l = center.toLocation();
		        double squared = range * range;
		        for (Player player : Main.getOnlinePlayers()) {
		          if ((player.getWorld().getName().equals(worldName)) && (player.getLocation().distanceSquared(l) <= squared)) {
		            sendTo(center, player);
		          }
		        }
			    
		    }
		    
		    
	}

}
