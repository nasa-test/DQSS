package gov.nasa.gsfc.dqss;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class ShortDataField extends datafield {
	
	
	 
	 public ShortDataField(String id) {
		 name = id.replace("View", "");
		 dims    = new ArrayList<String>();
		 type = (short )1;
	 }
	 
	
}
