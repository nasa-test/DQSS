package gov.nasa.gsfc.dqss;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;

import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Group;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Node;

public  abstract class MaskFactory {
	
	protected long rows = -1;
	protected long cols = -1;
	protected long layers = -1;
	protected String dataVariable = null;
	public static DQSSNumber FillValue = new DQSSNumber(-7777);
	/*
	public static String ontology = "http://mirador.gsfc.nasa.gov/ontologies/dqss-Instances.owl";
	protected String navigation = "http://mirador.gsfc.nasa.gov/ontologies/DQSSNavigation.xml";
	protected static int  debugLevel=0;
	public static   String stagingDirectory = "/var/tmp/staging";
	public static String NoScreeningString;
	public static String MaskName;
	public static String OrigName;
	public static String RejectName;
	public static String OntologyCriteriaURL;
	public static boolean UseServlet = true;
	public static boolean UseCache = true;
	public static String CacheDir;
	
	public int SkipBecauseNoScreeningSelected;
	*/
	
	protected String dir;
	protected String config;
	static Logger logger = Logger.getLogger(MaskFactory.class.getName());
    DQSS dqss;
    
	public MaskFactory() {
		  /*
		   * The layer number in the SDS nBestStd is the lowest altitude 
		   * layer for which the quality is Best.
		   * if datalayer >= qualitypixel[i] set data[i] = 1, else fillvalue  
		   */
		 
	}
	public MaskFactory(String dir, String configPath) {
		//stagingDirectory = dir;
		//log4jSession sessionLog4j = new  log4jSession(dir + File.separator, DQSS.class);
        //logger = sessionLog4j.getLogger();
		// No longer needed. Will be done when DQSS class is instantiated
        //DQSS.SetStagingDirectory(dir);
		//DQSS.ReadConfigFile(configPath);
		
		
        
	}
	
	public abstract  void PrepMasking(DataFile file, String snvid, String dataVariable, 
			String criteria, DQSSNumber fillvalue);
	public abstract  Statistics StartMasking(AggregateExpression he,  DQSSNumber fillvalue);

	public abstract  String LoadOntology( String snvid, String dataVariable, 
			String criteria);
	
    private Group getMaskGroup(String mask, DataFile file, String dataVariable) {
	    	
	        Group currg = file.getGroup(mask,file.topGroup);
	        if (currg != null) {
	        	if (currg.getName().equals(mask)) {
	        		logger.warn("Using existing group:" + currg.getPath());
	        		return currg;
	        	}
	        }
	        else {
	        		Dataset temp =   file.getDataset(dataVariable,file.topGroup);
	        		if (temp == null) {
	        			logger.error("Could not get dataset matching datavariable:" + dataVariable +
	        					" Something serious is incorrect like perhaps the filename or type of file");
	        			return null;
	        		}
	        		currg = file.createGroup(temp,"mask");
	        		if (currg == null) {
	        			logger.error("Could not create mask group");
	        			return null;
	        		}
	        		else {
	        			logger.warn("Using NEW group:" + currg.getName() );
	        			return currg;
	        		}
	        }
	        return currg;
	  }
	  

	private void addRefTypesToQualityTypes(String[]types, String[]refs) {
		
		int typeslen = types.length;
	
		for (int i = 0; i < refs.length; ++i) {
			if (refs[i] != null) {
				if (refs[i].length() > 1) {
					logger.debug("Adding " + refs[i] + " to sf_types");
					types[typeslen] = refs[i];
					
					++typeslen;
				}
			}
		}
	}
	
	 public MaskFactory(String parameter,String criteria, long rows, long cols, long layers) {
		 this.rows = rows;
		 this.cols = cols;
		 this.layers = layers;
		 dataVariable = parameter;
	 }
	 //public  void doThisMask(Dataset dv, Dataset[] qs) throws Exception;
	 
	

	  
	   
	 	   public  static DQSSNumber getFillValue() {
			return FillValue;
		}
}
