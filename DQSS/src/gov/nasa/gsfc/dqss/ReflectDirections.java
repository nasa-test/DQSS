package gov.nasa.gsfc.dqss;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

public class ReflectDirections {
	
    protected ReflectDirections rfldir = null;
	protected Class reflectedClass = null;
	protected Object reflist[] = new Object[2];
	static Logger logger = Logger.getLogger(ReflectDirections.class.getName());
	
	
	
	public ReflectDirections(){
		 try {
			reflectedClass = Class.forName("gov.nasa.gsfc.dqss.ReflectDirections");
		} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
				
	 }
	
	public boolean lt(int a, int b) {
		if (a < b ) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean lt(float a, float b) {
		if (a < b ) {
			return true;
		}
		else {
			return false;
		}
	}	
	public boolean gt(float a, float b) {
		if (a > b ) {
			return true;
		}
		else {
			return false;
		}
	}	
	public boolean lt(byte a, float b) {
		if (a < b ) {
			return true;
		}
		else {
			return false;
		}
	}	
	public boolean lt(float a, long b) {
		if (a < b ) {
			return true;
		}
		else {
			return false;
		}
	}	
	public boolean gt(int a, int b) {
		if (a > b ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean le(int a, int b) {
		if (a <= b ) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean notbetween(float a, float b,  float c ) {
		if (a < b || a > c ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean le(short a, long b) {
		if (a <= b ) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean le(byte a, long b) {
		if (a <= b ) { // criteria is b, this pixel is a
			return true;
		}
		else {
			return false;
		}
	}
	public boolean le(float a, float b) {
		if (a <= b ) { // criteria is b, this pixel is a
			return true;
		}
		else {
			return false;
		}
	}
	public boolean lt(byte a, long b) {
		if (a < b ) { // criteria is b, this pixel is a
			return true;
		}
		else {
			return false;
		}
	}
	public boolean ge(int a, int b) {

		if (a >= b ) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean ge(int a, long b) {

		if (a >= b ) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean ge(float a, float b) {
	
		if (a >= b ) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean ge(float a, long b) {
		
		if (a >= b ) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean ge(int a, short b) {

		if (a >= b ) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean ge(byte a, long b) {
		
		if (a >= b ) {
			return true;
		}
		else {
			return false;
		}
	}
	
public boolean ge(byte a, byte b) {
		
		if (a >= b ) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean eq(int a, int b) {
		if (a == b ) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean eq(float a, float b) {
		if (a == b ) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean eq(byte a, byte b) {
		if (a == b ) {
			return true;
		}
		else {
			return false;
		}
	}
	public Method getMethod(String direction, Class typea, Class typeb) {
			   Method refl = null;
			   Class types[] = new Class[2];
			   types[0] = typea;
			   types[1] = typeb;
		
		    	try {
		    		if (direction.contains("negative") ) {
		    			refl = reflectedClass.getMethod("le", types);
		    		}
		    		else if (direction.contains("positive") ) {
		    			refl = reflectedClass.getMethod("ge", types);
		    		}
		    		else if (direction.contains("lessThan") ) {
		    			refl = reflectedClass.getMethod("lt", types);
		    		}
		    		else if (direction.contains("greaterThan") ) {
		    			refl = reflectedClass.getMethod("gt", types);
		    		}
		    		else if (direction.contains("equals") ) {
		    			refl = reflectedClass.getMethod("eq", types);
		    		}
		    		else if (direction.contains("greaterThanOrEqualTo") ) {
		    			refl = reflectedClass.getMethod("ge", types);
		    		}
		    		else {
		    			System.err.println("Could not find method for:" + direction);
		    			System.exit(2);
		    		}
		    		
		    		
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error("Could not find a reflection method for direction:" + direction);
				}
				logger.debug(refl.getName());
		return refl;
	}
	public Method get3Method(String direction, Class typea, Class typeb, Class typec) {
		   Method refl = null;
		   Class types[] = new Class[3];
		   types[0] = typea;
		   types[1] = typeb;
		   types[2] = typec;
	
	    	try {
	    		if (direction.contains("notbetween") ) {
	    			refl = reflectedClass.getMethod("notbetween", types);
	    		}
	    		else if (direction.contains("positive") ) {
	    			refl = reflectedClass.getMethod("ge", types);
	    		}
	    		else if (direction.contains("lessThan") ) {
	    			refl = reflectedClass.getMethod("lt", types);
	    		}
	    		else if (direction.contains("equals") ) {
	    			refl = reflectedClass.getMethod("eq", types);
	    		}
	    		else if (direction.contains("greaterThanOrEqualTo") ) {
	    			refl = reflectedClass.getMethod("ge", types);
	    		}
	    		else {
	    			System.err.println("Could not find method for:" + direction);
	    			System.exit(2);
	    		}
	    		
	    		
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("Could not find a reflection method for direction:" + direction);
			}
			logger.debug(refl.getName());
	return refl;

	}
}
