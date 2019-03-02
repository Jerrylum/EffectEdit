package io.Jerry.EffectEdit.lib;

import java.util.HashMap;

import io.Jerry.EffectEdit.Util.Particle;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import io.Jerry.EffectEdit.Util.EffectSession;
import io.Jerry.EffectEdit.Util.I18n;

public class LuaEffect extends LuaTable{
	private static HashMap<String,String> list = null;
	
	public Particle type;
	public int count;
	public int osx;
	public int osy;
	public int osz;
	public EffectSession es;
	
	public LuaEffect(){}
	
	public LuaEffect(String type,int count,int osx,int osy,int osz) {
		this.type = getParticle(type);
		this.count = count;
		this.osx = osx;
		this.osy = osy;
		this.osz = osz;
		this.es = new EffectSession(this);
		set(I18n.t("LuaEffect.circle"),new Circle(this));
		set(I18n.t("LuaEffect.fill"),new Fill(this));
		set(I18n.t("LuaEffect.line"),new Line(this));
		set(I18n.t("LuaEffect.outline"),new Outline(this));
		set(I18n.t("LuaEffect.point"),new Point(this));
		set(I18n.t("LuaEffect.sphere"),new Sphere(this));
		//set(I18n.t("LuaEffect.circle"),new Circle(this));
	}

	public LuaValue call(LuaValue modname, LuaValue env) {		
		env.set(I18n.t("LuaEffect.Title"), new newEffect());
		env.get("package").get("loaded").set(I18n.t("LuaEffect.Title"), new newEffect());
		return env;
	}
	
	static class Circle extends VarArgFunction {
		LuaEffect le;
		public Circle(LuaEffect le) {
			this.le = le;
		}

		public Varargs invoke(Varargs args) {			
			LuaLocation loc;
			double radiusX;
			double radiusZ;
			double up;
		    boolean fill;
			
			int n = args.narg();
			if(n == 5){
				loc = (LuaLocation) args.checkvalue(1);
				radiusX = args.checkdouble(2);
				radiusZ = args.checkdouble(3);
				up = args.checkdouble(4);
				fill = args.checkboolean(5);
//			}else if(n == 4){
//				LuaLocation loc1 = (LuaLocation) args.checkvalue(1);
//				LuaLocation loc2 = (LuaLocation) args.checkvalue(2);
//				if(!loc1.world.equals(loc2.world)){
//					argerror(1,"two location have different would");
//				}
//				
//				if(loc1.y < 0 || loc2.y <0){
//					argerror(1,"y asix can not under then 0");
//				}
//				
//				loc = new LuaLocation(loc1.world,
//						Math.abs(Math.abs(loc1.x) - Math.abs(loc2.x))/2,
//						Math.abs(Math.abs(loc1.y) - Math.abs(loc2.y))/2,
//						Math.abs(Math.abs(loc1.z) - Math.abs(loc2.z))/2);
//				radiusX = (Math.abs(loc1.x) + Math.abs(loc2.x)) /2;
//				radiusZ = (Math.abs(loc1.z) + Math.abs(loc2.z)) /2;
//				up = Math.abs(loc1.y - loc2.y);
//				fill = args.checkboolean(4);
			}else{
				argerror(1,"index out of range");
				return NONE;
			}
			
			le.es.makeCylinder(loc,radiusX,radiusZ,up,fill);
			
			
			
			return NONE;
		}
	}
	
	static class Fill extends TwoArgFunction {
		LuaEffect le;
		public Fill(LuaEffect le) {
			this.le = le;
		}

		public LuaValue call(LuaValue arg1, LuaValue arg2) {			
			le.es.makeFill((LuaLocation) arg1,(LuaLocation) arg2);
			return NONE;
		}
	}
	
	static class Line extends TwoArgFunction {
		LuaEffect le;
		public Line(LuaEffect le) {
			this.le = le;
		}

		public LuaValue call(LuaValue arg1, LuaValue arg2) {			
			le.es.makeLine((LuaLocation) arg1,(LuaLocation) arg2);
			return NONE;
		}
	}
	
	static class Outline extends TwoArgFunction {
		LuaEffect le;
		public Outline(LuaEffect le) {
			this.le = le;
		}

		public LuaValue call(LuaValue arg1, LuaValue arg2) {			
			le.es.makeOutline((LuaLocation) arg1,(LuaLocation) arg2);
			return NONE;
		}
	}
	
	static class Point extends OneArgFunction {
		LuaEffect le;
		public Point(LuaEffect le) {
			this.le = le;
		}

		public LuaValue call(LuaValue args) {			
			le.es.effect((LuaLocation) args);
			return NONE;
		}
	}
	
	static class Sphere extends VarArgFunction {
		LuaEffect le;
		public Sphere(LuaEffect le) {
			this.le = le;
		}

		public Varargs invoke(Varargs args) {			
			LuaLocation loc;
			double radiusX;
			double radiusY;
			double radiusZ;
		    boolean fill;
			
			int n = args.narg();
			if(n == 5){
				loc = (LuaLocation) args.checkvalue(1);
				radiusX = args.checkdouble(2);
				radiusY = args.checkdouble(3);
				radiusZ = args.checkdouble(4);
				fill = args.checkboolean(5);
//			}else if(n == 3){
//				LuaLocation loc1 = (LuaLocation) args.checkvalue(1);
//				LuaLocation loc2 = (LuaLocation) args.checkvalue(2);
//				if(!loc1.world.equals(loc2.world)){
//					argerror(1,"two location have different would");
//				}
//				
//				if(loc1.y < 0 || loc2.y <0){
//					argerror(1,"y asix can not under then 0");
//				}
//				
//				loc = new LuaLocation(loc1.world,
//						Math.abs(Math.abs(loc1.x) - Math.abs(loc2.x))/2,
//						Math.abs(Math.abs(loc1.y) - Math.abs(loc2.y))/2,
//						Math.abs(Math.abs(loc1.z) - Math.abs(loc2.z))/2);
//				radiusX = (Math.abs(loc1.x) + Math.abs(loc2.x)) /2;
//				radiusY = (Math.abs(loc1.y) + Math.abs(loc2.y)) /2;
//				radiusZ = (Math.abs(loc1.z) + Math.abs(loc2.z)) /2;
//				fill = args.checkboolean(3);
			}else{
				argerror(1,"index out of range");
				return NONE;
			}
			
			le.es.makeSphere(loc,radiusX,radiusY,radiusZ,fill);
			
			
			
			return NONE;
		}
	}
	
	static class newEffect extends VarArgFunction {
		public Varargs invoke(Varargs args) {
			int n = args.narg();
			if(n > 5 || n < 2){
				argerror(1,"index out of range");
			}
			return new LuaEffect(args.checkjstring(1),
					args.checkint(2),
					n > 2 ? args.checkint(3) : 0,
					n > 3 ? args.checkint(4) : 0,
					n > 4 ? args.checkint(5) : 0);
		}
	}
	
	public boolean eq_b(LuaValue value){
	    if(value instanceof LuaEffect){
	    	LuaEffect t = (LuaEffect)value;
	    	if(t.type.equals(this.type) && t.osx == this.osx && t.osy == this.osy && t.osz == this.osz && t.count == this.count) {
	    		return true;
	    	}
	    }
	    return false;
	}
	
	private Particle getParticle(String type){
		if(type == null || type.equals("")){
			argerror(1,"unknow particle type");
		}

		if(list == null){
			list = new HashMap<String,String>();
			try{
				String strList = I18n.t("LuaEffect.Type");
				String[] split = strList.split(";");
				String[] split2;
				for(String str : split){
					split2 = str.split(":");
					
					list.put(split2[0], split2[1]);
				}
			}catch(Exception e){}
		}
		

		String name = list.get(type);
		if(name == null){
			name = type.toUpperCase();
		}
		return Particle.valueOf(name);
	}
}
