package gov.nasa.gsfc.dqss;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class FloatDataField extends datafield {
	
	
	 
	 public FloatDataField(String id) {
		 name = id.replace("View", "");
		 dims    = new ArrayList<String>();
		 type = (float )1;
	 }
}