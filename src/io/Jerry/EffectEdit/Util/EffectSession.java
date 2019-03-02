package io.Jerry.EffectEdit.Util;

import org.bukkit.Bukkit;
import org.bukkit.World;

import io.Jerry.EffectEdit.effect.EffectUtil;
import io.Jerry.EffectEdit.lib.LuaEffect;
import io.Jerry.EffectEdit.lib.LuaLocation;

public class EffectSession {
	public LuaEffect le;
	public World world;
	//private ParticlePacket pack;
	
	public EffectSession(LuaEffect le){
		this.le = le;
		//this.pack = new EffectUtil.ParticlePacket(le.type, le.osx, le.osy, le.osz, 0, le.count, false);
	}
	
	public void effect(LuaLocation ll){
		new EffectUtil.ParticlePacket(le.type, le.osx, le.osy, le.osz, 0, le.count, false).sendTo(ll, 16);
	}
	
	public void makeCylinder(LuaLocation loc, double radiusX, double radiusZ, double height, boolean filled) {
	    if (height == 0) {
	    	return;
	    }
	    
	    world = Bukkit.getWorld(loc.world);
	    if(world == null){
	    	return;
	    }
	    
	    double invRadiusX = 1.0D / radiusX;
	    double invRadiusZ = 1.0D / radiusZ;
	    
	    double ceilRadiusX = radiusX;
	    double ceilRadiusZ = radiusZ;
	    
	    double nextXn = 0.0D;
	    forX: for (double x = 0; x <= ceilRadiusX; x = x + 0.25){
	    	double xn = nextXn;
	    	nextXn = (x + 0.25) * invRadiusX;
	    	
	    	double nextZn = 0.0D;
	    	forZ: for (double z = 0; z <= ceilRadiusZ; z = z + 0.25){
	    		double zn = nextZn;
	    		nextZn = (z + 0.25) * invRadiusZ;
	        
	    		double distanceSq = lengthSq(xn, zn);
	    		if (distanceSq > 0.25)	{
	    			if (z == 0) {
                        break forX;
                    }
                    break forZ;
	    		}
	    		
	    		if ((filled) || (lengthSq(nextXn, zn) > 0.25D) || (lengthSq(xn, nextZn) > 0.25D)) {
	    			for (int y = 0; y < height; y++){
	    				effect(loc.add(x, y, z));
	    				effect(loc.add(-x, y, z));
	    				effect(loc.add(x, y, -z));
	    				effect(loc.add(-x, y, -z));
	    			}
	    		}
	    		
	    	}
	    }
	    
	}
	
	public void makeFill(LuaLocation a, LuaLocation b) {
		double x = a.x < b.x ? a.x : b.x;
		double y = a.y < b.y ? a.y : b.y;
		double z = a.z < b.z ? a.z : b.z;
		
		double x2 = a.x < b.x ? b.x : a.x;
		double y2 = a.y < b.y ? b.y : a.y;
		double z2 = a.z < b.z ? b.z : a.z;
		
		for(double x_ = x;x_ <= x2; x_ += 1){
			for(double y_ = y;y_ <= y2; y_ += 1){
				for(double z_ = z;z_ <= z2; z_ += 1){
					effect(new LuaLocation(a.world,x_,y_,z_));
				}	
			}
		}
	}
	
	private static double lengthSq(double x, double z){
	    return x * x + z * z;
	}

	private static double lengthSq(double x, double y, double z){
	    return x * x + y * y + z * z;
	}
	
	public void makeLine(LuaLocation pos1, LuaLocation pos2) {
		//Set<Vector> vset = new HashSet();
	    boolean notdrawn = true;
	    
	    double x1 = pos1.getX();
	    double y1 = pos1.getY();
	    double z1 = pos1.getZ();
	    
	    double x2 = pos2.getX();
	    double y2 = pos2.getY();
	    double z2 = pos2.getZ();
	    
	    double tipx = x1;double tipy = y1;double tipz = z1;
	    
	    double dx = Math.abs(x2 - x1);
	    double dy = Math.abs(y2 - y1);
	    double dz = Math.abs(z2 - z1);
	    
	    
	    if (dx + dy + dz == 0){
	    	effect(new LuaLocation(pos1.world,tipx,tipy,tipz));
	    	notdrawn = false;
	    }
	    
	    if ((notdrawn) && (Math.max(Math.max(dx, dy), dz) == dx)){
	    	for (double domstep = 0; domstep <= dx; domstep+= 0.25){
	    		tipx = x1 + domstep * (x2 - x1 > 0 ? 1 : -1);
	    		tipy = y1 + domstep * dy / dx * (y2 - y1 > 0 ? 1 : -1);
	    		tipz = z1 + domstep * dz / dx * (z2 - z1 > 0 ? 1 : -1);
	        
	    		effect(new LuaLocation(pos1.world,tipx,tipy,tipz));
	    	}
	    	notdrawn = false;
	    }
	    
	    if ((notdrawn) && (Math.max(Math.max(dx, dy), dz) == dy)){
	    	for (double domstep = 0; domstep <= dy; domstep+= 0.25){
	    		tipy = y1 + domstep * (y2 - y1 > 0 ? 1 : -1);
	    		tipx = x1 + domstep * dx / dy * (x2 - x1 > 0 ? 1 : -1);
	    		tipz = z1 + domstep * dz / dy * (z2 - z1 > 0 ? 1 : -1);
	        
	    		effect(new LuaLocation(pos1.world,tipx,tipy,tipz));
	    	}
	    	notdrawn = false;
	    }
	    
	    if ((notdrawn) && (Math.max(Math.max(dx, dy), dz) == dz)){
	    	for (double domstep = 0; domstep <= dz; domstep+= 0.25){
	    		tipz = z1 + domstep * (z2 - z1 > 0 ? 1 : -1);
	    		tipy = y1 + domstep * dy / dz * (y2 - y1 > 0 ? 1 : -1);
	    		tipx = x1 + domstep * dx / dz * (x2 - x1 > 0 ? 1 : -1);
	        
	    		effect(new LuaLocation(pos1.world,tipx,tipy,tipz));
	    	}
	    	notdrawn = false;
	    }
	    
//	    vset = getBallooned(vset, radius);
//	    if (!filled) {
//	      vset = getHollowed(vset);
//	    }
	}

	public void makeOutline(LuaLocation a, LuaLocation b) {
		double x = a.x < b.x ? a.x : b.x;
		double y = a.y < b.y ? a.y : b.y;
		double z = a.z < b.z ? a.z : b.z;
		
		double x2 = a.x < b.x ? b.x : a.x;
		double y2 = a.y < b.y ? b.y : a.y;
		double z2 = a.z < b.z ? b.z : a.z;
		
		for(double x_ = x;x_ <= x2; x_ += 1){
			for(double y_ = y;y_ <= y2; y_ += 1){
				for(double z_ = z;z_ <= z2; z_ += 1){
					if(x_ == x || x_ == x2 || y_ == y || y_ == y2 || z_ == z || z_ == z2)
					effect(new LuaLocation(a.world,x_,y_,z_));
				}	
			}
		}
	}

	public void makeSphere(LuaLocation loc, double radiusX, double radiusY, double radiusZ, boolean filled) {	  	    
	    double invRadiusX = 1.0D / radiusX;
	    double invRadiusY = 1.0D / radiusY;
	    double invRadiusZ = 1.0D / radiusZ;
	    
	    double ceilRadiusX = radiusX;
	    double ceilRadiusY = radiusY;
	    double ceilRadiusZ = radiusZ;
	    
	    //System.out.println("start");
	    double nextXn = 0.0D;
	    forX: for (double x = 0; x <= ceilRadiusX; x = x + 0.25){
	    	//System.out.println("x");
	    	double xn = nextXn;
	    	nextXn = (x + 0.25) * invRadiusX;
	    	
	    	double nextYn = 0.0D;
	    	forY: for (double y = 0; y <= ceilRadiusY; y = y + 0.25){
	    		//System.out.println("y");
	    		double yn = nextYn;
	    		nextYn = (y + 0.25) * invRadiusY;
	      
	    		double nextZn = 0.0D;
	    		forZ: for (double z = 0; z <= ceilRadiusZ; z = z + 0.25){
	    			//System.out.println("z");
	    			double zn = nextZn;
	    			nextZn = (z + 0.25) * invRadiusZ;
	          
	    			double distanceSq = lengthSq(xn, yn, zn);
	    			if (distanceSq > 0.25D){
	    				//System.out.println("z1");
	    				if (z == 0) {
	    					//System.out.println("z2");
                            if (y == 0) {
                            	//System.out.println("z3");
                                break forX;
                            }
                            break forY;
                        }
                        break forZ;
	    			}
	    			if ((filled) || 
	    				(lengthSq(nextXn, yn, zn) > + 0.25) || (lengthSq(xn, nextYn, zn) > + 0.25) || (lengthSq(xn, yn, nextZn) > + 0.25)){
	    				effect(loc.add(x, y, z));
	    				effect(loc.add(-x, y, z));
	    				effect(loc.add(x, -y, z));
	    				effect(loc.add(x, y, -z));
	    				effect(loc.add(-x, -y, z));
	    				effect(loc.add(x, -y, -z));
	    				effect(loc.add(-x, y, -z));
	    				effect(loc.add(-x, -y, -z));
	    			}
	    		}
	    	}
	    }

	}

	
}
