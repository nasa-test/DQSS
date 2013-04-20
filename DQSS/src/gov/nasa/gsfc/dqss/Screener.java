package gov.nasa.gsfc.dqss;

import gov.nasa.gsfc.Stopwatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import ncsa.hdf.hdflib.HDFException;
import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.Datatype;
import org.apache.log4j.Logger;

public class Screener {

	static Logger logger = Logger.getLogger(Screener.class.getName());
	
	public Screener() {}
	

	/*
	 * This is the one we are using:
	 */
	public Screener(String dataFile,  String dir, String config) {
	
	  
	    
	
	
		DQSS factory = new DQSS(dir,config);
		logger.warn("SCREENING STARTED");
		
		Stopwatch s = new Stopwatch();
		s.start();
		
		ExpressionDataFile data = new ExpressionDataFile(dataFile,dataFile);
			   
			    
			  
	    /*
	     * They are no longer in a separate group by themselves.
	     * However all of the masks end with the same string.
	     */
    	 Dataset[] masks = data.getMaskDatasets(DQSS.MaskName);
    	 
    	 if (masks.length < 1) {
    		 logger.error("No masked datasets were found in " + 
    				 dataFile);
                 System.exit(1);
    	 }
    	 /*
    	  * Find and return corresponding datasets in dataFile 
    	  */
    	 
    	 
    	 Dataset[] datavars = data.getCorrespondingDatasetsInSameGroup(masks,DQSS.MaskName);
    	 
    	 if (datavars.length < 1) {
    		 if (DQSS.PassThru) {
    			 logger.warn("No datasets were found in " + dataFile + 
    				 " corresponding to the datsets put in mask group of " +
    				 dataFile + "Because config item DQSS.PassThru is true," +
    				 		" assuming that no variables were to be screened.");
    		 }
    		 else {
    		 logger.error("No datasets were found in " + dataFile + 
    				 " corresponding to the datsets put in mask group of " +
    				 dataFile );
                 System.exit(1);
    		 }
    	 }
    	 /*
    	  * copy original datasets into new names
    	  */
    	 
    	 Group g = data.getGroupOfDataset(datavars[0].getName());
    	 data.copyIntoSameGroup(datavars, g, DQSS.OrigName);
    	 
    	
	     s.stop();
	     logger.debug("Screening Dataset prep: elapsed time: " + s.getElapsedTime());
    	 /*
    	  * Apply mask to dataFile
    	  */
    	 s.reset();
    	 s.start();
    	 
    	//DQSSNumber[] FillValues = SDgetFillValues(dataFile,datavars);
    	 List<String> params = new ArrayList<String>();
    	 
    	 for (int i=0;i<datavars.length; ++i) {
    		 params.add(datavars[i].getName());
    	 }
    	List<DQSSNumber> FillValues = null;
    	 if (FillValues == null || FillValues.size() == 0 ) {
			 if (data.HDFType == "HDF4") {
				 //FillValues = SDPreProcessing(inputFile,params);
				
				 FillValues = data.getH4FillValues(params); 
			 }
			 else {
				 FillValues = data.getHEFillValues(params); 
			 }
			 if (FillValues == null) {
				 logger.error("No fill values found for:" + params.get(0)  + ". Probably doesn't exist in this file.");
				 return;
			 }
			 
		}
	
    	 data.ApplyMasks(datavars, masks, FillValues);
    	 
    	
    	s.stop();
    	logger.debug("Masking: elapsed time: " + s.getElapsedTime());
    	
    	if (data.HDFType == "HDF4") {
    		data.close();
    		SDProcessing(dataFile, datavars,FillValues);
    		TransferMissingAttributes(dataFile,datavars);
    	}
    	else {
    		 for ( int i=0; i < datavars.length; ++i) {
 		    	String name = datavars[i].getName();
 		    	List<Attribute> attrs = data.getAllHE5Attributes(name);
 		    	for (int j=0; j < attrs.size(); ++j) {
 		    		logger.info(name + " attr:" + attrs.get(j).getName() + attrs.get(j).getValue());
 		    		if (attrs.get(j).getType().getDatatypeClass() == Datatype.CLASS_STRING ||
 		    				attrs.get(j).getType().getDatatypeClass() == Datatype.CLASS_FLOAT){ 
 		    			//String val = (String) attrs.get(j).getValue();
 		    			String[] classValue = { "hello" };
 		    			try {
							Attribute newAttr = data.getYourHDF5Attribute(attrs.get(j).getName(), classValue );
						data.setHE5Attribute(name + DQSS.OrigName, attrs.get(j));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
 		    			
 		    		}
 		    	}
    		 }
    		// HE5 is done with same library so it doesn't need to be
    		// close and reopened
    		data.close();
    	}
        System.exit(0);
		    	
	
}
	void TransferMissingAttributes(String inputFile, Dataset[] datavars)
    {
	

	if (datavars == null) {
		logger.error("No datavars array available. ie no dataset names with which to retrieve ids");
		return;
	}
	
    try {
	    SDDataFile sid = new SDDataFile(inputFile);
	  
	    for ( int i=0; i < datavars.length; ++i) {
 		   	String name = datavars[i].getName();
	    	/*
	    	 * fill in attributes names for newly created *_orig
	    	 */
        	if (name.length() > 0) {
		    	try {
		    		/*
					 * This returns the attribute name, datatype and string length
					 */
				   
				    HashMap<String,Integer[]> nameAndLen = new HashMap<String,Integer[]>();
				    sid.GetListOfAttributeNames(name, nameAndLen);
				    Iterator it = nameAndLen.entrySet().iterator();
				   
				    while (it.hasNext()) {
				       try {
				    	Map.Entry pairs = (Map.Entry)it.next();
				        Object attributevalue = sid.GetAttribute(name, (String)pairs.getKey(), (Integer[])pairs.getValue());
				        Integer[]   dimensions = (Integer[])pairs.getValue();
				        if (dimensions[0] == 4) {
				        logger.debug ("Attribute " + (String)pairs.getKey() + " has value:" + (String)attributevalue);
				        }
				        else if  (dimensions[0] == 22) {
				        	Short[] IntVals = (Short[])attributevalue;
					        logger.debug ("Attribute " + (String)pairs.getKey() + " last value is:" + IntVals[dimensions[1]-1]);
					        }
				        else if  (dimensions[0] == 24) {
				        	Integer[] IntVals = (Integer[])attributevalue;
					        logger.debug ("Attribute " + (String)pairs.getKey() + " last value is:" + IntVals[dimensions[1]-1]);
					        }
				        else if  (dimensions[0] == 6) {
				        	Double[] IntVals = (Double[])attributevalue;
					        logger.debug ("Attribute " + (String)pairs.getKey() + " last value is:" + IntVals[dimensions[1]-1]);
					        }
				        else if  (dimensions[0] == 5) {
				        	Float[] IntVals = (Float[])attributevalue;
					        logger.debug ("Attribute " + (String)pairs.getKey() + " last value is:" + IntVals[dimensions[1]-1]);
					        }
				        else {
				        	logger.error("Need code for type: " + dimensions[0]);
				        }
				        
				        sid.SetAllAttributes(name + DQSS.OrigName, (String)pairs.getKey() ,dimensions,attributevalue);
				        logger.debug("Set attribute" + (String)pairs.getKey() + " to "+ attributevalue + " in " + name + DQSS.OrigName);
				       }
				       catch (HDFException e) {
				    	   logger.error("Datatype problem with attributes");
				       }
				    }
			    	
			    	
			    	
		    	} catch (NoSuchElementException e) {
		    		
		    		logger.error("Elements for SDPostProcessing name, criterion, dimname (" + i + ")");
		    		logger.error(name);
		    		
		    		
		    	}
	    	}
	    
	    	
	    	
	    }
	    
	    sid.destroy();
    }
    catch (HDFException e) {
    	e.printStackTrace();
    	logger.error("SD stuff failed");
    }
    
}
	/*
	 * No segment fault if you open readonly and then close.
	 */
	protected DQSSNumber[] SDgetFillValues(String dataFile, Dataset[] datavars) {
		
		
		SDDataFile sid;
		DQSSNumber[] fv =null;
		try {
			sid = new SDDataFile(dataFile,"readonly");
			fv =  sid.getFillValues(datavars) ;
			sid.destroy();
		} catch (HDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fv;
	}
	
	
	public void SDProcessing(String dataFile, Dataset[] datavars, List<DQSSNumber> FillValues) {
		try {
    	    SDDataFile sid = new SDDataFile(dataFile);
    	    for ( int i=0; i < datavars.length; ++i) {
    		    	String name = datavars[i].getName();
    		    	String[] newdimnames = sid.GetDimensionNames(name);
    		    	if (newdimnames != null) { 
    		    	sid.UpdateDimensionNames(name + DQSS.OrigName, newdimnames);
    		    	sid.UpdateDimensionNames(name + DQSS.MaskName, newdimnames);
    		    	}
    		    	else {
    		    		logger.warn( DQSS.OrigName + " has no  dimension names so we could not update " + DQSS.MaskName);
    		    	}
    		    	sid.SetFill(name + DQSS.OrigName, FillValues.get(i));
    		    	
    		    }
    		    sid.destroy();
    	    }
    	    catch (HDFException e) {
    	    	e.printStackTrace();
    	    }
	}	
	public  static void main(String args[])
	{
		if (args.length == 3) {
			String dataFile = args[0];
			
			String stagingDir = args[1];
			String config     = args[2];
			Screener screened = new Screener(dataFile, stagingDir, config);
		}    
		
		else {
			logger.error("Inputs are datafile, stagingdir, configfile");
			for (int i =0; i < args.length; ++i) {
				logger.error(i+":"+args[i]);
			}
		}
		    
	}
}
