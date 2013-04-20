package gov.nasa.gsfc.dqss;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class screeningfield {

	 private List<String> dims    = new ArrayList<String>();
	 private String name = null;
	 private String type = null;
	 private String direction = null;
	 private String constrel = null;
	 private String datatype = null;
	 private long level = -777;
	 private Class reflectType;
	 private int rank=0;
	 private long[] dimsize;
	 private long startbit;
	 private long stopbit;
	 private long startlayer;
	 private long stoplayer;
	 private boolean endian = true;
	 private String variable;
	 private String ColOrRowMatch;
	 
    static Logger logger = Logger.getLogger(DataFile.class.getName());
    
	 public screeningfield(String id) {
		 name = id;
	 }
	 
	 public void addDimension(String dimName, int index) {
		 if (existsAlready(dimName)) {
			 return;
		 }
		
		 dims.add(index,dimName);
	 }
	 public void setVariable(String t) {
		 this.variable = t;
	 }
	 public void setRank(long t) {
		 rank = (int)t;
	 }
	 public void setDimSize(int ith, long size) {
		 if (dimsize == null) {
			 if (rank ==0 ) {
				 logger.error("Rank needs to be defined first");
				 return;
			 }
			 dimsize = new long[rank];
		 }
		 dimsize[ith] = size;
	 }
	 private boolean existsAlready(String id) {
			Iterator itr = dims.iterator();
			while(itr.hasNext()) {
				String dimen = (String) itr.next();
				if (dimen.contains(id)) {
					return true;
				}
			}
			return false;
		}
	 public int dimcount () {
		 return dims.size();
	 }
	 
	 public String getName() {
		 return name;
	 }
	 
	 public String getType() {
		 return type;
	 }
	 public String getVariable() {
		 return this.variable;
	 }
	 public Class getClassDatatype() {
		 return this.reflectType;
	 }
	 
	 public String getDirection() {
		 return direction;
	 }
	 public String getConstraintRelationship () {
		 return this.constrel;
	 }
	 public String getDimension(int i) {
		 if (dims.size() < i-1) {
			 logger.warn("2 dimensional datasets may not have this dimension defined. Not sure if this is an ERROR");
			 return "";
		 }
		 return dims.get(i);
	 }
	 public String getDatatype() {
		 return  this.datatype;
	 }
	 public List<String> getDimensions() {
		 return dims;
	 }
	 public Long getLevel() {
		 return level;
	 }
	 public long getStartBit() {
		 return this.startbit;
	 }
	 public long getStopBit() {
		 return this.stopbit;
	 }
	 public boolean getEndian() {
		 return this.endian;
	 }
	 public int getRank() {
		 return rank;
	 }
	 public void set1DType(String t) {
		 ColOrRowMatch = t;
	 }
	 public String get1DType() {
		 return ColOrRowMatch;
	 }
 	 public void setDatatype (String t) {
			   
				if (t.contains("INT8")) {
		    		reflectType = Byte.TYPE;
		    	}
				else if (t.contains("INT16")) {
		    		reflectType = Short.TYPE;
		    	}
				else if (t.contains("INT32")) {
		    		reflectType = Long.TYPE;
		    	}
				else if (t.contains("FLOAT32")) {
		    		reflectType = Float.TYPE;
		    	}
				else {
					logger.error("No handler yet for datatype:" + t );
				}
				if (t != null) {
					this.datatype = t;
				}
	 }
	 public void setDir (String t) {
		 if (direction == null && t != null) {
			 direction = t;
		 }
		 
	 }
	 public void setLevel (long lev) {
		 level = lev;
	 }
	 
	 public void setConstraintRelationship (String t) {
		 constrel = t;
	 }
	 public void setType(String t) {
		 this.type = t;
	 }
	 public void setStartBit(long t) {
		 this.startbit = t;
	 }
	 public void setStopBit(long t) {
		 this.stopbit = t;
	 }
	 public void setStartLayer(long t) {
		 this.startlayer = t;
	 }
	 public void setStopLayer(long t) {
		 this.stoplayer = t;
	 }
	 public void setEndian(String t) {
		 t = t.toLowerCase();
		 if (t.contains("big")) {
			 this.endian = true;
		 }
		 else {
			 endian = false;
		 }
	 }
}
