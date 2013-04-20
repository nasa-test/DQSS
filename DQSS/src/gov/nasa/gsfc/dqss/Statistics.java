package gov.nasa.gsfc.dqss;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ncsa.hdf.object.Dataset;

import org.apache.log4j.Logger;

/*
 * If the user increments 'kept' using inc() then he just needs 
 * to setTotal(i) at end .
 */
public class Statistics {

	int kept;
	int prefill = 0;
	int total = 0;
	int discarded;
	String directory;
	private String dataVariable;
	private String algorithm;
	private String filetype;
	//private String[] qualityLevel = new String[5];
	List<String> qualityLevel = new ArrayList<String>();
	//private String datavariable = "";
	List<String> datavariable = new ArrayList<String>();
	List<String> qualityName = new ArrayList<String>();
	List<String> sfType = new ArrayList<String>();
	static Logger logger = Logger.getLogger(Statistics.class.getName());
	HashMap<Integer,Integer> cflags = new HashMap<Integer,Integer>(); 
	HashMap<Integer,Integer> qaflags = new HashMap<Integer,Integer>();  
	private long npixels;
	
	
	public Statistics(DataFile file) {
		filetype = file.FileType;
	}
	
	public Statistics(String dir, String param, AggregateExpression he, Dataset[] QualityDatasets) {
		total = -1;
		kept = 0;
		discarded = 0;
		directory = dir;
		/* mask(name) is passed but we need the normal name in the
		 * global attributes.
		 */
		dataVariable = param.replace(DQSS.MaskName, "");
		
	    filetype = he.file.FileType;
	    logger.debug("Statistics constructor:" + dataVariable +
	    		":" + he.file.FileType); 
	    
		Iterator it = he.SATypes.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry SApair = (Map.Entry) it.next();
			String[] typeslist = he.SATypes.get(SApair.getKey());
			datavariable.add( typeslist[0]);
			sfType.add(typeslist[1]);
			qualityLevel.add(typeslist[3]);
			qualityName.add(typeslist[2]);
			// not quite ready for prime time:. setQualityLevel(Long.parseLong(typeslist[3]), 0, typeslist[2]);
		}
	
	}
	/*
	 * Used with null dataset (MOD04 Deep Blue)
	 */
	public Statistics(String param) {
		total = -1;
		prefill = -1;
		kept = 0;
		discarded = 0;
		dataVariable = param.trim();
	    
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public int getTotal() {
		return total;
	}
	
	public int getKept() {
		return kept;
	}
	
	public void setQualityLevel(long level, int ith, String name) {
		logger.debug(name + ":" + level);
		if (filetype.contains("AIRS")) {
			switch((int) level) {
			case 0: qualityLevel.add("Best");
			break;
			case 1: qualityLevel.add("Good");
			break;
			case 2: qualityLevel.add("DoNotUse");
			break;
			default: ;
			}
				
		}
		else if (filetype.contains("MOD")) {
			if (name.contains("Cloud")) {
				switch((int) level) {
				case 0: qualityLevel.add(DQSS.Q2String0);
				break;
				case 1: qualityLevel.add(DQSS.Q2String1);
				break;
				case 2: qualityLevel.add(DQSS.Q2String2);
				break;
				case 3: qualityLevel.add(DQSS.Q2String3);
				default: ;
				}
			}
			else {
				switch((int) level) {
				case 0: qualityLevel.add(DQSS.Q1String0);
				break;
				case 1: qualityLevel.add(DQSS.Q1String1);
				break;
				case 2: qualityLevel.add(DQSS.Q1String2);
				break;
				case 3: qualityLevel.add(DQSS.Q1String3);
				default: ;
				}
			}
		}
	}

	public String getQualityLevel(int ith) {
		if (this.qualityLevel.size() >= ith + 1) {
			return this.qualityLevel.get(ith);
		}
		return null;
	}
	public int getDiscarded() {
		
		//Float pre = new Float(this.getPrePercentage());
		//Float after = new Float(this.getPercentage());
		//if (pre - after < 1) {
		/*
		 * Only report if the difference is quite small.
		 */
			if (discarded > 0 && discarded < .001 * total) {
				return discarded;
			}
		//}
		return 0;
	}
	public void setTotal(int total) {
		 this.total = total;
		 /*
		 if (kept > 0) {
			 setDiscarded(total - kept);
		 }
		 */
	}
	public void setKept(int kept) {
		 this.kept = kept;
		 /*
		 if (total > 0) {
			 setDiscarded(total - kept);
		 }
		 */
	}
	
	public void setDiscarded(int discarded) {
		 this.discarded = discarded;
	}
	
	/*
	 * Number of pixels set to 1 in mask
	 */
	public void inc() {
		++kept;
	}
	
	/* 
	 * Number of pixels set to fill value before masking
	 */
	public void countfill() {
		++prefill;
	}
	
	public String getVariable() {
		return dataVariable;
	}
	public String getQualityVariable(int ith) {
		return this.datavariable.get(ith);
	}
	public String getQualityVariableType(int ith) {
		return this.sfType.get(ith);
	}
	public String getQualityName(int ith) {
		return this.qualityName.get(ith);
	}
	/*
	 * Percentage of pixels in mask set to 1
	 */
	public String getPercentage() {
		 float n = this.kept;
		 float d = this.total;
		 if (d > 0) {
		 float percentage = (float) (100.0 * (n/d));
		 logger.debug("kept:" + n + " total:" + d + " prefill:" + prefill);
		 return new DecimalFormat("##.##").format(percentage);
		 }
		 else {
			 return null;
		 }
	}
	
	/*
	 * percentage of non-fill values before mask.
	 */
	public String getPrePercentage() {
		 float n = this.prefill;
		 float d = this.total;
		 if (d > 0) {
			 float percentage = (float)( 100 - (100.0 * (n/d)));		
		 	return new DecimalFormat("##.##").format(percentage);
		 }
		 else {
			 return null;
		 }
	}
	
	
	 public boolean writeStats() {
		 String percentage = getPercentage();	 
		 String output = dataVariable  + " mask retained " +
		 kept + " of " + total + " pixels. " + percentage + "%";
		 //String currentcontents = fileRead();
		 return DQSS.fileAppend(output);
	 }
	 /*
	  * just integers for flags
	  */
	 public void countCldflags(int value) 
	 {
		 Integer currentTotalForValue = this.cflags.get(value);
		 if (currentTotalForValue == null) {
			 currentTotalForValue = 0;
		 }
		 currentTotalForValue += 1;
		 this.cflags.put(value,currentTotalForValue);
	 }
	 public void countQAflags(int value) 
	 {
		 Integer currentTotalForValue = this.qaflags.get(value);
		 if (currentTotalForValue == null) {
			 currentTotalForValue = 0;
		 }
		 currentTotalForValue += 1;
		 this.qaflags.put(value,currentTotalForValue);
	 }
	 public void setpixels(long rows, long cols) {
		 npixels = rows * cols;
	 }
	 public void publishCldFlags() {
		
		 logger.debug("Publish Cld Flags");
		 Iterator it = this.cflags.entrySet().iterator();
		 long total = 0;
		 while (it.hasNext()) {
              Map.Entry pairs = (Map.Entry)it.next();
              Integer key = (Integer)pairs.getKey();
              Integer value = (Integer)pairs.getValue();
              logger.warn("Flags:" + key + " Value:" + value);
              total += value;
        }
		 logger.warn("Total C Flags Pixels:" + total + "(" + npixels + ")");
	 }
	 public void publishQAFlags() {
			
		 logger.debug("Publish QA Flags");
		 Iterator it = this.qaflags.entrySet().iterator();
		 long total = 0;
		 while (it.hasNext()) {
              Map.Entry pairs = (Map.Entry)it.next();
              Integer key = (Integer)pairs.getKey();
              Integer value = (Integer)pairs.getValue();
              logger.warn("Flags:" + key + " Value:" + value);
              total += value;
        }
		 logger.warn("Total QA Flags Pixels:" + total + "(" + npixels + ")");
	 }
		
}
