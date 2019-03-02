package io.Jerry.EffectEdit.lib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import io.Jerry.EffectEdit.Util.I18n;

public class LuaLocation extends LuaTable{
	public double x;
	public double y;
	public double z;
	public String world;

	public LuaLocation(){}
	
	public String getWorld(){return world;}
	public double getX(){return x;}
	public double getY(){return y;}
	public double getZ(){return z;}
	
	public LuaLocation(String w, double x, double y, double z) {
		this.world = w;
		this.x = x;
		this.y = y;
		this.z = z;
		set(I18n.t("LuaLocation.world"), this.world);
		set("x", this.x);
		set("y", this.y);
		set("z", this.z);
	}

	public LuaValue call(LuaValue modname, LuaValue env) {		
		env.set(I18n.t("LuaLocation.Title"), new newLoc());
		env.get("package").get("loaded").set(I18n.t("LuaLocation.Title"), new newLoc());
		return env;
	}
	
	static class newLoc extends VarArgFunction {
		public Varargs invoke(Varargs args) {
			int n = args.narg();
			if(n != 4){
				argerror(1,"index out of range");
			}
			return new LuaLocation(args.arg(1).checkjstring(),
					args.arg(2).checkdouble(),
					args.arg(3).checkdouble(),
					args.arg(4).checkdouble());
		}
	}
	
	public boolean eq_b(LuaValue value){
	    if(value instanceof LuaLocation){
	    	LuaLocation t = (LuaLocation)value;
	    	if(t.world.equals(this.world) && t.x == this.x && t.y == this.y && t.z == this.z) {
	    		return true;
	    	}
	    }
	    return false;
	}

	public LuaLocation add(double x2, double y2, double z2) {
		return new LuaLocation(this.world ,this.x + x2, this.y + y2, this.z + z2);
	}

	public Location toLocation() {
		return new Location(Bukkit.getWorld(world),x,y,z);
	}
}
