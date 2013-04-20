package gov.nasa.gsfc.dqss;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public abstract class datafield {

	 List<String> dims;//    = new ArrayList<String>();
	 String name = null;
	 String datatype = null;
	 int rank=0;
	 long[] dimsize; 
	 static Logger logger = Logger.getLogger(DataFile.class.getName());
	 long[] layers;
	 Class kind;
	 public Object type;
	 
	 public datafield(){
		 
	 }
	 
	 public datafield(String id) {
		
		 name = id.replace("View", "");
		 
	 }
	 public void setName (String id ) {
		 name = id;
	 }
	 public void setDataType (String t) {
		 datatype = t;
		 if (t.contains("INT16")) {
			 kind = Short.TYPE;
		 }
	 }
	 public void addDimension(String dimName) {
		 if (existsAlready(dimName)) {
			 return;
		 }
		 dims.add(dimName);
	 }
	 public void addDimension(String dimName,int index) {
		 if (dimName.isEmpty() || dimName.contains("none")) {
			 dimName = "Dim" + dimsize[index];
		 }
		 if (existsAlready(dimName)) {
			 return;
		 }
		//Note dimensions in ontology are not always in order
		if (dims.size() < index+1) {
			for (int i = dims.size(); i <= index+1; ++i) {
				dims.add("");
			}
		}
		dims.set(index,dimName);
	 }
	 public void cleanUpDims() {
			for (int i = 0; i < dims.size(); ++i) {
				if (dims != null) {
					if (dims.get(i) != null) {
						if (dims.get(i).isEmpty()) {
							dims.remove(i);
						}
					}
					else {
						logger.warn("dimension issue?");
					}
				}
				else {
					logger.warn("dimension issue?");
				}
			}
	 }
	 public long[] getLayers() {
		 return layers;
	 }
	 public List<String> getDimensions() {
		 return dims;
	 }
	 public void setRank(int t) {
		 rank = t;
	 }
	 public void setDimSize(int ith, long size) {
		 if (dimsize == null) {
			 if (rank ==0 ) {
				 logger.error("Rank needs to be defined first");
				 return;
			 }
			 dimsize = new long[rank];
			 if (rank > 2) {
				 layers = new long[rank-2];
			 }
		 }
		 if (ith < 2) {
			 dimsize[ith] = size;
		 }
		 else {
			
			    layers[ith-2] = size;
			 
		 }
	 }
	 private boolean existsAlready(String id) {
		    if (dims == null) {
		    	return false;
		    }
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
	 
	 public String getDimension(int i) {
		 return dims.get(i);
	 }
	 public long getDimSize(int i) {
		 return this.dimsize[i];
	 }
	 public String getDatatype() {
		 return this.datatype;
	 }
	 public void destory() {
		 name = null;
		 datatype = null;
		 dims.clear();
		 layers = null;
		 dimsize = null;
		 rank=0;
		 kind = null;
		 
		
	 }
	
}
